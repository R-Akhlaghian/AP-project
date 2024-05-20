package controller;


import model.Task;
import model.User;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class CalenderController {
    public void showCalender(User user) {
        List<Task> tasks = Task.userTasksMap.get(user);
        if (tasks == null || tasks.isEmpty()){
            System.out.println("no deadlines");
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       tasks.stream().sorted(Comparator.comparingLong(Task::getDeadlineRemainingDay))
                .forEach(t-> System.out.println(getTaskStar(t.getDeadlineRemainingDay()) + simpleDateFormat.format(t.getDeadline()) + "__remaining days:" + t.getDeadlineRemainingDay()));
    }

    private String getTaskStar(Long day) {
        if (day < 4)
            return "***";
        else if(day <= 10)
            return "**";
        else
            return "*";
    }
}
