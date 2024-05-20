package controller;

import model.Team;
import model.User;

import javax.swing.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController {
    public void showUserProfile(String username) {
        User user = User.getUserByUsername(username);
        if (user == null){
            System.out.println("There is no user with this username");
            return;
        }

        System.out.println(user.toString());
    }

    public void banUser(String username) {
        User user = User.getUserByUsername(username);

        if (user == null){
            System.out.println("There is no user with this username");
            return;
        }

        User.getUsers().remove(user);
    }

    public void changeUserRole(String username, String role) {
        User user = User.getUserByUsername(username);
        if (user == null){
            System.out.println("There is no user with this username");
            return;
        }
        user.setUserType(role);
    }

    public void sendNotificationToAll(String notification) {
        User.getUsers().forEach(u -> u.getTeamLeadNotifications().add(notification));
    }

    public void sendNotificationToUser(String username, String notification) {
        User user = User.getUserByUsername(username);
        if (user == null){
            System.out.println("There is no user with this username");
            return;
        }
        user.getTeamLeadNotifications().add(notification);
    }

    public void sendNotificationToTeam(String teamName, String notification) {
        Team team = Team.getTeams().stream().filter(t -> t.getName().equals(teamName)).findAny().orElse(null);
        if (team == null){
            System.out.println("There is no team with this name");
            return;
        }

        User user = new User("sysadmin");
        team.getNotifications().add(new AbstractMap.SimpleEntry<User, String>(user, notification));

    }

    public void showScoreBoard(String teamName) {
        Team team = Team.getTeams().stream().filter(t -> t.getName().equals(teamName)).findAny().orElse(null);
        if (team == null){
            System.out.println("There is no team with this name");
            return;
        }

        new TeamController().showScoreBoard(team);
    }

    public void showPendingTeams() {
        List<Team> pending = Team.getTeams().stream().filter(t -> t.getStatus().equals("pending")).collect(Collectors.toList());
        if (pending.isEmpty())
            System.out.println("There are no Teams in Pending Status!");

        pending.forEach(t -> {
            System.out.println(t.getName());
        });
    }

    public void acceptTeams(String[] names) {
        List<String> teamNames = Arrays.stream(names).collect(Collectors.toList());
        List<Team> pending = Team.getTeams().stream().filter(t -> t.getStatus().equals("pending") && teamNames.contains(t.getName())).collect(Collectors.toList());
        if (pending.size() != teamNames.size()){
            System.out.println("Some teams are not in pending status! Try again");
        }else {
            pending.forEach(t -> {
                t.setStatus("accept");
            });

        }

    }

    public void rejectTeams(String[] names) {
        List<String> teamNames = Arrays.stream(names).collect(Collectors.toList());
        List<Team> pending = Team.getTeams().stream().filter(t -> t.getStatus().equals("pending") && teamNames.contains(t.getName())).collect(Collectors.toList());
        if (pending.size() != teamNames.size()){
            System.out.println("Some teams are not in pending status! Try again");
        }else {
            pending.forEach(t -> {
                t.setStatus("reject");
            });

        }
    }
}
