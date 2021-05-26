package main.java.Project_v3;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import main.java.Utility.IO;
import main.java.Utility.Reader;

public class User {




    //QUESTO METODO HA SENSO DI ESISTERE SE NELLE PROSSIME VERSIONE L'UTENTE POTRà FARE ANCHE ALTRO OLTRE CHE AD AVVIARE LA SIMULAZIONE
    public void operation(NetManager netM) throws FileNotFoundException {

        //selezione di una rete
        int select;
        PetriNet selected;
        do {
//per ora è brutto ma faccio così

            IO.print(IO.YOU_HAVE_TO_LOAD_A_NET_WHICH_ONE_DO_YOU_WANT);
            do {
                netM.loadNet(IO.JSON_PETRI_FILE);
            }while(IO.yesOrNo(IO.DO_YOU_WANT_TO_LOAD_OTHER_NETS));
            IO.printNet(netM.getNetList());
            select=IO.readInteger(IO.INSERT_THE_NUMBER_OF_THE_NET_THAT_YOU_WANT_TO_USE, 1, netM.getNetList().size());

     selected= (PetriNet) netM.getNetList().get(select-1);
     simulation(selected, selected.getInitialMarking());
        } while (IO.yesOrNo(IO.DO_YOU_WANT_TO_MAKE_AN_OTHER_SIMULATION));
    }


    public void simulation(PetriNet pN, ArrayList<Pair> initialMark) {

        ArrayList<Transition> temp = new ArrayList<Transition>();
        boolean[] visit = new boolean[initialMark.size()];
        IO.printElementWithToken(initialMark);

       temp= pN.initialization();

        //se temp è zero significa che non si sono transizioni abilitate
        if (temp.size() == 0) {
            IO.print(IO.THERE_AREN_T_ANY_TRANSITION_AVAILABLE);

        } else {
            //altrimenti mostro le transizioni abilitate e chiedo quale si voglia far scattare
            IO.print(IO.THE_FOLLOWING_TRANSITION_ARE_AVAILABLE);
            for (int i = 0; i < temp.size(); i++) {
                System.out.println((i+1) +") " + temp.get(i).getName());
            }

            int risp=IO.readInteger(IO.INSERT_THE_NUMBER_OF_THE_TRANSITION_YOU_WANT_TO_USE, 1, temp.size())-1;
            int weightTotal = getWeightTotal(pN, temp, risp);
            setPreandPost(pN, temp, risp, weightTotal);


            simulation(pN, initialMark);
        }
    }

  /*  private void initialization(ArrayList<Pair> initialMark, ArrayList<Transition> temp, boolean[] visit) {
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
    }*/



    private int getWeightTotal(PetriNet pN, ArrayList<Transition> temp, int risp) {
        int weightTotal=0;
        //ciclo su tutti i pre della transizione
        for(int i = 0; i< temp.get(risp).sizePre(); i++) {
            //controllo che il place sia presente nella pre
            for (Place p : pN.getSetOfPlace()) {
                //se è uguale aggiorno il numero dei token
                if (p.getName().equals(temp.get(risp).getName())) {
                    int a = p.getNumberOfToken() - pN.getPair(p, temp.get(risp)).getWeight();
                    //trovo il peso totale per far scattare la transizione
                    weightTotal = weightTotal + pN.getPair(p, temp.get(risp)).getWeight();
                    p.setToken(a);
                }
            }
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
