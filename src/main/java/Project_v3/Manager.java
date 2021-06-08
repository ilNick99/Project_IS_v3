package main.java.Project_v3;

import main.java.Utility.IO;

import java.io.IOException;
public class Manager {

    NetManager manager = new NetManager();
    User user = new User();
    Configurator config = new Configurator();

    public void start() throws IOException {
        int choise = IO.readInteger(IO.CHOOSE_THE_ELEMENT, 0, 2);
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
