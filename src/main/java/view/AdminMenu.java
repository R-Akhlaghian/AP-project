package view;

import controller.AdminController;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminMenu {

    private static Pattern showProfilePattern = Pattern.compile("^show profile --usernaem ([\\S]+)$");
    private static Pattern banUserPattern = Pattern.compile("^ban user --user ([\\S]+)$");
    private static Pattern changeUserRolePattern = Pattern.compile("^change role --user ([\\S]+) --role ([\\S]+)$");

    private static Pattern sendNotificationAllPattern = Pattern.compile("^send --notification ([\\S]+) --all$");
    private static Pattern sendNotificationToUserPattern = Pattern.compile("^send --notification ([\\S]+) --user ([\\S]+)$");
    private static Pattern sendNotificationToTeamPattern = Pattern.compile("^send --notification ([\\S]+) --team ([\\S]+)$");


    private static Pattern showScoreBoardPattern = Pattern.compile("^show --scoreboard --team ([\\S]+)$");

    public void run() {
        Scanner scanner = new Scanner(System.in);
        AdminController adminController = new AdminController() ;
        while(true) {
            String input = scanner.nextLine();
            if(showProfilePattern.matcher(input).matches()) {
                Matcher matcher = showProfilePattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    adminController.showUserProfile(username);
                }
            }else if(banUserPattern.matcher(input).matches()) {
                Matcher matcher = banUserPattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    adminController.banUser(username);
                }
            }else if(changeUserRolePattern.matcher(input).matches()) {
                Matcher matcher = changeUserRolePattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    String role = matcher.group(2);
                    adminController.changeUserRole(username, role);
                }
            }else if(sendNotificationAllPattern.matcher(input).matches()) {
                Matcher matcher = sendNotificationAllPattern.matcher(input);
                while (matcher.find()) {
                    String notification = matcher.group(1);
                    adminController.sendNotificationToAll(notification);
                }
            }else if(sendNotificationToUserPattern.matcher(input).matches()) {
                Matcher matcher = sendNotificationToUserPattern.matcher(input);
                while (matcher.find()) {
                    String notification = matcher.group(1);
                    String username = matcher.group(2);
                    adminController.sendNotificationToUser(username, notification);
                }
            }else if(sendNotificationToTeamPattern.matcher(input).matches()) {
                Matcher matcher = sendNotificationToTeamPattern.matcher(input);
                while (matcher.find()) {
                    String notification = matcher.group(1);
                    String teamName = matcher.group(2);
                    adminController.sendNotificationToTeam(teamName, notification);
                }
            }else if(showScoreBoardPattern.matcher(input).matches()) {
                Matcher matcher = showScoreBoardPattern.matcher(input);
                while (matcher.find()) {
                    String teamName = matcher.group(1);
                    adminController.showScoreBoard(teamName);
                }
            }else if(input.equals("show --pendingTeams")){
                adminController.showPendingTeams();
            }else if(input.startsWith("accept --teams ")){
                adminController.acceptTeams(input.replace("accept --teams ","").split(" "));
            }else if(input.startsWith("reject --teams ")){
                adminController.rejectTeams(input.replace("reject --teams ","").split(" "));
            }else if(input.equals("logout")){
                return;
            }else {
                System.out.println("invalid command");
            }
        }
    }
}
