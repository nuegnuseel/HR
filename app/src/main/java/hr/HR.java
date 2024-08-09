package hr;

import cli.CLI;
import controller.Controller;

import java.io.IOException;
import java.sql.SQLException;

public class HR {
    private static CLI cli;
    private static HR hr = null;

    private HR(CLI commandLineInterface) {
        cli = commandLineInterface;
    }

    public static HR getInstance() {
        if(hr == null) {
            hr = new HR(CLI.getInstance());
        }
        return hr;
    }

    public void run() throws IOException, SQLException {
        while(true) {
            cli.printSystemMenu();
            int select = cli.getSelect();
            if(select == 0) {
                break;
            }
            Controller.router(select);
        }
    }
}
