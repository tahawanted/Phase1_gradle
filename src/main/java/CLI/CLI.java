package CLI;

import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import User.PasswordUsername;
import User.User;
import User.createUser;
import Utility.ClosestMatch;

import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Logger;

import static User.UserFunctions.userLogIn;
import static User.UserFunctions.userLogOut;

public class CLI {
    enum locations{
        preGameEntry, gameEntry, createUser, userPanel, store, collectionsAndDeck, hero,
        cardFabricationAndEnhancement, userSettings, wheelOfFortune, enterCredentials
    }
    private static Logger main_logger = LoggingClass.getMainLoggerInstance();
    private static Logger user_logger = null;
    private static String username; // Logged in as.

    public static boolean inputGeneralCommandHandling(String command, Stack<locations> followedPath, User currentUser){
        /*
        The return value of this function determines whether or not the code should use a break
        in the switch case or rather just continue on.
         */
        if (command.equals("Hearthstone help")) {
            Commands.printAllCommands();
            return true;
        } else if (command.equals("help")) {
            Commands.printValidCommands(followedPath.peek());
            return true;
        }else if (command.equals("exit -a")) {
            inputGeneralCommandHandling("exit", followedPath, currentUser);
            followedPath.pop();
            return true;
        } else if (command.equals("exit")) {
            userLogOut(currentUser);
            for (int i = followedPath.size(); i>2; i--){
                followedPath.pop();
            }
            return true;
        } else if(command.equals("back")){
            if (followedPath.peek().equals(locations.userPanel))
                return inputGeneralCommandHandling("exit", followedPath, currentUser);
            if (followedPath.peek().equals(locations.enterCredentials)){
                followedPath.pop();
                return true;
            } if (followedPath.peek().equals(locations.createUser)){
                followedPath.pop();
                return true;
            }
            followedPath.pop();
            return true;
        } else {
            return false;
        }
    }
    static String readALine(Scanner scanner, Stack<locations> followedPath, boolean []shouldBreak, User currentUser){
        String command = scanner.nextLine();
        shouldBreak[0] = inputGeneralCommandHandling(command, followedPath, currentUser);
        return command;
    }
    static void invalidCommandPrint(String commandToHandle, Stack<locations> followedPath){
        System.out.println("Invalid command. Try again.");
        System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                commandToHandle, Commands.getCurrentLevelCommands(followedPath.peek())));
    }
    public static void main(String[] args) {
        boolean flag = Main_config_file.createRequiredDirectories();
        if(!flag){
            System.out.println("Error could not create the directories");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        String commandToHandle;
        Stack<locations> followedPath = new Stack<>();
        followedPath.push(locations.preGameEntry);
        followedPath.push(locations.gameEntry);
        String  password;
        boolean []shouldBreak = {false};
        User currentUser = null;

        System.out.println("\t\t\t\t\tWelcome to my Hearthstone." +
                "\n Sorry for the inconvenience that you are forced to work" +
                "with this petty command line\n rather than a beautiful graphic user interface. Still, we must work with" +
                "what we have. To see the system help, enter \"Hearthstone help\". To exit the game in any stage, " +
                "enter \"exit -a\". To log out of your user, enter \"exit\"." +
                "\nIf you already have an account, enter y, otherwise enter n:");

        try {

            // HELP is not implemented


            outer: while (true) {
                switch (followedPath.peek()){
                    case preGameEntry:
                        currentUser = null;
                        break outer;
                    case gameEntry:
                        currentUser = null;
                        commandToHandle = readALine(scanner, followedPath, shouldBreak, currentUser);
                        if (shouldBreak[0]) break;
                        if (commandToHandle.equals("n") || commandToHandle.equals("no")
                                || commandToHandle.equals("No") || commandToHandle.equals("N")){
                            followedPath.push(locations.createUser);
                        }
                        else if (commandToHandle.equals("y") || commandToHandle.equals("Y")
                                || commandToHandle.equals("Yes") || commandToHandle.equals("yes")){
                            followedPath.push(locations.enterCredentials);
                        } else {
                            invalidCommandPrint(commandToHandle, followedPath);
                        }
                        break;

                    case enterCredentials:
                        currentUser = null;
                        System.out.println("Username:");
                        username = readALine(scanner, followedPath, shouldBreak, currentUser);
                        if (shouldBreak[0]) break;
                        System.out.println("Password:");
                        password = readALine(scanner, followedPath, shouldBreak, currentUser);
                        if (shouldBreak[0]) break;
                        currentUser = userLogIn(username, password);
                        if (currentUser != null) {
                            main_logger.info("User log in successful. Welcome");
                            followedPath.pop();
                            followedPath.push(locations.userPanel);

                        } else {
                            System.out.println("Log in unsuccessful. Try again.");
                            break;
                        }
                        break;
                    case createUser:
                        currentUser = null;
                        System.out.println(PasswordUsername.passwordFormat);
                        System.out.println("Username:");
                        username = readALine(scanner, followedPath, shouldBreak, currentUser);
                        if (shouldBreak[0]) break;
                        boolean usernameNotTaken = PasswordUsername.CheckUsernameValidity(username);
                        if (!usernameNotTaken) {
                            break;
                        }
                        System.out.println("Password:");
                        password = readALine(scanner, followedPath, shouldBreak, currentUser);
                        if (shouldBreak[0]) break;
                        boolean passwordFormatCheck = false;
                        while (!passwordFormatCheck) {
                            passwordFormatCheck = PasswordUsername.PasswordFormatCheck(password);
                            if (passwordFormatCheck) {
                                followedPath.pop();
                                followedPath.push(locations.userPanel);
                                currentUser = createUser.createAUser(username, password);
                                System.out.println("Password accepted. Welcome to my Hearthstone.");
                                break;
                            }
                            password = readALine(scanner, followedPath, shouldBreak, currentUser);
                            if (shouldBreak[0]) break;
                        }
                        main_logger.info("The current Balance of the user " + currentUser.getWalletBalance());
                        main_logger.info("Your current hero: " + currentUser.getCurrentHeroName() +
                                " with hero level: " + currentUser.getHeroLevel());
                        main_logger.info("The cards in your heroes deck:\n"
                                + currentUser.getCardsInDeck());
                        while (true) {
                            System.out.println("If you wish to see the details of your current cards, enter y, otherwise," +
                                    " enter n to continue to the user panel");
                            commandToHandle = scanner.nextLine();
                            if (commandToHandle.equals("n") || commandToHandle.equals("no")
                                    || commandToHandle.equals("No") || commandToHandle.equals("N")){
                                break;
                            }
                            else if (commandToHandle.equals("y") || commandToHandle.equals("Y")
                                    || commandToHandle.equals("Yes") || commandToHandle.equals("yes")){
                                currentUser.printDeckCards();
                                break;
                            } else {
                                System.out.println("Invalid input. Try again.");
                            }
                        }
                        break;
                    case userPanel:
                        System.out.println("Welcome to the user panel. From here you can access the store, " +
                                "your collections, user settings, hero settings, card manipulation " +
                                "and in future versions, the arena. Enter your command:");
                        commandToHandle = readALine(scanner, followedPath, shouldBreak, currentUser);
                        if(shouldBreak[0]) break;

                        break ;
                }

            }
        } finally {
            // Serialize USER if it is open


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
