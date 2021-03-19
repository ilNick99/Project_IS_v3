package Project_v3;

import Utility.Reader;

import java.util.*;

public class PetriNet extends Net {
    public static final String HOW_MANY_TOKEN = "How many tokens do you want this place to have?\n(if you don't want tokens enter 0)";

    private static HashMap<Pair, Integer> initialMarking = new HashMap<>();
    private static ArrayList<Pair> initialMark=new ArrayList<>();

    public PetriNet(Net genericNet) {
        super(genericNet);
        addWeight();
        addToken();
        saveInitialMark();
        simulazione();
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

        while(Reader.yesOrNo("do You want to add tokens in the Petri's net?")) {
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
        for(Pair p: super.getNet()){
            if(p.getPlace().getNumberOfToken()!=0)
                initialMarking.put(p,p.getPlace().getNumberOfToken());
        }

        for (Pair p: super.getPair()){
            if(p.getPlace().getNumberOfToken()!=0){
                initialMark.add(p);
            }
        }
    }

    public HashMap<Pair, Integer> getInitialMarking(){
    return initialMarking;
    }

    public void simulazione() {

        ArrayList<Transition> temp = new ArrayList<Transition>();
        boolean[] visit = new boolean[initialMark.size()];
        System.out.println("The first marking is given by:");
        for (int i = 0; i < initialMark.size(); i++) {
            System.out.println(initialMark.get(i).getPlace().getName() + " where there are " + initialMark.get(i).getPlace().getNumberOfToken());
        }
        int n = 0;
        for (int i = 0; i < initialMark.size(); i++) {
            //se la coppia è stat visitata salto in avanti
            if (visit[i] == true) {
                continue;
            }
            // se il posto non è nei predecessori della transizione pur avendo dei token viene saltata perchè non contribuisce allo scatto
            if (initialMark.get(i).getTrans().isIn(initialMark.get(i).getPlace().getName()) == false) {
                continue;
            }
            //se si ha un unico pre e si hanno abbastanza token la transizione viene subito aggiunta
            if (initialMark.get(i).getTrans().sizePre() == 1 && initialMark.get(i).getWeight() <= initialMark.get(i).getPlace().getNumberOfToken()) {
                temp.add(initialMark.get(i).getTrans());

            } else {
                //altrimenti inizio a capire il peso totale in ingresso della transizione
                visit[i] = true;
                n = initialMark.get(i).getWeight();
                //ciclo sulle altre coppie in modo da capire se c'è un'altra coppia in cui è presente la stessa transizione
                for (int j = i + 1; j < initialMark.size() ; j++) {

                    if(visit[j]==true){
                        continue;
                    }
                    //controllo se l'elemento ha la stessa transizione
                    if (initialMark.get(i).getTrans().equals(initialMark.get(j).getTrans())) {
                        //se è vero controllo se la coppia faccia parte dei pre della transizione
                        for (String s : initialMark.get(j).getTrans().getIdPre()) {
                            if (initialMark.get(j).getTrans().isIn(s) && initialMark.get(j).getWeight() <= initialMark.get(j).getPlace().getNumberOfToken()) {
                              //aggiorno il peso totale
                                n = n + initialMark.get(j).getWeight();
                                //indico che ho visitato il nodo
                                visit[j] = true;
                            }
                        }
                    }

                }

            }
            //se il peso totale è maggiore o uguale a quello richiesto lo aggiungo
            if (n >= initialMark.get(i).getWeight()) {
                temp.add(initialMark.get(i).getTrans());
            }
            n = 0;
        }

        //se temp è zero significa che non si sono transizioni abilitate
        if (temp.size() == 0) {
            System.out.println("There aren't any transition available ");
        } else {
            //altrimenti mostro le transizioni abilitate e chiedo quale si voglia far scattare
            System.out.println("The following transition are available");
            for (int i = 0; i < temp.size(); i++) {
                System.out.println((i+1) +") " + temp.get(i).getName());
            }
            int risp=Reader.leggiIntero("Insert the number of the transition you want to use0", 1, temp.size());


        }
    }
    }

