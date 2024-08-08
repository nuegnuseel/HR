package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class AttendanceService {
    static private List menuList;
    static private Scanner scanner;

    static private AttendanceService attendanceService;

    private AttendanceService() {
        if(menuList == null) {
            menuList = new ArrayList<String>();
            initMenuList();
        }

        if(scanner == null) {
            scanner = new Scanner(System.in);
        }
    }

    public static AttendanceService getInstance() {
        if(attendanceService == null) {
            attendanceService = new AttendanceService();
        }
        return attendanceService;
    }


    private void initMenuList () {
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

    private void router(int select) {
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
                memberCurrentByMonth();
                break;
            case 5:
                deptCurrentByMonth();
                break;
            default:
                break;
        }
    }

    private void insert() {
        System.out.println("근태 입력하는 함수");
    }

    private void update() {
        System.out.println("근태 업데이트 함수");
    }

    private void delete() {
        System.out.println("근태 삭제 함수");
    }

    private void memberCurrentByMonth() {
        System.out.println("직원별 월별 근태 현황 보기");
    }

    private void deptCurrentByMonth() {
        System.out.println("부서별 월별 근태 현황 보기");
    }

    public void run() {
        while(true) {
            printMenu();
            int select = getSelect();
            if(select == 0) {
                break;
            }
            router(select);
        }
    }
}
