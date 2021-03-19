package Project_v3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import Utility.Reader;

public class User {

    //QUESTO METODO HA SENSO DI ESISTERE SE NELLE PROSSIME VERSIONE L'UTENTE POTRà FARE ANCHE ALTRO OLTRE CHE AD AVVIARE LA SIMULAZIONE
    public void operation(NetManager netM) throws FileNotFoundException {

        //selezione di una rete
        int select;
        PetriNet selected;
        do {
//per ora è brutto ma faccio così


            System.out.println("\nYou have to load a net, which one do you want?");
            do {
                netM.loadNet();
            }while(Reader.yesOrNo("Do you want to load other nets?"));

            for (int i = 0; i < netM.getNetList().size(); i++) {
                System.out.println((i) + ")" + netM.getNetList().get(i).getName());
            }

            select=Reader.leggiInteroNonNegativo("Insert the number of the net that you want to use");
     selected= (PetriNet) netM.getNetList().get(select);
     startSimulation(selected);
        } while (Reader.yesOrNo("Do you want to make an other simulation?"));
    }
    public void startSimulation(PetriNet net){
        //TODO
     HashMap<Pair, Integer> temp=   net.getInitialMarking();
     ArrayList<Transition> present= new ArrayList<Transition>();
    for(Pair p: net.getPair()){
        //if the hashsmap contains the pair that means this pair is in it
        if(temp.containsKey(p)){
if(net.getSetOfTrans().contains(p.getTrans())){
                System.out.println("CIAO");
            }
        }
    }

    }

}
