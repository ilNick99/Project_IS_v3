package Project_v3;

import java.util.ArrayList;
import java.util.Objects;

public class Transition {
    private String name;
    private ArrayList<String> idPre= new ArrayList<>();
    private ArrayList<String> idPost= new ArrayList<>();

    Transition(String name){
        this.name=name;
    }

    Transition(String name, int inOut) {
        this.name=name;
        if (inOut == 1)
            idPre.add(name);
        else
            idPost.add(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIdPre() {
        return idPre;
    }

    public ArrayList<String> getIdPost() {
        return idPost;
    }


    public void addPre(String a){
        idPre.add(a);
    }

    public void addPost(String a){
        idPost.add(a);
    }

    public int sizePre(){
        return idPre.size();
    }

    public int sizePost(){
        return idPost.size();
    }

    public int getInputOutput(String name) {
        int ret = -1;
        for (String item: idPre)
            if (item.equals(name))
                ret = 1;
        for (String item: idPost)
            if (item.equals(name))
                ret = 0;
        return ret;
    }
    public boolean isIn(String s) {
        for(String n: idPre){
            if(s.equals(n)){
                return true;
            }

        }
        return  false;
        }

    @Override
    /**
     * this method return the hashcode of the transition
     */
    public int hashCode() {
        return Objects.hash(name);
    }
    }