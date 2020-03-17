package Utility;

import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import org.json.simple.JSONArray;

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
    public static void SaveJsonUserListArray(JSONArray array){
        File file = new File(Main_config_file.user_list_location);
        SaveJsonUserListArray(array, file);
    }
    public static void SaveJsonUserListArray(JSONArray array, File file){
        try {
            FileWriter fr = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fr);
            out.write(array.toJSONString());
            out.close();
            fr.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
