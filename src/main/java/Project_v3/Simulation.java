package main.java.Project_v3;

import java.util.ArrayList;
import java.util.HashMap;

public interface Simulation {

    public void initialSituationInTheNet( ArrayList<Pair> initialMark, ArrayList<Transition> temp, HashMap<Transition, ArrayList<Pair>> finalTrans) ;
    public boolean checkTheElementMultipleCase(ArrayList<Pair> initialMark, boolean[] visit, ArrayList<Pair> pairInTheTrans, int i) ;

    }
