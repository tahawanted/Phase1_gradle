package Utility;

import Card.Cards;
import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import User.User;

import java.io.*;
import java.util.logging.Logger;

public class SerializationFunctions {
    private static Logger main_logger = LoggingClass.getMainLoggerInstance();
    public static void Serialize(Object object, String fileLocation){
        //Saving of object in a file
     try{
            FileOutputStream file = new FileOutputStream(fileLocation);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(object);

            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        main_logger.info("Object has been serialized");
    }
    public static User userDeserialize(String fileLocation){
        // You must put the object name like below after you have called this method
        // returnObject = (className) returnObject;
        User returnObject = null;
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = null;
            in = new ObjectInputStream(file);
            // Method for deserialization of object
             returnObject = (User)in.readObject();
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        return returnObject;
    }
    public static Cards.card cardDeserialize(String cardNameOrFileLocation){
        // You must put the object name like below after you have called this method
        // returnObject = (className) returnObject;
        String fileLocation;
        if(cardNameOrFileLocation.contains(".ser"))
            fileLocation = cardNameOrFileLocation;
        else
            fileLocation= Main_config_file.returnCardSaveDataLocation(cardNameOrFileLocation);
        Cards.card returnObject = null;
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = null;
            in = new ObjectInputStream(file);
            // Method for deserialization of object
            returnObject = (Cards.card)in.readObject();
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        return returnObject;
    }
}
