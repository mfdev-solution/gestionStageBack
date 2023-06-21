package sn.sonatel.mfdev.service.helps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateFormater {

    public static String[] month = {
        "Janvier",
        "Fevrier",
        "Mars",
        "Avril",
        "Mai",
        "Juin",
        "Juillet",
        "Aout",
        "Septembre",
        "Obtobre",
        "Novembre",
        "Decembre",
    };

    public static String formateDate(Date date) {
        return date.getDate() + " " + month[date.getMonth()] + " " + (date.getYear() - 100 + 2000);
    }

    public static String getMonth(Date date) {
        return month[date.getMonth()];
    }

    public static int calculDuree(Date debut, Date fin) {
        return fin.getMonth() - debut.getMonth();
    }

    public static Date formateExcelDate(Date date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            String formattedDate = inputFormat.format(date);
            return inputFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
