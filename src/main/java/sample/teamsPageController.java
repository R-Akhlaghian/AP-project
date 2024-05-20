package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Team;
import model.User;
import util.UserAccessChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class teamsPageController {
    public VBox teamsVBox;
    public User loggedInUser;
    public TextField searchBox;
    public Button searchButton;

    public void initialize(){
        loggedInUser = User.getLoggedInUser();

        addToList(null);
    }

    public ArrayList<Team> addToList(ArrayList<Team> givenTeams){
        teamsVBox.getChildren().removeAll(teamsVBox.getChildren());
        if (givenTeams==null) {
            ArrayList<Team> teams = new ArrayList<>();

            if (loggedInUser != null && UserAccessChecker.isLeader(loggedInUser)) {
                for (Team team : Team.teamLeaderMap.keySet()) {
                    if (Team.teamLeaderMap.get(team).equals(loggedInUser)) {
                        teams.add(team);
                    }
                }
            } else if (loggedInUser != null && UserAccessChecker.isMember(loggedInUser)) {
                for (Team team : Team.teamUsersMap.keySet()) {
                    if (Team.teamUsersMap.get(team).contains(loggedInUser)) {
                        teams.add(team);
                    }
                }
            }

            if (!teams.isEmpty()) {
                for (Team team : teams) {
                    Label teamLabel = new Label(team.getName());
                    teamLabel.setCursor(Cursor.HAND);
                    teamLabel.setTextFill(Color.BLUE.darker());
                    teamsVBox.getChildren().add(teamLabel);
                    teamLabel.setOnMouseClicked(event -> {
                        try {
                            enterTeam(team);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            return teams;
        }
        else{
            if (!givenTeams.isEmpty()){
                for (Team team : givenTeams){
                    Label teamLabel = new Label(team.getName());
                    teamLabel.setCursor(Cursor.HAND);
                    teamLabel.setTextFill(Color.BLUE.darker());
                    teamsVBox.getChildren().add(teamLabel);
                    teamLabel.setOnMouseClicked(event -> {
                        try {
                            enterTeam(team);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            return givenTeams;
        }
    }

    public void search(ActionEvent actionEvent) {
        ArrayList<Team> teams= addToList(null);
        List<String> items = new ArrayList<>();
        ArrayList<Team> temp = new ArrayList<>();
        List<String> searchedList;
        for (Team team : teams){
            items.add(team.getName());
        }
        if (searchBox.getText() !=null) {
            searchedList = searchList(searchBox.getText(), items);
            for (String s : searchedList){
                for (Team team : teams){
                    if (team.getName().equals(s)){
                        temp.add(team);
                    }
                }
            }
            addToList(temp);
        }
        else{
            System.out.println("search box is empty");
        }
    }

    public List<String> searchList(String searchWords , List<String> listOfStrings){
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }


    public void enterTeam(Team team) throws IOException {
        if (team!=null) {
            loggedInUser.setCurrentTeam(team);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/team.fxml")));
            ((Stage) teamsVBox.getScene().getWindow()).setScene(new Scene(root));
        }
    }


    public void back(ActionEvent actionEvent) {}
}
