package hu.lorem.ipsum.mdf.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Year;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParseUtilTests {

    @Test
    void emptyYearStringIsParsedToZero() {
        assertStringIsParsedToYear("", 0);
    }

    @Test
    void invalidYearStringIsParsedToZero() {
        assertStringIsParsedToYear("apple", 0);
    }

    @Test
    void oldYearStringIsParsedToYear() {
        assertStringIsParsedToYear("995", 995);
    }

    @Test
    void validYearStringIsParsedToYear() {
        assertStringIsParsedToYear("1964", 1964);
    }

    @Test
    void dateStringIsParsedToYearPart() {
        assertStringIsParsedToYear("1955-12-11", 1955);
    }
    
    private void assertStringIsParsedToYear(String stringToParse, int expectedValue) {
        Year year = ParseUtil.parseYear(stringToParse);
        assertThat(year.getValue())
                .describedAs("Year value for %s", stringToParse)
                .isEqualTo(expectedValue);
    }
    
}
