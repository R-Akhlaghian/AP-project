package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private static List<Board> boards = new ArrayList<>();

    public enum BoardState {
        FIRST,
        SECOND;
    }
    private Team team;
    private String name;
    private BoardState state;
    private Map<String, List<Task>> categoriesTaskMap = new HashMap<>();
    private List<Task> tasks = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public Board(Team team, String name){
        this.team = team;
        this.name = name;
        this.state = BoardState.FIRST;
        categories.add("done");
        categories.add("failed");
        categories.forEach(c -> categoriesTaskMap.put(c, new ArrayList<>()));
    }

    public static List<Board> getBoards() {
        return boards;
    }

    public static void setBoards(List<Board> boards) {
        Board.boards = boards;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BoardState getState() {
        return state;
    }

    public void setState(BoardState state) {
        this.state = state;
    }

    public Map<String, List<Task>> getCategoriesTaskMap() {
        return categoriesTaskMap;
    }

    public void setCategoriesTaskMap(Map<String, List<Task>> categoriesTaskMap) {
        this.categoriesTaskMap = categoriesTaskMap;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
