package CLI;

import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import User.PasswordUsername;
import User.User;
import User.createUser;
import Utility.ClosestMatch;

import java.io.IOException;
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
        command = command.replaceAll("^[ \t]+|[ \t]+$", "");
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
                "what we have.\nTo see the system help, enter \"Hearthstone help\". To exit the game in any stage, " +
                "enter \"exit -a\". To log out of your user, enter \"exit\"." +
                "\nIf you already have an account, enter y, otherwise enter n:");

        try {

            // HELP is not implemented


            outer: while (true) {
                inner: switch (followedPath.peek()){
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
                        switch (commandToHandle){
                            case "store":
                                user_logger.info("Navigation: store");
                                followedPath.push(locations.store);
                                break;
                            case "collection":
                                user_logger.info("Navigation: collections");
                                followedPath.push(locations.store);
                                break;
                            case "hero":
                                user_logger.info("Navigation: hero");
                                followedPath.push(locations.store);
                                break;
                            case "cardEnhFab":
                                user_logger.info("Navigation: cardEnhFab");
                                followedPath.push(locations.store);
                                break;
                            case "userSet":
                                user_logger.info("Navigation: userSet");
                                followedPath.push(locations.store);
                                break;
                            case "wheelOfFortune":
                                user_logger.info("Navigation: wheelOfFortune");
                                followedPath.push(locations.store);
                                break;
                            case "play":
                                System.out.println("This section has not yet been implemented.");
                                break;
                            default:
                                invalidCommandPrint(commandToHandle, followedPath);
                        }
                        break ;
                    case store:
                        commandToHandle = readALine(scanner, followedPath, shouldBreak, currentUser);
                        if(shouldBreak[0]) break;
                        switch (commandToHandle){
                            case "wallet":
                                user_logger.info("Command: wallet");
                                System.out.println("The current wallet balance is: " + currentUser.getWalletBalance());
                                break inner;
                            case "listBuy":
                                user_logger.info("Command: listBuy");
                                System.out.println("The cards that you can buy: \n"
                                        + currentUser.getComplementAvailableCards());
                                break inner;
                            case "listSell":
                                user_logger.info("Command: listSell");
                                System.out.println("The cards that you can sell. Heed attention to the fact that " +
                                        "if a card is currently in your deck, you cannot sell it.");
                                System.out.println(currentUser.getAvailableCards());
                                break inner;
                            case "ls":
                                user_logger.info("Command: ls");
                                System.out.println("The purchasable cards:\n"
                                        + currentUser.getComplementAvailableCards());
                                System.out.println("The cards that you can sell:\n" + currentUser.getAvailableCards());
                                break inner;
                        }
                        if(commandToHandle.contains("buySingle")){
                            String cardName = commandToHandle.substring(9)
                                    .replaceAll("^[ \t]+|[ \t]+$", "");
                            int result = currentUser.buyCard(cardName);
                            switch (result){
                                case 404:
                                    user_logger.info("Command: buySingle " + cardName +
                                            "\nError. Card name is wrong");
                                    System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                                            cardName, currentUser.getAllCards()));
                                    break inner;
                                case 402:
                                    user_logger.info("Command: buySingle " + cardName +
                                            "\nError. Insufficient funds");
                                    break inner;
                                case 400:
                                    user_logger.info("Command: buySingle " + cardName +
                                            "\nError. The card is already in your collection. You high?");
                                    break inner;
                                case 200:
                                    user_logger.info("Command: buySingle " + cardName +
                                            "\nPurchase successful.");
                                    break inner;
                            }
                        } else if (commandToHandle.contains("sell")){
                            String cardName = commandToHandle.substring(4)
                                    .replaceAll("^[ \t]+|[ \t]+$", "");
                            int result = currentUser.sellCard(cardName);
                            switch (result){
                                case 404:
                                    user_logger.info("Command: sell " + cardName +
                                            "\nError. Card name is wrong");
                                    System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                                            cardName, currentUser.getAllCards()));
                                    break inner;
                                case 403:
                                    user_logger.info("Command: sell " + cardName +
                                            "\nError. The card is currently in your deck." +
                                            " First remove it from your deck.");
                                    break inner;
                                case 401:
                                    user_logger.info("Command: sell " + cardName +
                                            "\nError. You do not own this card.");
                                    break inner;
                                case 200:
                                    user_logger.info("Command: sell " + cardName +
                                            "\nTransaction Successful");
                                    break inner;
                            }
                        } else if (commandToHandle.contains("detail")) {
                            String cardName = commandToHandle.substring(4)
                                    .replaceAll("^[ \t]+|[ \t]+$", "");
                            try {
                                currentUser.printCardInformation(cardName);
                                user_logger.info("Command: detail " + cardName);
                            } catch (Exception e){
                                user_logger.info("Command: detail + " +cardName + "\nError. Card name incorrect.");
                                System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                                        cardName, currentUser.getAllCards()));
                            }
                        }else if (commandToHandle.contains("buyPack")){
                            System.out.println("This section has not yet been implemented.");
                        } else {
                            invalidCommandPrint(commandToHandle, followedPath);
                        }
                        break ;
                    case collectionsAndDeck:
                }

            }
        } finally {
            // Serialize USER if it is open


            main_logger.info("Exiting");

            LoggingClass.closeMainLogger();
        }
    }
}
