package main.java.Project_v3;

import Utility.Reader;

import java.util.*;

public class PetriNet extends Net {
    public static final String HOW_MANY_TOKEN = "How many tokens do you want this place to have?\n(if you don't want tokens enter 0)";

    private static HashMap<Pair, Integer> initialMarking = new HashMap<>();
    private static ArrayList<Pair> initialMark=new ArrayList<>();

    public PetriNet(Net genericNet) {
        super(genericNet);
        saveInitialMark();
        simulazione();
    }
    public void addWeight(String nameTrans, String placeMod, int weight) {
       /* ArrayList<Transition> transTemp = new ArrayList<>(super.getSetOfTrans());
        int i;

            //stampo tutte le transizioni
            //TODO metodo che permette la scelta
            //TODO: CAMBIARE POSIZIONE
            IO.printTransition(transTemp);
            //salvo una transizione
            Transition transToChange = transTemp.get(IO.readInteger("What transition you want change?", 0, transTemp.size()));
            i = 0;

            //creo un array temporaneo di  posti e stampo tutti i posti associati a quella transizione
            System.out.println("the place connected to " + transToChange.getName() + ":");
            ArrayList<String> placeTemp = new ArrayList<>();
            //TODO: CAMBIARE POSiZIONE
            IO.printString(transToChange.getIdPre());

            placeTemp.addAll(transToChange.getIdPre());
        placeTemp.addAll(transToChange.getIdPost());
            //TODO: CAMBIARE POSiZIONE
            IO.printString(transToChange.getIdPost());

            for (String placeName : transToChange.getIdPost()) {
                placeTemp.add(placeName);

            }
        String placeToChange = placeTemp.get(IO.readInteger("What place you want change?", 0, i));
        //ciclo le coppie finche non trovo quella desiderata
            //TODO: refattorizzare un botto
            for(i=0; i<super.getNet().size(); i++){
                if(placeToChange.compareTo(super.getNet().get(i).getPlaceName())==0 && transToChange.getName().compareTo(super.getNet().get(i).getTransName())==0){
                    int newWeight = IO.readIntegerWithMin("Insert the new Weight",1);
                    super.getNet().get(i).setWeight(newWeight);
                    IO.print("\n");
                    break;
                }
            }
            placeTemp.clear();

        */

        //unica parte da lasciare qui


        //we research the transition and the place that the user wants to change
        Transition transition = researchTrans(nameTrans);
        Place place = researchPlace(placeMod);

        //when we have the transition and the place we research the matching pair
        Pair pair = researchPair(transition, place);
        //we set its weight
        pair.setWeight(weight);


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
                if(placeToChange.compareTo(super.getNet().get(i).getPlaceName())==0 && transToChange.getName().compareTo(super.getNet().get(i).getTransName())==0){
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
    public boolean addToken(String placeId, int token) {
        ArrayList<Place> tempPlace = new ArrayList<>(super.getSetOfPlace());
        //String placeId;
        //int token;
        boolean check = false;
        boolean again = true;
        int i;

        // while (IO.yesOrNo("You want to add tokens in the Petri's net?")) {

             /*   i = 0;
                //Stampo tutte le alternative
                System.out.println("Place:");
                IO.printPlace(super.getSetOfPlace());


                int choise = IO.readInteger("where do you want to add the tokens?", 0, i - 1);
                placeId = tempPlace.get(choise).getName();
*/
        Place placeChoosen=researchPlace(placeId);
        if(placeChoosen==null){
            return false;
        } else{
            placeChoosen.setToken(token);
            return true;
        }
             /*   //inizio ricerca per vedere se esiste
                for (Place place : super.getSetOfPlace()) {
                    if (place.getName().compareTo(placeId) == 0) {
                        check = true;
                        break;
                    }
                }*/
        //Se check è falso non l'ha trovato, stampo l'error e ricomincio



        // }

    }

    public void saveInitialMark(){
        for(Pair p: super.getNet()){
            if(p.getPlace().getNumberOfToken()!=0)
                initialMarking.put(p,p.getPlace().getNumberOfToken());
        }

        for (Pair p: super.getPairs()){
            if(p.getPlace().getNumberOfToken()!=0){
                initialMark.add(p);
            }
        }
    }

    public ArrayList<Pair> getInitialMarking(){
    return initialMark;
    }

    public void simulazione() {

        ArrayList<Transition> temp = new ArrayList<Transition>();
        boolean[] visit = new boolean[initialMark.size()];
        System.out.println("The first marking is given by:");
        for (int i = 0; i < initialMark.size(); i++) {
            System.out.println(initialMark.get(i).getPlaceName() + " where there are " + initialMark.get(i).getToken());
        }
        int n = 0;
        for (int i = 0; i < initialMark.size(); i++) {
            //se la coppia è stat visitata salto in avanti
            if (visit[i] == true) {
                continue;
            }
            // se il posto non è nei predecessori della transizione pur avendo dei token viene saltata perchè non contribuisce allo scatto
            if (initialMark.get(i).getTrans().isIn(initialMark.get(i).getPlaceName()) == false) {
                continue;
            }
            //se si ha un unico pre e si hanno abbastanza token la transizione viene subito aggiunta
            if (initialMark.get(i).getTrans().sizePre() == 1 && initialMark.get(i).getWeight() <= initialMark.get(i).getToken()) {
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
                            if (initialMark.get(j).getTrans().isIn(s) && initialMark.get(j).getWeight() <= initialMark.get(j).getToken()) {
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
            int risp=Reader.leggiIntero("Insert the number of the transition you want to use", 1, temp.size())-1;
            int weightTotal=0;
            //ciclo su tutti i pre della transizione
            for(int i=0; i<temp.get(risp).sizePre(); i++) {
                //controllo che il place sia presente nella pre
                for (Place p : getSetOfPlace()) {
                    //se è uguale aggiorno il numero dei token
                    if (p.getName().equals(temp.get(risp).getName())) {
                        int a = p.getNumberOfToken() - getPair(p, temp.get(risp)).getWeight();
                        //trovo il peso totale per far scattare la transizione
                        weightTotal = weightTotal + getPair(p, temp.get(risp )).getWeight();
                        p.setToken(a);
                    }
                }
            }
                //aggiorno tutti i post della transizione modificando il valore dei loro pesi
               if(temp.get(risp).sizePost()==1){
                   //al post ci metto la somma degli elementi dei pesi dei pre, è nelle coppie
                   getPair(getPlace(temp.get(risp).getIdPost().get(0)), temp.get(risp)).setWeight(weightTotal);
               }else{
                   System.out.println("This transition can move the tokens in different places");
                   for(int i=0; i<temp.get(risp-1).sizePost(); i++){
                       System.out.println(i+1+") " + temp.get(risp).getIdPost().get(i));

                   }
                    //elemento è il post che devo modificare
                   int elem=Reader.leggiIntero("Where do you want to put the token?", 1,temp.get(risp).sizePost() )-1;
                   getPair(getPlace(temp.get(risp).getIdPost().get(elem)), temp.get(risp)).setWeight(weightTotal);
               }


            simulazione();
            }
        }

    }

