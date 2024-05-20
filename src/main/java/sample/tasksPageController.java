package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Task;
import model.User;
import util.UserAccessChecker;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class tasksPageController {
    public ScrollPane scrollPane;
    public VBox vBox;
    public ArrayList<Label> titleLabels = new ArrayList<>();
    public ArrayList<Label> priorityLabels = new ArrayList<>();
    public VBox vboxPriority;

    public MenuItem lexicographicItem;
    public MenuItem priorityItem;
    public MenuItem descendingItem;
    public MenuItem AscendingItem;
    public MenuItem deadlineItem;
    public String currentSort = "L";
    public boolean isCurrentOrderDescending = true;
    public Button searchButton;
    public TextField searchBox;
    public AnchorPane tasksAnchorPane;
    public Button addTaskButton;
    public TabPane tabPane;
    private User loggedInUser;


    public void initialize(){
        loggedInUser = User.getLoggedInUser();
        ArrayList<Task> tasks = orderLexicographic(true);
        if (tasks!=null){
            addToList(tasks);
        }

        if (!UserAccessChecker.isLeader(loggedInUser)) {
            tasksAnchorPane.getChildren().remove(addTaskButton);
        }
    }

    public ArrayList<Task> orderLexicographic(boolean descending){
        List<Task> tasks = Task.userTasksMap.get(loggedInUser);
        if (tasks!=null) {
            ArrayList<String> names = new ArrayList<>();
            for (Task task : tasks) {
                names.add(task.getTitle());
            }
            Collections.sort(names);
            if (!descending) {
                Collections.reverse(names);
            }
            ArrayList<Task> temp = new ArrayList<>();

            for (String s : names) {
                for (Task task : tasks) {
                    if (task.getTitle().equals(s)) {
                        if (!temp.contains(task)) {
                            temp.add(task);
                        }

                    }
                }
            }
            return temp;
        }
        return null;
    }


    public ArrayList<Task> orderPriority(boolean descending){
        ArrayList<Task> tasks = orderLexicographic(true);
        ArrayList<Task> temp = new ArrayList<>();

        if (tasks!=null) {

            ArrayList<String> priorities = new ArrayList<>();
            priorities.add("Highest");
            priorities.add("High");
            priorities.add("Low");
            priorities.add("Lowest");

            for (String priority : priorities) {
                for (Task task : tasks) {
                    if (task.getPriority().equals(priority)) {
                        temp.add(task);
                    }
                }
            }

            if (!descending) {
                Collections.reverse(temp);
            }
            return temp;
        }
        return null;
    }

    public ArrayList<Task> orderDeadline(boolean descending){
        ArrayList<Task> tasks = orderLexicographic(true);
        if (tasks!=null) {
            ArrayList<Task> temp = new ArrayList<>();
            ArrayList<String> dateStrings = new ArrayList<>();
            for (Task task : tasks) {
                dateStrings.add(task.getDeadline().toString());
            }

            Collections.sort(dateStrings);
            for (int j = 0; j < dateStrings.size(); j++) {
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getDeadline().toString().equals(dateStrings.get(j))) {
                        temp.add(tasks.get(i));
                        tasks.remove(tasks.get(i));
                        dateStrings.remove(dateStrings.get(j));
                    }
                }
            }
            if (descending) {
                Collections.reverse(temp);
            }
            return temp;
        }
        return null;
    }

    public void lexicographicButton(ActionEvent actionEvent) {
        ArrayList<Task> tasks;
        if (isCurrentOrderDescending) {
            tasks = orderLexicographic(true);
        }
        else{
            tasks = orderLexicographic(false);
        }
        currentSort = "L";
        if (tasks!=null) {
            addToList(tasks);
        }
    }

    public void priorityButton(ActionEvent actionEvent) {
        ArrayList<Task> tasks;
        if (isCurrentOrderDescending){
            tasks = orderPriority(true);
        }
        else{
            tasks = orderPriority(false);
        }
        currentSort = "P";
        if (tasks!=null) {
            addToList(tasks);
        }
    }

    public void deadlineButton(ActionEvent actionEvent) {
        ArrayList<Task> tasks;
        if (isCurrentOrderDescending) {
            tasks = orderDeadline(true);
        }
        else{
            tasks = orderDeadline(false);
        }
        currentSort = "D";
        if (tasks!=null) {
            addToList(tasks);
        }
    }

    public void descendingButton(ActionEvent actionEvent) {
        ArrayList<Task> tasks;
        if (currentSort.equals("L")){
            tasks = orderLexicographic(true);
        }
        else if(currentSort.equals("P")){
            tasks = orderPriority(true);
        }
        else{
            tasks = orderDeadline(true);
        }
        isCurrentOrderDescending = true;
        if (tasks!=null) {
            addToList(tasks);
        }
    }

    public void AscendingButton(ActionEvent actionEvent) {
        ArrayList<Task> tasks;
        if (currentSort.equals("L")){
            tasks = orderLexicographic(false);
        }
        else if (currentSort.equals("P")){
            tasks = orderPriority(false);
        }
        else{
            tasks = orderDeadline(false);
        }
        isCurrentOrderDescending = false;
        if (tasks!=null) {
            addToList(tasks);
        }
    }


    public void addToList(ArrayList<Task> tasks){
        titleLabels = new ArrayList<>();
        priorityLabels = new ArrayList<>();

        if (tasks!=null) {

            for (int i = 0; i < tasks.size(); i++) {
                Label title = new Label(tasks.get(i).getTitle());
                Label priority = new Label(tasks.get(i).getPriority());
                int finalI = i;
                title.setOnMouseClicked(event -> {
                    loggedInUser.setCurrentTask(tasks.get(finalI));
                    try {
                        openTask();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                priority.setOnMouseClicked(event -> {
                    loggedInUser.setCurrentTask(tasks.get(finalI));
                    try {
                        openTask();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                titleLabels.add(title);
                priorityLabels.add(priority);
            }

            vBox.getChildren().removeAll(vBox.getChildren());
            vboxPriority.getChildren().removeAll(vboxPriority.getChildren());

            for (int i = 0; i < tasks.size(); i++) {
                vBox.getChildren().add(titleLabels.get(i));
                vboxPriority.getChildren().add(priorityLabels.get(i));
            }
        }
    }

    public List<String> searchList(String searchWords , List<String> listOfStrings){
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    public void search(ActionEvent actionEvent) {
        ArrayList<Task> tasks= orderLexicographic(true);
        List<String> items = new ArrayList<>();
        ArrayList<Task> temp = new ArrayList<>();
        List<String> searchedList;
        for (Task task : tasks){
            items.add(task.getTitle());
            items.add(task.getTeam().getName());
        }
        if (!(searchBox.getText() ==null)) {
            searchedList = searchList(searchBox.getText(), items);
            for (String s : searchedList){
                for (Task task : tasks){
                    if (task.getTitle().equals(s) || task.getTeam().getName().equals(s)){
                        temp.add(task);
                    }
                }
            }
            addToList(temp);
        }
        else{
            System.out.println("search box is empty");
        }
    }

    public void addTask(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/addTask.fxml")));
        ((Stage) addTaskButton.getScene().getWindow()).setScene(new Scene(root));
    }

    public void openTask() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/openTask.fxml")));
        ((Stage) vBox.getScene().getWindow()).setScene(new Scene(root));
    }

    public void back(ActionEvent actionEvent) {
    }
}
