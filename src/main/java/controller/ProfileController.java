package controller;

import model.Team;
import model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class ProfileController {

    public String changePassword(User user, String oldPassword, String newPassword) {
        if(!user.getPassword().equals(oldPassword))
            return "wrong old password!";

        if(user.getPassword().equals(newPassword))
            return "Please type a New Password!";

        if(!isValidPassword(newPassword))
            return "Please Choose A strong Password (Containing at least 8 characters including 1 digit and 1 Capital Letter)";

        user.setPassword(newPassword);
        return null;
    }

    private boolean isValidPassword(String newPassword) {
        return newPassword.length() >= 8
                && newPassword.chars().anyMatch(Character::isDigit)
                && newPassword.chars().anyMatch(Character::isUpperCase);
    }

    public String changeUsername(User user, String newUsername) {

        if(newUsername.length() < 4)
            return "Your new username must include at least 4 characters!";
        if(user.getUsername().equals(newUsername))
            return "you already have this username!";
        if (User.existUserByUsername(newUsername)) {
            return "username already taken !";
        }
        if(!Pattern.compile("^[a-z]*[A-Z]*[0-9]*[-]*$").matcher(newUsername).matches())
            return "New username contains Special Characters! Please remove them and try again";

        user.setUsername(newUsername);
        return null;
    }

    public void showTeams(User user) {
        List<Team> teams = Team.usersTeamMap.get(user);

        if(teams == null)
            return;

        for (Team team : teams) {
            System.out.println(team.getName());
        }
    }

    public void showTeam(User user, String teamName) {
        List<Team> teams = Team.usersTeamMap.get(user);

        if(teams == null)
            return;

        for (Team team : teams) {
            if(team.getName().equals(teamName)){
                System.out.println(team.getName());
                User leader = Team.teamLeaderMap.get(team);
                System.out.println(leader.getUsername());
                if(!user.getUsername().equals(leader.getUsername()))
                    System.out.println(user.getUsername());

                List<User> teamUsers = Team.teamUsersMap.get(team);
                if(teamUsers == null)
                    return;

                teamUsers.stream().map(User::getUsername).filter(username -> !username.equals(user.getUsername()))
                        .sorted(String::compareTo)
                        .forEach(System.out::println);
            }
        }
    }

    public void showLogs(User user) {
        for (LocalDateTime loginTime : user.getLoginTimes()) {
            System.out.println(loginTime.toString());
        }
    }

    public void showLeaderNotifications(User user) {
       user.getTeamLeadNotifications().forEach(System.out::println);

    }

    public void showMyProfile(User user) {
        System.out.println("name: " + user.getName());
        System.out.println("username: " + user.getUsername());
        System.out.println("birthDay: " + user.getBirthDay());
        System.out.println("role: " + user.getUserType());
        System.out.println("totalScore: " + user.getTotalScore());
    }
}
