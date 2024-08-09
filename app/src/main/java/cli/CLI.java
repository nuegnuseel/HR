package cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class CLI {

    private static Scanner scan;
    private static CLI cli = null;

    private static List menuList;

    private CLI() {
        scan = new Scanner(System.in);
        menuList = new ArrayList<String>();
        initMenuList();
    }

    private void initMenuList() {
        menuList.add("조직/직무 관리");
        menuList.add("인사행정");
        menuList.add("근태 관리");
        menuList.add("급여/정산");
        menuList.add("사회보험");
        menuList.add("평가 관리");
        menuList.add("연말정산");
        menuList.add("승진 관리");
        menuList.add("핵심 인재 관리");
        menuList.add("월별 종합 현황 보기");
    }

    public static CLI getInstance() {
        if (cli == null) {
            cli = new CLI();
        }
        return cli;
    }

    public int getSelect() {
        while (true) {
            System.out.print("입력하세요: ");
            try {
                return Integer.parseInt(scan.nextLine().trim());
            } catch (NumberFormatException numberFormatException) {
                System.out.println("잘못된 입력입니다");
            }
        }
    }

    public void printSystemMenu() {
        System.out.println("====인적 자원 관리 시스템====");
        IntStream.rangeClosed(1, menuList.size()).forEach(index -> {
            System.out.println(index + ". " + menuList.get(index - 1));
        });
        System.out.println("0. 종료\n");
    }

}
