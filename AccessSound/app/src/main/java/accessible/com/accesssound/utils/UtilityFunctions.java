package accessible.com.accesssound.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UtilityFunctions {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}
