package Project_v3;

import Utility.Reader;

public class PetriNet extends Net {
    public static final String HOW_MANY_TOKEN = "How many tokens do you want this place to have?\n(if you don't want tokens enter 0)";
    private static int i;
    //private boolean def; //indicates whether the petri net has the default values or not


    public PetriNet(Net genericNet, String name) {
        super(genericNet);
        addWeight();
        addToken();
    }

    public void addDefaultWeight(){
        for (Transition t: super.getSetOfTrans()) {
            t.setWeight(t.sizePre());
        }
    }

    public void addWeight() {
        String transId;
        Transition tempTrans=null;
        int weight;
        boolean check = false;
        boolean again = true;

        //aggiungo i pesi di default
        addDefaultWeight();

        do {
            do {
                i=0;
                //Stampo tutte le alternative
                System.out.println("Transition:");
                for (Transition trans : super.getSetOfTrans()) {
                    System.out.println(i + ") " + trans.getName());
                    i++;
                }
                System.out.println("If you want exit insert: EXIT");
                transId = Reader.readNotEmpityString("Enter the name of the transition you want to edit: ");
                if (transId.compareTo("EXIT") == 0) {   // posso fare di meglio
                    again = false;
                    break;
                }
                //inizio ricerca per vedere se esiste
                for (Transition trans : super.getSetOfTrans()) {
                    if (trans.getName().compareTo(transId) == 0) {
                        tempTrans = trans;
                        check = true;
                        break;
                    }
                }
                //Se check è falso non l'ha trovato, stampo l'error e ricomincio
                if (!check) {
                    System.out.println("ERROR, WRONG Name");
                    again = Reader.yesOrNo("You want continue?");
                } else { //se check è true la ricerca è andata a buon fine, posso chiedere all'utente il peso della transizione e modificarla nella rete
                    //il minimo peso è = al numero di pre
                    weight = Reader.leggiInteroConMinimo("Insert the weight of the transition",tempTrans.sizePre());
                   //ciclo il set di transizioni e inserisco il peso
                    for (Transition trans: super.getSetOfTrans()) {
                        if(tempTrans.getName().compareTo(trans.getName())==0){
                            trans.setWeight(weight);
                            break;
                        }
                    }
                    again = Reader.yesOrNo("You want continue?");
                }
            } while (!check);
        } while (again);

    }

    //metodo per l'aggiunta dei token nella rete
    public void addToken() {
        Place tempPlace=null;
        String placeId;
        int token;
        boolean check = false;
        boolean again = true;
        int i;

        do {
            do {
                i=0;
                //Stampo tutte le alternative
                System.out.println("Place:");
                for (Place place : super.getSetOfPlace()) {
                    System.out.println(i + ") " + place.getName());
                    i++;
                }
                System.out.println("If you want exit insert: EXIT");
                placeId = Reader.readNotEmpityString("Enter the Name of the Place you want to edit: ");
                if (placeId.compareTo("EXIT") == 0) {
                    again = false;
                    break;
                }
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
                    again = Reader.yesOrNo("You want continue?");
                } else { //se check è true la ricerca è andata a buon fine, posso chiedere all'utente il peso della transizione e modificarla nella rete
                    token = Reader.leggiInteroConMinimo("Insert the number of tokens: ", 0);
                    for (Place p: super.getSetOfPlace()) {
                        if(p.getName().compareTo(placeId)==0){
                            p.setToken(token);
                            break;
                        }
                    }
                    again = Reader.yesOrNo("You want continue?");
                }
            } while (!check);

        } while (again);

    }


}

