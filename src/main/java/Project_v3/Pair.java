package main.java.Project_v3;

import java.util.ArrayList;

public class Pair {
    private Place place;
    private Transition trans;
    private int weight = 1;



    public Pair(Place place, Transition trans){
        assert !place.equals(null);
        this.place=place;
        this.trans=trans;
    }


    public Transition getTrans() {
        assert !place.equals(null);
        assert !trans.equals(null);
        return trans;
    }

    public Pair(String place_name, int token, String trans_name, int direction, int weight) {
        this.place = new Place(place_name, token);
        this.trans = new Transition(trans_name);
        this.trans.addPreOrPost(place_name, direction);
        this.weight = weight;
    }



    public Pair(Place place, Transition transition, int weight) {
        assert place != null;
        assert transition != null;
        assert weight >= 0;
        this.place = place;
        this.trans = transition;
        this.weight = weight;
    }
    public void setWeight(int weight) {

        this.weight = this.weight;
    }

    public Place getPlace() {
        return place;
    }
    /**
     * this method check if the current pair is equal to the other one
     * @param toCompare the pair which is compared
     * @return true if the pairs are equal
     */

    public boolean compare(Pair toCompare) {
        assert !toCompare.compare(null);
        //check if the place's ID is equal to the toCompare's ID, and then check if the trans'S ID is equal to the toCOmpare'S ID
        if( (place.getName().compareTo(toCompare.getPlaceName()) == 0) &&
                (trans.getName().compareTo(toCompare.getTransName()) == 0)){
            return true;
        }
        return false;
    }

    public String getTransName(){
        assert !trans.getName().equals(null);
        return  trans.getName();
    }
    public String getPlaceName(){
        assert !place.getName().equals(null);
        return  place.getName();
    }

    public int getNumberOfToken() {
        return place.getNumberOfToken();
    }

    public String getIdPreviusPlaceByIndex(int i) {
        return trans.getIdPreviusPlaceByIndex(i);
    }

    public String getIdPostPlaceByIndex(int i) {
        return trans.getIdPostPlaceByIndex(i);
    }

    public int getWeight() {
        return weight;
    }
}