package forif.univ_hanyang.util;

import java.time.LocalDateTime;

public class DateUtils {
    public static int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }

    public static int getCurrentSemester() {
        // 현재 날짜를 기준으로 학기를 결정하는 로직 예시
        int month = LocalDateTime.now().getMonthValue();
        // 2~7월은 1학기, 8~1월은 2학기로 간주
        return (month > 1 && month <= 7) ? 1 : 2;
    }
}
