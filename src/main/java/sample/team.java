package sample;

import controller.TaskController;
import controller.TeamController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Team;
import model.User;
import util.UserAccessChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class team {
    public User loggedInUser;
    public Team team;
    public User chosenMember;
    public VBox membersVBox;
    public AnchorPane alertAnchorPane;
    public AnchorPane mainAnchorPane;
    public AnchorPane closeAlert;
    public TaskController taskController;
    public TeamController teamController;


    public void initialize(){
        loggedInUser = User.getLoggedInUser();
        team = loggedInUser.getCurrentTeam();

        chosenMember = null;

        taskController = new TaskController();
        teamController = new TeamController();

        mainAnchorPane.getChildren().remove(alertAnchorPane);

        addToMembersList(null);

    }

    public List<User> addToMembersList(ArrayList<User> givenUsers) {
        membersVBox.getChildren().removeAll(membersVBox.getChildren());

        if (givenUsers == null) {

            List<User> users;
            if (loggedInUser != null && team != null && UserAccessChecker.isLeader(loggedInUser)) {
                users = Team.teamUsersMap.get(team);
                if (users!= null && !users.isEmpty()){
                    for (User member : users){
                        Label label = new Label(member.getUsername());
                        label.setCursor(Cursor.HAND);
                        label.setTextFill(Color.BLUE.darker());
                        label.setOnMouseClicked(event -> {
                            chosenMember = member;
                            showAlert();
                        });
                        membersVBox.getChildren().add(label);
                    }
                }

            } else if (loggedInUser != null && team != null && UserAccessChecker.isMember(loggedInUser)) {
                users = Team.teamUsersMap.get(team);
                if (users!= null && !users.isEmpty()) {
                    for (User member : users) {
                        Label label = new Label(member.getUsername());
                        label.setCursor(Cursor.HAND);
                        label.setTextFill(Color.BLUE.darker());
                        label.setOnMouseClicked(event -> {
                            openProfile(member);
                        });
                        membersVBox.getChildren().add(label);
                    }
                }

            } else {
                users = null;
            }

            return users;
        }

        if (!givenUsers.isEmpty()){
            for (User member : givenUsers){
                Label label = new Label(member.getUsername());
                label.setCursor(Cursor.HAND);
                label.setTextFill(Color.BLUE.darker());
                if (UserAccessChecker.isLeader(loggedInUser)){
                    label.setOnMouseClicked(event -> {
                        chosenMember = member;
                        showAlert();
                    });
                }
                else if (UserAccessChecker.isMember(loggedInUser)){
                    label.setOnMouseClicked(event -> {
                        openProfile(member);
                    });
                }
                membersVBox.getChildren().add(label);
            }
        }
        return givenUsers;
    }

    private void showAlert() {
        mainAnchorPane.getChildren().add(alertAnchorPane);
        closeAlert.setOnMouseClicked(event -> {
            chosenMember=null;
            mainAnchorPane.getChildren().remove(alertAnchorPane);
        });
    }

    public void openProfile(User member){

    }

    public void sendNotification(ActionEvent actionEvent) {

    }

    public void showProfile(ActionEvent actionEvent) {
        openProfile(chosenMember);
    }

    public void removeMember(ActionEvent actionEvent) {
        teamController.deleteMember(loggedInUser , team , chosenMember.getUsername());
        addToMembersList(null);
    }

    public void goToChatRoom(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/chatroom.fxml")));
        ((Stage) mainAnchorPane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void back(ActionEvent actionEvent) throws IOException {
        loggedInUser.setCurrentTeam(null);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/teamPage.fxml")));
        ((Stage) mainAnchorPane.getScene().getWindow()).setScene(new Scene(root));
    }
}
