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

    public ArrayList<Transition >  initialization( ArrayList<Pair> initialSituation){
        ArrayList<Transition> temp=new ArrayList<>();
        boolean[] visit = new boolean[initialSituation.size()];
                for(int i=0; i<initialSituation.size(); i++)        {
                    if (visit[i] == true) {
                        continue;
                    }
                    // se il posto non è nei predecessori della transizione pur avendo dei token viene saltata perchè non contribuisce allo scatto
                    if (initialSituation.get(i).getTrans().isIn(initialSituation.get(i).getPlace().getName()) == false) {
                        continue;
                    }
                    //se si ha un unico pre e si hanno abbastanza token la transizione viene subito aggiunta
                    if (initialSituation.get(i).getTrans().sizePre() == 1 && initialSituation.get(i).getWeight() <= initialSituation.get(i).getPlace().getNumberOfToken()) {
                        temp.add(initialSituation.get(i).getTrans());

                    } else {
                        visit[i]=true;
                        //devo controllare che la transizione del primo elemento è abilitata
                        int elementOfTrans=1;
                        ArrayList<Pair> pairInTheTrans=new ArrayList<>();
                        //calcolo quanti elementi della trans t sono presenti in intial
                        for(int j=i+1; j<initialSituation.size(); j++){
                            if(initialSituation.get(i).getTrans().equals(initialSituation.get(j).getTrans())){
                                elementOfTrans++;
                                visit[j]=true;
                                pairInTheTrans.add(initialSituation.get(j));
                            }
                        }
                        //nel caso in cui si abbiano il numero giusto trans in initial faccio altri controlli faccio altro
                        if(initialSituation.get(i).getTrans().getIdPre().size()==elementOfTrans){
                            //modifico i valori dei token se è necessari, se non sono abbastanza la transizione non parte
                            for(int l=0; l<pairInTheTrans.size(); l++ ){
                                if(pairInTheTrans.get(l).getWeight()>pairInTheTrans.get(l).getPlace().getNumberOfToken()){
                                    continue;
                                }else {
                                    pairInTheTrans.get(l).getPlace().differenceToken(pairInTheTrans.get(l).getWeight());

                                }

                            }
                            temp.add(pairInTheTrans.get(i).getTrans());
                        }else {
                            continue;
                        }
                    }
                }

        return temp;
    }
    public ArrayList<Transition> initialization2( ArrayList<Pair> initialSituation) {
        ArrayList<Transition> temp=new ArrayList<>();
        HashMap <Pair, Transition> initial= new HashMap<>();



        for(int i=0; i<initialSituation.size(); i++){
            initial.put(initialSituation.get(i), initialSituation.get(i).getTrans());
        }
        for(Pair p: initialSituation){

        }

        return temp;
    }

    public int calculateN(ArrayList<Pair> initialMark, boolean[] visit, int n, int i) {

        for (int j = i + 1; j < initialMark.size(); j++) {

            if (visit[j] == true) {
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
                    }else{
                        return 0;
                    }

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

