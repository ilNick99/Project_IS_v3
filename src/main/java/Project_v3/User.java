package main.java.Project_v3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import main.java.Utility.IO;
import main.java.Utility.JsonManager;

public class User {




    //QUESTO METODO HA SENSO DI ESISTERE SE NELLE PROSSIME VERSIONE L'UTENTE POTRà FARE ANCHE ALTRO OLTRE CHE AD AVVIARE LA SIMULAZIONE
    public void operation(NetManager netM) throws FileNotFoundException {
        ArrayList<PetriNet> loadNetPetri=new ArrayList<>();
        //selezione di una rete
        int select;
        PetriNet selected;
        do {
            //per ora è brutto ma faccio così

            IO.print(IO.YOU_HAVE_TO_LOAD_A_NET_WHICH_ONE_DO_YOU_WANT);

            do {

                loadNetPetri.add(JsonManager.loadPetriNet());

            }while(IO.yesOrNo(IO.DO_YOU_WANT_TO_LOAD_OTHER_NETS));

            IO.printNets(loadNetPetri);
            select=IO.readInteger(IO.INSERT_THE_NUMBER_OF_THE_NET_THAT_YOU_WANT_TO_USE, 1, loadNetPetri.size());
            selected=loadNetPetri.get(select-1);
           IO.showPetriNet(selected);
            selected.saveInitialMark();
     simulation2(selected, loadNetPetri.get(select-1).getInitialMark());
            //simulazione(selected, selected.getInitialMark());
        } while (IO.yesOrNo(IO.DO_YOU_WANT_TO_MAKE_AN_OTHER_SIMULATION));
    }

    public void simulation2(PetriNet pN, ArrayList<Pair> initialMark) {
        ArrayList<Transition> temp = new ArrayList<Transition>();
        boolean[] visit = new boolean[initialMark.size()];
        ArrayList<Pair> pairInTheTrans = new ArrayList<>();
       HashMap<Transition, ArrayList<Pair>> finalTrans= new HashMap<>();
        for (int i = 0; i < initialMark.size(); i++) {
            pairInTheTrans = new ArrayList<>();
            if (visit[i] == true) {
                continue;
            }


            visit[i] = true;

            // se il posto non è nei predecessori della transizione pur avendo dei token viene saltata perchè non contribuisce allo scatto
            if (initialMark.get(i).getTrans().isIn(initialMark.get(i).getPlace().getName()) == false) {
                continue;
            }

            //se si ha un unico pre e si hanno abbastanza token la transizione viene subito aggiunta
            if (initialMark.get(i).getTrans().sizePre() == 1 && initialMark.get(i).getWeight() <= initialMark.get(i).getPlace().getNumberOfToken()) {
                temp.add(initialMark.get(i).getTrans());
                pairInTheTrans.add(initialMark.get(i));
                finalTrans.put(initialMark.get(i).getTrans(), pairInTheTrans);
                continue;
            }

            //significa che la transazione non potrà mai scattare
            if (initialMark.get(i).getNumberOfToken() > initialMark.get(i).getWeight()) {
                //devo controllare che la transizione del primo elemento è abilitata
                int elementOfTrans = 1;
                //int sumOfEveryTrans=initialMark.get(i).getNumberOfToken();
               pairInTheTrans = new ArrayList<>();
                boolean errato = true;
                pairInTheTrans.add(initialMark.get(i));
                //calcolo quanti elementi della trans t sono presenti in intial
                for (int j = i + 1; j < initialMark.size(); j++) {
                    //se errato è falso significa che non devo fare controlli ma indico già che è visitato
                    if (errato == false) {

                        visit[j] = true;
                        continue;
                    }

                    if (initialMark.get(i).getTrans().equals(initialMark.get(j).getTrans())) {
                        //se non rispetta questa condizione significa che non si hanno abbastanza elementi totali, non continuo a fare controlli ma pongo gli altri elementi in modo
                        //non ci siano ulteriori controlli
                        if (pairInTheTrans.get(j).getNumberOfToken() < pairInTheTrans.get(j).getWeight()) {
                            errato = false;
                            continue;
                        }

                        elementOfTrans++;
                        // sumOfEveryTrans=sumOfEveryTrans+initialMark.get(j).getNumberOfToken();
                        visit[j] = true;
                        pairInTheTrans.add(initialMark.get(j));
                    }

                }
                //ho meno elementi di quelli che dovrei avere passo oltre o se un elemento non era corretto
                if (elementOfTrans < initialMark.get(i).getTrans().sizePre() || errato == false) {
                    continue;
                }

                //devo controllare se togliendo il peso vado sotto zero

                temp.add(initialMark.get(i).getTrans());
                finalTrans.put(initialMark.get(i).getTrans(), pairInTheTrans);
            }



            }

            //ho fatto i controlli possibili in pairInTheTrans ho le transazioni che possono scattare
                if (temp.size() == 0) {
                    IO.print(IO.THERE_AREN_T_ANY_TRANSITION_AVAILABLE);

                } else {
                    //altrimenti mostro le transizioni abilitate e chiedo quale si voglia far scattare
                    IO.print(IO.THE_FOLLOWING_TRANSITION_ARE_AVAILABLE);
                    for (int i = 0; i < temp.size(); i++) {
                        System.out.println((i+1) +") " + temp.get(i).getName());
                    }

                    //mi dice quale transazione devo far scattare
                    int risp=IO.readInteger(IO.INSERT_THE_NUMBER_OF_THE_TRANSITION_YOU_WANT_TO_USE, 1, temp.size())-1;

                    var var= finalTrans.get( temp.get(risp));
                    //sposto i token tolti in quelli nei post
                    int weightTotal = getWeightTotal( finalTrans.get(temp.get(risp)));


                    //devo modificare gli elementi dei preSottraendo ai token il weight
                    for(Pair p: finalTrans.get(temp.get(risp))){
                        p.getPlace().differenceToken(p.getWeight());
                    }



                    setPreandPost(pN, temp, risp, weightTotal);

                    ArrayList<Pair> newInit=new ArrayList<>();
                    //devo calcolare la nuova situazione iniziale
                    for (Pair p: pN.getPairs()){
                        if(p.getPlace().getNumberOfToken()!=0){
                            System.out.println(p.getNumberOfToken());
                           newInit.add(p);
                        }
                    }

                    simulation2(pN, newInit);
                }


        }





    public void simulation(PetriNet pN, ArrayList<Pair> initialMark) {

        ArrayList<Transition> temp = new ArrayList<Transition>();
        boolean[] visit = new boolean[initialMark.size()];
        IO.printElementWithToken(initialMark);

       temp= pN.initialization(initialMark);

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
           // int weightTotal = getWeightTotal( temp, risp);

            //setPreandPost(pN, temp, risp, weightTotal);

            simulation(pN, initialMark);
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
