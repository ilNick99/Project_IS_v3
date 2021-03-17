package Project_v3;

import Utility.Reader;

import java.util.*;

import Utility.Reader;

public class Net {
    public static final String INSERT_PLACE_S_ID = "Insert place's Name ";
    public static final String INSERT_TRANSITION_S_ID = "Insert transition's Name ";
    public static final String YOU_CAN_T_ADD_THIS_PAIR_BECAUSE_ALREADY_EXISTS = "You can't Add this pair because it already exists";
    public static final String YOU_WANT_ADD_ANOTHER_PAIR = "You want add another Pair?";

    private HashSet<Place> setOfPlace = new HashSet<Place>();
    private HashSet<Transition> setOfTrans = new HashSet<Transition>();
    private ArrayList<Pair> net = new ArrayList<Pair>();
    private String ID;
    private String name;
    private static int i;

    public HashSet<Place> getSetOfPlace() {
        return setOfPlace;
    }

    public HashSet<Transition> getSetOfTrans() {
        return setOfTrans;
    }

    public ArrayList<Pair> getNet() {
        return net;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Net(Net _net) {
        net.addAll(_net.getNet());
        this.setOfPlace.addAll(_net.setOfPlace);
        this.setOfTrans.addAll(_net.setOfTrans);
        this.name = _net.getName();
        this.ID = _net.getID();
    }

    /**
     * constructor for the net
     *
     * @param name the name that the user what to give to the net
     */
    public Net(String name) {
        this.name = name;
        this.ID = name + (++i); //DA FARE CONTROLLO SULL'ID
        addPair();
        // addPrePost();
    }

    public Net(String name, String id) {
        this.name = name;
        this.ID = id;
    }

    /**
     * this method allow to insert a new node, it is composed by a place and a transition
     */
    public void addPair() {
        boolean checkPlace = false, checkTrans = false;
        String placeName;
        String transName;
        int inOrOut;

        do {
            // ask to user the place's ID and the transition's ID
            placeName = Reader.readNotEmpityString(INSERT_PLACE_S_ID);
            transName = Reader.readNotEmpityString(INSERT_TRANSITION_S_ID);
            inOrOut = Reader.leggiIntero("Which type of connection there is between the place " + placeName + "and the transition " + transName + "? \n 1)" + placeName + " is an INPUT of " + transName + "\n 2)" + placeName + " is an OUTPUT of " + transName + "\n", 1, 2);
            //this If check if the new node is equal to another one which is already in the net

            //I create a temporary transition and a temporary place because it makes easy to check them
            Transition t = new Transition(transName);
            Place p = new Place(placeName);


            //if the net is empty I don't recall the check method because this is useless
            if (net.size() == 0) {
                //with this if I check if the node is a in or an output
                if (inOrOut == 1) {
                    //if this is an in I add the place to the pre
                    t.addPre(placeName);
                } else {
                    //if this is an in I add the place to the p0st
                    t.addPost(placeName);
                }
                //we add the pair at the net because it is correct
                net.add(new Pair(p, t));

                setOfPlace.add(p);
                setOfTrans.add(t);

            } else {
                //I check if the pair is equal than an other one which already exists
                if (checkPair(new Pair(p, t)) == true) {
                    //with this if I check if the node is a in or an output
                    if (inOrOut == 1) {
                        //if this is an in I add the place to the pre
                        t.addPre(placeName);
                    } else {
                        //if this is an in I add the place to the post
                        t.addPost(placeName);
                    }
                    net.add(new Pair(p, t));
                    //this for looks for if the place already exist
                    for (Place pl : setOfPlace) {
                        if (placeName.compareTo(pl.getName()) == 0) {
                            checkPlace = true;
                        }
                    }
                    //this for looks for if the place already exist
                    for (Transition tr : setOfTrans) {
                        if (transName.compareTo(tr.getName()) == 0) {
                            if (inOrOut == 1) {
                                tr.addPre(p.getName());
                            } else {
                                tr.addPost(p.getName());
                            }
                            checkTrans = true;
                        }
                    }
                    // 1) if checkPlace and checkTrans are false it means that the places and transitions I want to add have not been found in the sets,
                    // 2) if checkPlace = false and checkTrans = true then it means that I have to add only the transitions in the set
                    // 3) if checkPlace = true and checkTrans = false then it means that I have to add only the place in the set
                    if (!checkPlace && !checkTrans) {
                        setOfTrans.add(t);
                        setOfPlace.add(p);
                    } else if (!checkPlace && checkTrans) {
                        setOfPlace.add(p);
                    } else if (checkPlace && !checkTrans) {
                        setOfTrans.add(t);
                    }
                    checkTrans = false;
                    checkPlace = false;

                } else {
                    //I say to the user that the pair already exists
                    System.out.println(YOU_CAN_T_ADD_THIS_PAIR_BECAUSE_ALREADY_EXISTS);
                }
            }
        } while (Reader.yesOrNo(YOU_WANT_ADD_ANOTHER_PAIR));
    }


    public void addPairFromJson(Pair pair) {
        net.add(pair);
    }

    //metodo per tornare un posto dal set dato il nome
    public Place getPlace(String name) {
        for (Place p : setOfPlace) {
            if (name.compareTo(p.getName()) == 0) {
                return p;
            }
        }
        return null;
    }

    //metodo per tornare una transizione dal set dato il nome
    public Transition getTrans(String name) {
        for (Transition t : setOfTrans) {
            if (name.compareTo(t.getName()) == 0) {
                return t;
            }
        }
        return null;
    }


    /**
     * This metod check if two Pair are equal
     *
     * @param pairToCheck
     * @return false if Pair are equal
     */
    public boolean checkPair(Pair pairToCheck) {
        for (Pair element : net) {
            if (element.compare(pairToCheck) == true) {
                return false;
            }
        }
        return true;
    }

    //controllo dei nodi pendenti, da rivedere e commentare
    public boolean checkPendantNode() {
        for (int i = 0; i < net.size(); i++) {
            boolean check = false;
            String toCheck = net.get(i).getTrans().getName();
            for (int j = 0; j < net.size(); j++) {
                if (i != j && toCheck.compareTo(net.get(j).getTrans().getName()) == 0) {
                    check = true;
                }
            }
            if (check == false) {
                return false;
            }
        }
        return true;
    }


    /**
     * this method allows to fill the sets of places and transitions starting from an already existing net
     */
    public void fillSet() {
        int i;
        boolean check = false;
        for (i = 0; i < net.size(); i++) {
            for (Transition t : setOfTrans) {
                if (net.get(i).getTrans().getName().compareTo(t.getName()) == 0) {
                    check = true;
                }
            }
            if(!check) setOfTrans.add(net.get(i).getTrans());
            check=false;
            for (Place p : setOfPlace) {
                if (net.get(i).getPlace().getName().compareTo(p.getName()) == 0) {
                    check=true;
                }
            }
            if(!check)setOfPlace.add(net.get(i).getPlace());
            check=false;
        }
    }


    /**
     * Deep search algorithm to check if the graph is connected
     * @return true if connected otherwise it returns false
     */
    public boolean checkConnect() {

        String firstPlace = null; //
        /*
         * First of all I have to create maps that allow me to move on the graph with a recursive method
         * I need a map keyed to the name of the places and a boolean as an element, as I will have to indicate which nodes I have already visited.
         * Also I need another map that allows me to associate its neighbors to each node
         */
        HashMap<String, Boolean> check = new HashMap<>();
        HashMap<String, ArrayList<String>> map = new HashMap<>();


        ArrayList<String> temp = new ArrayList<>(); //Temporary arraylist of strings that I use to create maps

        //I fill the maps, both those of bool and those with links
        for (Place place : setOfPlace) { //cycle on all places
            firstPlace = place.getName();
            check.put(place.getName(), false);
            for (Transition trans : setOfTrans) { // at this point I cycle over all the transitions to see if that place is part of its pre / post
                //trans Pre
                for (String name : trans.getIdPre()) {
                    if (name.compareTo(place.getName()) == 0) { // if that post is part of the predecessors then I loop all its posts and add them to a temporary array
                        for (String namePost : trans.getIdPost()) {
                            temp.add(namePost);
                        }
                    }
                }
            }
            // I insert the place associated with its pre / post in the map
            map.put(place.getName(), new ArrayList<>(temp));
            temp.clear();
        }

        // loop again in reverse to add the nodes I haven't visited in one way and update the data already there
        for (Place place : setOfPlace) {
            // if the key is not already present in the map I proceed as before without problems
            if (map.containsKey(place.getName()) == false) {
                for (Transition trans : setOfTrans) {
                    for (String name : trans.getIdPost()) { // loop over all transitions to see if that post is part of his posts
                        if (name.compareTo(place.getName()) == 0) {
                            for (String namePre : trans.getIdPre()) {
                                temp.add(namePre);
                            }
                        }
                    }
                }
                map.put(place.getName(), new ArrayList<>(temp));
                temp.clear();
            } else { // otherwise the key already exists and I have to add the nodes to the already existing array
                for (Transition trans : setOfTrans) {
                    for (String name : trans.getIdPost()) { // loop over all transitions to see if that post is part of his posts
                        if (name.compareTo(place.getName()) == 0) { // check if it is present as a pre in some transition
                            for (String namePre : trans.getIdPre()) { // if it is present then loop on the pre to insert it into the map
                                map.get(place.getName()).add(namePre);
                            }
                        }
                    }
                }
            }


        }

        // start recursion
        recursion(map, check, firstPlace);

        // Check on the results of the recursion, if a node in the map is false it means that it has not been visited; therefore it is not connected
        for (Place id : setOfPlace) {
            if (check.get(id.getName()) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Recursive method that allows, starting from a random node, to visit all the nodes of the graph if it is connected.
     * Starting from the first node, it is marked as already visited on the boolmap, then it cycles on the neighbors and invoking this method again if they have not been visited.
     *
     * @param map     it is the map on which I perform the recursion to move from one place to its neighbors, it does not have to be empty
     * @param boolmap is the map in which I sign if the node has been visited or not, there must be all the places present in the network
     * @param key     is the key to the place to recursion, it does not have to be empty
     */
    public void recursion(HashMap<String, ArrayList<String>> map, HashMap<String, Boolean> boolmap, String key) {
        boolmap.replace(key, true);
        for (String nextKey : map.get(key)) {
            if (!boolmap.get(nextKey)) {
                recursion(map, boolmap, nextKey);
            }
        }

    }

    public boolean checkTrans() {
        for (Transition t : setOfTrans) {
            if (t.sizePre() == 0 || t.sizePost() == 0) {
                return false;
            }
        }
        return true;
    }
}