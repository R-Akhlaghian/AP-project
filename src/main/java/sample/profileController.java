package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.Objects;

public class profileController {
    public User loggedInUser;
    public User chosenMember;
    public Label scoreBox;
    public Label usernameBox;
    public Label nameBox;

    public void initialize(){
//        loggedInUser = User.getLoggedInUser();
//        chosenMember = loggedInUser.getChosenMember();

        show();
    }

    public void show(){
        if (chosenMember!=null) {
            nameBox.setText(chosenMember.getName());
            usernameBox.setText(chosenMember.getUsername());
            scoreBox.setText("" + chosenMember.getTotalScore());
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        loggedInUser.setChosenMember(null);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/team.fxml")));
        ((Stage) nameBox.getScene().getWindow()).setScene(new Scene(root));
    }
}
