package view;

import controller.ProfileController;
import model.Team;
import model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu extends AbstractMenu {

    private static Pattern changePasswordPattern = Pattern.compile("^Profile --change --oldPassword ([\\S]+) --newPassword ([\\S]+)$");
    private static Pattern changeUsernamePattern = Pattern.compile("^Profile --change --username ([\\S]+)$");
    private static Pattern showTeamPattern = Pattern.compile("^Profile --showTeam ([\\S]+)$");


    @Override
    public String getName() {
        return "Profile Menu";
    }

    public void run(User user){
        Scanner scanner = new Scanner(System.in);
        ProfileController profileController = new ProfileController();
        while(true) {
            String input = scanner.nextLine();
            if (changePasswordPattern.matcher(input).matches()){
                Matcher matcher = changePasswordPattern.matcher(input);
                while (matcher.find()) {
                    String oldPassword = matcher.group(1);
                    String newPassword = matcher.group(2);
                    String result = profileController.changePassword(user, oldPassword, newPassword);
                    if(result != null)
                        System.out.println(result);
                }
            }else if (changeUsernamePattern.matcher(input).matches()) {
                Matcher matcher = changeUsernamePattern.matcher(input);
                while (matcher.find()) {
                    String newUsername = matcher.group(1);
                    String result = profileController.changeUsername(user, newUsername);
                    if(result != null)
                        System.out.println(result);
                }
            }else if(input.equals("Profile --showTeams")) {
                profileController.showTeams(user);
            }else if(showTeamPattern.matcher(input).matches()){
                Matcher matcher = showTeamPattern.matcher(input);
                while (matcher.find()) {
                    String teamName = matcher.group(1);
                    profileController.showTeam(user, teamName);
                }
            }else if(input.equals("profile --show logs")){
                profileController.showLogs(user);
            }else if(input.equals("profile --show notifications")){
                profileController.showLeaderNotifications(user);
            }else if (input.equals("Profile --show --myProfile")) {
                profileController.showMyProfile(user);
            }else if (input.equals("back")){
                return;
            }else {
                System.out.println("invalid command");
            }
        }
    }
}
