package main.java.Utility;


import main.java.Project_v3.Place;
import main.java.Project_v3.Transition;

import java.io.File;
import java.util.*;

public class IO {
    public static final String THERE_AREN_T_ANY_FILES_TO_LOAD = "There aren't any files to load";
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
    public static final String JSON_PETRI_FILE = "src/main/java/JsonPetri";
    public static final String HOW_MANY_TOKEN = "How many tokens do you want this place to have?\n(if you don't want tokens enter 0)";
    private final static String ERROR_FORMAT = "Warning: the entered data are in the wrong format.";
    private final static String MINIMUM_ERROR = "Warning: the value must to be grater or equal to ";
    private final static String STRING_EMPTY_ERROR = "Warning: the string entered is empty";
    private final static String MAXIMUM_ERROR = "Warning: the value must to be lower or equal to ";
    private final static String MESSAGES_ALLOWED = "Warning, the value allowed are: ";
    private final static char YES_ANSWER = 'S';
    private final static char NO_ANSWER = 'N';

    private static Scanner reader = scannerBuild();

    public static void print(String s){
        System.out.println(s);
    }
    public static void printError(String s){
        System.err.println(s);
    }

    public static String readNotEmpityString(String message) {
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

    public static boolean yesOrNo(String message) {
        String myMessage = message + "(" + YES_ANSWER + "/" + NO_ANSWER + ")";
        char readValue = readUpperChar(myMessage, String.valueOf(YES_ANSWER) + String.valueOf(NO_ANSWER));

        if (readValue == YES_ANSWER)
            return true;
        else
            return false;
    }

    public static double readDouble(String message) {
        boolean finish = false;
        double readValue = 0;
        do {
            print(message);
            try {
                readValue = reader.nextDouble();
                finish = true;
            } catch (InputMismatchException e) {
                print(ERROR_FORMAT);
                String toDelete = reader.next();
            }
        } while (!finish);
        return readValue;
    }

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

    public static char readUpperChar(String message, String allowed) {
        boolean finish = false;
        char readValue = '\0';
        do {
            readValue = leggiChar(message);
            readValue = Character.toUpperCase(readValue);
            if (allowed.indexOf(readValue) != -1)
                finish = true;
            else
                print(MESSAGES_ALLOWED+allowed);
        } while (!finish);
        return readValue;
    }

    public static char leggiChar(String message) {
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
        //creato.useDelimiter(System.getProperty("line.separator"));
        created.useDelimiter(System.lineSeparator() + "|\n");
        return created;
    }

    public static String ReadString(String message) {
        print(message);
        return reader.next();
    }

    public static void printPlace(Iterable<Place> list){
        int i=1;

        for(Place p:list){
            IO.print(i+") "+ p.getName());
            i++;
        }

    }
    public static void printTransition(Iterable<Transition> list){
        int i=1;

        for(Transition t:list){
            IO.print(i+") "+ t.getName());
            i++;
        }
    }


    public static void printString(List<String> list) {
        for(int i=0; i<list.size();i++){
            IO.print((i+1)+") "+list.get(i));
        }
    }
}
