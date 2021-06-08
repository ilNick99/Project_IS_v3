package main.java.Utility;


import main.java.Project_v3.Net;
import main.java.Project_v3.Pair;
import main.java.Project_v3.Place;
import main.java.Project_v3.Transition;

import java.io.File;
import java.util.*;

public class IO {
    public static final String CHOOSE_THE_ELEMENT = "choose the mode:\n0) Exit \n1) User mode \n2) Configurator mode";
    public static final String MARKING_WITH_TOKEN = "The first marking is given by:";
    public static final String WHERE_THERE_ARE = " where there are ";
    public static final String SET_NEW_NAME = "A network with this name already exists, please enter a different name";
    public static final String DO_YOU_WANT_TO_SAVE_THAT_PETRI_S_NET = "Do you want to save that Petri's Net?";
    public static final String THERE_AREN_T_ANY_TRANSITION_AVAILABLE = "There aren't any transition available ";
    public static final String THE_FOLLOWING_TRANSITION_ARE_AVAILABLE = "The following transition are available";
    public static final String INSERT_THE_NUMBER_OF_THE_TRANSITION_YOU_WANT_TO_USE = "Insert the number of the transition you want to use";
    public static final String HOW_MANY_TOKEN = "How many tokens do you want this place to have?\n(if you don't want tokens enter 0)";
    public static final String DO_YOU_WANT_TO_LOAD_OTHER_NETS = "Do you want to load other nets?";
    public static final String DO_YOU_WANT_TO_ADD_TOKEN_TO_PLACE = "Do you want to add token to place ? ";
    public static final String THERE_AREN_T_ANY_FILES_TO_LOAD = "There aren't any files to load";
    public static final String WHERE_DO_YOU_WANT_TO_PUT_THE_TOKEN = "Where do you want to put the token?";
    public static final String THIS_TRANSITION_CAN_MOVE_THE_TOKENS_IN_DIFFERENT_PLACES = "This transition can move the tokens in different places";
    public static final String DO_YOU_WANT_TO_MAKE_AN_OTHER_SIMULATION = "Do you want to make an other simulation?";
    public static final String YOU_HAVE_TO_LOAD_A_NET_WHICH_ONE_DO_YOU_WANT = "\nYou have to load a net, which one do you want?";
    public static final String INSERT_THE_NUMBER_OF_THE_NET_THAT_YOU_WANT_TO_USE = "Insert the number of the net that you want to use";
    public static final String INSERT_PLACE_S_ID = "Insert place's Name ";
    public static final String INSERT_TRANSITION_S_ID = "Insert transition's Name ";
    public static final String YOU_CAN_T_ADD_THIS_PAIR_BECAUSE_ALREADY_EXISTS = "You can't Add this pair because it already exists";
    public static final String YOU_WANT_ADD_ANOTHER_PAIR = "You want add another Pair?";
    public static final String WRITING_FILE_ERROR = "writing file error.";
    public static final String TYPE_OF_NET = "Do you want load:\n1) simple net\n2) Petri Net\n" ;
    public static final String FILE_IS_LOADED = "File is loaded";
    public static final String VISUALIZE_THE_LIST = "Visualize the list";
    public static final String ADD_WEIGHT = "Do you want to add weight to the transition? If you say no we insert the default value";
    public static final String TRANSITION_CHOOSE = "These are the transition in the Net, do you have to choose which one modify: (insert the number)" ;
    private static final String path = new File("src/main/java/JsonFile").getAbsolutePath();
    private static final String petriPath = new File("src/main/java/JsonPetri").getAbsolutePath();
    public static final String ANOTHER_NET = "You want add another Net?";
    public static final String NAME_OF_NET = "Add the name of Net:";
    public static final String MENU = "What do you want do?\n0)EXIT\n1)Add new Net\n2)Load net\n3)Create a new Petri's Net";
    public static final String WANT_TO_DO_ANOTHER_OPERATION = "you want to do another operation ";
    public static final String SAVE_NET = "Do you want to save the net that you have already made? ";
    public static final String DIGIT_YOUR_CHOISE = "Digit your choise ";
    public static final String DIGIT_VALID_CHOISE = "Digit valid choise!";
    public static final String THE_NET_IS_INCORRECT_IT_CAN_T_BE_SAVED = "The net is incorrect, it can't be saved";
    public static final String THE_NET_IS_CORRECT_WE_ARE_GOING_TO_SAVE_IT = "The net is correct, we are going to save it";
    public static final String NO_NORMAL_NET = "There aren't any nets! You have to insert or load a net before adding a Petri Net";
    public static final String JSON_FILE = "src/main/java/JsonFile";
    public static final String JSON_PETRI_FILE = "src/main/java/JsonPetri/";
   // public static final String HOW_MANY_TOKEN = "How many tokens do you want this place to have?\n(if you don't want tokens enter 0)";
    public final static String WHAT_PLACE_YOU_WANT_CHANGE = "What place you want change?";
    private final static String ERROR_FORMAT = "Warning: the entered data are in the wrong format.";
    private final static String MINIMUM_ERROR = "Warning: the value must to be grater or equal to ";
    private final static String STRING_EMPTY_ERROR = "Warning: the string entered is empty";
    private final static String MAXIMUM_ERROR = "Warning: the value must to be lower or equal to ";
    private final static String MESSAGES_ALLOWED = "Warning, the value allowed are: ";
    private final static char YES_ANSWER = 'Y';
    private final static char NO_ANSWER = 'N';
    public static final String INSERT_THE_WEIGHT_THAT_YOU_WANT_TO_GIVE_TO_THE_PLACE = "Insert the weight that you want to give to the place";
    public static final String DO_YOU_WANT_TO_ADD_OTHER_WEIGHTS_TO_THIS_TRANSITION = "Do you want to add other weights to this transition?";
    public static final String WHERE_DO_YOU_WANT_TO_ADD_THE_TOKENS = "where do you want to add the tokens?";
    public static final String INSERT_THE_NUMBER_OF_TOKENS = "Insert the number of tokens: ";
    public static final String THE_WEIGHT_HAS_BEEN_ADDED = "The weight has been added";
    public static final String THE_PLACE_DOESN_T_EXIST = "The place doesn't exist";


