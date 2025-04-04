package ru.swat1x.jsonexpector.util;

import java.math.BigDecimal;
import java.util.Comparator;

public class NumberComparatorUtil {

    public static boolean compareNumbers(Number a, Number b) {
        return new NumberComparator().compare(a, b) != 0;
    }

    private static class NumberComparator implements Comparator<Number> {

        @Override
        public int compare(Number n1, Number n2) {
            if (n1 == null && n2 == null) return 0;
            if (n1 == null) return -1;
            if (n2 == null) return 1;

            BigDecimal bd1 = toBigDecimal(n1);
            BigDecimal bd2 = toBigDecimal(n2);

            return bd1.compareTo(bd2);
        }

        private BigDecimal toBigDecimal(Number number) {
            if (number instanceof BigDecimal) {
                return (BigDecimal) number;
            } else if (number instanceof Float || number instanceof Double) {
                return BigDecimal.valueOf(number.doubleValue());
            } else {
                return BigDecimal.valueOf(number.longValue());
            }
        }

    }

}
