/*
 * @author: Michael Hawkins
 * @description: Application that takes input from a file, converts to a HashMap, writes back to file
 */
package CompSci;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    
    //this takes an input file and maps the contents to a custom MyHashMap
    public static MyHashMap<Integer, String> hashmapFromFile(String inputFile) throws FileNotFoundException, IOException {
            MyHashMap<Integer, String> fileData = new MyHashMap<Integer, String>();
            Path path = Paths.get(inputFile);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines){                
                //split the line into an array of multiple parts; delimited by the colon
                //check for comment lines with double slashes
                if (!line.startsWith("//") && line.length() > 0){   
                    //split by the delimiter into an array
                    String[] lineparts = line.split(":");
                    //put the parts of the array into the hashmap
                    fileData.put(Integer.parseInt(lineparts[0]), lineparts[1]);
                }
            }
            return fileData;
    }
    
    //this prints the HashMap as a list for the user to manipulate from the command line
    public static void printSelectionList(MyHashMap<Integer, String> hashMap){
        if (hashMap.size() > 0 && !hashMap.isEmpty()){
            Set<MyMap.Entry<Integer, String>> set = hashMap.entrySet();
            for (MyMap.Entry<Integer, String> entry : set){
                System.out.println(Integer.toString(entry.key) + ": " + entry.value);
            }
        }else{
            //if the file is empty, the hashmap will be, too.
            //so, we tell the user nothing was found
            System.out.println("None found.");
        }      
    }
    
    //this is invoked with the "w" command and writes the hashmap back to the file
    public static void writeHashMapToFile(MyHashMap<Integer, String> hashMap, String fileName) throws FileNotFoundException{
        PrintWriter writer = new PrintWriter(fileName);
        //make sure there are items in the hashmap first
        if (hashMap.size() > 0 && !hashMap.isEmpty()){
            Set<MyMap.Entry<Integer, String>> set = hashMap.entrySet();
            for (MyMap.Entry<Integer, String> entry : set){
               writer.println(Integer.toString(entry.key) + ":" + entry.value);
            }
        }else{
            //nothing was found, clear to file contents to reflect the empty hashmap instance
            writer.print("");
        } 
        writer.close();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //this initial MyHashMap instance set from the intput file in the project's path
        MyHashMap<Integer, String> input = Main.hashmapFromFile("hashfile.txt");
        
        System.out.println("Instructions:\n Type /add: <value> to add a value, type /delete: <key> to delete a value by its key,\n where <key> is the integer seen to the left of the printed values (viewed with the 'P' command).\n Type 'P' to print, 'W' to write the file, and 'Q' to quit.");
        System.out.println("\nCurrent Data Contents:\n--------------------");
        printSelectionList(input);
        System.out.println("--------------------");
        boolean done = false;
        Scanner in = new Scanner(System.in);
        while (!done){
            //get the user's command
            System.out.println("Type Command:");
            String userInput = in.nextLine();
            //user wants to quit, we are done
            if (userInput.equalsIgnoreCase("Q")){
                done = true;
                break;
            //prints the list in a nice format for the user
            }else if(userInput.equalsIgnoreCase("P")){
                System.out.println("Current List:\n--------------------");
                printSelectionList(input);
                System.out.println("--------------------");
            //writes the hashmap back to the initial file
            }else if(userInput.equalsIgnoreCase("W")){
                writeHashMapToFile(input, "hashFile.txt");
            }else{
                //checking user commands for add and delete and taking appropriate action
                if (userInput.contains("/add:")){
                    String[] commandParts = userInput.split(":");
                    Random rand = new Random();
                    int posRandInt = rand.nextInt(Integer.MAX_VALUE);
                    while (input.containsKey(posRandInt)){
                        posRandInt = rand.nextInt(Integer.MAX_VALUE);
                    }
                    input.put(posRandInt, commandParts[1].trim());
                }else if (userInput.contains("/delete:")){
                    String[] commandParts = userInput.split(":");
                    input.remove(Integer.parseInt(commandParts[1].trim()));
                }
            }            
        }
        
    }
}
