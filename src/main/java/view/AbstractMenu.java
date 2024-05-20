package view;

import model.User;

public abstract class AbstractMenu {

    public abstract String getName();

    public abstract void run(User user);
}
