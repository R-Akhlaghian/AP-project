package view;

import controller.BoardController;
import model.Team;
import model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardMenu {

    private static Pattern createNewBoardPattern = Pattern.compile("^board --new --name ([\\S]+)$");
    private static Pattern removeBoardPattern = Pattern.compile("^board --remove --name ([\\S]+)$");
    private static Pattern addCategoryPattern = Pattern.compile("^board --new --category ([\\S]+) --name ([\\S]+)$");
    private static Pattern changeCategoryColumnPattern = Pattern.compile("^board --category ([\\S]+) --column ([\\d]+) --name ([\\S]+)$");
    private static Pattern makeBoardDonePattern = Pattern.compile("^board --done --name ([\\S]+)$");

    private static Pattern addTaskToBoardPattern = Pattern.compile("^board --add ([\\d]+) --name ([\\S]+)$");
    private static Pattern assignTaskToTeamMemberPattern = Pattern.compile("^board --assign ([\\S]+) --task ([\\d]+) --name ([\\S]+)$");
    private static Pattern forceChangeTaskCategoryPattern = Pattern.compile("^board --force --category ([\\S]+) --task ([\\S]+) --name ([\\S]+)$");
    private static Pattern moveTaskCategoryPattern = Pattern.compile("^board --category next --task ([\\S]+) --name ([\\S]+)$");
    private static Pattern showDoneFailTaskPattern = Pattern.compile("^board --show ([\\S]+) --name --board ([\\S]+)$");
    private static Pattern showBoardPattern = Pattern.compile("^Board --show --name ([\\S]+)$");

    public void run(User user, Team team) {
        BoardController boardController = new BoardController();
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            if(createNewBoardPattern.matcher(input).matches()){
                Matcher matcher = createNewBoardPattern.matcher(input);
                while (matcher.find()) {
                    String boardName = matcher.group(1);
                    boardController.creatNewBoard(user, team, boardName );
                }
            }else if(removeBoardPattern.matcher(input).matches()){
                Matcher matcher = removeBoardPattern.matcher(input);
                while (matcher.find()) {
                    String boardName = matcher.group(1);
                    boardController.removeBoard(user, team, boardName );
                }
            }else if(addCategoryPattern.matcher(input).matches()){
                Matcher matcher = addCategoryPattern.matcher(input);
                while (matcher.find()) {
                    String categoryName = matcher.group(1);
                    String boardName = matcher.group(2);
                    boardController.addCategoryToBoard(user, team, boardName, categoryName);
                }
            }else if(changeCategoryColumnPattern.matcher(input).matches()){
                Matcher matcher = changeCategoryColumnPattern.matcher(input);
                while (matcher.find()) {
                    String categoryName = matcher.group(1);
                    Integer columnNumber = Integer.parseInt(matcher.group(2));
                    String boardName = matcher.group(3);
                    boardController.changeCategoryColumnNumber(user, team, boardName, categoryName, columnNumber);
                }
            }else if(makeBoardDonePattern.matcher(input).matches()){
                Matcher matcher = makeBoardDonePattern.matcher(input);
                while (matcher.find()) {
                    String boardName = matcher.group(1);
                    boardController.makeBoardDone(user, team, boardName);
                }
            }else if(addTaskToBoardPattern.matcher(input).matches()){
                Matcher matcher = addTaskToBoardPattern.matcher(input);
                while (matcher.find()) {
                    Integer taskId = Integer.parseInt(matcher.group(1));
                    String boardName = matcher.group(2);
                    boardController.addTaskToBoard(user, team, boardName, taskId);
                }
            }else if(assignTaskToTeamMemberPattern.matcher(input).matches()){
                Matcher matcher = assignTaskToTeamMemberPattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    Integer taskId = Integer.parseInt(matcher.group(2));
                    String boardName = matcher.group(3);
                    boardController.assignTaskToMember(user, team, boardName, username, taskId);
                }
            }else if(forceChangeTaskCategoryPattern.matcher(input).matches()){
                Matcher matcher = forceChangeTaskCategoryPattern.matcher(input);
                while (matcher.find()) {
                    String categoryName = matcher.group(1);
                    String taskTitle = matcher.group(2);
                    String boardName = matcher.group(3);
                    boardController.forceChangeTaskCategory(user, team, boardName, taskTitle, categoryName);
                }
            }else if(moveTaskCategoryPattern.matcher(input).matches()){
                Matcher matcher = moveTaskCategoryPattern.matcher(input);
                while (matcher.find()) {
                    String taskTitle = matcher.group(1);
                    String boardName = matcher.group(2);
                    boardController.moveTaskCategory(user, team, boardName, taskTitle);
                }
            }else if(showDoneFailTaskPattern.matcher(input).matches()){
                Matcher matcher = showDoneFailTaskPattern.matcher(input);
                while (matcher.find()) {
                    String type = matcher.group(1);
                    String boardName = matcher.group(2);
                    boardController.showBoardTasks(user, team, boardName, type);
                }
            }else if(showBoardPattern.matcher(input).matches()){
                Matcher matcher = showBoardPattern.matcher(input);
                while (matcher.find()) {
                    String boardName = matcher.group(1);
                    boardController.showBoard(user, team, boardName );
                }
            }else if (input.equals("back")){
                return;
            }else {
                System.out.println("invalid command");
            }
        }
    }
}
