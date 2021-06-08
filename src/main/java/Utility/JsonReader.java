package main.java.Utility;

import main.java.Project_v3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * class to read Json file
 */
public class JsonReader {
    /**
     * read file method
     *
     * @param pathname
     * @return Net
     * @throws FileNotFoundException
     */
    public static Net readJson(String pathname) throws FileNotFoundException {
        //initialize String object that contains absolute pathname of Json directory
        String path = new File(pathname).getAbsolutePath();

        //initialize StringBuilder object
        StringBuilder sb = new StringBuilder();
        //initialize Scanner object
        Scanner sc = new Scanner(new File(path));

        //while Scanner detect new line append to StringBuilder object the line of json file
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append("\n");
        }
        //System.out.println(sb.toString());
        //initialize JsonObject that contains the file Json
        JSONObject objectJson = new JSONObject(sb.toString());
        //parsing the name and id of the Json file net
        String netName = objectJson.getString("@name");
        //initialize the JsonArray that contains the pairs of the net
        JSONArray pairsNet = objectJson.getJSONArray("@pairs");
        //initialize new net
        Net net = new Net(netName);

        //for every pair in the net build JsonObject composed by place and transition
        for (int i = 0; i < pairsNet.length(); i++) {
            JSONObject obj = (JSONObject) pairsNet.get(i);
            String placeName = obj.getString("@place");
            Place placeIneed;
            if (net.getPlace(placeName) == null) {
                placeIneed = new Place(placeName);
                net.addSetOfPlace(placeIneed);
            }
            else {
                placeIneed = net.getPlace(placeName);
            }

            String transName = obj.getString("@transition");
            int direction = obj.getInt("@direction");
            Transition transitionIneed;
            if (net.getTrans(transName) == null) {
                transitionIneed = new Transition(transName);
                net.addSetOfTransition(transitionIneed);
            }
            else {
                transitionIneed = net.getTrans(transName);
            }


            //initialize new Pair object and add it to the net
            net.addPair(transitionIneed, placeIneed, direction);

        }
        //the net is built and return
        return net;
    }
    /**
     * this method allows to read a Petri's net contain in a file
     *
     * @param pathname
     * @return Net
     * @throws FileNotFoundException
     */
    public static PetriNet readPetriJson(String pathname) throws FileNotFoundException {
        //initialize String object that contains absolute pathname of Json directory
        String path = new File(pathname).getAbsolutePath();

        //initialize StringBuilder object
        StringBuilder sb = new StringBuilder();
        //initialize Scanner object
        Scanner sc = new Scanner(new File(path));

        //while Scanner detect new line append to StringBuilder object the line of json file
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append("\n");
        }
        //System.out.println(sb.toString());
        //initialize JsonObject that contains the file Json
        JSONObject objectJson = new JSONObject(sb.toString());
        //parsing the name and id of the Json file net
        String netName = objectJson.getString("@name");

        //initialize the JsonArray that contains the pairs of the net
        JSONArray pairsNet = objectJson.getJSONArray("@pairs");
        //initialize new net
        Net netToConvert = new Net(netName);
        PetriNet net = new PetriNet(netToConvert);

        //for every pair in the net build JsonObject composed by place and transition
        for (int i = 0; i < pairsNet.length(); i++) {
            JSONObject obj = (JSONObject) pairsNet.get(i);
            JSONObject placeJson = obj.getJSONObject("@place");
            String placeName = placeJson.getString("@name");
            int token = placeJson.getInt("@token");
            int direction = obj.getInt("@direction");
            Place placeIneed;
            if (net.getPlace(placeName) == null) {
                placeIneed = new Place(placeName, token);
                net.addSetOfPlace(placeIneed);
            }
            else {
                placeIneed = net.getPlace(placeName);
            }

            String transName = obj.getString("@transition");
            Transition transitionIneed;
            if (net.getTrans(transName) == null) {
                transitionIneed = new Transition(transName);
                net.addSetOfTransition(transitionIneed);
            }
            else {
                transitionIneed = net.getTrans(transName);
            }
            transitionIneed.addPreOrPost(placeName, direction);

            int weight = obj.getInt("@weight");
            //initialize new Pair object and add it to the net
            Pair pairToAdd = new Pair(placeIneed, transitionIneed, weight);
            //Pair pair = new Pair(placeName, token, trans, direction, weight);

            net.addPair(pairToAdd);
        }
        //the net is built and return
        return net;
    }

}