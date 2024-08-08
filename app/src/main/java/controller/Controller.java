package controller;

import service.AttendanceService;

public class Controller {
    static private Controller controller;

    public static Controller getInstance() {
        if(controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public static void router(int select) {
        switch (select) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                AttendanceService.getInstance().run();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            default:
                break;
        }
    }
}
