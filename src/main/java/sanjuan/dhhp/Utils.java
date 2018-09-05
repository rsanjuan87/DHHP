package sanjuan.dhhp;

import java.text.DecimalFormat;

/**
 * Created by San Juan on 02/05/2018 to DHHP.
 */

class Utils {
    static String format = "0.000";
    public static String format(double num) {
        return new DecimalFormat(format).format(num);
    }
}
