package Project_v3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

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
     simulazione(selected, selected.getInitialMarking());
        } while (Reader.yesOrNo("Do you want to make an other simulation?"));
    }


    public void simulazione(PetriNet pN, ArrayList<Pair> initialMark) {

        ArrayList<Transition> temp = new ArrayList<Transition>();
        boolean[] visit = new boolean[initialMark.size()];
        System.out.println("The first marking is given by:");
        for (int i = 0; i < initialMark.size(); i++) {
            System.out.println(initialMark.get(i).getPlace().getName() + " where there are " + initialMark.get(i).getPlace().getNumberOfToken());
        }
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

                }

            }
            //se il peso totale è maggiore o uguale a quello richiesto lo aggiungo
            if (n >= initialMark.get(i).getWeight()) {
                temp.add(initialMark.get(i).getTrans());
            }
            n = 0;
        }

        //se temp è zero significa che non si sono transizioni abilitate
        if (temp.size() == 0) {
            System.out.println("There aren't any transition available ");

        } else {
            //altrimenti mostro le transizioni abilitate e chiedo quale si voglia far scattare
            System.out.println("The following transition are available");
            for (int i = 0; i < temp.size(); i++) {
                System.out.println((i+1) +") " + temp.get(i).getName());
            }
            int risp=Reader.leggiIntero("Insert the number of the transition you want to use", 1, temp.size())-1;
            int weightTotal=0;
            //ciclo su tutti i pre della transizione
            for(int i=0; i<temp.get(risp).sizePre(); i++) {
                //controllo che il place sia presente nella pre
                for (Place p : pN.getSetOfPlace()) {
                    //se è uguale aggiorno il numero dei token
                    if (p.getName().equals(temp.get(risp).getName())) {
                        int a = p.getNumberOfToken() - pN.getPair(p, temp.get(risp)).getWeight();
                        //trovo il peso totale per far scattare la transizione
                        weightTotal = weightTotal + pN.getPair(p, temp.get(risp )).getWeight();
                        p.setToken(a);
                    }
                }
            }
            //aggiorno tutti i post della transizione modificando il valore dei loro pesi
            if(temp.get(risp).sizePost()==1){
                //al post ci metto la somma degli elementi dei pesi dei pre, è nelle coppie
                pN.getPair(pN.getPlace(temp.get(risp).getIdPost().get(0)), temp.get(risp)).setWeight(weightTotal);
            }else{
                System.out.println("This transition can move the tokens in different places");
                for(int i=0; i<temp.get(risp-1).sizePost(); i++){
                    System.out.println(i+1+") " + temp.get(risp).getIdPost().get(i));

                }
                //elemento è il post che devo modificare
                int elem=Reader.leggiIntero("Where do you want to put the token?", 1,temp.get(risp).sizePost() )-1;
                pN.getPair(pN.getPlace(temp.get(risp).getIdPost().get(elem)), temp.get(risp)).setWeight(weightTotal);
            }


            simulazione(pN, initialMark);
        }
    }
}
