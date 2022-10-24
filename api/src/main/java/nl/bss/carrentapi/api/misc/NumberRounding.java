package nl.bss.carrentapi.api.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberRounding {
    /**
     * Rounds a given Double to a given amount of decimal places.
     *
     * @param value  to round
     * @param places to round
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
