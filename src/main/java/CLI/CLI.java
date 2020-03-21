package CLI;

import Card.InitiateCards;
import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import User.PasswordUsername;
import User.User;
import User.createUser;
import Utility.ClosestMatch;

import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Logger;

import static User.DeleteUser.deleteUser;
import static User.PasswordUsername.ChangePassword;
import static User.PasswordUsername.PasswordValidityCheck;
import static User.UserFunctions.userLogIn;
import static User.UserFunctions.userLogOut;

public class CLI {
    enum locations{
        preGameEntry, gameEntry, createUser, userPanel, store, collectionsAndDeck, hero,
        cardFabricationAndEnhancement, userSettings, wheelOfFortune, enterCredentials
    }
    private static Logger main_logger;
    private static Logger user_logger = null;
    private static String username; // Logged in as.

    static User currentUser = null, cloneUser = null;

    public static boolean inputGeneralCommandHandling(String command, Stack<locations> followedPath){
        /*
        The return value of this function determines whether or not the code should use a break
        in the switch case or rather just continue on.
         */
        if (command.equals("Hearthstone help")) {
            if (user_logger != null) {
                user_logger.info("Command: Hearthstone help");
            }
            Commands.printAllCommands();
            return true;
        } else if (command.equals("help")) {
            if (user_logger != null){
                user_logger.info("Command: help");
            }
            Commands.printValidCommands(followedPath.peek());
            return true;
        }else if (command.equals("exit -a")) {
            if (followedPath.peek().equals(locations.collectionsAndDeck) &&
                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() < cloneUser.getDeckSize()){
                main_logger.info("Error. You cannot exit the collection section until you complete your deck");
                user_logger.info("Command: exit -a\nError. Deck not at full capacity.");
                return true;
            } else if (followedPath.peek().equals(locations.collectionsAndDeck) &&
                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() == cloneUser.getDeckSize()){
                main_logger.info("Deck cards OK.");
                cloneUser = null;
            }
            user_logger.info("Command: exit -a");
            inputGeneralCommandHandling("exit", followedPath);
            followedPath.pop();
            return true;
        } else if (command.equals("exit")) {
            if (followedPath.peek().equals(locations.collectionsAndDeck) &&
                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() < cloneUser.getDeckSize()){
                main_logger.info("Error. You cannot exit the collection section until you complete your deck");
                user_logger.info("Command: exit\nError. Deck not at full capacity.");
                return true;
            } else if (followedPath.peek().equals(locations.collectionsAndDeck) &&
                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() == cloneUser.getDeckSize()){
                main_logger.info("Deck cards OK.");
                cloneUser = null;
            }
            user_logger.info("Command: exit");
            cloneUser = null;
            if (currentUser != null)
                currentUser = userLogOut(currentUser);
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
            } if (followedPath.peek().equals(locations.createUser)){
                followedPath.pop();
                return true;
            }
            if (followedPath.peek().equals(locations.collectionsAndDeck) &&
                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() < cloneUser.getDeckSize()) {
                main_logger.info("Error. You cannot exit the collection section until you complete your deck");
                user_logger.info("Command: back\nError. Deck not at full capacity.");
                return true;
            } else if (followedPath.peek().equals(locations.collectionsAndDeck) &&
                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() == cloneUser.getDeckSize()){
                main_logger.info("Deck cards OK.");
                cloneUser = null;
            }
            // ADD IT TO HERE. ALSO ADD A MECHANISM IN THE ABOVE ONES THAT CHECKS CLONEUSER IS NULL AT EVERY PLACE.
            followedPath.pop();
            return true;
        } else {
            return false;
        }
    }
    static String  removeRedundantSpace(String st){
        return st.replaceAll("^[ \t]+|[ \t]+$", "");
    }
    static String readALine(Scanner scanner, Stack<locations> followedPath, boolean []shouldBreak){
        System.out.println("Enter your command:");
        String command = scanner.nextLine();
        command = removeRedundantSpace(command);
        shouldBreak[0] = inputGeneralCommandHandling(command, followedPath);
        return command;
    }
    static void invalidCommandPrint(String commandToHandle, Stack<locations> followedPath){
        System.out.println("Invalid command. Try again.");
        System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                commandToHandle, Commands.getCurrentLevelCommands(followedPath.peek())));
        String [] splittedCommand = commandToHandle.split(" ");
        if (splittedCommand.length > 1)
        System.out.println("Or Perhaps you meant: " + ClosestMatch.getClosestMatch(
                splittedCommand[0], Commands.getCurrentLevelCommands(followedPath.peek())));
    }
    static void detailACard(String commandToHandle){
        String cardName = removeRedundantSpace(commandToHandle.substring(6));
        try {
            currentUser.printCardInformation(cardName);
            user_logger.info("Command: detail " + cardName);
        } catch (Exception e){
            user_logger.info("Command: detail + " +cardName + "\nError. Card name incorrect.");
            System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                    cardName, currentUser.getAllCards()));
        }
    }

    public static void main(String[] args) {
        boolean flag = Main_config_file.createRequiredDirectories();
        if(!flag){
            System.out.println("Error could not create the directories");
            return;
        }
        main_logger =  LoggingClass.getMainLoggerInstance();
        Scanner scanner = new Scanner(System.in);
        String commandToHandle;
        main_logger.info("Should the card objects be instantiated? Enter y if this is a first run or if the cards have changed. Else, enter n:");
        commandToHandle = scanner.nextLine();
        if (commandToHandle.equals("y") || commandToHandle.equals("Y")
                || commandToHandle.equals("Yes") || commandToHandle.equals("yes")){
            InitiateCards.InstantiateAllCards();
            main_logger.info("Instantiated all cards");
        }
        commandToHandle = "help";
        Stack<locations> followedPath = new Stack<>();
        followedPath.push(locations.preGameEntry);
        followedPath.push(locations.gameEntry);
        String  password;
        boolean []shouldBreak = {false};


        System.out.println("\t\t\t\t\tWelcome to my Hearthstone." +
                "\n Sorry for the inconvenience that you are forced to work" +
                "with this petty command line\n rather than a beautiful graphic user interface. Still, we must work with" +
                "what we have.\nTo see the system help, enter \"Hearthstone help\". To exit the game in any stage, " +
                "enter \"exit -a\". To log out of your user, enter \"exit\"." +
                "\nIf you already have an account, enter y, otherwise enter n:");

        try {
            outer: while (true) {
                inner: switch (followedPath.peek()){
                    case preGameEntry:
                        main_logger.info("Pre Game Entry:");
                        currentUser = null;
                        break outer;
                    case gameEntry:
                        cloneUser = null;
                        main_logger.info("Game Entry:");
                        currentUser = null;
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
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
                        main_logger.info("Enter your credentials");
                        currentUser = null;
                        System.out.println("Username:");
                        username = readALine(scanner, followedPath, shouldBreak);
                        if (shouldBreak[0]) break;
                        System.out.println("Password:");
                        password = readALine(scanner, followedPath, shouldBreak);
                        if (shouldBreak[0]) break;
                        currentUser = userLogIn(username, password);
                        if (currentUser != null) {
                            main_logger.info("User log in successful. Welcome");
                            followedPath.pop();
                            followedPath.push(locations.userPanel);
                            user_logger = LoggingClass.getUserLogger();

                        } else {
                            System.out.println("Log in unsuccessful. Try again.");
                            break;
                        }
                        break;
                    case createUser:
                        main_logger.info("Create user:");
                        currentUser = null;
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
                                currentUser = createUser.createAUser(username, password);
                                System.out.println("Password accepted. Welcome to my Hearthstone.");
                                user_logger = LoggingClass.getUserLogger();
                                break;
                            }
                            password = readALine(scanner, followedPath, shouldBreak);
                            if (shouldBreak[0]) break;
                        }
                        main_logger.info("The current Balance of the user " + currentUser.getWalletBalance());
                        main_logger.info("Your current hero: " + currentUser.getCurrentHeroName() +
                                " with hero level: " + currentUser.getHeroLevel());
                        main_logger.info("The cards in your heroes deck:\n"
                                + currentUser.getCardsInDeckString());

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
                                user_logger.info("Command: view deck cards after user creation.");
                                currentUser.printDeckCardsWithDetails();
                                break;
                            } else {
                                System.out.println("Invalid input. Try again. Enter n for No and y for Yes.");
                            }
                        }
                        break;
                    case userPanel:
                        cloneUser = null;
                        main_logger.info("User panel:");
                        System.out.println("Welcome to the user panel. From here you can access the store, " +
                                "your collections, user settings, hero settings, card manipulation " +
                                "and in future versions, the arena. Enter your command:");

                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
                        if(shouldBreak[0]) break;
                        switch (commandToHandle){
                            case "store":
                                user_logger.info("Navigation: store");
                                followedPath.push(locations.store);
                                break;
                            case "collection":
                                user_logger.info("Navigation: collection");
                                followedPath.push(locations.collectionsAndDeck);
                                break;
                            case "hero":
                                user_logger.info("Navigation: hero");
                                followedPath.push(locations.hero);
                                break;
                            case "cardEnhFab":
                                user_logger.info("Navigation: cardEnhFab");
                                followedPath.push(locations.cardFabricationAndEnhancement);
                                break;
                            case "userSet":
                                user_logger.info("Navigation: userSet");
                                followedPath.push(locations.userSettings);
                                break;
                            case "wheelOfFortune":
                                user_logger.info("Navigation: wheelOfFortune");
                                followedPath.push(locations.wheelOfFortune);
                                break;
                            case "play":
                                System.out.println("This section has not yet been implemented.");
                                break;
                            default:
                                invalidCommandPrint(commandToHandle, followedPath);
                        }
                        break ;
                    case store:
                        main_logger.info("Store:");
                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
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
                            String cardName = removeRedundantSpace(commandToHandle.substring(9));

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
                                    currentUser.serializeUser();
                                    break inner;
                                default:
                                    main_logger.info("buyCard returned unexpected result. Exiting");
                                    followedPath.push(locations.preGameEntry);
                                    break inner;
                            }
                        } else if (commandToHandle.contains("sell")){
                            String cardName = removeRedundantSpace(commandToHandle.substring(4));

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
                                    currentUser.serializeUser();
                                    break inner;
                                default:
                                    main_logger.info("sellCard returned unexpected result. Exiting");
                                    followedPath.push(locations.preGameEntry);
                                    break inner;
                            }
                        } else if (commandToHandle.contains("detail")) {
                            detailACard(commandToHandle);
                        }else if (commandToHandle.contains("buyPack")){
                            user_logger.info("Command: buyPack");
                            System.out.println("This section has not yet been implemented.");
                            //currentUser.serializeUser();
                            break ;

                        } else {
                            invalidCommandPrint(commandToHandle, followedPath);
                        }
                        break ;
                    case collectionsAndDeck:
                        main_logger.info("Collection:");
                        System.out.println("Welcome to the collections. Here you can edit your deck.\n " +
                                "Your deck has a limit of " + currentUser.getDeckSize() + " and it cannot in any way " +
                                "exceed this amount.\nThus to add a card to your deck, you must first make an " +
                                "empty slot.\nTo ensure that your deck stays full, your changes will only be " +
                                "executed every time the deck is at its full capacity");
                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
                        if(shouldBreak[0]) break;
                        // PAY ATTENTION TO THE LINE BELOW IN FUTURE CHAGNES.
                        if (cloneUser == null) {
                            try {
                                cloneUser = currentUser.clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }

                        switch (commandToHandle){
                            case "listDeck":
                                user_logger.info("Command: listDeck");
                                System.out.println(cloneUser.getCardsInDeckString());
                                break inner;
                            case "listAll":
                                user_logger.info("Command: listAll");
                                cloneUser.printAvailableCards();
                                break inner;
                            case "listAddable":
                                user_logger.info("Command: listAddable");
                                System.out.println(cloneUser.getAddableCards());
                                break inner;
                            case "ls":
                                user_logger.info("Command: ls");
                                System.out.println("Printing all available cards.");
                                cloneUser.printAvailableCards();
                                break inner;
                        }
                        if (commandToHandle.contains("detail")){
                            detailACard(commandToHandle);
                        } else if(commandToHandle.contains("add")){
                            String cardName = removeRedundantSpace(commandToHandle.substring(3));
                            int result = cloneUser.addToDeck(cardName);
                            switch (result) {
                                case 404:
                                    user_logger.info("Command: add " + cardName +
                                            "\nError. Card name is wrong");
                                    System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                                            cardName, currentUser.getAllCards()) + "\n");
                                    break inner;
                                case 401:
                                    user_logger.info("Command: add " + cardName + "\nError. You do not own this card.");
                                    break inner;
                                case 409:
                                    user_logger.info("Command: add " + cardName + "\nError. The deck is at full capacity.");
                                    break inner;
                                case 403:
                                    user_logger.info("Command: add " + cardName + "\nError. Maximum instances of card in deck reached.");
                                    break inner;
                                case 200:
                                    user_logger.info("Command: add " + cardName + "\nSuccessful.");
                                    if (cloneUser.currentNumberOfCardsInDeck() == cloneUser.getDeckSize()) {
                                        cloneUser.serializeUser();
                                        currentUser = User.deserializeUser(currentUser.getUsername(), currentUser.getUserID());
                                    }
                                    break inner;
                                default:
                                    main_logger.info("add returned unexpected result. Exiting");
                                    followedPath.push(locations.preGameEntry);
                                    break inner;
                            }

                        } else if(commandToHandle.contains("remove")){
                            String cardName = removeRedundantSpace(commandToHandle.substring(6));
                            int result = cloneUser.removeFromDeck(cardName);
                            switch (result){
                                case 404:
                                    user_logger.info("Command: remove " + cardName +
                                            "\nError. Card name is wrong");
                                    System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                                            cardName, currentUser.getAllCards()));
                                    break inner;
                                case 401:
                                    user_logger.info("Command: remove " + cardName + "\nError. You do not own this card.");
                                    break inner;
                                case 406:
                                    user_logger.info("Command: remove " + cardName + "\nError. The deck is empty.");
                                    break inner;
                                case 403:
                                    user_logger.info("Command: remove " + cardName + "\nError. No instances of card in deck.");
                                    break inner;
                                case 200:
                                    user_logger.info("Command: remove " + cardName + "\nSuccessful. Don't forget to add a card.");
                                    break inner;
                                default:
                                    main_logger.info("remove returned unexpected result. Exiting");
                                    followedPath.push(locations.preGameEntry);
                                    break inner;
                            }
                        } else {
                            invalidCommandPrint(commandToHandle, followedPath);
                        }

                        break ;
                    case hero:
                        main_logger.info("Hero:");
                        System.out.println("" + currentUser.getCurrentHeroIndex() + "\n");
                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
                        if(shouldBreak[0]) break;
                        switch (commandToHandle){
                            case "upHero":
                                user_logger.info("Command: upHero");
                                System.out.println("This section has not yet been implemented.");
                                break inner;
                            case "ls":
                                user_logger.info("Command: ls");
                                currentUser.printAllHeroesInformation();
                                System.out.println("Current hero: " + currentUser.getCurrentHeroName());
                                break inner;
                        }
                        if (commandToHandle.contains("chHero")){
                            String heroName = removeRedundantSpace(commandToHandle.substring(6));
                            int result = currentUser.changeHero(heroName);
                            switch (result){
                                case 404:
                                    user_logger.info("Command chHero " + heroName + "\nError. Hero name incorrect.");
                                    System.out.println("Perhaps you meant: " + ClosestMatch.getClosestMatch(
                                            heroName, currentUser.getHeroNames()));
                                    break inner;
                                case 400:
                                    user_logger.info("Command chHero " + heroName + "\nError. Hero already selected.");
                                    break inner;
                                case 200:
                                    user_logger.info("Command chHero " + heroName + "\nSuccessful.");
                                    currentUser.serializeUser();
                                    break inner;
                                default:
                                    main_logger.info("chHero returned unexpected result. Exiting");
                                    followedPath.push(locations.preGameEntry);
                                    break inner;
                            }
                        } else {
                            invalidCommandPrint(commandToHandle, followedPath);
                        }
                        break;
                    case userSettings:
                        main_logger.info("User settings:");
                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
                        if(shouldBreak[0]) break;
                        String passCheckResult;
                        switch (commandToHandle){
                            case "chPass":
                                System.out.print("Enter the current password: ");
                                String currentPassword = readALine(scanner, followedPath, shouldBreak);
                                if(shouldBreak[0]) break inner;
                                passCheckResult = PasswordValidityCheck(currentUser.getUsername(),
                                        currentUser.getUserID(), currentPassword);
                                switch (passCheckResult){
                                    case "Correct":
                                        break; // this is correct. must not break inner.
                                    case "Incorrect":
                                        user_logger.info("Command: chPass\nError. Password incorrect.");
                                        break inner;
                                    default:
                                        main_logger.info("Password check returned unexpected result. Exiting");
                                        followedPath.push(locations.preGameEntry);
                                        break inner;
                                }
                                System.out.print("Enter the new password: ");
                                String newPasswrod = readALine(scanner, followedPath, shouldBreak);
                                if(shouldBreak[0]) break inner;
                                int result = ChangePassword(currentUser.getUsername(), currentUser.getUserID(),
                                        currentPassword, newPasswrod, currentUser);
                                switch (result){
                                    case 405:
                                        user_logger.info("chPass \n Error. New password format is incorrect.");
                                        break inner;
                                    case 200:
                                        user_logger.info("chPass\nSuccessful.");
                                        currentUser.serializeUser();
                                        break inner;
                                    default:
                                        user_logger.info("chPass\n ChangePassword function returned unexpected result. Exiting");
                                        followedPath.push(locations.preGameEntry);
                                }
                                break inner;
                            case "deleteUser":
                                System.out.print("Enter your password:");
                                String yourPassword = readALine(scanner, followedPath, shouldBreak);
                                if(shouldBreak[0]) break inner;
                                passCheckResult = PasswordValidityCheck(currentUser.getUsername(),
                                        currentUser.getUserID(), yourPassword);
                                switch (passCheckResult){
                                    case "Correct":
                                        break; // this is correct. must not break inner.
                                    case "Incorrect":
                                        user_logger.info("Command: deleteUser\nError. Password incorrect.");
                                        break inner;
                                    default:
                                        main_logger.info("Password check returned unexpected result. Exiting");
                                        followedPath.push(locations.preGameEntry);
                                        break inner;
                                }
                                // RETURN VALUE
                                deleteUser(currentUser.getUsername(), currentUser.getUserID());
                                user_logger.info("Command: deleteUser\nSuccessful. Log in again to continue.");
                                currentUser.serializeUser();
                                user_logger.info("Command: exit");
                                currentUser = userLogOut(currentUser);
                                for (int i = followedPath.size(); i>2; i--){
                                    followedPath.pop();
                                }
                                break inner;
                            default:
                                invalidCommandPrint(commandToHandle, followedPath);
                        }
                        break;
                    case cardFabricationAndEnhancement:
                        main_logger.info("Card enhancement and fabrication:");
                        System.out.println("This section has not yet been implemented");
                        followedPath.pop();
                        break ;
                    case wheelOfFortune:
                        main_logger.info("Wheel of Fortune:");
                        System.out.println("This section has not yet been implemented.");
                        followedPath.pop();
                        break ;
                }

            }
        } catch (Exception e){
            main_logger.info(e.getStackTrace().toString());
            main_logger.info(e.getMessage());
        } finally {
            // Serialize USER if it is open
            main_logger.info("Exiting");

            LoggingClass.closeMainLogger();
        }
    }
}
