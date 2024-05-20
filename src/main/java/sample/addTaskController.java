package sample;

import controller.TeamController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Task;
import model.Team;
import model.User;
import util.UserAccessChecker;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class addTaskController {
    public TextField titleField;
    public DatePicker deadlineDatePicker;
    public DatePicker creationDatePicker;
    public TextField descriptionField;
    public MenuButton usersDropDown;
    public TeamController teamController;
    private User loggedInUser;
    public String priority;
    public Team team;
    public ArrayList<User> assignedUsers;

    public void initialize(){
        loggedInUser = User.getLoggedInUser();
        assignedUsers = new ArrayList<>();
        for (Team t : Team.teamLeaderMap.keySet()){
            if (Team.teamLeaderMap.get(t).equals(loggedInUser)){
                team = t;
            }
        }
        setUsersDropDown();
    }

    public void addTask(ActionEvent actionEvent) {
        LocalDate creationLocalDate = creationDatePicker.getValue();
        LocalDate deadlineLocalDate = deadlineDatePicker.getValue();

        String creation = creationLocalDate.getYear()+"-"+creationLocalDate.getMonthValue()+"-"+creationLocalDate.getDayOfMonth()+"|00:00";
        String deadline = deadlineLocalDate.getYear()+"-"+deadlineLocalDate.getMonthValue()+"-"+deadlineLocalDate.getDayOfMonth()+"|00:00";

        teamController = new TeamController();

        if (UserAccessChecker.isLeader(loggedInUser)) {
            if (!Team.teamLeaderMap.isEmpty()) {
                for (Team team : Team.teamLeaderMap.keySet()) {
                    if (Team.teamLeaderMap.get(team).equals(loggedInUser) && titleField!=null && creation!=null && deadline!=null) {
                        teamController.createTask(loggedInUser, team, titleField.getText(), creation, deadline);
                        if (priority != null) {
                            Task.tasks.stream().filter(task -> task.getTitle() == titleField.getText()).forEach(task -> {
                                task.setPriority(priority);
                            });
                        }
                        if (!assignedUsers.isEmpty()) {
                            Task.tasks.stream().filter(task -> task.getTitle() == titleField.getText()).forEach(task -> {
                                for (User user : assignedUsers){
                                    teamController.assignTaskToUser(loggedInUser , team , task.getId() , user.getUsername());
                                }
                            });
                        }


                    }
                }
            }

        }
    }

    public void setUsersDropDown(){
        if (Team.teamUsersMap.get(team)!=null && !Team.teamUsersMap.get(team).isEmpty()) {
            for (User teammate : Team.teamUsersMap.get(team)) {
                if (teammate != loggedInUser && !assignedUsers.contains(teammate)) {
                    MenuItem menuItem = new MenuItem();
                    menuItem.setText(teammate.getUsername());
                    usersDropDown.getItems().add(menuItem);
                    menuItem.setOnAction(event -> {
                        assignedUsers.add(teammate);
                        usersDropDown.getItems().remove(menuItem);
                        setUsersDropDown();
                    });
                }
            }
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/tasksPage.fxml")));
        ((Stage) titleField.getScene().getWindow()).setScene(new Scene(root));
    }

    public void highestButton(ActionEvent actionEvent) {
        priority = "Highest";
    }

    public void highButton(ActionEvent actionEvent) {
        priority = "High";
    }

    public void lowButton(ActionEvent actionEvent) {
        priority = "Low";
    }

    public void lowestButton(ActionEvent actionEvent) {
        priority = "Lowest";
    }
}
