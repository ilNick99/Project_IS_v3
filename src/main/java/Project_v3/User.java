package main.java.Project_v3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import main.java.Utility.IO;
import main.java.Utility.JsonManager;

public class User {



    /**
     * In this method there are all the action that the user can do. This method allows to do the correct action
     * @param netM we pass the NetManager because that can load the net which will be use
     * @throws FileNotFoundException
     */
     public void operation(NetManager netM) throws FileNotFoundException {
        ArrayList<PetriNet> loadNetPetri=new ArrayList<>();

        int select;
        PetriNet selected;

         boolean check=false;
         int choise = 0;
       //this switch manage the operations
         do {
             choise=IO.readInteger(IO.WHAT_DO_YOU_WANT_DO_0_EXIT_1_START_SIMULATION,0,2 );


             switch(choise){
            case 0:
                check=true;
                break;
            case 1:

                //the user decides which nets he wants to load
                    IO.print(IO.YOU_HAVE_TO_LOAD_A_NET_WHICH_ONE_DO_YOU_WANT);

                    do {

                        loadNetPetri.add(JsonManager.loadPetriNet());

                    }while(IO.yesOrNo(IO.DO_YOU_WANT_TO_LOAD_OTHER_NETS));

                    IO.printNets(loadNetPetri);
                check = IO.yesOrNo(IO.DO_YOU_WANT_CLOSE_THE_PROGRAM);

                break;
                //after the load the user can simulate the net
            case 2:
                if(loadNetPetri.size()==0){
                    IO.print(IO.THERE_AREN_T_ANY_NETS_LOADED_YOU_HAVE_TO_LOAD_ONE_NET_BEFORE_THE_SIMULATION);
                }else {
                   //the user have to choosen one net
                    IO.printNets(loadNetPetri);
                    select = IO.readInteger(IO.INSERT_THE_NUMBER_OF_THE_NET_THAT_YOU_WANT_TO_USE, 1, loadNetPetri.size());
                    selected = loadNetPetri.get(select - 1);
                   //we shows the net
                    IO.showPetriNet(selected);
                    selected.saveInitialMark();
                    //we start the simulation
                    simulation(selected, loadNetPetri.get(select - 1).getInitialMark());
                }
                check = IO.yesOrNo(IO.DO_YOU_WANT_CLOSE_THE_PROGRAM);

                break;

        }
         } while (!check );
    }

    /**
     * this method allows to simulate the actions of the Petri's net
     * @param pN this is the net that the user had choose
     * @param initialMark this is the initial mark or in the case of recursive option is the initial situazion in that time
     */
    public void simulation(PetriNet pN, ArrayList<Pair> initialMark) {
       assert  pN!=null;
       assert initialMark!=null;

        //in the we put the transition that can be chosen
        ArrayList<Transition> transitionThatCanWork = new ArrayList<Transition>();
        //visit avoid to check elements that we have already checked

        ArrayList<Pair> pairInTheTrans = new ArrayList<>();
       HashMap<Transition, ArrayList<Pair>> finalTrans= new HashMap<>();
       //initial situation give us the possibile element
       pN.initialSituationInTheNet(initialMark, transitionThatCanWork, finalTrans);


              //we have made all the checks, so in transitionThatCanWork there are the transitions that  we can use for the simulation
            //ho fatto i controlli possibili in pairInTheTrans ho le transazioni che possono scattare
                if (transitionThatCanWork.size() == 0) {
                    //In this case there aren't any transitions avaible
                    IO.print(IO.THERE_AREN_T_ANY_TRANSITION_AVAILABLE);

                } else {
                    //we ask to the user which transition he wants to use
                    int risp = whichPostisChosen(transitionThatCanWork);
                    //if the answer is negative that means that the user want to stop the simulation
                    if(risp<0){
                        //dovrei printare la rete
                        IO.showPetriNet(pN);
                        return;
                    }
                    //we have to change the token in the post transition
                    int weightTotal = getWeightTotal( finalTrans.get(transitionThatCanWork.get(risp)));
                    setPreandPost(pN, transitionThatCanWork, risp, weightTotal);
                    modifyThePrePair(finalTrans.get(transitionThatCanWork.get(risp)));
                    //we have to remove the token in the pre pairs
                    ArrayList<Pair> newInit=new ArrayList<>();
                    //we have to calculate the new situation
                    calculateNewInitialSituation(pN, newInit);

                    //we start a new simulation
                    simulation(pN, newInit);
                }


        }

    /**
     * this method ask to the user which transition he want to use
     * @param temp all the transitions avaible
     * @return the transition that the user has chosen
     */
    private int whichPostisChosen(ArrayList<Transition> temp) {
        assert temp!=null;
        IO.print(IO.THE_FOLLOWING_TRANSITION_ARE_AVAILABLE);
        //we print the transition
        IO.printTransition(temp);
        IO.print(IO.STOP);
//the user inserts the number of the transition that he wants to use
        return IO.readInteger(IO.INSERT_THE_NUMBER_OF_THE_TRANSITION_YOU_WANT_TO_USE, 0, temp.size()) - 1;
    }

    /**
     * this method removes the token in the pre paris
     * @param PreElement the elements that should be modified
     */
    private void modifyThePrePair(ArrayList<Pair> PreElement) {
        assert PreElement!=null;
    //we have to make the difference between the token and the weight
    for(Pair p: PreElement){
            p.getPlace().differenceToken(p.getWeight());
        }
    }

    /**
     * this method allows us to calculate the new situation
     * @param pN the petri net that we use
     * @param newInit the array where we put the element
     */
    private void calculateNewInitialSituation(PetriNet pN, ArrayList<Pair> newInit) {
        assert pN!=null;
        assert newInit!=null;
        ArrayList<Place> temporaryPlace= new ArrayList<>();
        for (Pair p: pN.getPairs()){
            //ew check if the place has some tokens and we don't want to add place more than once
            if(p.getPlace().getNumberOfToken()!=0 && !temporaryPlace.contains(p.getPlace())){
                IO.print("Place "+ p.getPlace().getName() + " has " + p.getNumberOfToken() + " token");
               newInit.add(p);
               temporaryPlace.add(p.getPlace());
            }
        }
    }




    private int getWeightTotal( ArrayList<Pair> temp) {



        int weightTotal=0;

        for(Pair p: temp){
            weightTotal = weightTotal + p.getWeight();
        }
        return weightTotal;
    }

    private void setPreandPost(PetriNet pN, ArrayList<Transition> temp, int risp, int weightTotal) {
        //aggiorno tutti i post della transizione modificando il valore dei loro pesi
        if(temp.get(risp).sizePost()==1){
            //al post ci metto la somma degli elementi dei pesi dei pre, è nelle coppie
            pN.getPair(pN.getPlace(temp.get(risp).getIdPost().get(0)), temp.get(risp)).setWeight(weightTotal);
        }else{
            IO.print(IO.THIS_TRANSITION_CAN_MOVE_THE_TOKENS_IN_DIFFERENT_PLACES);
            IO.printString(temp.get(risp).getIdPost());
         /*   System.out.println("This transition can move the tokens in different places");
            for(int i = 0; i< temp.get(risp -1).sizePost(); i++){
                System.out.println(i+1+") " + temp.get(risp).getIdPost().get(i));

            }*/
            //elemento è il post che devo modificare
            int elem=IO.readInteger(IO.WHERE_DO_YOU_WANT_TO_PUT_THE_TOKEN, 1, temp.get(risp).sizePost() )-1;
            pN.getPair(pN.getPlace(temp.get(risp).getIdPost().get(elem)), temp.get(risp)).setWeight(weightTotal);
        }
    }


}
