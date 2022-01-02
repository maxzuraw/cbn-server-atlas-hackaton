package pl.krejnstudio.smarttools.coldbedroomnotifier.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    private static final String MSG_START = "Expected not null value (";
    private static final String MSG_END = " )!";

    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    public int compare(BigDecimal first, BigDecimal second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException(MSG_START + " left=" + first + ", right=" + second + MSG_END);
        }

        return setScale(first).compareTo(setScale(second));
    }

    private BigDecimal setScale(BigDecimal value) {
        BigDecimal scaled;
        int scale = 20;
        if (value.scale() > scale) {
            scaled = value.setScale(scale, roundingMode);
        } else if (value.scale() < 0) {
            scaled = value.setScale(0, roundingMode);
        } else {
            scaled = value;
        }
        return scaled;
    }
}
