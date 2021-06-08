package main.java.Project_v3;

import java.util.ArrayList;

public interface Simulation {

    public  ArrayList<Transition> initialization(ArrayList<Pair> initialSituation);
    public int calculateN(ArrayList<Pair> initialMark, boolean[] visit, int n, int i);

    }
