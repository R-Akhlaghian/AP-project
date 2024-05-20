package sample;

import controller.TeamController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Team;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class chatroomController {
    public Team team;
    public User loggedInUser;
    public TeamController teamController;
    public VBox messagesVBox;
    public TextField textBox;
    public Rectangle sendButton;
    public AnchorPane mainAnchorPane;

    public void initialize(){
        loggedInUser = User.getLoggedInUser();
        team = loggedInUser.getCurrentTeam();

        teamController = new TeamController();



        addToMessages();
    }

    public void addToMessages(){
        messagesVBox.getChildren().removeAll(messagesVBox.getChildren());

        if (loggedInUser!=null && team!=null){
            List<Map.Entry<User , String>> notifications = team.getNotifications();
            for (Map.Entry<User , String> message : notifications){
                Label username = new Label(message.getKey().getUsername()+":");
                username.setTextFill(Color.PURPLE.darker());
                messagesVBox.getChildren().add(username);
                Label text = new Label(message.getValue());
                text.setTextFill(Color.DARKBLUE.brighter());
                messagesVBox.getChildren().add(text);
                messagesVBox.getChildren().add(new Label("_________________________________________________________________________"));
            }
        }

    }

    public void send(ActionEvent actionEvent) {
        teamController.sendMessage(loggedInUser , team , textBox.getText());
        addToMessages();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/team.fxml")));
        ((Stage) messagesVBox.getScene().getWindow()).setScene(new Scene(root));
    }
}
