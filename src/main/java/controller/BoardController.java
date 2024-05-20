package controller;

import model.Board;
import model.Task;
import model.Team;
import model.User;

import java.util.*;
import java.util.stream.Collectors;

public class BoardController {
    private static final String accessDenied = "You do not have the permission to do this action!";
    private static final String boardNotFinished = "Please finish creating the board first";
    private static final String boardNotFound = "There is no board with this name";

    public void creatNewBoard(User user, Team team, String boardName) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername())) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();
            if (boards.stream().anyMatch(b -> b.getName().equals(boardName))) {
                System.out.println("There is already a board with this name");

            } else {
                Board board = new Board(team, boardName);
                boards.add(board);
            }
            Team.teamBoardsMap.put(team, boards);
        } else {
            System.out.println(accessDenied);
        }
    }

    public void removeBoard(User user, Team team, String boardName) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername())) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();

            Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
            if (boardOptional.isPresent()) {
                boards.remove(boardOptional.get());
            } else {
                System.out.println(boardNotFound);
            }

            Team.teamBoardsMap.put(team, boards);
        } else {
            System.out.println(accessDenied);
        }
    }

    public void addCategoryToBoard(User user, Team team, String boardName, String categoryName) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername())) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();

            Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
            if (boardOptional.isPresent()) {
                Board board = boardOptional.get();
                if (board.getCategories().contains(categoryName)) {
                    System.out.println("The name is already taken for a category!");
                } else {
                    board.getCategories().add(categoryName);
                    board.getCategoriesTaskMap().put(categoryName, new ArrayList<>());
                }

            } else {
                System.out.println(boardNotFound);
            }

        } else {
            System.out.println(accessDenied);
        }
    }

    public void changeCategoryColumnNumber(User user, Team team, String boardName, String categoryName, Integer columnNumber) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername())) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();

            Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
            if (boardOptional.isPresent()) {
                Board board = boardOptional.get();
                List<String> categories = board.getCategories();
                if (categories.contains(categoryName)) {
                    if (columnNumber - 1 > categories.size()) {
                        System.out.println("wrong column!");
                    } else {
                        categories.remove(categoryName);
                        categories.add(columnNumber, categoryName);
                    }
                } else {
                    System.out.println("There is no category with this name");
                }

            } else {
                System.out.println(boardNotFound);
            }

        } else {
            System.out.println(accessDenied);
        }
    }

    public void makeBoardDone(User user, Team team, String boardName) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername())) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();

            Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
            if (boardOptional.isPresent()) {
                Board board = boardOptional.get();
                List<String> categories = board.getCategories();
                if (categories.size() < 3) {
                    System.out.println("Please make a category first");
                } else {
                    board.setState(Board.BoardState.SECOND);
                }

            } else {
                System.out.println(boardNotFound);
            }
        } else {
            System.out.println(accessDenied);
        }
    }

    public void addTaskToBoard(User user, Team team, String boardName, Integer taskId) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername())) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();

            Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
            if (boardOptional.isPresent()) {
                Board board = boardOptional.get();
                if (board.getState().equals(Board.BoardState.FIRST)) {
                    System.out.println(boardNotFinished);
                    return;
                }
                if (board.getTasks().stream().anyMatch(t -> t.getId() == taskId)) {
                    System.out.println("This task has already been added to this board");
                } else {
                    Optional<Task> taskOptional = team.getTasks().stream().filter(t -> t.getId() == taskId).findFirst();
                    if (taskOptional.isPresent()) {
                        Task task = taskOptional.get();
                        if (task.getDeadline().compareTo(new Date()) <= 0) {
                            System.out.println("The deadline of this task has already passed");
                        } else {
                            List<User> users = Task.taskUsersMap.get(task);
                            if (users == null || users.isEmpty()) {
                                System.out.println("Please assign this task to someone first");
                            } else {
                                board.getTasks().add(task);
                                String firstCategory = board.getCategories().get(0);
                                List<Task> tasks = board.getCategoriesTaskMap().get(firstCategory);
                                tasks.add(task);
                                board.getCategoriesTaskMap().put(firstCategory, tasks);
                            }
                        }
                    } else {
                        System.out.println("Invalid task id!");
                    }
                }
            } else {
                System.out.println(boardNotFound);
            }
        } else {
            System.out.println(accessDenied);
        }
    }

    public void assignTaskToMember(User user, Team team, String boardName, String username, Integer taskId) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername())) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();

            Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
            if (boardOptional.isPresent()) {
                Board board = boardOptional.get();
                if (board.getState().equals(Board.BoardState.FIRST)) {
                    System.out.println(boardNotFinished);
                    return;
                }
                List<User> members = Team.teamUsersMap.get(team);
                if (members == null)
                    members = new ArrayList<>();
                Optional<User> memberOptional = members.stream().filter(u -> u.getUsername().equals(username)).findFirst();
                if (!memberOptional.isPresent()) {
                    System.out.println("Invalid teammate");
                    return;
                }
                User member = memberOptional.get();

                Task task = board.getTasks().stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);
                if (task == null) {
                    System.out.println("Invalid task id");
                    return;
                }

                if (board.getCategoriesTaskMap().get("done").contains(task)) {
                    System.out.println("This task has already finished");
                    return;
                }

                Task.assignToUser(task, member);
            } else {
                System.out.println(boardNotFound);
            }
        } else {
            System.out.println(accessDenied);
        }
    }

    public void forceChangeTaskCategory(User user, Team team, String boardName, String taskTitle, String categoryName) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername())) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();

            Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
            if (boardOptional.isPresent()) {
                Board board = boardOptional.get();
                if (board.getState().equals(Board.BoardState.FIRST)) {
                    System.out.println(boardNotFinished);
                    return;
                }

                Task task = board.getTasks().stream().filter(t -> t.getTitle().equals(taskTitle)).findFirst().orElse(null);
                if (task == null) {
                    System.out.println("There is no task with given information");
                    return;
                }

                if (!board.getCategories().contains(categoryName)) {
                    System.out.println("Invalid category");
                    return;
                }
                board.getCategoriesTaskMap().forEach((c, taskList) -> {
                    taskList.remove(task);

                    if (c.equals(categoryName))
                        taskList.add(task);
                });

            } else {
                System.out.println(boardNotFound);
            }
        } else {
            System.out.println(accessDenied);
        }
    }

    public void moveTaskCategory(User user, Team team, String boardName, String taskTitle) {

        List<Board> boards = Team.teamBoardsMap.get(team);
        if (boards == null)
            boards = new ArrayList<>();

        Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            if (board.getState().equals(Board.BoardState.FIRST)) {
                System.out.println(boardNotFinished);
                return;
            }

            Task task = board.getTasks().stream().filter(t -> t.getTitle().equals(taskTitle)).findFirst().orElse(null);
            if (task == null) {
                System.out.println("Invalid task!");
                return;
            }
            if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername()) || Task.taskUsersMap.get(task).contains(user)) {

                if (task.getDeadline().compareTo(new Date()) <= 0) {
                    board.getCategoriesTaskMap().forEach((c, taskList) -> {
                        taskList.remove(task);

                        if (c.equals("failed"))
                            taskList.add(task);
                    });

                    Map<User, Integer> userIntegerMap = Team.teamScoreBoard.get(team);
                    if (userIntegerMap == null)
                        userIntegerMap = new HashMap<>();
                    Integer score = userIntegerMap.get(user);
                    if (score != null) {
                        score -= 5;
                        user.updateScore(-5);
                    }
                    userIntegerMap.put(user, score);

                    return;
                }

                for (Map.Entry<String, List<Task>> entry : board.getCategoriesTaskMap().entrySet()) {
                    if (entry.getValue().contains(task)) {
                        String category = entry.getKey();

                        int index = board.getCategories().indexOf(category);
                        if (index < board.getCategories().size() - 2) {
                            String newCategory = board.getCategories().get(index + 1);
                            List<Task> tasks = board.getCategoriesTaskMap().get(newCategory);
                            tasks.add(task);
                            board.getCategoriesTaskMap().put(newCategory, tasks);

                            entry.getValue().remove(task);

                            if (newCategory.equals("done")) {
                                Map<User, Integer> userIntegerMap = Team.teamScoreBoard.get(team);
                                if (userIntegerMap == null)
                                    userIntegerMap = new HashMap<>();
                                Integer score = userIntegerMap.get(user);
                                if (score == null)
                                    score = 0;

                                score += 10;
                                user.updateScore(10);

                                userIntegerMap.put(user, score);
                            }
                        }
                        return;
                    }

                }


            } else {
                System.out.println("This task is not assigned to you");
            }

        } else {
            System.out.println(boardNotFound);
        }

    }

    public void showBoardTasks(User user, Team team, String boardName, String type) {
        List<Board> boards = Team.teamBoardsMap.get(team);
        if (boards == null)
            boards = new ArrayList<>();

        Optional<Board> boardOptional = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst();
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            if (board.getState().equals(Board.BoardState.FIRST)) {
                System.out.println(boardNotFinished);
                return;
            }
            if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername()) || Team.teamUsersMap.get(team).contains(user)) {

                List<Task> tasks = board.getCategoriesTaskMap().get(type);
                if (tasks != null)
                    tasks.forEach(t -> System.out.println(t.getTitle()));
            } else {
                System.out.println(accessDenied);
            }


        } else {
            System.out.println(boardNotFound);
        }
    }

    public void showBoard(User user, Team team, String boardName) {
        if (Team.teamLeaderMap.get(team).getUsername().equals(user.getUsername()) || Team.teamUsersMap.get(team).contains(user)) {
            List<Board> boards = Team.teamBoardsMap.get(team);
            if (boards == null)
                boards = new ArrayList<>();

            Board board = boards.stream().filter(b -> b.getName().equals(boardName)).findFirst().orElse(null);
            if (board == null)
                return;

            System.out.println("Board name: " + board.getName());
            System.out.println("Board completion: " + (100 * (board.getCategoriesTaskMap().get("done").size() / board.getTasks().size())));
            System.out.println("Board failed: " + (100 * (board.getCategoriesTaskMap().get("failed").size() / board.getTasks().size())));
            System.out.println("Board leader: " + Team.teamLeaderMap.get(team).getUsername());

            Map<String, List<Task>> tasksMapByPriority = board.getTasks().stream().collect(Collectors.groupingBy(Task::getPriority));

            Task.priorities.forEach(p -> {
                System.out.println(p + " Priority: ");
                List<Task> tasks = tasksMapByPriority.get(p);
                if (tasks == null)
                    tasks = new ArrayList<>();
                tasks.stream().sorted((t1, t2) -> t1.getTitle().compareTo(t2.getTitle())).forEach(t -> {
                    System.out.println("Title: " + t.getTitle());
                    String category = getTaskCategory(board, t);
                    System.out.println("Category: " + category);
                    System.out.println("Description; " + t.getDescription());
                    System.out.println("Creation Date: " + t.getCreateDate());
                    System.out.println("Deadline: " + t.getDeadline());
                    List<User> users = Task.taskUsersMap.get(t);
                    if (users == null)
                        users = new ArrayList<>();

                    System.out.println("Assigned to:" + Arrays.toString(users.stream().map(User::getUsername).toArray()));
                    System.out.println("Status: " + category);
                });
            });
        } else {
            System.out.println(accessDenied);
        }
    }

    private String getTaskCategory(Board board, Task t) {
        Map<String, List<Task>> categoriesTaskMap = board.getCategoriesTaskMap();
        for (Map.Entry<String, List<Task>> entry : categoriesTaskMap.entrySet()) {
            if (entry.getValue().contains(t))
                return entry.getKey();
        }

        return "";
    }
}