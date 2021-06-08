package main.java.Project_v3;

import java.util.ArrayList;
import java.util.Objects;

public class Transition {
    private String name;
    private ArrayList<String> idPre = new ArrayList<>();
    private ArrayList<String> idPost = new ArrayList<>();

    public Transition(String name) {
        this.name = name;
    }

    public void addPreOrPost(String placeName, int inOut) {
        if (inOut == 1)
            idPre.add(placeName);
        else
            idPost.add(placeName);
    }

    /**
     * this method gets the name of the transition
     *
     * @return the name of the transition
     */
    public String getName() {
        assert name != null;
        return name;
    }

    /**
     * this method sets a new name
     *
     * @param name the new name
     */
    public void setName(String name) {
        assert name != null;
        this.name = name;
    }

    /**
     * this method return the arraylist of the places which are in input of the transition
     *
     * @return all the Id of the pre
     */
    public ArrayList<String> getIdPre() {
        assert idPre != null;
        return idPre;
    }

    /**
     * this method return the arraylist of the places which are in output of the transition
     *
     * @return all the Id of the post
     */
    public ArrayList<String> getIdPost() {
        assert idPost != null;
        return idPost;
    }


    /**
     * this method allows to add the a pre to the arraylist
     *
     * @param placeName the place adds to the arraylist
     */
    public void addPre(String placeName) {
        assert placeName != null;
        idPre.add(placeName);
    }

    /**
     * this method allows to add the a post to the arraylist
     *
     * @param placeName the place adds to the arraylist
     *                  PRECONDITION:assert  a!=null
     */
    public void addPost(String placeName) {
        assert placeName != null;
        idPost.add(placeName);
    }

    /**
     * this method allows to know the size of pre
     *
     * @return the size of the arraylist of pre
     */
    public int sizePre() {
        assert idPre != null;
        return idPre.size();
    }

    /**
     * this method allows to know the size of post
     *
     * @return the size of post
     */
    public int sizePost() {
        assert idPost != null;
        return idPost.size();
    }

    /**
     * this method allows to understand if a connection is a pre or a post
     *
     * @param name
     * @return
     */
    public int getInputOutput(String name) {
        int ret = -1;
        for (String item : idPre)
            if (item.equals(name))
                ret = 1;
        for (String item : idPost)
            if (item.equals(name))
                ret = 0;
        return ret;
    }

    @Override
    /**
     * this method return the hashcode of the transition
     */
    public int hashCode() {
        assert name != null;
        return Objects.hash(name);
    }

    /**
     * Override Equals
     *
     * @param obj
     * @return true if two Transition have same name
     * @return false if two Transition have different name
     */
    @Override
    public boolean equals(Object obj) {
        assert obj instanceof Transition;
        Transition t = (Transition) obj;
        if (name.equals(t.getName())) {
            return true;
        }
        return false;
    }

    public String getIdPreviusPlaceByIndex(int i) {
        return idPre.get(i);
    }

    public String getIdPostPlaceByIndex(int i) {
        return idPost.get(i);
    }

    public boolean isIn(String s) {
        for(String n: idPre){
            if(s.equals(n)){
                return true;
            }

        }
        return  false;
    }
}