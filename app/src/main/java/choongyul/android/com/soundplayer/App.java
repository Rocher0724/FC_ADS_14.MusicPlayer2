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
//    public static final String ACTION_REWIND = "choongyul.android.com.soundplayer.action.rewind";
//    public static final String ACTION_FAST_FORWARD = "choongyul.android.com.soundplayer.action.fast.foward";
    public static final String ACTION_NEXT = "choongyul.android.com.soundplayer.action.next";
    public static final String ACTION_PREVIOUS = "choongyul.android.com.soundplayer.action.previous";

    public static final String ARG_POSITION = "position";
    public static final String ARG_LIST_TYPE = "list-type";


    public static boolean APP_RESTART = false;
    // 플레이어 상태 플래그
    public static String playStatus = "";
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

    //서버등록 플래그
//    public static boolean mainObserverFlag = true;


//
//    // 서비스플레이 액션 정의
//    public static final String ACTION_PLAY = "choongyul.android.com.mediaplayerservice.play";
//    public static final String ACTION_PAUSE = "choongyul.android.com.mediaplayerservice.pause";
//    public static final String ACTION_REWIND = "choongyul.android.com.mediaplayerservice.rewind";
//    public static final String ACTION_FAST_FORWARD = "choongyul.android.com.mediaplayerservice.fast.foward";
//    public static final String ACTION_NEXT = "choongyul.android.com.mediaplayerservice.next";
//    public static final String ACTION_PREVIOUS = "choongyul.android.com.mediaplayerservice.previous";
//    public static final String ACTION_STOP = "choongyul.android.com.mediaplayerservice.stop";
}
