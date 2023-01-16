package com.schedule.app.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author : Thành Lộc
 * @since : 11/23/2022, Wed
 **/
public class NumberConverter {

    public static String convertToMoneyFormat(BigDecimal amount) {
        Locale lc = new Locale("vi","VN"); //Định dạng locale việt nam

        NumberFormat nf = NumberFormat.getInstance(lc);
        return  nf.format(amount);
    }

}
