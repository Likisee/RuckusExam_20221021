package com.ruckusexam.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameUtil {
    public static boolean isValidUsername(String name) {

        // Regex to check valid username.
        String regex = "^[A-Za-z]\\w{1,10}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the username is empty
        // return false
        if (name == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given username
        // and regular expression.
        Matcher m = p.matcher(name);

        // Return if the username
        // matched the ReGex
        return m.matches();
    }

    public static String getUsername(String name) {

        if (isValidUsername(name)) {
            return name.toLowerCase();
        }
        return null;
    }
}
