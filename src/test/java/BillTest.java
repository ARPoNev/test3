import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BillTest {
    @Test
    @Order(1)
    @DisplayName("开始通话和结束通话时间均在标准时间内,且通话为整数分钟")
    void testBetweenStandardTime() {

        LocalDateTime startingTime =
                LocalDateTime.of(2021,2,3,12,30,0);
        ZonedDateTime zonedStartTime =
                startingTime.atZone(ZoneId.of("America/New_York"));

        LocalDateTime endingTime =
                LocalDateTime.of(2021, 2,3,12,50,0);
        ZonedDateTime zonedEndTime =
                endingTime.atZone(ZoneId.of("America/New_York"));

        Bill holdingTime = new Bill(zonedStartTime, zonedEndTime);

        assertThat(holdingTime.getHoldingTime()).isEqualTo(20);
    }

    @Test
    @Order(2)
    @DisplayName("开始通话和结束通话时间均在标准时间内,且通话不为整数分钟")
    void testBetweenStandardTime_with_seconds() {

        LocalDateTime startingTime =
                LocalDateTime.of(2021,2,3,12,30,0);
        ZonedDateTime zonedStartTime =
                startingTime.atZone(ZoneId.of("America/New_York"));

        LocalDateTime endingTime =
                LocalDateTime.of(2021, 2,3,12,50,25);
        ZonedDateTime zonedEndTime =
                endingTime.atZone(ZoneId.of("America/New_York"));

        Bill holdingTime = new Bill(zonedStartTime, zonedEndTime);

        assertThat(holdingTime.getHoldingTime()).isEqualTo(21);
    }

    @Test
    @Order(3)
    @DisplayName("开始通话在标准时间，结束通话时间在夏令时内,且通话为整数分钟")
    void test_standard_to_DaylightTime() {

        LocalDateTime startingTime = LocalDateTime.of(2021,3,14,0,40,0);
        ZonedDateTime zonedStartTime = startingTime.atZone(ZoneId.of("America/New_York"));

        LocalDateTime endingTime = LocalDateTime.of(2021, 3,14,3,40,0);
        ZonedDateTime zonedEndTime = endingTime.atZone(ZoneId.of("America/New_York"));

        Bill holdingTime = new Bill(zonedStartTime, zonedEndTime);

        assertThat(holdingTime.getHoldingTime()).isEqualTo(120);
    }
    @Test
    @Order(4)
    @DisplayName("开始通话在夏令时内，结束通话时间在标准时间内,且通话为整数分钟")
    void test_DaylightTime_to_standard() {

        LocalDateTime startingTime = LocalDateTime.of(2021,11,7,2,55,0);
        ZonedDateTime zonedStartTime = startingTime.atZone(ZoneId.of("America/New_York"));

        LocalDateTime endingTime = LocalDateTime.of(2021, 11,7,2,5,0);
        ZonedDateTime zonedEndTime = endingTime.atZone(ZoneId.of("America/New_York"));

        Bill holdingTime = new Bill(zonedStartTime, zonedEndTime);

        assertThat(holdingTime.getHoldingTime()).isEqualTo(10);
    }

    @ParameterizedTest
    @CsvSource({
            "2021, 2, 14, 2, 00, 0, 2021, 2, 14, 2, 10, 0, 0.5",
            "2021, 2, 14, 2, 00, 0, 2021, 2, 14, 2, 20, 0, 1",
            "2021, 2, 14, 2, 00, 0, 2021, 2, 14, 2, 30, 0, 2",
            "2021, 2, 14, 2, 00, 0, 2021, 2, 14, 2, 19, 59, 1",
            "2021, 3, 30, 2, 00, 0, 2021, 3, 30, 2, 10, 0, 0.5",
            "2021, 3, 30, 2, 00, 0, 2021, 3, 30, 2, 20, 0, 1",
            "2021, 3, 30, 2, 00, 0, 2021, 3, 30, 2, 30, 0, 2",
            "2021, 3, 30, 2, 00, 0, 2021, 3, 30, 2, 19, 59, 1",
            "2021, 3, 14, 1, 55, 0, 2021, 3, 14, 3, 5, 0, 0.5",
            "2021, 3, 14, 1, 50, 0, 2021, 3, 14, 3, 10, 0, 1",
            "2021, 3, 14, 1, 50, 0, 2021, 3, 14, 3, 20, 0, 2",
            "2021, 3, 14, 1, 50, 0, 2021, 3, 14, 3, 9, 59, 1",
            "2021, 11, 7, 2, 55, 0, 2021, 11, 7, 2, 5, 0, 0.5",
            "2021, 11, 7, 2, 50, 0, 2021, 11, 7, 2, 10, 0, 1",
            "2021, 11, 7, 2, 50, 0, 2021, 11, 7, 2, 20, 0, 2",
            "2021, 11, 7, 2, 50, 0, 2021, 11, 7, 2, 9, 59, 1"

    })
    @Order(5)
    @DisplayName("决策表")
    void testDecisionTable(int year1, int month1, int dayOfMonth1, int hour1,int minute1,int second1,int year2, int month2, int dayOfMonth2, int hour2,int minute2,int second2,double excepted) {

        LocalDateTime startingTime =
                LocalDateTime.of(year1, month1, dayOfMonth1, hour1, minute1, second1);
        ZonedDateTime zonedStartTime =
                startingTime.atZone(ZoneId.of("America/New_York"));

        LocalDateTime endingTime =
                LocalDateTime.of(year2, month2, dayOfMonth2, hour2, minute2, second2);
        ZonedDateTime zonedEndTime = endingTime.atZone(ZoneId.of("America/New_York"));

        Bill holdingTime = new Bill(zonedStartTime, zonedEndTime);
        //System.out.println(holdingTime.getHoldingTime());
        assertThat(holdingTime.PhoneBillCaculator(holdingTime.getHoldingTime())).isEqualTo(excepted);
    }
}
