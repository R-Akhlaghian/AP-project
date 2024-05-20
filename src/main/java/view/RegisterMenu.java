package view;

import controller.RegisterController;
import model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMenu {

    private static Pattern createUserPattern = Pattern.compile("^user create --username ([\\S]+) --password1 ([\\S]+) --password2 ([\\S]+) --email Address ([\\S]+)$");
    private static Pattern loginUserPattern = Pattern.compile("^user login --username ([\\S]+) --password ([\\S]+)$");
    private static Pattern emailPattern  = Pattern.compile("^[a-zA-Z0-9.]+@(gmail|yahoo).com$");
    private RegisterController registerController = new RegisterController();


    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
           if (createUserPattern.matcher(input).matches()){
                Matcher matcher = createUserPattern.matcher(input);
                while (matcher.find()) {
                    String username = matcher.group(1);
                    String password1 = matcher.group(2);
                    String password2 = matcher.group(3);
                    String email = matcher.group(4);
                    System.out.println(createUser(username, password1, password2, email));
                }
            }else if(loginUserPattern.matcher(input).matches()){
               Matcher matcher = loginUserPattern.matcher(input);
               while (matcher.find()) {
                   String username = matcher.group(1);
                   String password = matcher.group(2);
                   User user = registerController.loginUser(username, password);
                   if(user != null) {
                       if (username.equals("sysadmin") && username.equals(password))
                           new AdminMenu().run();
                       else
                            new MainMenu().run(user);
                   }
               }
           }else {
                System.out.println("invalid command");
            }
        }
    }

    private String createUser(String username, String password1, String password2, String email) {
        if(!password1.equals(password2)){
            return "Your passwords are not the same!";
        }
        if(!emailPattern.matcher(email).matches())
            return "Email address is invalid!";

        return registerController.createUser(username, password1, email);

    }
}
