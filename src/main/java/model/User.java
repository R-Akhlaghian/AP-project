package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private static List<User> users = new ArrayList<>();
    private static User loggedInUser;

    private String name;
    private String username;
    private String email;
    private String password;
    private String userType;
    private String birthDay;
    private int totalScore = 0;
    private List<LocalDateTime> loginTimes = new ArrayList<>();
    private List<String> teamLeadNotifications = new ArrayList<>();
    private Task currentTask;
    private Team currentTeam;
    private User chosenMember;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        userType = "Team Member";
    }

    public User(String username) {
        this.username = username;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> users) {
        User.users = users;
    }

    public static boolean existUserByUsername(String username) {
        for (User user : users) {
            if(user.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public static boolean existUserByEmail(String email) {
        for (User user : users) {
            if(user.getEmail().equals(email))
                return true;
        }
        return false;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if(user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public static User getUserByUsername(List<User> users,String username) {
        for (User user : users) {
            if(user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<LocalDateTime> getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(List<LocalDateTime> loginTimes) {
        this.loginTimes = loginTimes;
    }

    public List<String> getTeamLeadNotifications() {
        return teamLeadNotifications;
    }

    public void setTeamLeadNotifications(List<String> teamLeadNotifications) {
        this.teamLeadNotifications = teamLeadNotifications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Task getCurrentTask(){return this.currentTask;}

    public void setCurrentTask(Task task){this.currentTask=task;}

    public void setCurrentTeam(Team team){this.currentTeam = team;}

    public Team getCurrentTeam(){return this.currentTeam;}

    public void setChosenMember(User member){this.chosenMember = member;}

    public User getChosenMember(){return this.chosenMember;}

    public static void setLoggedInUser(User user){ loggedInUser = user; }

    public static User getLoggedInUser(){return loggedInUser;}

    public void updateScore(int i) {
        totalScore += i;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", totalScore=" + totalScore +
                ", loginTimes=" + loginTimes +
                '}';
    }
}
