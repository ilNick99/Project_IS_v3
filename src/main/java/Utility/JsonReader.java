package main.java.Utility;

import main.java.Project_v3.Net;
import main.java.Project_v3.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * class to read Json file
 */
public class JsonReader {
    //private static String path = new File("src/main/java/Json/Net.json").getAbsolutePath();

    /**
     * read file method
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
        System.out.println(sb.toString());
        //initialize JsonObject that contains the file Json
        JSONObject objectJson = new JSONObject(sb.toString());
        //parsing the name and id of the Json file net
        String netName = objectJson.getString("@name");
        String idNet = objectJson.getString("@net");
        //initialize the JsonArray that contains the pairs of the net
        JSONArray pairsNet = objectJson.getJSONArray("@pairs");
        //initialize new net
        Net net = new Net(netName, idNet);

        //for every pair in the net build JsonObject composed by place and transition
        for (int i = 0; i < pairsNet.length(); i++) {
            JSONObject obj = (JSONObject) pairsNet.get(i);
            String place = obj.getString("@place");
            String trans = obj.getString("@transition");
            int direction = obj.getInt("@direction");
            //initialize new Pair object and add it to the net
            Pair pair = new Pair(place, trans, direction);
            net.addPairFromJson(pair);
        }
        //the net is built and return
        return net;
    }
    public static Net readPetriJson(String pathname) throws FileNotFoundException {
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
        String idNet = objectJson.getString("@net");
        //initialize the JsonArray that contains the pairs of the net
        JSONArray pairsNet = objectJson.getJSONArray("@pairs");
        //initialize new net
        Net net = new Net(netName, idNet);

        //for every pair in the net build JsonObject composed by place and transition
        for (int i = 0; i < pairsNet.length(); i++) {
            JSONObject obj = (JSONObject) pairsNet.get(i);
            JSONObject placeJson = obj.getJSONObject("@place");
            String placeName = placeJson.getString("@name");
            int token = placeJson.getInt("@token");
            int direction = obj.getInt("@direction");
            String trans = obj.getString("@transition");
            int weight = obj.getInt("@weight");
            //initialize new Pair object and add it to the net
            Pair pair = new Pair(placeName, token, trans, direction, weight);
            net.addPairFromJson(pair);
        }
        // fill the sets with transitions and nodes
        net.fillSet();
        //the net is built and return
        return net;
    }
}
