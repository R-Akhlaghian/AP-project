package view;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu {

    public static Pattern selectMenuPattern = Pattern.compile("^enter menu ([a-zA-Z\\s]+)$");
    private static List<AbstractMenu> menus = new ArrayList<>();

    static {
        menus.add(new CalenderMenu());
        menus.add(new NotificationMenu());
        menus.add(new ProfileMenu());
        menus.add(new TaskMenu());
        menus.add(new TeamMenu());
    }

    public void run(User user) {
        printMenusName();

        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            if(selectMenuPattern.matcher(input).matches()) {
                Matcher matcher = selectMenuPattern.matcher(input);
                while (matcher.find()) {
                    String menuName = matcher.group(1);
                    if(isValidMenuName(menuName)){
                        menus.stream().filter(m -> m.getName().equals(menuName)).forEach(m -> m.run(user));
                    }else {
                        System.out.println("enter valid menuName");
                    }
                }
            }else if(input.equals("logout")){
                return;
            }else {
                System.out.println("invalid command");
            }
        }
    }

    private void printMenusName() {
        System.out.println("Profile Menu");
        System.out.println("Team Menu → { Chat Room – Board Menu– ScoreBoard – RoadMaps }");
        System.out.println("Tasks Page");
        System.out.println("Calendar Menu");
        System.out.println("Notification BarProfile Menu");
    }

    private boolean isValidMenuName(String menuName) {
        return menus.stream().anyMatch(m -> m.getName().equals(menuName));
    }
}
