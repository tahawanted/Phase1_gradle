package CLI;

import LoggingModule.LoggingClass;
import User.PasswordUsername;
import User.createUser;

import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Logger;

import static User.User.userLogIn;
import static User.User.userLogOut;

public class CLI {
    enum locations{
        preGameEntry, gameEntry, createUser, userPanel, store, collectionsAndDeck, hero,
        cardFabricationAndEnhancement, userSettings, wheelOfFortune, enterCredentials
    }
    private static Logger main_logger = LoggingClass.getMainLoggerInstance();
    private static Logger user_logger = null;
    private static String username; // Logged in as.

    public static boolean inputGeneralCommandHandling(String command, Stack<locations> followedPath){
        /*
        The return value of this function determines whether or not the code should use a break
        in the switch case or rather just continue on.
         */
        if (command.equals("Hearthstone help")) {
            Commands.printAllCommands();
            return true;
        } else if (command.equals("exit -a")) {
            inputGeneralCommandHandling("exit", followedPath);
            followedPath.pop();
            return true;
        } else if (command.equals("exit")) {
            userLogOut(username);
            for (int i = followedPath.size(); i>2; i--){
                followedPath.pop();
            }
            return true;
        } else if(command.equals("back")){
            if (followedPath.peek().equals(locations.userPanel))
                return inputGeneralCommandHandling("exit", followedPath);
            if (followedPath.peek().equals(locations.enterCredentials)){
                followedPath.pop();
                return true;
            }
            if (followedPath.peek().equals(locations.createUser)){
                followedPath.pop();
                return true;
            }
            followedPath.pop();
            return true;
        } else {
            return false;
        }
    }
    static String readALine(Scanner scanner, Stack<locations> followedPath, boolean []shouldBreak){
        String command = scanner.nextLine();
        shouldBreak[0] = inputGeneralCommandHandling(command, followedPath);
        return command;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command_to_handle;
        Stack<locations> followedPath = new Stack<>();
        followedPath.push(locations.preGameEntry);
        followedPath.push(locations.gameEntry);
        String  password;
        boolean []shouldBreak = {false};

        System.out.println("\t\t\t\t\tWelcome to my Hearthstone." +
                "\n Sorry for the inconvenience that you are forced to work" +
                "with this petty command line\n rather than a beautiful graphic user interface. Still, we must work with" +
                "what we have. To see the system help, enter \"Hearthstone help\". To exit the game in any stage, " +
                "enter \"exit -a\". To log out of your user, enter \"exit\"." +
                "\nIf you already have an account, enter y, otherwise enter n:");

        try {
            outer: while (true) {
                switch (followedPath.peek()){
                    case preGameEntry:
                        break outer;
                    case gameEntry:
                        command_to_handle = readALine(scanner, followedPath, shouldBreak);
                        if (shouldBreak[0]) break;
                        if (command_to_handle.equals('n') || command_to_handle.equals("no")
                                || command_to_handle.equals("No") || command_to_handle.equals("N")){
                            followedPath.push(locations.createUser);
                        }
                        else if (command_to_handle.equals('y') || command_to_handle.equals("Y")
                                || command_to_handle.equals("Yes") || command_to_handle.equals("yes")){
                            followedPath.push(locations.enterCredentials);
                        }
                        else if (command_to_handle.equals("back") || command_to_handle.equals("exit -a"))
                            followedPath.pop();
                        else{
                            System.out.println("Invalid command. Try again.");
                        }
                        break;

                    case enterCredentials:
                        System.out.println("Username:");
                        username = readALine(scanner, followedPath, shouldBreak);
                        if (shouldBreak[0]) break;
                        System.out.println("Password:");
                        password = readALine(scanner, followedPath, shouldBreak);
                        if (shouldBreak[0]) break;
                        boolean logInStatus = userLogIn(username, password);
                        if (logInStatus) {
                            main_logger.info("User log in successful. Welcome");
                            followedPath.pop();
                            followedPath.push(locations.userPanel);
                        } else {
                            System.out.println("Log in unsuccessful. Try again.");
                            break;
                        }
                        break;
                    case createUser:
                        System.out.println(PasswordUsername.passwordFormat);
                        System.out.println("Username:");
                        username = readALine(scanner, followedPath, shouldBreak);
                        if (shouldBreak[0]) break;
                        boolean usernameNotTaken = PasswordUsername.CheckUsernameValidity(username);
                        if (!usernameNotTaken) {
                            break;
                        }
                        System.out.println("Password:");
                        password = readALine(scanner, followedPath, shouldBreak);
                        if (shouldBreak[0]) break;
                        boolean passwordFormatCheck = false;
                        while (!passwordFormatCheck) {
                            passwordFormatCheck = PasswordUsername.PasswordFormatCheck(password);
                            if (passwordFormatCheck) {
                                followedPath.pop();
                                followedPath.push(locations.userPanel);
                                createUser.createAUser(username, password);
                                System.out.println("Password accepted. Welcome to my Hearthstone.");
                            }
                        }
                        break;
                    case userPanel:
                }

            }
        } finally {
            main_logger.info("Exiting");

            try {
                user_logger.info("Exiting");
                LoggingClass.closeUserLogger();
            } catch (Exception e){
                main_logger.info(e.getMessage());
                main_logger.info("Ran into this error whilst trying to exit user_logger. It has probably " +
                        "been closed beforehand.");
            }
            LoggingClass.closeMainLogger();
        }
    }
}
