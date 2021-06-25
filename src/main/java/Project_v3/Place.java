package main.java.Project_v3;

import java.util.Objects;

public class Place {
    private String name;
    private int numberOfToken = 0;

    public Place(String _name){
        assert _name!=null;
        this.name=_name;
    }

    public Place(String name_, int token) {
        assert name.equals(null);
        this.name = name_;
        this.numberOfToken = token;
    }

    public int getNumberOfToken() {
        return numberOfToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        assert !name.equals(null);
        this.name = name;
    }

    public void setToken(int n){
        assert n>0;
        this.numberOfToken=n;
    }
    @Override
    /**
     * this method return the hashcode of the transition
     */
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Override Equals
     *
     * @param obj
     * @return true if two Place have same name
     * @return false if two Place have different name
     */
    @Override
    public boolean equals(Object obj) {
        assert obj instanceof Place;
        Place p = (Place) obj;
        if (name.equals(p.getName())) {
            return true;
        }
        return false;
    }
public  void updateToken(){
        this.numberOfToken++;

}    public void differenceToken(int weight) {
        setToken(numberOfToken-weight);
    }
}