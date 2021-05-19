package main.java.Project_v3;

import Utility.Reader;
import java.io.IOException;
public class Manager {

    public static final String CHOOSE_THE_ELEMENT = "choose the mode:\n0) Exit \n1) User mode \n2) Configurator mode";
    NetManager manager = new NetManager();
    User user = new User();
    Configurator config = new Configurator();

    public void start() throws IOException {
        int choise = Reader.leggiIntero(CHOOSE_THE_ELEMENT, 0, 2);
        switch (choise){
            case 0:
                break;

            case 1:
                user.operation(manager);
                break;

            case 2:
                config.operation(manager);
                break;
        }
    }


}
