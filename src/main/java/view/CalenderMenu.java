package view;

import controller.CalenderController;
import model.User;

import java.util.Scanner;

public class CalenderMenu extends AbstractMenu{



    @Override
    public String getName() {
        return "Calender Menu";
    }

    public void run(User user){
        Scanner scanner = new Scanner(System.in);
        CalenderController calenderController = new CalenderController();
        while (true){
            String input = scanner.nextLine();
            if (input.equals("calender --show deadlines")){
                calenderController.showCalender(user);
            }else if (input.equals("back")){
                return;
            }else {
                System.out.println("invalid command");
            }
        }
    }


}
