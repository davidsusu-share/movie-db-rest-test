package hu.lorem.ipsum.mdf.util;

import java.time.Year;

public final class ParseUtil {
    
    // any movie should have year, this is a last resort only
    private static final Year FALLBACK_YEAR = Year.of(1900);
    
    
    private ParseUtil() {
        // utility class
    }
    

    /**
     * Convert the given string to {@link Year} in a simple fault tolerant way
     * 
     * @param yearString
     * @return The resulting {@code Year} object
     */
    public static Year parseYear(String yearString) {
        if (yearString == null || yearString.isEmpty()) {
            return FALLBACK_YEAR;
        }
        
        try {
            return Year.of(Integer.parseInt(
                    yearString.length() >= 4 ? yearString.substring(0, 4) : yearString));
        } catch (NumberFormatException e) {
            return FALLBACK_YEAR;
        }
    }
    
}
