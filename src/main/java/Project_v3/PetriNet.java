package Project_v3;

import Utility.Reader;

import java.util.*;

public class PetriNet extends Net {
    public static final String HOW_MANY_TOKEN = "How many tokens do you want this place to have?\n(if you don't want tokens enter 0)";

    private static HashMap<String, Integer> initialMarking = new HashMap<>();


    public PetriNet(Net genericNet) {
        super(genericNet);
        addWeight();
        addToken();
        saveInitialMark();
    }

    public void addWeight() {
        ArrayList<Transition> transTemp = new ArrayList<>(super.getSetOfTrans());
        int i;
        while (Reader.yesOrNo("You want change the weight of pair?")) {
            //stampo tutte le transizioni
            for (i = 0; i < transTemp.size(); i++) {
                System.out.println(i + ") " + transTemp.get(i).getName());
            }
            //salvo una transizione
            Transition transToChange = transTemp.get(Reader.leggiIntero("What transition you want change?", 0, i));
            i = 0;

            //creo un array temporaneo di  posti e stampo tutti i posti associati a quella transizione
            System.out.println("the place connected to " + transToChange.getName() + ":");
            ArrayList<String> placeTemp = new ArrayList<>();
            for (String placeName : transToChange.getIdPre()) {
                placeTemp.add(placeName);
                System.out.println(i + ") " + placeName);
                i++;
            }
            for (String placeName : transToChange.getIdPost()) {
                placeTemp.add(placeName);
                System.out.println(i + ") " + placeName);
                i++;
            }
            String placeToChange = placeTemp.get(Reader.leggiIntero("What place you want change?", 0, i));

            //ciclo le coppie finche non trovo quella desiderata
            for(i=0; i<super.getNet().size(); i++){
                if(placeToChange.compareTo(super.getNet().get(i).getPlace().getName())==0 && transToChange.getName().compareTo(super.getNet().get(i).getTrans().getName())==0){
                    int newWeight = Reader.leggiInteroConMinimo("Insert the new Weight",1);
                    super.getNet().get(i).setWeight(newWeight);
                    System.out.println("\n");
                    break;
                }
            }
            placeTemp.clear();
        }
    }

    //metodo per l'aggiunta dei token nella rete
    public void addToken() {
        ArrayList<Place> tempPlace = new ArrayList<>(super.getSetOfPlace());
        String placeId;
        int token;
        boolean check = false;
        boolean again = true;
        int i;

        while(Reader.yesOrNo("You want to add tokens in the Petri's net?")) {
            do {
                i = 0;
                //Stampo tutte le alternative
                System.out.println("Place:");
                for (Place place : super.getSetOfPlace()) {
                    System.out.println(i + ") " + place.getName());
                    i++;
                }

                int choise = Reader.leggiIntero("where do you want to add the tokens?", 0, i-1);
                placeId = tempPlace.get(choise).getName();

                //inizio ricerca per vedere se esiste
                for (Place place : super.getSetOfPlace()) {
                    if (place.getName().compareTo(placeId) == 0) {
                        check = true;
                        break;
                    }
                }
                //Se check è falso non l'ha trovato, stampo l'error e ricomincio
                if (check == false) {
                    System.out.println("ERROR, WRONG ID");
                } else { //se check è true la ricerca è andata a buon fine, posso chiedere all'utente il peso della transizione e modificarla nella rete
                    token = Reader.leggiInteroConMinimo("Insert the number of tokens: ", 0);
                    for (Place p : super.getSetOfPlace()) {
                        if (p.getName().compareTo(placeId) == 0) {
                            p.setToken(token);
                            break;
                        }
                    }
                }
            } while (!check);

        }
    }

    public void saveInitialMark(){
        for (Place p: super.getSetOfPlace()) {
            initialMarking.put(p.getName(), p.getNumberOfToken());
        }
    }


}
