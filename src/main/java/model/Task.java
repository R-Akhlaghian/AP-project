package model;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Task {

    public static List<Task> tasks = new ArrayList<>();
    public static List<String> priorities = new ArrayList<>();
    public static Map<User, List<Task>> userTasksMap = new HashMap<>();
    public static  Map<Task, List<User>> taskUsersMap = new HashMap<>();

    static {
        priorities.add("Highest");
        priorities.add("High");
        priorities.add("Low");
        priorities.add("Lowest");
    }

    private static int autoId = 1;
    private int id;
    private String title;
    private String description;
    private String priority;
    private Date createDate;
    private Date deadline;
    private Team team;

    public Task(String title, Date createDate, Date deadline, Team team) {
        this.title = title;
        this.createDate = createDate;
        this.deadline = deadline;
        this.team = team;

        id = autoId;
        autoId += 1;
        priority = priorities.get(3);
    }

    public static void assignToUser(Task task, User user) {
        List<Task> tasks = userTasksMap.get(user);
        if (tasks ==  null)
            tasks = new ArrayList<>();
        tasks.add(task);

        List<User> users = taskUsersMap.get(task);
        if (users == null)
            users = new ArrayList<>();
        users.add(user);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public long getDeadlineRemainingDay() {
        Date date = new Date();
        long diff = deadline.getTime() - date.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
