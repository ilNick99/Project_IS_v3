package Project_v3;

import Utility.Reader;
import java.io.IOException;
public class Manager {

    NetManager manager = new NetManager();
    User user = new User();
    Configurator config = new Configurator();

    public void start() throws IOException {
        int choise = Reader.leggiIntero("choose the mode:\n0) Exit \n2) User mode \n3) Configurator mode", 0, 3);
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
