package service;

import db.MysqlConfig;
import db.MysqlConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class AttendanceService {
    static private List menuList;
    static private Scanner scanner;

    static private AttendanceService attendanceService;

    static private MysqlConnection mysqlConnection;
    static private Connection connection;

    private AttendanceService() throws IOException {
        if (menuList == null) {
            menuList = new ArrayList<String>();
            initMenuList();
        }

        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        if (mysqlConnection == null) {
            mysqlConnection = MysqlConnection.from(MysqlConfig.getInstance());
        }
    }

    public static AttendanceService getInstance() throws IOException {
        if (attendanceService == null) {
            attendanceService = new AttendanceService();
        }
        return attendanceService;
    }


    private void initMenuList() {
        menuList.add("근태 입력");
        menuList.add("근태 수정");
        menuList.add("근태 삭제");
        menuList.add("직원별 월별 근태 현황 보기");
        menuList.add("부서별 월별 근태 현황 보기");
    }

    public int getSelect() {
        while (true) {
            System.out.print("입력하세요: ");
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException numberFormatException) {
                System.out.println("잘못된 입력입니다");
            }
        }
    }

    public void printMenu() {
        System.out.println("\n====근태관리====");
        IntStream.rangeClosed(1, menuList.size()).forEach(index -> {
            System.out.println(index + ". " + menuList.get(index - 1));
        });
        System.out.println("0. 메인 메뉴로 돌아가기\n");
    }

    private void router(int select, Connection connection) {
        switch (select) {
            case 1:
                insert();
                break;
            case 2:
                update();
                break;
            case 3:
                delete();
                break;
            case 4:
                memberCurrentByMonth(connection);
                break;
            case 5:
                deptCurrentByMonth(connection);
                break;
            default:
                break;
        }
    }

    private void insert() {
        System.out.print("직원 ID 입력: ");

    }

    private void update() {
        System.out.println("근태 업데이트 함수");
    }

    private void delete() {
        System.out.println("근태 삭제 함수");
    }

    private void memberCurrentByMonth(Connection connection) {
        System.out.println("직원별 월별 근태 현황 보기");
    }

    private void deptCurrentByMonth(Connection connection) {
        System.out.println("부서별 월별 근태 현황 보기");
        String sql = "SELECT u.User_ID, ca.Attend_No, u.`user`, ca.date \n" +
                "FROM checkattend ca \n" +
                "INNER JOIN checkdept cd ON ca.User_ID = cd.User_ID2\n" +
                "INNER JOIN dept d ON cd.Dept_ID = d.Dept_ID AND d.dept = ?\n" +
                "INNER JOIN `user` u ON ca.User_ID = u.User_ID\n" +
                "WHERE SUBSTRING(ca.date, 6, 2) = '08'\n" +
                "GROUP BY u.User_ID, ca.Attend_No, u.`user`, ca.date;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "품질관리팀");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idx = 1;
                String value = resultSet.getString(idx);
                System.out.println(value);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() throws SQLException {
        connection = mysqlConnection.getConnection();
        while (true) {
            printMenu();
            int select = getSelect();
            if (select == 0) {
                break;
            }
            router(select, connection);
        }
        mysqlConnection.retrieveConnection(connection);
    }
}
