package Project_v3;

public class Place {
    private String name;
    private int numberOfToken = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Place(String _name){
        this.name=_name;
    }

    public void setToken(int n){
        this.numberOfToken=n;
    }
}
