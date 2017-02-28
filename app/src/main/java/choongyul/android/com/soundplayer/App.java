package choongyul.android.com.soundplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by myPC on 2017-02-28.
 */

public class App {

    public static MediaPlayer player = null;

    // 서비스플레이 액션 정의
    public static final String ACTION_PLAY = "choongyul.android.com.soundplayer.action.play";
    public static final String ACTION_PAUSE = "choongyul.android.com.soundplayer.action.pause";
    public static final String ACTION_RESTART = "choongyul.android.com.soundplayer.action.restart";
    public static final String ACTION_STOP = "choongyul.android.com.soundplayer.action.stop";
    public static boolean APP_RESTART = false;
    // 플레이어 상태 플래그
    public static final int PLAY = 0;
    public static final int PAUSE = 1;
    public static final int STOP = 2;
    public static int playStatus = STOP;
    public static int position = 0; // 현재 음악 위치


    // 플레이어
    public static void initSound(Context context) {
        Uri soundUri = null;
        player = MediaPlayer.create(context, soundUri);
    }


    public static void playSound() {
        player.start();
    }

    public static void pauseSound() {
        player.pause();
    }

    public static void restartSound() {
        player.start();
    }

    public static void stop() {
        if( player != null ) {
            player.release();
            player = null;
        }
    }
}
