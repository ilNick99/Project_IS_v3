package Project_v3;

public class Pair {
    private Place place;
    private Transition trans;
    private int weight = 1;



    public  Pair(Place place, Transition trans){
        this.place=place;
        this.trans=trans;
    }

    public Pair(String placeName, Transition trans){
        this.place=new Place(placeName);
        this.trans=trans;
    }

    public Pair(Place place, String transName){
        this.place=place;
        this.trans=new Transition(transName);
    }
    public Transition getTrans() {
        return trans;
    }



    public Pair(String place_name, String trans_name, int inOut){
        this.place = new Place(place_name);
        this.trans = new Transition(trans_name, inOut);
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
        //check if the place's ID is equal to the toCompare's ID, and then check if the trans'S ID is equal to the toCOmpare'S ID
        if( (place.getName().compareTo(toCompare.getPlace().getName()) == 0) &&
                (trans.getName().compareTo(toCompare.getTrans().getName()) == 0)){
            return true;
        }
        return false;
    }

    public int getWeight() {
        return weight;
    }
}