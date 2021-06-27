package main.java.Project_v3;

import main.java.Utility.IO;

import java.util.*;

public class PetriNet extends Net implements  Simulation{



    private HashMap<Pair, Integer> initialMarking = new HashMap<>();
    private ArrayList<Pair> initialMark=new ArrayList<>();
    public HashMap<Pair, Integer> getInitialMarking() {
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

    /**
     * this method check all the differnt case in  a Petri's Net in order to decide which transitions can work
     * @param initialMark, this parameter identify the initial situazion in that moment and it doesn't always indicate che initial mark of the net
     * @param temp the arraylist where we put the transition that can be moved
     * @param finalTrans we put the element that will be shows to the user, who has to choose which one has to work
     */
    public void initialSituationInTheNet( ArrayList<Pair> initialMark, ArrayList<Transition> temp, HashMap<Transition, ArrayList<Pair>> finalTrans) {
       assert initialMark!=null;
       assert temp!=null;
       assert finalTrans!=null;
       //this array avoid to check pair that we have already checked
        boolean[] visit = new boolean[initialMark.size()];

        ArrayList<Pair> pairInTheTrans;
        for (int i = 0; i < initialMark.size(); i++) {
            //first of all we check if the pair has already checked
            pairInTheTrans = new ArrayList<>();
            if (visit[i] == true) {
                continue;
            }

            //we put visit true, so in the future this pair will not be check again
            visit[i] = true;

            //if the place isn't in the precs of the transition, that isn't usefull
            // se il posto non è nei predecessori della transizione pur avendo dei token viene saltata perchè non contribuisce allo scatto
            if (initialMark.get(i).getTrans().isIn(initialMark.get(i).getPlace().getName()) == false) {
                continue;
            }

            //if there is only and there are enough token the transition is add to temp and to th other elemet
            //se si ha un unico pre e si hanno abbastanza token la transizione viene subito aggiunta
            if (initialMark.get(i).getTrans().sizePre() == 1 && initialMark.get(i).getWeight() <= initialMark.get(i).getPlace().getNumberOfToken()) {
                temp.add(initialMark.get(i).getTrans());
                pairInTheTrans.add(initialMark.get(i));
                finalTrans.put(initialMark.get(i).getTrans(), pairInTheTrans);
                //checkTheElementMultipleCase(initialMark, visit, pairInTheTrans, i, 1, true);
                continue;
            }

            //we check if there are enough token in the transition, in the opposite case there the transition can't work
            //significa che la transazione non potrà mai scattare
            if (initialMark.get(i).getNumberOfToken() > initialMark.get(i).getWeight()) {

                pairInTheTrans = new ArrayList<>();

                pairInTheTrans.add(initialMark.get(i));

                //we check if the other element in the initial mark that refers to the same transition are correct
                if (checkTheElementMultipleCase(initialMark, visit, pairInTheTrans, i)) continue;


            //we add to temp the current transition
                temp.add(initialMark.get(i).getTrans());
                //we insert in final trans the final elements, so the element that can be used for the simulation
                finalTrans.put(initialMark.get(i).getTrans(), pairInTheTrans);
            }



        }
    }

    /**
     * this method dies all the actions necessary for the case where there are more than pair connected to the transition, so we have to check if all the pair are correct
     * @param initialMark contains all the pair that has token
     * @param visit this array allows to check only the element that we haven't checked
     * @param pairInTheTrans this arrayList contains or it will contain the pair connected to the transition that we are checking
     * @param i give us the indication about which element we are checking
     * @return
     */
    public boolean checkTheElementMultipleCase(ArrayList<Pair> initialMark, boolean[] visit, ArrayList<Pair> pairInTheTrans, int i) {
        assert initialMark!=null;
        assert visit!=null;
        assert pairInTheTrans!=null;
        assert i>-1;
        //this variable contains the number of the elements in the initial markink which refer the current transition
        int elementOfTrans = 1;
        boolean checkIfTheTransitionCanWork = true;
        //we loop on all the pair in the initialMark in order to find only the one that hase the same transition of the pair that we are checking
        //calcolo quanti elementi della trans t sono presenti in intial
        for (int j = i + 1; j < initialMark.size(); j++) {
            //if checkIfTheTransitionCanWork is false that means that we don't have to check other pair
            //se checkIfTheTransitionCanWork è falso significa che non devo fare controlli ma indico già che è visitato
            if (checkIfTheTransitionCanWork == false) {
            //we decided to continue the loop because we can set visit true for all the other elements which have the same transition in order to avoid multiple check in the next actions
            //the element will not be add to the final array so che size will be incorrect
                visit[j] = true;
                continue;
            }

            // we want the elements that has the same transition of the pair that we are checking
            if (initialMark.get(i).getTrans().equals(initialMark.get(j).getTrans())) {

                //se non rispetta questa condizione significa che non si hanno abbastanza elementi totali, non continuo a fare controlli ma pongo gli altri elementi in modo
                //non ci siano ulteriori controlli
                //if the pair has less token that the transition required the transition can't work, so all the other actions will be usefull
                if (initialMark.get(j).getNumberOfToken() < initialMark.get(j).getWeight()) {
                    checkIfTheTransitionCanWork = false;
                    continue;
                }
                //in the case that the condition is  not verify we can continue to do action
                elementOfTrans++;
                // sumOfEveryTrans=sumOfEveryTrans+initialMark.get(j).getNumberOfToken();

                visit[j] = true;
                    //so we had to add the pair to the arraylist
                    pairInTheTrans.add(initialMark.get(j));
            }

        }

        //if the element of trans has less element than the element in the pre of the transition, we can't add the transition to the final check
        //also if the check in the transition can work is false the addition can be made. The second check is redundant
        //ho meno elementi di quelli che dovrei avere passo oltre o se un elemento non era corretto
        if (elementOfTrans < initialMark.get(i).getTrans().sizePre() || checkIfTheTransitionCanWork == false) {
            return true;
        }
        return false;
    }

    /**
     * this method is necessary to have the initial situation in the correct structures
     * @return the element that contains all the transitions that can work and their pair
     */
    public HashMap<Transition, ArrayList<Pair>> simulation(ArrayList<Pair> initialMark){

        //in the we put the transition that can be chosen
        ArrayList<Transition> transitionThatCanWork = new ArrayList<Transition>();
        HashMap<Transition, ArrayList<Pair>> finalTrans= new HashMap<>();
        //
        initialSituationInTheNet(initialMark, transitionThatCanWork, finalTrans);
        return finalTrans;
    }

    /**
     * this method allows to modifu the token in the post place
     * @param transitionThatWeHaveToModify this structure contains the elements that we have to modify
     */
    public void setPreandPost( Transition transitionThatWeHaveToModify) {
        //aggiorno tutti i post della transizione modificando il valore dei loro pesi
        if (transitionThatWeHaveToModify.sizePost() == 1) {
            //we modify the only element presents
            getPair(getPlace(transitionThatWeHaveToModify.getIdPost().get(0)), transitionThatWeHaveToModify).getPlace().updateToken();
        } else {
            //we have to modify the token in the place due to the transition
            for (int i = 0; i < transitionThatWeHaveToModify.getIdPost().size(); i++) {
                getPair(getPlace(transitionThatWeHaveToModify.getIdPost().get(i)), transitionThatWeHaveToModify).getPlace().updateToken();
            }
        }
    }

    /**
     * this method allows us to calculate the new situation

     * @param newInit the array where we put the element
     */
    public void calculateNewInitialSituation(ArrayList<Pair> newInit) {

        assert newInit!=null;
        ArrayList<Place> temporaryPlace= new ArrayList<>();
        for (Pair p: getPairs()){
            //ew check if the place has some tokens and we don't want to add place more than once
            if(p.getPlace().getNumberOfToken()!=0 ){
                newInit.add(p);
                temporaryPlace.add(p.getPlace());
            }
        }
    }
}

