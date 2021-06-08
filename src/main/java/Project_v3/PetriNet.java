package main.java.Project_v3;

import java.util.*;

public class PetriNet extends Net implements Simulation {



    private static final HashMap<Pair, Integer> initialMarking = new HashMap<>();
    private static ArrayList<Pair> initialMark=new ArrayList<>();
    public static HashMap<Pair, Integer> getInitialMarking() {
        return initialMarking;
    }

    public PetriNet(Net genericNet) {
        super(genericNet);
        saveInitialMark();
    }

    /**
     * this method allows to add the weight to the pair
     * @param nameTrans name of the trans
     * @param placeMod name of the place
     * @param weight the quantity of weight
     */
    public void addWeight(String nameTrans, String placeMod, int weight) {
        //we research the transition and the place that the user wants to change
        Transition transition = researchTrans(nameTrans);
        Place place = researchPlace(placeMod);

        //when we have the transition and the place we research the matching pair
        Pair pair = researchPair(transition, place);
        //we set its weight
        pair.setWeight(weight);
    }

    /**
     * method for adding tokens in the network
     * @param placeId is the place where i want add token
     * @param token is the number of token
     * @return false if the place doesn't exist, true if I add it correctly
     */
    public boolean addToken(String placeId, int token) {
        Place placeChoosen = researchPlace(placeId);
        if (placeChoosen == null) {
            return false;
        } else {
            placeChoosen.setToken(token);
            return true;
        }
    }

    /**
     * Method that allows you to save the initial marking
     */
    public void saveInitialMark() {
        for (Pair p : super.getNet()) {
            if (p.getPlace().getNumberOfToken() != 0)
                initialMarking.put(p, p.getPlace().getNumberOfToken());
        }
        for (Pair p: super.getPairs()){
            if(p.getPlace().getNumberOfToken()!=0){
                initialMark.add(p);
            }
        }
    }


    public ArrayList<Pair> getInitialMark(){
        return initialMark;
    }

    public ArrayList<Transition> initialization( ) {
        ArrayList<Transition> temp=new ArrayList<>();
        boolean[] visit = new boolean[initialMark.size()];
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
                    n = calculateN(initialMark, visit, n, i, j);

                }

            }
            //se il peso totale è maggiore o uguale a quello richiesto lo aggiungo
            if (n >= initialMark.get(i).getWeight()) {
                temp.add(initialMark.get(i).getTrans());
            }
            n = 0;
        }
        return temp;
    }

    public int calculateN(ArrayList<Pair> initialMark, boolean[] visit, int n, int i, int j) {
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
        return n;
    }
    /**
     * Override of the equals method which allows me to check if two petri nets are equal
     * @param obj is element to check
     * @return true if two Petri's Net are equals
     */
    @Override
    public boolean equals(Object obj) {

        assert obj instanceof PetriNet;
        assert obj != null;
        int tokenNumber;
        PetriNet pt = (PetriNet) obj;
        int numberOfPlace= pt.getSetOfPlace().size() ;
        int numberOfTrans = pt.getSetOfTrans().size();

        //If they have a different number of places and transitions I know they are two different networks
        if (numberOfPlace != super.getSetOfPlace().size() || numberOfTrans != super.getSetOfTrans().size()){
            return false;
        }

        //Check if the sets of transitions and places are the same, if they are different the two networks are different
        if(!super.getSetOfPlace().containsAll(pt.getSetOfPlace())){
            return false;
        }
        if(!super.getSetOfTrans().containsAll(pt.getSetOfTrans())){
            return false;
        }

        //At this point I check the initial marking,
        // if two places have a different number of tokens it means that the initial marking of the two networks is different
        for (Pair p: super.getNet()) {
            tokenNumber = (pt.getInitialMarking().get(p));
            if (tokenNumber!=initialMarking.get(p)) {
                return false;
            }
        }
        return true;
    }


}

