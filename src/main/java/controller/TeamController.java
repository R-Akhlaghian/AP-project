package controller;

import model.Task;
import model.Team;
import model.User;
import util.UserAccessChecker;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.*;
import java.util.stream.Collectors;

public class TeamController {

    public void printUserTeams(User user){
        List<Team> teams = Team.usersTeamMap.get(user);
        if(teams == null || teams.isEmpty())
            return;

        for (int i = 0; i < teams.size(); i++) {
            System.out.println(i+ 1 + " " + teams.get(i).getName());
        }
    }

    public Team getTeamByUserAndTeamNameOrIndex(User user, String teamNameOrId) {
        List<Team> teams = Team.usersTeamMap.get(user);
        if(teams == null || teams.isEmpty())
            return null;

        if(teamNameOrId.chars().allMatch(Character::isDigit)){
            int teamIndex = Integer.parseInt(teamNameOrId);
            if(teams.size() >= teamIndex -1)
                return teams.get(teamIndex - 1);
        }else {
            for (Team team : teams) {
                if (team.getName().equals(teamNameOrId))
                    return team;
            }
        }

        return null;
    }

    public void showScoreBoard(Team team) {
        List<User> users = Team.teamUsersMap.get(team);
        if (users == null || users.isEmpty()) {
            System.out.println("There is no member in this team!");
            return;
        }

        Map<User, Integer> usersScore = Team.teamScoreBoard.get(team);
        if (usersScore == null)
            return;

        List<Map.Entry<User, Integer>> list = new ArrayList<>(usersScore.entrySet());
        list.sort(Map.Entry.comparingByValue());
        System.out.println("Rank    Username    Score");
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<User, Integer> entry = list.get(i);
            System.out.println(i + 1  + " " + entry.getKey().getUsername() + " : " + entry.getValue());
        }
    }

    public void showRoadMap(Team team) {
        List<Task> tasks = team.getTasks();
        if (tasks.isEmpty()){
            System.out.println("no task yet");
            return;
        }

        for (Task task : tasks) {
            System.out.println(task.getTitle());
        }
    }

    public void showChatroom(Team team) {
        if (team.getNotifications().isEmpty()) {
            System.out.println("no message yet");
            return ;
        }
        for (Map.Entry<User, String> notification : team.getNotifications()) {
            System.out.println(notification.getKey().getUsername() + ": " + notification.getValue());
        }
    }

    public void sendMessage(User user, Team team, String message) {
         team.getNotifications().add(new AbstractMap.SimpleEntry<User, String>(user, message));
    }

    public void showTasks(Team team) {

        Comparator<Task> compareTask = Comparator
                .comparing(Task::getDeadline)
                .thenComparing(t -> Task.priorities.indexOf(t.getPriority()));
        List<Task> sortedTasks = team.getTasks().stream().sorted(compareTask).collect(Collectors.toList());
        if (sortedTasks.isEmpty()) {
            System.out.println("no task yet");
            return;
        }
        for (int i = 0; i < sortedTasks.size(); i++) {
            Task t = sortedTasks.get(i);
            List<User> users = Task.taskUsersMap.get(t);
            List<String> username = new ArrayList<>();
            if(users != null)
                username = users.stream().map(User::getUsername).collect(Collectors.toList());
            System.out.println(i + 1 + "." + t.getTeam()  +":id" + t.getId() + ",creation date:" + t.getCreateDate().toInstant()
            +",deadline:" + t.getDeadline() + ",assign to:" + Arrays.toString(username.toArray()) + ",priority:" + t.getPriority());
        }
    }

    public void showTask(Team team, Integer taskId) {
        for (Task t : team.getTasks()) {
            if (t.getId() == taskId){
                List<User> users = Task.taskUsersMap.get(t);
                List<String> username = new ArrayList<>();
                if(users != null)
                    username = users.stream().map(User::getUsername).collect(Collectors.toList());

                System.out.println( t.getTeam()  +":id" + t.getId() + ",creation date:" + t.getCreateDate().toInstant()
                        +",deadline:" + t.getDeadline() + ",assign to:" + Arrays.toString(username.toArray()) + ",priority:" + t.getPriority());
            }
        }
    }

    public void showTeams(User user) {
        List<Team> teams = Team.usersTeamMap.get(user);
        if (teams == null || teams.isEmpty()){
            System.out.println("There is no team for you!");
        }else {
            for (int i = 0; i < teams.size(); i++) {
                System.out.println(i + 1 + "-" + teams.get(i).getName() );
            }
        }

    }

    public Team showTeamByIdOrName(User user, String teamNameOrId) {
        List<Team> teams = Team.usersTeamMap.get(user);
        if(teams == null || teams.isEmpty()) {
            System.out.println("Team not found!");
            return null;
        }

        if(teamNameOrId.chars().allMatch(Character::isDigit)){
            int teamIndex = Integer.parseInt(teamNameOrId);
            if(teams.size() >= teamIndex -1) {
                System.out.println(teams.get(teamIndex - 1).getName());
                return teams.get(teamIndex - 1);
            }
        }else {
            for (Team team : teams) {
                if (team.getName().equals(teamNameOrId)){
                    System.out.println(team.getName());
                    return team;
                }
            }
        }

        return null;
    }

    public void createTeam(User user, String teamName) {
        if (UserAccessChecker.isLeader(user)) {
            List<Team> teams = Team.usersTeamMap.get(user);
            if (teams == null)
                teams = new ArrayList<>();
            if (teams.stream().anyMatch(t -> t.getName().equals(teamName))){
                System.out.println("There is another team with this name!");
            }else if (teamName.length() < 5 || teamName.length() >12 || teamName.chars().noneMatch(Character::isDigit)||teamName.chars().noneMatch(Character::isUpperCase)){
                System.out.println("Team name is invalid!");
            }else{
                Team team = new Team(teamName);
                teams.add(team);
                Team.usersTeamMap.put(user, teams);
                Team.teamUsersMap.put(team, new ArrayList<>());
                Team.teamLeaderMap.put(team, user);
                Team.teamScoreBoard.put(team, new HashMap<>());
                Team.teamBoardsMap.put(team, new ArrayList<>());
                System.out.println("Team created successfully! Waiting For Admin’s confirmation…");
            }
        }
    }

    public void showAllTask(User user, Team team) {

        if (UserAccessChecker.isLeader(user)){
            Comparator<Task> compareTask = Comparator
                    .comparing(Task::getCreateDate)
                    .thenComparing(t -> Task.priorities.indexOf(t.getPriority()));
            List<Task> sortedTasks = team.getTasks().stream().sorted(compareTask).collect(Collectors.toList());
            if (sortedTasks.isEmpty()) {
                System.out.println("no task yet");
                return;
            }
            for (int i = 0; i < sortedTasks.size(); i++) {
                Task t = sortedTasks.get(i);
                List<User> users = Task.taskUsersMap.get(t);
                List<String> username = new ArrayList<>();
                if(users != null)
                    username = users.stream().map(User::getUsername).collect(Collectors.toList());
                System.out.println(i + 1 + ".title: " + t.getTitle()  +":id" + t.getId() + ",creation date:" + t.getCreateDate().toInstant()
                        +",deadline:" + t.getDeadline() + ",assign to:" + Arrays.toString(username.toArray()) + ",priority:" + t.getPriority());
            }
        }else{
            System.out.println("invalid command");
        }
    }

    public void createTask(User user, Team team, String title, String startTime, String deadline) {

        if (UserAccessChecker.isLeader(user)){
            List<Task> tasks = team.getTasks();

            Date creationDate = TaskController.getTaskDate(startTime);
            Date deadLine = TaskController.getTaskDate(deadline);
            if (tasks.stream().anyMatch(t -> t.getTitle().equals(title))) {
                System.out.println("There is another task with this title!");
            }else if(creationDate == null || creationDate.compareTo(new Date()) < 0){
                System.out.println("Invalid start date!");
            }else if(deadLine == null|| deadLine.compareTo(new Date()) <= 0){
                System.out.println("Invalid deadline!");
            }else{
                Task task = new Task(title, creationDate, deadLine, team);
                Task.tasks.add(task);
                Task.taskUsersMap.put(task, new ArrayList<>());

                team.getTasks().add(task);
                System.out.println("Task created successfully!");
            }
        }else{
            System.out.println("invalid command");
        }
    }

    public void showMembers(User user, Team team) {
        if (UserAccessChecker.isLeader(user)){
            List<User> users = Team.teamUsersMap.get(team);
            if (users != null){
                users.stream().map(User::getUsername).sorted(String::compareTo)
                        .forEach(System.out::println);
            }
        }else{
            System.out.println("invalid command");
        }
    }

    public void addMember(User user, Team team, String username) {
        if (UserAccessChecker.isLeader(user)){
           User member = User.getUserByUsername(username);
           if (member == null)
               System.out.println("No user exists with this username!");
           else{
               List<User> users = Team.teamUsersMap.get(team);
               if (users == null)
                   users = new ArrayList<>();
               users.add(member);
               List<Team> teams = Team.usersTeamMap.get(member);
               if (teams == null)
                   teams = new ArrayList<>();
               teams.add(team);
           }
        }else{
            System.out.println("invalid command");
        }
    }

    public void deleteMember(User user, Team team, String username) {
        if (UserAccessChecker.isLeader(user)){

            List<User> users = Team.teamUsersMap.get(team);
            if (users == null)
                users = new ArrayList<>();
            User member = User.getUserByUsername(username);
            if (member == null || !users.contains(member)){
                System.out.println("No user exists with this username!");
            }else{
                users.remove(member);
                Team.usersTeamMap.get(member).remove(team);
                team.getSuspendUsers().remove(member);
            }

        }else{
            System.out.println("invalid command");
        }
    }

    public void suspendMember(User user, Team team, String username) {
        if (UserAccessChecker.isLeader(user)){

            List<User> users = Team.teamUsersMap.get(team);
            if (users == null)
                users = new ArrayList<>();
            User member = User.getUserByUsername(username);
            if (member == null || !users.contains(member)){
                System.out.println("No user exists with this username!");
            }else{
                team.getSuspendUsers().add(member);
            }

        }else{
            System.out.println("invalid command");
        }
    }

    public boolean promoteUser(User user, Team team, String username) {
        if (UserAccessChecker.isLeader(user)){

            List<User> users = Team.teamUsersMap.get(team);
            if (users == null)
                users = new ArrayList<>();
            User member = User.getUserByUsername(username);
            if (member == null){
                System.out.println("No user exists with this username!");
            }else{
                users.remove(member);
                Team.teamLeaderMap.put(team, member);
                member.setUserType("Team Leader");
                return true;
            }
        }else{
            System.out.println("invalid command");
        }
        return false;
    }

    public void assignTaskToUser(User user, Team team, Integer taskId, String username) {
        if (UserAccessChecker.isLeader(user)){

            List<User> users = Team.teamUsersMap.get(team);
            if (users == null)
                users = new ArrayList<>();
            User member = User.getUserByUsername(users, username);
            if (member == null){
                System.out.println("No user exists with this username!");
            }else{
                Task task = team.getTasks().stream().filter(t -> t.getId() == taskId).findAny().orElse(null);
                if (task != null) {
                    List<User> taskUsers = Task.taskUsersMap.get(task);
                    if (taskUsers == null)
                        taskUsers = new ArrayList<>();
                    taskUsers.remove(member);
                    taskUsers.add(member);

                    List<Task> tasks = Task.userTasksMap.get(member);
                    if (tasks == null)
                        tasks = new ArrayList<>();
                    tasks.remove(task);
                    tasks.add(task);


                    System.out.println("Member Assigned Successfully!");
                }else {
                    System.out.println("No Task exists with this id!");
                }
            }
        }else{
            System.out.println("invalid command");
        }
    }

    public void sendNotificationToUser(User user, Team team, String notification, String username) {
        if (UserAccessChecker.isLeader(user)){

            List<User> users = Team.teamUsersMap.get(team);
            if (users == null)
                users = new ArrayList<>();
            User member = User.getUserByUsername(users, username);
            if (member == null){
                System.out.println("No user exists with this username!");
            }else{
              member.getTeamLeadNotifications().add(notification);
            }
        }else{
            System.out.println("invalid command");
        }
    }

    public void sendNotificationToTeam(User user, String teamName, String notification) {
        if (UserAccessChecker.isLeader(user)){

            Team team = Team.getTeams().stream().filter(t -> t.getName().equals(teamName)).findAny().orElse(null);
            if (team != null){
                team.getNotifications().add(new AbstractMap.SimpleEntry<User, String>(user, notification));
            }else {
                System.out.println("No team exists with this name !");
            }
        }else{
            System.out.println("invalid command");
        }
    }
}
