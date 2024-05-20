package view;

import model.User;

public class NotificationMenu  extends AbstractMenu {

    @Override
    public String getName() {
        return "Notification Bar";
    }

    public void run(User user) {
        System.out.println(getName());
    }
}