    private static Scanner reader = scannerBuild();

    /**
     * method to print a string
     * @param s is a string to print
     */
    public static void print(String s){
        System.out.println(s);
    }

    /**
     * method to print an error
     * @param s is the error to print
     */
    public static void printError(String s){
        System.err.println(s);
    }

    /**
     * method to read a not empity string
     * @param message to print
     */
    public static String readNotEmptyString(String message) {
        boolean finish = false;
        String read = null;
        do {
            read = ReadString(message);
            read = read.trim();
            if (read.length() > 0)
                finish = true;
            else
                print(STRING_EMPTY_ERROR);
        } while (!finish);

        return read;
    }

    /**
     * method to check if the user want do something
     * @param message is the operation on which confirmation is requested
     */
    public static boolean yesOrNo(String message) {
        String myMessage = message + "(" + YES_ANSWER + "/" + NO_ANSWER + ")";
        char readValue = readUpperChar(myMessage, String.valueOf(YES_ANSWER) + String.valueOf(NO_ANSWER));

        if (readValue == YES_ANSWER)
            return true;
        else
            return false;
    }

    /**
     * This method allows me (concisely) to create the string I use to ask the user if the place he wants to add is a successor or predecessor of a transition
     * @param transName
     * @param placeName
     * @return the final string
     */
    public static String connectionBetweenPlaceandTrans(String transName, String placeName){
        String question = String.format("Which type of connection there is between the place %s and the transition %s? ", placeName, transName);
        String chose1 = String.format("\n1) %s is an INPUT of %s ", placeName, transName);
        String chose2 = String.format("\n2) %s is an OUTPUT of %s ", placeName, transName);
        String finalString = (question.concat(chose1)).concat(chose2);
        return finalString;
    };


    /**
     * method to read a Int with a minimum an maximus
     * @param message to print
     */
    public static int readInteger(String message, int min, int max) {
        boolean finish = false;
        int readValue = 0;
        do {
            readValue = readNumber(message);
            if (readValue >= min && readValue <= max)
                finish = true;
            else if (readValue < min)
                print(MINIMUM_ERROR + min);
            else
                print(MAXIMUM_ERROR + max);
        } while (!finish);

        return readValue;
    }

