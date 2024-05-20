package sample;

import controller.TaskController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Task;
import model.Team;
import model.User;
import util.UserAccessChecker;

import java.io.IOException;
import java.util.Objects;

public class openTaskController {
    public Tab openTaskTab;
    public Label titleBox;
    public Label idBox;
    public Label priorityBox;
    public Label creationBox;
    public Label deadlineBox;
    public Label descriptionBox;
    public Tab editTaskTab;
    public TabPane tabPane;
    public AnchorPane editTaskAnchorPane;
    public AnchorPane headAnchorPane;
    public VBox usersList;
    public TextField titleField;
    public TextField deadlineField;
    public TextField descriptionField;
    public MenuButton removeUserDropDown;
    public MenuButton assignUserDropDown;
    public TaskController taskController;
    private User loggedInUser;
    public Task task;

    public void initialize(){
        loggedInUser = User.getLoggedInUser();
        task = loggedInUser.getCurrentTask();
        taskController=new TaskController();

        if (!UserAccessChecker.isLeader(loggedInUser)) {
            tabPane.getTabs().remove(editTaskTab);
        }
        setBoxes();

        setAssignUserDropDown();
        setRemoveUserDropDown();
    }

    public void setAssignUserDropDown(){
        if (Team.teamUsersMap.get(task.getTeam())!=null) {
            for (User user : Team.teamUsersMap.get(task.getTeam())) {
                if (!Task.taskUsersMap.get(task).contains(user)) {
                    MenuItem menuItem = new MenuItem(user.getUsername());
                    assignUserDropDown.getItems().add(menuItem);
                    menuItem.setOnAction(event -> {
                        assignUser(user);
                    });
                }
            }
        }
    }

    public void setRemoveUserDropDown(){
        if (Team.teamUsersMap.get(task.getTeam())!=null) {
            for (User user : Team.teamUsersMap.get(task.getTeam())) {
                if (Task.taskUsersMap.get(task).contains(user)) {
                    MenuItem menuItem = new MenuItem(user.getUsername());
                    removeUserDropDown.getItems().add(menuItem);
                    menuItem.setOnAction(event -> {
                        removeUser(user);
                    });
                }
            }
        }
    }

    public void assignUser(User user){
        taskController.addUserFromTaskList(loggedInUser , task.getId() , user.getUsername());
        setAssignUserDropDown();
        setRemoveUserDropDown();
    }

    public void removeUser(User user){
        taskController.removeUserFromTaskList(loggedInUser , task.getId() , user.getUsername());
        setAssignUserDropDown();
        setRemoveUserDropDown();
    }

    public void changeTitle(ActionEvent actionEvent) {
        task.setTitle(titleField.getText());
        setBoxes();
    }

    public void changeDeadline(ActionEvent actionEvent) {
        taskController.editTaskDeadline(loggedInUser , task.getId() , deadlineField.getText());
        setBoxes();
    }

    public void changeDescription(ActionEvent actionEvent) {
        task.setDescription(descriptionField.getText());
        setBoxes();
    }

    public void setBoxes(){
        openTaskTab.setText(task.getTitle());
        titleBox.setText(task.getTitle());
        idBox.setText(Integer.toString(task.getId()));
        priorityBox.setText(task.getPriority());
        creationBox.setText(task.getCreateDate().toString());
        deadlineBox.setText(task.getDeadline().toString());
        descriptionBox.setText(task.getDescription());

        if (Task.taskUsersMap.containsKey(task)) {
            for (User user : Task.taskUsersMap.get(task)) {
                usersList.getChildren().add(new Label(user.getUsername()));
            }
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        loggedInUser.setCurrentTask(null);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/tasksPage.fxml")));
        ((Stage) titleBox.getScene().getWindow()).setScene(new Scene(root));
    }

    public void highestButton(ActionEvent actionEvent) {
        task.setPriority("Highest");
        setBoxes();
    }

    public void highButton(ActionEvent actionEvent) {
        task.setPriority("High");
        setBoxes();
    }

    public void lowButton(ActionEvent actionEvent) {
        task.setPriority("Low");
        setBoxes();
    }

    public void lowestButton(ActionEvent actionEvent) {
        task.setPriority("Lowest");
        setBoxes();
    }
}
