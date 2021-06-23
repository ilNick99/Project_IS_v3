package main.java.Project_v3;

import main.java.Utility.IO;

import java.io.IOException;

public class Manager {

    NetManager manager = new NetManager();
    User user = new User();
    Configurator config = new Configurator();

    public void start() throws IOException {
        boolean check=false;
        do {
            int choice = IO.readInteger(IO.CHOOSE_THE_ELEMENT, 0, 2);
            switch (choice) {
                case 0:
                    check=false;
                    break;
                case 1:
                    user.operation(manager);
                    check = IO.yesOrNo("Do you want change the mode?\n");
                    break;

                case 2:
                    config.operation(manager);
                    check = IO.yesOrNo("Do you want change the mode?\n");
                    break;
            }
        } while (check);
    }

}
