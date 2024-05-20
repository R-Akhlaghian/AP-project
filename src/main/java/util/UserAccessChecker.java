package util;

import model.User;

public class UserAccessChecker {

    public static boolean isLeader(User user) { return user.getUserType().equals("Team Leader"); }

    public static boolean isMember(User user){
        return user.getUserType().equals("Team Member");
    }

    public static boolean isAdmin(User user) {
        return user.getUserType().equals("sysadmin");
    }
}
