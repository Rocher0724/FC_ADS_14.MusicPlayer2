package choongyul.android.com.soundplayer.util;

/**
 * Created by myPC on 2017-03-02.
 */

public class TimeUtil {

    public static String covertMiliToTime(long mili){
        long min = mili / 1000 / 60;
        long sec = mili / 1000 % 60;


        return String.format("%02d", min) + ":" + String.format("%02d", sec);
    }
}
