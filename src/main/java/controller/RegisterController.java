package controller;

import model.User;

import java.time.LocalDateTime;

public class RegisterController {

    public String createUser(String username, String password, String email) {
        if(User.existUserByUsername(username))
            return "user with username " + username + "already exists!";
        if(User.existUserByEmail(email))
            return "User with this email already exists!";

        User user = new User(username, email, password);
        User.getUsers().add(user);
        return "user created successfully!";
    }

    public User loginUser(String username, String password) {
        User user = User.getUserByUsername(username);
        if(user == null){
            System.out.println("There is not any user with username: " + username + "!");
            return null;
        }

        if(!user.getPassword().equals(password)){
            System.out.println("Username and password didn’t match!");
            return null;
        }
        System.out.println("user logged in successfully!");
        user.getLoginTimes().add(LocalDateTime.now());
        return user;
    }
}
