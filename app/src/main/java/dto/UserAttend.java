package dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserAttend {
    private String userId;
    private Map<String, Integer> attendMap;
    private String name;

    public UserAttend(String userId, String name) {
        this.userId = userId;
        this.name = name;
        attendMap = new HashMap();
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getAttendMap() {
        return attendMap;
    }

    public String getUserId() {
        return userId;
    }

    public void printAttend() {
        System.out.println("- 직원 ID: " + this.getUserId() + ", 이름: " + this.getName());
        int attend = Optional.ofNullable(this.getAttendMap().get("No_01")).orElse(0);
        int miss = Optional.ofNullable(this.getAttendMap().get("No_02")).orElse(0);
        int vacation = Optional.ofNullable(this.getAttendMap().get("No_03")).orElse(0);
        System.out.println(" - 출근율: " + String.format("%.2f", (attend / 24.0) * 100.0) + "%");
        System.out.println(" - 출근: " + attend + "일, " + "결근: " + miss + "일, " + "휴가: " + vacation + "일\n");
    }
}
