import hr.HR;

import java.io.IOException;
import java.sql.SQLException;

public class  Main{
    public static void main(String[] args) throws IOException, SQLException {
        HR hr = HR.getInstance();
        hr.run();
    }
}