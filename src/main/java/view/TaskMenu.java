package view;

import controller.TaskController;
import model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskMenu extends AbstractMenu{

    private static Pattern editTaskTitlePattern = Pattern.compile("^edit --task --id ([\\d]+) --title ([\\S]+)$");
    private static Pattern editTaskDescriptionPattern = Pattern.compile("^edit --task --id ([\\d]+) --description ([\\S]+)$");
    private static Pattern editTaskPriorityPattern = Pattern.compile("^edit --task --id ([\\d]+) --priority ([\\S]+)$");
    private static Pattern editTaskDeadLinePattern = Pattern.compile("^edit --task --id ([\\d]+) --Deadline ([\\S]+)$");
    private static Pattern removeUserPattern = Pattern.compile("^edit --task --id ([\\d]+) --assignedUsers ([\\S]+) --remove$");
    private static Pattern addUserPattern = Pattern.compile("^edit --task --id ([\\d]+) --assignedUsers ([\\S]+) --add$");

    @Override
    public String getName() {
        return  "Task Page";
    }

    public void run(User user){
        Scanner scanner = new Scanner(System.in);
        TaskController taskController = new TaskController();
        while(true) {
            String input = scanner.nextLine();
            if (editTaskTitlePattern.matcher(input).matches()){
                Matcher matcher = editTaskTitlePattern.matcher(input);
                while (matcher.find()) {
                    Integer taskId = Integer.parseInt(matcher.group(1));
                    String title = matcher.group(2);
                    taskController.editTaskTitle(user, taskId, title);
                }
            }else if (editTaskDescriptionPattern.matcher(input).matches()){
                Matcher matcher = editTaskDescriptionPattern.matcher(input);
                while (matcher.find()){
                    Integer taskId = Integer.parseInt(matcher.group(1));
                    String description = matcher.group(2);
                    taskController.editTaskDescription(user, taskId, description);
                }
            }else if (editTaskPriorityPattern.matcher(input).matches()){
                Matcher matcher = editTaskPriorityPattern.matcher(input);
                while (matcher.find()){
                    Integer taskId = Integer.parseInt(matcher.group(1));
                    String priority = matcher.group(2);
                    taskController.editTaskPriority(user, taskId, priority);
                }
            }else if (editTaskDeadLinePattern.matcher(input).matches()){
                Matcher matcher = editTaskDeadLinePattern.matcher(input);
                while (matcher.find()){
                    Integer taskId = Integer.parseInt(matcher.group(1));
                    String deadLine = matcher.group(2);
                    taskController.editTaskDeadline(user, taskId, deadLine);
                }
            }else if (removeUserPattern.matcher(input).matches()){
                Matcher matcher = removeUserPattern.matcher(input);
                while (matcher.find()){
                    Integer taskId = Integer.parseInt(matcher.group(1));
                    String username = matcher.group(2);
                    taskController.removeUserFromTaskList(user, taskId, username);
                }
            }else if (addUserPattern.matcher(input).matches()){
                Matcher matcher = addUserPattern.matcher(input);
                while (matcher.find()){
                    Integer taskId = Integer.parseInt(matcher.group(1));
                    String username = matcher.group(2);
                    taskController.addUserFromTaskList(user, taskId, username);
                }
            }else if (input.equals("back")){
                return;
            }else {
                System.out.println("invalid command");
            }
        }
    }
}
