package Project_v3;

import java.util.ArrayList;

public class Transition {
    private String name;
    private int weight = 1;
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

    public void setWeight(int weight) {
        this.weight = weight;
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
}