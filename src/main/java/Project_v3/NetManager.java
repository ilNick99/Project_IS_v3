package main.java.Project_v3;

import Utility.IO;
import Utility.JsonReader;
import Utility.JsonWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetManager {
    public  static final String JSON_PETRI_FILE = "src/main/java/JsonPetri";
    private ArrayList<Net> netList = new ArrayList<Net>();

    public void menageOption() throws IOException {
        boolean check = true;
        int choise = 0;
        do {
            IO.print(IO.MENU);
            choise = IO.readNumber(IO.DIGIT_YOUR_CHOISE);
            while (choise < 0 || choise > 4) {
                IO.print(IO.DIGIT_VALID_CHOISE);
                choise = IO.readNumber(IO.DIGIT_YOUR_CHOISE);
            }

            switch (choise) {
                case 0:
                    check = false;
                    break;
                case 1:

                    addNet();
                    check = IO.yesOrNo(IO.WANT_TO_DO_ANOTHER_OPERATION);
                    break;

                case 2:

                    int typeNet = IO.readInteger(IO.TYPE_OF_NET, 1, 2);
                    if (typeNet == 1)
                        loadNet(IO.JSON_FILE);
                    else
                        loadNet(IO.JSON_PETRI_FILE);
                    check = IO.yesOrNo(IO.WANT_TO_DO_ANOTHER_OPERATION);

                    break;

                case 3:
                    if (netList.size() == 0) {
                        IO.print(IO.NO_NORMAL_NET);
                    } else {
                        addPetriNet();
                    }
                    break;

            }
        } while (check == true);

    }


    //Metodo per la creazione di petri net;
    public void addPetriNet() {
        PetriNet newPetriNet = new PetriNet(loadOneNet());
        //we add the token to the place
        while (IO.yesOrNo("Do you want to add token to place ? ")){
            addTokentoPetriNet(newPetriNet);
        }


        //we add the weight to the transition
        addWeightToPetriNet(newPetriNet);


        JsonWriter.writeJsonPetri(newPetriNet);
        netList.add(newPetriNet);
    }

    private void addWeightToPetriNet(PetriNet newPetriNet) {

        while (IO.yesOrNo(IO.ADD_WEIGHT)) {
            ArrayList<Transition> transTemp = new ArrayList<>(newPetriNet.getSetOfTrans());

            //qua scelgo la transizione da modificare
            IO.printTransition(transTemp);
            int choose = IO.readInteger(IO.TRANSITION_CHOOSE, 0, transTemp.size())-1;

            //qui mostro i pre e i post della transizione collegata
            ArrayList<String> placeTemp = new ArrayList<>();
            placeTemp.addAll(transTemp.get(choose).getIdPre());
            placeTemp.addAll(transTemp.get(choose).getIdPost());

            do {


                IO.printString(placeTemp);

                //qui chiedo quale transazione-posto vuole modificare
                String placeToChange = placeTemp.get(IO.readInteger("What place you want change?", 0, placeTemp.size())-1);
                int weight = IO.readIntegerWithMin("Insert the weight that you want to give to the place", 0);
                newPetriNet.addWeight(transTemp.get(choose).getName(), placeToChange, weight);
            }while(IO.yesOrNo("Do you want to add other weights to this transition?"));



        }

    }

    private void addTokentoPetriNet(PetriNet newPetriNet) {

        ArrayList<Place> tempPlace = new ArrayList<>(newPetriNet.getSetOfPlace());


        IO.printPlace(newPetriNet.getSetOfPlace());
        int choise = IO.readInteger("where do you want to add the tokens?", 0, tempPlace.size());
        int token = IO.readIntegerWithMin("Insert the number of tokens: ", 0);

        String  placeId = tempPlace.get(choise).getName();

        if(newPetriNet.addToken(placeId, token)){
            IO.print("The weight has been added");
        }else{
            IO.print("The place doesn't exist");
        }


    }

    /**
     * this method allows to add a net
     */
    public void addNet() {

        do {
            Net n = new Net(IO.readNotEmpityString(IO.NAME_OF_NET));
            //if the new net is correct we show it to the user and ask if he wants to save it
            if (checkNet(n) && n.checkTrans() && n.checkConnect()) {
                showNet(n);
                IO.print(IO.THE_NET_IS_CORRECT_WE_ARE_GOING_TO_SAVE_IT);
                netList.add(n);

                if (IO.yesOrNo(IO.SAVE_NET)) {
                    JsonWriter.writeJsonFile(n);
                }
            } else {
                //if the net is incorrect we inform the user
                IO.print(IO.THE_NET_IS_INCORRECT_IT_CAN_T_BE_SAVED);
            }
        } while (IO.yesOrNo(IO.ANOTHER_NET));
    }

    /**
     * the method check if there is only a place connect to a transition
     *
     * @param n the net we have to check
     * @return false if there are some problems and if there is one or more pendant connection
     */
    public boolean checkNet(Net n) {
//if there is a problem the method return false
        if (n.checkPendantNode() == false) {

            return false;
        } else {
            return true;
        }
    }

    /**
     * method to load a net by json file
     *
     * @throws FileNotFoundException
     */
    public void loadNet(String pathNet) throws FileNotFoundException {
        String path_ = new File(pathNet).getAbsolutePath();
        //initialize the File object directory
        File directory = new File(path_);
        //initialize the string that contains the list of name file
        String[] pathname = directory.list();
        int i = 0;
        //for every name file in the directory print the name
        if (pathname != null) {
            for (String s : pathname) {
                i++;
                IO.print(i + ") " + s);
            }
        }

        //if there are not files in the directory print this
        if (i == 0) {
            IO.print(IO.THERE_AREN_T_ANY_FILES_TO_LOAD);
        } else {
            //else ask to user to chose which file load
            int number = IO.readInteger("Insert the id of the file you want to load ", 1, i);
            //get the name of file by the pathname string array and decrement 1 because the print file start with 1
            String path = pathNet + "/" + pathname[number - 1];

            if (pathNet.equals(JSON_PETRI_FILE)) {
                Net newNet = JsonReader.readPetriJson(path);
                //add new net to the list of the net
                netList.add(newNet);
                IO.print(IO.FILE_IS_LOADED);
                IO.print(IO.VISUALIZE_THE_LIST);
                //view the new net
                showPetriNet(newNet);
            } else {
                Net newNet = JsonReader.readJson(path);
                //add new net to the list of the net
                netList.add(newNet);
                System.out.println(IO.FILE_IS_LOADED);
                System.out.println(IO.VISUALIZE_THE_LIST);
                //view the new net
                showNet(newNet);
            }
        }
    }

    //TODO: PENSO SIA MEGLIO SPOSTARLO IN IO

    /**
     * method to view the net
     *
     * @param net
     */
    public void showNet(Net net) {
        //get name and if of the net
        String nameNet = net.getName();
        String idNet = net.getID();
        //initialize the places and transitions arraylist
        ArrayList<String> places = new ArrayList<String>();
        ArrayList<String> transitions = new ArrayList<String>();
        ArrayList<Integer> directions = new ArrayList<>();

        //for every pair in the net get the name of place and name of transition
        for (Pair p : net.getNet()) {
            String place = p.getPlace().getName();
            String trans = p.getTrans().getName();
            int direction = p.getTrans().getInputOutput(p.getPlace().getName());
            //add place to arraylist of places
            places.add(place);
            //add transition to arraylist of transitions
            transitions.add(trans);
            directions.add(direction);
        }
        ArrayList<Integer> order = new ArrayList<>();
        //initialize hashmap that contains the index of place that have the same transition in common
        HashMap<Integer, Integer> index = new HashMap<Integer, Integer>();
        //for every transition in the arraylist check if there are other transition equal
        for (int i = 0; i < transitions.size(); i++) {
            for (int j = 0; j < transitions.size(); j++) {
                //if index i and j are different, check
                if (i != j) {
                    //if the transition in i position is equal to the transition in j position, put the index i and j put the index i and j in the hashmap of index
                    if (transitions.get(i).equals(transitions.get(j))) {
                        int dir = directions.get(i);
                        if (dir == 1) {
                            if (!existAlready(index, i, j)) {
                                index.put(i, j);
                                order.add(0);
                            }
                        } else {
                            if (!existAlready(index, i, j)) {
                                index.put(i, j);
                                order.add(1);
                            }
                        }
                    }
                }
            }
        }
        //initialize new hashmap of index without the copies of the same reference
        //HashMap<Integer, Integer> indexUpdate = checkDuplicate(index);
        //initialize new Arraylist of couples
        ArrayList<String> couples = new ArrayList<String>();
        //for every element in indexUpdate initialize a String that contains the two place and the transition in common
        int i = 0;
        String couple = "";
        for (Map.Entry<Integer, Integer> entry : index.entrySet()) {
            if (order.get(i) == 0) {
                couple = places.get(entry.getKey()) + "----->" + transitions.get(entry.getValue());
            } else {
                couple = places.get(entry.getKey()) + "<-----" + transitions.get(entry.getValue());
            }
            //add the string to the arraylist
            couples.add(couple);
            i++;
        }

        //print the name and id and print all the pairs with their transition
        IO.print("\nName net: " + nameNet + "\t\tID net: " + idNet);
        IO.print("List pairs:");
        for (String s : couples) {
            IO.print("\t" + s);
        }
        System.out.println();
    }

    private static boolean existAlready(HashMap<Integer, Integer> index, int i, int j) {
        boolean ctrl = false;
        for (Map.Entry<Integer, Integer> entry : index.entrySet()) {
            if (entry.getKey() == i) {
                if (entry.getValue() == j) {
                    ctrl = true;
                }
            }
        }
        return ctrl;
    }

    //TODO: PENSO SIA MEGLIO SPOSTARLO IN IO
    public void showPetriNet(Net net) {
        //get name and if of the net
        String nameNet = net.getName();
        String idNet = net.getID();
        //initialize the places and transitions arraylist
        ArrayList<String> places = new ArrayList<String>();
        ArrayList<String> transitions = new ArrayList<String>();
        ArrayList<String> tokens = new ArrayList<>();
        ArrayList<String> weights = new ArrayList<>();
        ArrayList<Integer> directions = new ArrayList<>();

        //for every pair in the net get the name of place and name of transition
        for (Pair p : net.getNet()) {
            String place = p.getPlace().getName();
            String trans = p.getTrans().getName();
            String tokenPlace = Integer.toString(p.getPlace().getNumberOfToken());
            int direction = p.getTrans().getInputOutput(p.getPlace().getName());
            String weightPair = Integer.toString(p.getWeight());
            //add place to arraylist of places
            places.add(place);
            //add transition to arraylist of transitions
            transitions.add(trans);
            directions.add(direction);
            tokens.add(tokenPlace);
            weights.add(weightPair);
        }
        ArrayList<Integer> order = new ArrayList<>();
        //initialize hashmap that contains the index of place that have the same transition in common
        HashMap<Integer, Integer> index = new HashMap<Integer, Integer>();
        //for every transition in the arraylist check if there are other transition equal
        for (int i = 0; i < transitions.size(); i++) {
            for (int j = 0; j < transitions.size(); j++) {
                //if index i and j are different, check
                if (i != j) {
                    //if the transition in i position is equal to the transition in j position, put the index i and j put the index i and j in the hashmap of index
                    if (transitions.get(i).equals(transitions.get(j))) {
                        int dir = directions.get(i);
                        if (dir == 1) {
                            if (!existAlready(index, i, j)) {
                                index.put(i, j);
                                order.add(0);
                            }
                        } else {
                            if (!existAlready(index, i, j)) {
                                index.put(i, j);
                                order.add(1);
                            }
                        }
                    }
                }
            }
        }
        //initialize new hashmap of index without the copies of the same reference
        //HashMap<Integer, Integer> indexUpdate = checkDuplicate(index);
        //initialize new Arraylist of couples
        ArrayList<String> couples = new ArrayList<String>();
        //for every element in indexUpdate initialize a String that contains the two place and the transition in common
        int i = 0;
        String couple = "";
        for (Map.Entry<Integer, Integer> entry : index.entrySet()) {
            if (order.get(i) == 0) {
                couple = places.get(entry.getKey()) + " <" + tokens.get(i) + "> ----------<" + weights.get(i) + ">----------▶ " + transitions.get(entry.getValue());
            } else {
                couple = places.get(entry.getKey()) + " <" + tokens.get(i) + "> ◀︎----------<" + weights.get(i) + ">---------- " + transitions.get(entry.getValue());
            }
            //add the string to the arraylist
            couples.add(couple);
            i++;
        }

        //print the name and id and print all the pairs with their transition
        IO.print("\nName net: " + nameNet + "\t\tID net: " + idNet);
        IO.print("List pairs:");
        for (String s : couples) {
            IO.print("\t" + s);
        }
        IO.print("");

    }

    //metodo che stampa tutte le net in netlist e ne restituisce una scelta dall'utente
    public Net loadOneNet() {

        for (int i = 0; i < netList.size(); i++) {
            System.out.println(i + ") " + netList.get(i).getName());
        }
        int choise = IO.readInteger("choose the network number ", 0, netList.size());
        return netList.get(choise);
    }

}