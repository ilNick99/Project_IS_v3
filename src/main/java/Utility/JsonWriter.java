package main.java.Utility;

import main.java.Project_v3.*;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.JSONArray;
//import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * class to save the net in json file
 */
public class JsonWriter {
    public static final String WRITING_FILE_ERROR = "writing file error.";
    //absolute pathname string of the json directory
    private static final String path = new File("src/main/java/JsonFile").getAbsolutePath();
    private static final String petriPath = new File("src/main/java/JsonPetri").getAbsolutePath();

    /**
     * method to write json
     * @param net
     */
    public static void writeJsonFile(Net net) {
        //get the json string of the net to print in the file
        String stringJson = stringNet(net);
        //get the pathname of the file to create
        String fileToWrite = makeFile();

        //write file
        try {
            FileWriter writer = new FileWriter(fileToWrite);
            writer.write(stringJson);
            writer.close();
        } catch (IOException e) {
            System.out.println(WRITING_FILE_ERROR);
            e.printStackTrace();
        }
    }

    /**
     * method to get the Json string of the net
     * @param net
     * @return stringJson
     */
    private static String stringNet(Net net) {
        assert net != net;
        //initalize the Arraylist of pairs of the net
        ArrayList<Pair> pairsList = net.getNet();

        //initialize the string  id and name of the net
        String nameNet = net.getName();

        //create json object to add to file
        JSONObject netJson = new JSONObject();
        //add propriety to json
        netJson.put("@name", nameNet);

        //initialize the JsonArray of the pairs of the net
        JSONArray pairs = new JSONArray() ;

        //for every pair in the net get place and transition
        for (Pair pair: pairsList) {
            Place place = pair.getPlace();
            //get the string name of the place
            String p = place.getName();

            Transition trans = pair.getTrans();
            //get the string name of the transition
            String t = trans.getName();
            int d = trans.getInputOutput(p);

            //initialize the jsonPair to build
            JSONObject jsonPair = new JSONObject();
            //add to json pair the tag place and the attribute
            jsonPair.put("@place", p);
            //add to json pair the tag transition and the attribute
            jsonPair.put("@transition", t);
            //add to json pair the tag direction and the attribute
            jsonPair.put("@direction", Integer.toString(d));

            //add the json pair to the json array of the pairs of the net
            pairs.put(jsonPair);
        }

        //add to the json net the json array of the pairs of the net
        netJson.put("@pairs", pairs);

        //extract json object the string to print in the file (the parameter is the indentation factor)
        String stringJson = netJson.toString(2);
        return stringJson;
    }

    /**
     * method to create a new json file where save the net and get its pathname
     * @return pathname
     */
    private static String makeFile() {
        //initialize file object of directory
        File directory = new File(path);
        //initialize the String array of the list of file in directory
        String[] pathname = directory.list();
        boolean ctrl;
        String name;
        do {
            //get the name of file insert by user
            name = IO.ReadString("Insert the name of the file:\n");
            //add the extension .json to the name file
            name=name+".json";
            //set ctrl false to exit by the loop
            ctrl = false;
            //for every file in the directory check if the string name exist already or not
            for (String s: pathname) {
                //check if the name input is equal to the string name of file
                if (name.equals(s)) {
                    System.out.println("File already exist, please try again.");
                    //if the name of file is already in the directory set ctrl true and stay in loop
                    ctrl = true;
                }
            }
        } while (ctrl); //until the ctrl boolean variable is true
        //return the path of directory and name of file
        return path+"/"+name;
    }

    /**
     * method to get the Json string of the Petri's net
     * @param net
     * @return stringJson
     */
    public static String stringPetriNet(PetriNet net) {
        //initalize the Arraylist of pairs of the net
        ArrayList<Pair> pairsList = net.getNet();

        //initialize the string  id and name of the net
        String nameNet = net.getName();

        //create json object to add to file
        JSONObject netJson = new JSONObject();
        //add propriety to json
        netJson.put("@name", nameNet);

        //initialize the JsonArray of the pairs of the net
        JSONArray pairs = new JSONArray();

        //for every pair in the net get place and transition
        for (Pair pair: pairsList) {
            Place place = pair.getPlace();
            //get the string name of the place
            String p = place.getName();
            //get the number of token of the place
            int token = place.getNumberOfToken();
            //build the transition object from pair
            Transition trans = pair.getTrans();
            int weight = pair.getWeight();
            //get the string name of the transition
            String t = trans.getName();
            int d = trans.getInputOutput(p);

            //initialize the jsonPair to build
            JSONObject jsonPair = new JSONObject();
            //initialize the placeJson to build
            JSONObject placeJson = new JSONObject();
            //add to json object place the name, number of token and direction of place
            placeJson.put("@name", p);
            placeJson.put("@token", token);
            //add to json pair the json object place and the attribute
            jsonPair.put("@place", placeJson);
            //add to json pair the tag direction and the attribute (1=input, 0=output)
            jsonPair.put("@direction", Integer.toString(d));
            //add to json pair the tag transition and the attribute
            jsonPair.put("@transition", t);
            //add to json pair the tag wight and the attribute
            jsonPair.put("@weight", weight);
            //add the json pair to the json array of the pairs of the net
            pairs.put(jsonPair);
        }

        //add to the json net the json array of the pairs of the net
        netJson.put("@pairs", pairs);

        //extract json object the string to print in the file (the parameter is the indentation factor)
        String stringJson = netJson.toString(2);
        return stringJson;
    }

    /**
     * method to create a new json file where save the Petri's net and get its pathname
     * @return pathname
     */
    private static String makePetriFile() {
        //initialize file object of directory
        File directory = new File(petriPath);
        //initialize the String array of the list of file in directory
        String[] pathname = directory.list();
        boolean ctrl;
        String name;
        do {
            //get the name of file insert by user
            name = IO.ReadString("Insert the name of the file:\n");
            //add the extension .json to the name file
            name=name+".json";
            //set ctrl false to exit by the loop
            ctrl = false;
            //for every file in the directory check if the string name exist already or not
            if (pathname!=null) {
                for (String s: pathname) {
                    //check if the name input is equal to the string name of file
                    if (name.equals(s)) {
                        System.out.println("File already exist, please try again.");
                        //if the name of file is already in the directory set ctrl true and stay in loop
                        ctrl = true;
                    }
                }
            }
        } while (ctrl); //until the ctrl boolean variable is true
        //return the path of directory and name of file
        return petriPath+"/"+name;
    }

    /**
     * method to write a Petri's net on json
     * @param net
     */
    public static void writeJsonPetri(PetriNet net) {
        //get the json string of the net to print in the file
        String stringJson = stringPetriNet(net);
        //get the pathname of the file to create
        String fileToWrite = makePetriFile();

        //write file
        try {
            FileWriter writer = new FileWriter(fileToWrite);
            writer.write(stringJson);
            writer.close();
        } catch (IOException e) {
            System.out.println(WRITING_FILE_ERROR);
            e.printStackTrace();
        }
    }


}