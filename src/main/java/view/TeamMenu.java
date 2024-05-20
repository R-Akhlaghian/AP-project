package view;

import controller.TeamController;
import model.Team;
import model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeamMenu extends AbstractMenu{

    private static Pattern enterTeamPattern = Pattern.compile("^Enter team ([a-zA-Z0-9]+)$");
    private static Pattern showTeamPattern = Pattern.compile("^show --team ([a-zA-Z0-9]+)$");
    private static Pattern createTeamPattern = Pattern.compile("^create --team ([\\S]+)$");

    @Override
    public String getName() {
        return "Team Menu";
    }

    public void run(User user){
        TeamController teamController = new TeamController();
        teamController.printUserTeams(user);
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            if (enterTeamPattern.matcher(input).matches()){
                Matcher matcher = enterTeamPattern.matcher(input);
                while (matcher.find()) {
                    String teamNameOrId = matcher.group(1);
                    Team team = teamController.getTeamByUserAndTeamNameOrIndex(user, teamNameOrId);
                    if(team != null)
                        new TeamPage().run(user, team);
                }
            }else if(showTeamPattern.matcher(input).matches()){
                Matcher matcher = showTeamPattern.matcher(input);
                while (matcher.find()) {
                    String teamNameOrId = matcher.group(1);
                    Team team = teamController.showTeamByIdOrName(user, teamNameOrId);
                    if (team != null)
                        new TeamPage().run(user, team);
                }
            }else if(createTeamPattern.matcher(input).matches()){
                Matcher matcher = createTeamPattern.matcher(input);
                while (matcher.find()) {
                    String teamName = matcher.group(1);
                    teamController.createTeam(user, teamName);
                }
            }else if (input.equals("show --teams")){
                teamController.showTeams(user);
            }else if (input.equals("back")){
                return;
            }else {
                System.out.println("invalid command");
            }
        }
    }
}
