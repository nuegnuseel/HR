package service;

import db.MysqlConfig;
import db.MysqlConnection;
import dto.UserAttend;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.IntStream;

public class AttendanceService {
    static private List menuList;
    static private Scanner scanner;

    static private AttendanceService attendanceService;

    static private MysqlConnection mysqlConnection;
    static private Connection connection;

    static private HashMap<String, Object> map;

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

        if (map == null) {
            map = new HashMap<>();
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
                personalCurrentByMonth(connection);
                break;
            case 5:
                deptCurrentByMonth(connection);
                break;
            default:
                break;
        }
    }

    private void insert() {
        System.out.println("==== 근태 입력 ====\n");
        System.out.print("직원 ID: ");
        String userId = scanner.nextLine().trim();
        System.out.print("삭제할 날짜: ");
        String date = scanner.nextLine().trim();
        String sql = "insert into checkattend values(?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, date);
            int result = preparedStatement.executeUpdate();
            if(result > 0) {
                System.out.println("근태 정보가 삭제되었습니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void update() {
        System.out.println("근태 업데이트 함수");
    }

    private void delete() {
        System.out.println("==== 근태 삭제 ====\n");
        System.out.print("직원 ID: ");
        String userId = scanner.nextLine().trim();
        System.out.print("삭제할 날짜: ");
        String date = scanner.nextLine().trim();
        String sql = "DELETE FROM checkattend WHERE User_ID = ? AND `date` = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, date);
            int result = preparedStatement.executeUpdate();
            if(result > 0) {
                System.out.println("근태 정보가 삭제되었습니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void memberCurrentByMonth(Connection connection) {
        System.out.println("직원별 월별 근태 현황 보기");
        System.out.print("");
    }

    private void personalCurrentByMonth(Connection connection) {
        System.out.println("==== 직원별 월별 근태 현황 ====\n");
        System.out.print("직원 ID: ");
        String userId = scanner.nextLine().trim();
        System.out.print("조회 월(1 ~ 12): ");
        String month = scanner.nextLine().trim();
        month = month.length() < 2 ? "0" + month : month;
        String sql = "select u.User_ID, ca.Attend_No, u.`user`, ca.`date`, d.dept\n" +
                "FROM checkattend ca\n" +
                "INNER JOIN checkdept cd ON ca.User_ID = cd.User_ID2\n" +
                "INNER JOIN dept d ON cd.Dept_ID = d.Dept_ID\n" +
                "INNER JOIN `user` u ON ca.User_ID = u.User_ID and u.User_ID = ?\n" +
                "WHERE SUBSTRING(ca.date, 6, 2) = ?\n" +
                "order by ca.`date` asc;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, month);
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<String, Integer> map = new HashMap<>();
            System.out.println("2024년 " + month + "월 근태 현황:");
            boolean firstFlag = true;
            while (resultSet.next()) {
                int idx = 1;
                String personalId = resultSet.getString(idx++);
                String attend = resultSet.getString(idx++);
                String name = resultSet.getString(idx++);
                String date = resultSet.getString(idx++);
                String dept = resultSet.getString(idx++);

                if (map.containsKey(attend)) {
                    map.put(attend, map.get(attend) + 1);
                } else {
                    map.put(attend, 1);
                }

                if (firstFlag) {
                    System.out.println("직원 ID: " + personalId);
                    System.out.println("이름: " + name);
                    System.out.println("부서: " + dept);
                    firstFlag = false;
                }
                System.out.println(" - " + date + ": " + attendIdToName(attend));
            }
            System.out.println("\n총 츨근 일수: " + Optional.ofNullable(map.get("No_01")).orElse(0) + "일");
            System.out.println("총 결근 일수: " + Optional.ofNullable(map.get("No_02")).orElse(0) + "일");
            System.out.println("총 휴가 일수: " + Optional.ofNullable(map.get("No_03")).orElse(0) + "일");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String attendIdToName(String attendId) {
        switch (attendId) {
            case "No_01":
                return "출근";
            case "No_02":
                return "결근";
            case "No_03":
                return "휴가";
            default:
                return "";
        }
    }

    private void deptCurrentByMonth(Connection connection) {
        System.out.println("==== 부서별 월별 근태 현황 ====\n");
        System.out.print("부서: ");
        String dept = scanner.nextLine().trim();
        System.out.print("조회 월(1 ~ 12): ");
        String month = scanner.nextLine().trim();
        month = month.length() < 2 ? "0" + month : month;
        String sql = "SELECT u.User_ID, ca.Attend_No, u.`user`, count(ca.Attend_No) as count\n" +
                "FROM checkattend ca\n" +
                "INNER JOIN checkdept cd ON ca.User_ID = cd.User_ID2\n" +
                "INNER JOIN dept d ON cd.Dept_ID = d.Dept_ID AND d.dept = ?\n" +
                "INNER JOIN `user` u ON ca.User_ID = u.User_ID\n" +
                "WHERE SUBSTRING(ca.date, 6, 2) = ?\n" +
                "GROUP BY u.User_ID, ca.Attend_No, u.`user`;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dept);
            preparedStatement.setString(2, month);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idx = 1;
                String userId = resultSet.getString(idx++);
                String attend = resultSet.getString(idx++);
                String name = resultSet.getString(idx++);
                int count = resultSet.getInt(idx++);
                UserAttend userAttend;
                if (!map.containsKey(userId)) {
                    userAttend = new UserAttend(userId, name);
                    map.put(userId, userAttend);
                } else {
                    userAttend = (UserAttend) map.get(userId);
                }
                userAttend.getAttendMap().put(attend, count);
            }

            map.forEach((key, value) -> {
                UserAttend ua = (UserAttend) value;
                ua.printAttend();
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            map = new HashMap<>();
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
