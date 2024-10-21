package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReusableMethods {
    public static String getCurrentTime()
    {
        String date=new SimpleDateFormat("HH_mm_ss_dd_MM_yyyy").format(new Date());

        return date;
    }
}
