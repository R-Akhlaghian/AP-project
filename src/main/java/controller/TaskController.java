package controller;


import model.Task;
import model.User;
import util.UserAccessChecker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class TaskController {
    public void editTaskTitle(User user, Integer taskId, String title) {
        if(!UserAccessChecker.isLeader(user)) {
            System.out.println("You Don’t Have Access To Do This Action!");
            return;
        }

        Task.tasks.stream().filter(task -> task.getId() == taskId).forEach(task -> {
            task.setTitle(title);
            System.out.println("Title updated successfully!");
        });

    }

    public void editTaskDescription(User user, Integer taskId, String description) {
        if(!UserAccessChecker.isLeader(user)) {
            System.out.println("You Don’t Have Access To Do This Action!");
            return;
        }

        Task.tasks.stream().filter(task -> task.getId() == taskId).forEach(task -> {
            task.setDescription(description);
            System.out.println("Description updated successfully!");
        });
    }

    public void editTaskPriority(User user, Integer taskId, String priority) {
        if(!UserAccessChecker.isLeader(user)) {
            System.out.println("You Don’t Have Access To Do This Action!");
            return;
        }

        if(Task.priorities.contains(priority)){
            Task.tasks.stream().filter(task -> task.getId() == taskId).forEach(task -> {
                task.setPriority(priority);
                System.out.println("Priority updated successfully!");
            });
        }else {
            System.out.println("invalid priority");
        }
    }

    public void editTaskDeadline(User user, Integer taskId, String deadline) {
        if(!UserAccessChecker.isLeader(user)) {
            System.out.println("You Don’t Have Access To Do This Action!");
            return;
        }

        Date deadLine = getTaskDate(deadline);

        if (deadLine == null){
            System.out.println("New deadline is invalid!");
            return;
        }
        Task.tasks.stream().filter(task -> task.getId() == taskId).forEach(task -> {
            if(task.getCreateDate().compareTo(deadLine) <= 0){
                task.setDeadline(deadLine);
                System.out.println("Deadline updated successfully!");
            }else {
                System.out.println("New deadline is invalid!");
            }
        });


    }

    public static Date getTaskDate(String dateInput) {
        Pattern pattern = Pattern.compile("^[1-9][0-9]{3}-[0-9]{2}-[0-9]{2}[|][0-9]{2}[:][0-9]{2}$");
        if (pattern.matcher(dateInput).matches()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD|HH:MM");
            try{
                return dateFormat.parse(dateInput);
            }catch (Exception e) {

            }
        }

        return null;
    }

    public void removeUserFromTaskList(User user, Integer taskId, String username) {
        if(!UserAccessChecker.isLeader(user)) {
            System.out.println("You Don’t Have Access To Do This Action!");
            return;
        }

        Task.tasks.stream().filter(task -> task.getId() == taskId).forEach(task -> {
            List<User> users = Task.taskUsersMap.get(task);
            if(users != null){
                User removedUser = User.getUserByUsername(username);
                if(users.remove(removedUser)){
                    List<Task> tasks = Task.userTasksMap.get(removedUser);
                    tasks.remove(task);
                    System.out.println("User " + username+" removed successfully!");
                    return;
                }
            }

            System.out.println("There is not any user with this username "+ username +" in list!");
        });

    }

    public void addUserFromTaskList(User user, Integer taskId, String username) {
        if(!UserAccessChecker.isLeader(user)) {
            System.out.println("You Don’t Have Access To Do This Action!");
            return;
        }

        User newUser = User.getUserByUsername(username);
        if(newUser == null) {
            System.out.println("There is not any user with this username "+ username +"!");
            return;
        }
        Task.tasks.stream().filter(task -> task.getId() == taskId).forEach(task -> {
            List<User> users = Task.taskUsersMap.get(task);
            if(users == null)
                users = new ArrayList<>();

            List<Task> tasks = Task.userTasksMap.get(newUser);

            if(tasks == null)
                tasks = new ArrayList<>();

            if(!tasks.contains(task)){
                tasks.add(task);
            }
            if(!users.contains(newUser)){
                users.add(newUser);
            }
            Task.taskUsersMap.put(task, users);
            Task.userTasksMap.put(newUser, tasks);
            System.out.println("User " + username+" added successfully!");
        });
    }
}
