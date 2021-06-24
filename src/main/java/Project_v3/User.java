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
                check = IO.yesOrNo("Do you want close the program?\n");

                //simulazione(selected, selected.getInitialMark());
               // } while (IO.yesOrNo(IO.DO_YOU_WANT_TO_MAKE_AN_OTHER_SIMULATION));
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
                check = IO.yesOrNo("Do you want close the program?\n");

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
        //in the we put the transition that can be chosen
        ArrayList<Transition> transitionThatCanWork = new ArrayList<Transition>();
        //visit avoid to check elements that we have already checked

        ArrayList<Pair> pairInTheTrans = new ArrayList<>();
       HashMap<Transition, ArrayList<Pair>> finalTrans= new HashMap<>();
       //initial situazion give us the possibile element
       pN.initialSituationInTheNet(initialMark, transitionThatCanWork, finalTrans);



            //ho fatto i controlli possibili in pairInTheTrans ho le transazioni che possono scattare
                if (transitionThatCanWork.size() == 0) {
                    IO.print(IO.THERE_AREN_T_ANY_TRANSITION_AVAILABLE);

                } else {
                    int risp = whichPostisChosen(transitionThatCanWork);
                    if(risp<0){
                        //dovrei printare la rete
                        return;
                    }
                    var var= finalTrans.get( transitionThatCanWork.get(risp));
                    //sposto i token tolti in quelli nei post
                    int weightTotal = getWeightTotal( finalTrans.get(transitionThatCanWork.get(risp)));





                    setPreandPost(pN, transitionThatCanWork, risp, weightTotal);
                    modifyThePrePair(transitionThatCanWork, finalTrans, risp);
                    ArrayList<Pair> newInit=new ArrayList<>();
                    //devo calcolare la nuova situazione iniziale
                    calculateNewInitialSituation(pN, newInit);

                    simulation(pN, newInit);
                }


        }

    private int whichPostisChosen(ArrayList<Transition> temp) {
        //altrimenti mostro le transizioni abilitate e chiedo quale si voglia far scattare
        IO.print(IO.THE_FOLLOWING_TRANSITION_ARE_AVAILABLE);
        for (int i = 0; i < temp.size(); i++) {
            System.out.println((i + 1) + ") " + temp.get(i).getName());
        }
        IO.print("If you want to stop the simulatoin press 0");
        //mi dice quale transazione devo far scattare
        return IO.readInteger(IO.INSERT_THE_NUMBER_OF_THE_TRANSITION_YOU_WANT_TO_USE, 0, temp.size()) - 1;
    }

    private void modifyThePrePair(ArrayList<Transition> temp, HashMap<Transition, ArrayList<Pair>> finalTrans, int risp) {
        //devo modificare gli elementi dei preSottraendo ai token il weight
        for(Pair p: finalTrans.get(temp.get(risp))){
            p.getPlace().differenceToken(p.getWeight());
        }
    }

    private void calculateNewInitialSituation(PetriNet pN, ArrayList<Pair> newInit) {
        ArrayList<Place> temporaryPlace= new ArrayList<>();
        for (Pair p: pN.getPairs()){
            if(p.getPlace().getNumberOfToken()!=0 && !temporaryPlace.contains(p.getPlace())){
                System.out.println("Place "+ p.getPlace().getName() + " has " + p.getNumberOfToken() + " token");
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
