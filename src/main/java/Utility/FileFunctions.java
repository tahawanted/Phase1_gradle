package Utility;

import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class FileFunctions {

    public static void replaceLine(String username, long userID, String oldLineRegEx, String newLine){
        //Instantiating the File class
        String filePath = LoggingClass.returnUserLoggerPath(username, userID);
        //Instantiating the Scanner class to read the file
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //instantiating the StringBuffer class
        StringBuffer buffer = new StringBuffer();
        //Reading lines of the file and appending them to StringBuffer
        int number_of_lines = 0;
        while (sc.hasNextLine() && number_of_lines < 30) {
            number_of_lines += 1;
            buffer.append(sc.nextLine() + System.lineSeparator());
        }
        String fileContents = buffer.toString();
        //closing the Scanner object
        sc.close();
        //Replacing the old line with new line

        fileContents = fileContents.replaceAll(oldLineRegEx, newLine);
        //instantiating the FileWriter class
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.append(fileContents);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveJsonArray(JSONArray array, String fileLocation){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(array.toJSONString());
        String prettyJsonString = gson.toJson(je);

        try {
            FileWriter writer = new FileWriter(fileLocation);
            writer.append(prettyJsonString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        File file = new File(fileLocation);
        try {
            FileWriter fr = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fr);
            out.write(array.toJSONString());
            out.close();
            fr.close();
        } catch (IOException e) {
            System.out.println(e);
        }

         */
    }
    public static JSONArray loadJsonArray(String fileLocation){
        JSONArray array = null;
        try (FileReader reader = new FileReader(fileLocation)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            array = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return array;
    }
}
