package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {

    public static Map<Team, User> teamLeaderMap = new HashMap<>();
    public static Map<Team, List<User>> teamUsersMap = new HashMap<>();
    public static Map<User, List<Team>> usersTeamMap = new HashMap<>();
    public static Map<Team, Map<User, Integer>> teamScoreBoard = new HashMap<>();
    public static Map<Team, List<Board>> teamBoardsMap = new HashMap<>();
    private static List<Team> teams = new ArrayList<>();

    private String name;
    private String status = "pending";
    private List<Map.Entry<User, String>> notifications = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();
    private List<User> suspendUsers = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static void setTeams(List<Team> teams) {
        Team.teams = teams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map.Entry<User, String>> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Map.Entry<User, String>> notifications) {
        this.notifications = notifications;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<User> getSuspendUsers() {
        return suspendUsers;
    }

    public void setSuspendUsers(List<User> suspendUsers) {
        this.suspendUsers = suspendUsers;
    }
}
