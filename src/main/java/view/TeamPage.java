package view;

import controller.TeamController;
import model.Team;
import model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeamPage {

    private static Pattern sendMessagePattern = Pattern.compile("^send --message ([\\S]+)$");
    private static Pattern selectTaskPattern = Pattern.compile("^show task --id ([\\d]+)$");
    private static Pattern creatTaskPattern = Pattern.compile("^create task --title ([\\S]+) --startTime ([\\S]+) --deadline ([\\S]+)$");
    private static Pattern addMemberToTeamPattern = Pattern.compile("^Add member --username ([\\S]+)$");
    private static Pattern deleteMemberPattern = Pattern.compile("^delete member --username ([\\S]+)$");
    private static Pattern suspendMemberPattern = Pattern.compile("^suspend member --username ([\\S]+)$");
    private static Pattern promoteMemberPattern = Pattern.compile("^promote --username ([\\S]+)$");

    private static Pattern assignMemberToTaskPattern = Pattern.compile("^assign member --task task ([\\d]+) --username ([\\S]+)$");
    private static Pattern sendNotificationToUserPattern = Pattern.compile("^send --notification ([\\S]+) --username ([\\S]+)$");
    private static Pattern sendNotificationToTeamPattern = Pattern.compile("^send --notification ([\\S]+) --team ([\\S]+)$");

    public void run(User user, Team team) {
        TeamController teamController = new TeamController();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("Scoreboard --show")) {
                teamController.showScoreBoard(team);

            } else if (input.equals("Roadmap --show")) {
                teamController.showRoadMap(team);
            } else if (input.equals("Chatroom --show")) {
                teamController.showChatroom(team);
            } else if (sendMessagePattern.matcher(input).matches()) {
                Matcher matcher = sendMessagePattern.matcher(input);
                while (matcher.find()) {
                    String message = matcher.group(1);
                    teamController.sendMessage(user, team, message);
                    teamController.showChatroom(team);
                }
            } else if (selectTaskPattern.matcher(input).matches()) {
                Matcher matcher = selectTaskPattern.matcher(input);
                while (matcher.find()) {
                    Integer taskId = Integer.parseInt(matcher.group(1));
                    teamController.showTask(team, taskId);
                }
            } else if (input.equals("show tasks")) {
                teamController.showTasks(team);
            } else if (MainMenu.selectMenuPattern.matcher(input).matches()) {
                Matcher matcher = MainMenu.selectMenuPattern.matcher(input);
                while (matcher.find()) {
                    String menuName = matcher.group(1);
                    if (menuName.equals("Board Menu"))
                        new BoardMenu().run(user, team);
                }
            }else if(input.equals("sudo show --all --tasks")){
                teamController.showAllTask(user, team);
            }  else if (creatTaskPattern.matcher(input).matches()) {
                Matcher matcher = creatTaskPattern.matcher(input);
                while (matcher.find()) {
                    String title = matcher.group(1);
                    String startTime = matcher.group(2);
                    String deadline = matcher.group(3);
                    teamController.createTask(user, team, title, startTime, deadline);
                }
            }else if (input.equals("show --members")){
                teamController.showMembers(user, team);
            }else if (addMemberToTeamPattern.matcher(input).matches()) {
                Matcher matcher = addMemberToTeamPattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    teamController.addMember(user, team, username);
                }
            }else if (deleteMemberPattern.matcher(input).matches()) {
                Matcher matcher = deleteMemberPattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    teamController.deleteMember(user, team, username);
                }
            }else if (suspendMemberPattern.matcher(input).matches()) {
                Matcher matcher = suspendMemberPattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    teamController.suspendMember(user, team, username);
                }
            }else if (promoteMemberPattern.matcher(input).matches()) {
                Matcher matcher = promoteMemberPattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    boolean result = teamController.promoteUser(user, team, username);
                    if (result)
                        return;
                }
            }else if (assignMemberToTaskPattern.matcher(input).matches()) {
                Matcher matcher = assignMemberToTaskPattern.matcher(input);
                while (matcher.find()) {
                    Integer taskId  = Integer.parseInt(matcher.group(1));
                    String username = matcher.group(2);
                    teamController.assignTaskToUser(user, team,taskId, username);
                }
            }else if (sendNotificationToUserPattern.matcher(input).matches()) {
                Matcher matcher = sendNotificationToUserPattern.matcher(input);
                while (matcher.find()) {
                    String notification  = matcher.group(1);
                    String username = matcher.group(2);
                    teamController.sendNotificationToUser(user, team, notification, username);
                }
            }else if (sendNotificationToTeamPattern.matcher(input).matches()) {
                Matcher matcher = sendNotificationToTeamPattern.matcher(input);
                while (matcher.find()) {
                    String notification  = matcher.group(1);
                    String teamName = matcher.group(2);
                    teamController.sendNotificationToTeam(user, teamName, notification);
                }
            }else if(input.equals("show --scoreboard")){
                teamController.showScoreBoard(team);
            }else if (input.equals("back")) {
                return;
            } else {
                System.out.println("invalid command");
            }
        }
    }
}