    /**
     * method to read a Int with a minimum
     * @param message to print
     */
    public static int readIntegerWithMin(String message, int min) {
        boolean finish = false;
        int readValue = 0;
        do {
            readValue = readNumber(message);
            if (readValue >= min)
                finish = true;
            else
                print(MINIMUM_ERROR+min);
        } while (!finish);

        return readValue;
    }


    public static int readNumber(String message) {
        boolean finish = false;
        int readValue = 0;
        do {
            print(message);
            try {
                readValue = reader.nextInt();
                finish = true;
            } catch (InputMismatchException e) {
                print(ERROR_FORMAT);
                String toDelete = reader.next();
            }
        } while (!finish);
        return readValue;
    }

    /**
     * Method to read char among those authorized
     * @param message to print
     * @param allowed is string allowed
     */
    public static char readUpperChar(String message, String allowed) {
        boolean finish = false;
        char readValue = '\0';
        do {
            readValue = readChar(message);
            readValue = Character.toUpperCase(readValue);
            if (allowed.indexOf(readValue) != -1)
                finish = true;
            else
                print(MESSAGES_ALLOWED+allowed);
        } while (!finish);
        return readValue;
    }

    /**
     * method to read a char
     * @param message to print
     */
    public static char readChar(String message) {
        boolean finish = false;
        char readValue = '\0';
        do {
            print(message);
            String read = reader.next();
            if (read.length() > 0) {
                readValue = read.charAt(0);
                finish = true;
            } else {
                print(STRING_EMPTY_ERROR);
            }
        } while (!finish);
        return readValue;
    }

    private static Scanner scannerBuild() {
        Scanner created = new Scanner(System.in);
        created.useDelimiter(System.lineSeparator() + "|\n");
        return created;
    }

    /**
     * method to read a string
     * @param message to print
     */
    public static String ReadString(String message) {
        print(message);
        return reader.next();
    }

    /**
     * method for print a list of Place
     * @param list to print
     */
    public static void printPlace(Iterable<Place> list){
        int i=1;
        for(Place p:list){
            IO.print(i+") "+ p.getName());
            i++;
        }

    }

    /**
     * method to print a list of Transition
     * @param list to print
     */
    public static void printTransition(Iterable<Transition> list){
        int i=1;

        for(Transition t:list){
            IO.print(i+") "+ t.getName());
            i++;
        }
    }

    /**
     * method to print a list of String
     * @param list to print
     */
    public static void printString(List<String> list) {
        for(int i=0; i<list.size();i++){
            IO.print((i+1)+") "+list.get(i));
        }
    }

    /**
     * method to view the Petri's net
     *
     * @param net
     */
    public static void showPetriNet(Net net) {
        //get name and if of the net
        String nameNet = net.getName();
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
                            if (!JsonManager.existAlready(index, i, j)) {
                                index.put(i, j);
                                order.add(0);
                            }
                        } else {
                            if (!JsonManager.existAlready(index, i, j)) {
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
        IO.print("\nName net: " + nameNet );
        IO.print("List pairs:");
        for (String s : couples) {
            IO.print("\t" + s);
        }
        IO.print("");

    }

    /**
     * method to view the net
     *
     * @param net
     */
    public static void showNet(Net net) {
        //get name and if of the net
        String nameNet = net.getName();
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
                            if (!JsonManager.existAlready(index, i, j)) {
                                index.put(i, j);
                                order.add(0);
                            }
                        } else {
                            if (!JsonManager.existAlready(index, i, j)) {
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
        IO.print("\nName net: " + nameNet );
        IO.print("List pairs:");
        for (String s : couples) {
            IO.print("\t" + s);
        }
        System.out.println();
    }

    public static void printElementWithToken(ArrayList<Pair> initialMark) {
        print(MARKING_WITH_TOKEN);
        printPair(initialMark);
    }
    public static void printPair(Iterable<Pair> pairs) {
        for (Pair p: pairs){
            IO.print(p.getPlace().getName() + WHERE_THERE_ARE + p.getPlace().getNumberOfToken());

        }

    }
}
