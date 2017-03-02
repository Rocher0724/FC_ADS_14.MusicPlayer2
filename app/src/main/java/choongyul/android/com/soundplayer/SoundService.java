package choongyul.android.com.soundplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import choongyul.android.com.soundplayer.domain.Common;
import choongyul.android.com.soundplayer.domain.Music;
import choongyul.android.com.soundplayer.util.fragment.PagerAdapter;

import static android.app.PendingIntent.getService;
import static choongyul.android.com.soundplayer.App.ACTION_NEXT;
import static choongyul.android.com.soundplayer.App.ACTION_PAUSE;
import static choongyul.android.com.soundplayer.App.ACTION_PLAY;
import static choongyul.android.com.soundplayer.App.ACTION_PREVIOUS;
import static choongyul.android.com.soundplayer.App.ACTION_RESTART;
import static choongyul.android.com.soundplayer.App.ACTION_STOP;
import static choongyul.android.com.soundplayer.App.ARG_LIST_TYPE;
import static choongyul.android.com.soundplayer.App.ARG_POSITION;
import static choongyul.android.com.soundplayer.App.playStatus;
import static choongyul.android.com.soundplayer.App.player;

public class SoundService extends Service {
    public static String listType = "";
    public static int position = -1;
    List<?> datas = new ArrayList<>();

    public SoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( intent != null && intent.getExtras() != null ) {
            listType = intent.getExtras().getString(ARG_LIST_TYPE);
            position = intent.getExtras().getInt(ARG_POSITION);

            if(player == null) {
                initMedia();
            }
        }

        handleAction( intent );
        return super.onStartCommand(intent, flags, startId);


//            String action = intent.getAction();
//            switch (action) {
//                case ACTION_PLAY:
//                    playMusic();
//                    break;
//                case ACTION_RESTART:
//                    playRestart();
//                    break;
//                case ACTION_PAUSE:
//                    playPause();
//                    break;
//            }
        }

        //서비스는 액티비티가 들고있고 서비슨느 핸들러를 참조해서 메시지를 보내야한다.
        /// 강사님 소스
//        if( intent != null && intent.getExtras() != null ) {
//            listType = intent.getExtras().getString(ListFragment.ARG_LIST_TYPE);
//            position = intent.getExtras().getInt(ListFragment.ARG_POSITION);
//
//            if(player == null) {
//                initMedia();
//            }
//        }
//
//        handleAction( intent );
//        return super.onStartCommand(intent, flags, startId);
//
//        ///
//        return super.onStartCommand(intent, flags, startId);
//    }

    // 1. 미디어 플레이어 기본값 설정
    public void initMedia() {
        if(datas.size() < 1) {
            switch (listType) {
                case ListFragment.TYPE_ALBUM:
                    datas = DataLoader.getArtistDatas(getBaseContext());
                    break;
                case ListFragment.TYPE_SONG:
                    datas = DataLoader.getMusicDatas(getBaseContext());
                    break;
            }
        }
        Uri musicUri = ((Common) datas.get(position)).getMusic_uri();
        player = MediaPlayer.create(this, musicUri); // 시스템파일 - context, 음원파일Uri . player를 최초 실행시키는 방법이다.
        player.setLooping(false);

        // 다음곡 자동재생
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //TODO next();
            }
        });
    }


    // 2. 명령어 실행
    // 인텐트 액션에 넘어온 명령어를 분기시키는 함수
    public void handleAction( Intent intent ) {
        if( intent == null || intent.getAction() == null )
            return;
        String action = intent.getAction();
        if( action.equalsIgnoreCase( ACTION_PLAY ) ) {
            // 음원 실행처리
            playerStart();
//            mController.getTransportControls().play();
        } else if ( action.equalsIgnoreCase(ACTION_PAUSE) ) {
//            mController.getTransportControls().pause();
        } else if ( action.equalsIgnoreCase(ACTION_PREVIOUS) ) {
//            mController.getTransportControls().skipToPrevious();
        } else if ( action.equalsIgnoreCase(ACTION_NEXT) ) {
//            mController.getTransportControls().skipToNext();
        } else if ( action.equalsIgnoreCase(ACTION_STOP) ) {
//            mController.getTransportControls().stop();
        }
    }

    // Noti.Action -> API Level 19
    // Activity에서 클릭 버튼 생성
    // NotificationCompat - compat 계열은 버전처리를 내부적으로 해주는 문법이다.
    private NotificationCompat.Action generateAction(int icon, String title, String intentAction ) {
        Intent intent = new Intent( getApplicationContext(), SoundService.class );
        intent.setAction( intentAction );
        //Pending Intent : 실행 대상이 되는 인텐트를 지연 시키는 역할                 여기 1은 구분자이다. 사용할때 리턴받아서 구분한다.
        // 인텐트를 서비스 밖에서 실행 시킬 수 있도록 담아두는 주머니니
       PendingIntent pendingIntent = getService(getApplicationContext(), 1, intent, 0);

        return new NotificationCompat.Action.Builder(icon, title, pendingIntent).build();
    }

    // 노티 바를 생성하는 함수
    private void buildNotification( NotificationCompat.Action action, String action_flag ) {
        Common common = (Common) datas.get(position);

        // Stop intent
        // 노티 바를 삭제 했을 때 실행되는 메인 인텐트
        Intent intentStop = new Intent( getApplicationContext(), SoundService.class );
        intentStop.setAction( ACTION_STOP );
        PendingIntent stopIntent = getService(getApplicationContext(), 1, intentStop, 0);


        // 노티 바 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this );

        builder.setSmallIcon(R.mipmap.ic_play20)
                .setContentTitle( common.getTitle() )
                .setContentText( common.getArtist() );

        // 현 상태가 퍼즈일 경우만 노티 삭제 가능
        if( action_flag == ACTION_PAUSE ) {
            builder.setDeleteIntent(stopIntent)
                    .setOngoing(true);
        }

        builder.addAction( generateAction( android.R.drawable.ic_media_previous, "Prev", ACTION_PREVIOUS ) );
        builder.addAction( action );
        builder.addAction( generateAction( android.R.drawable.ic_media_next, "Next", ACTION_NEXT ) );


        // 노티바를 화면에 보여준다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            notificationManager.notify( 1, builder.build() );
        }
    }

    public void playerStart() {
        player.start();
        buildNotification( generateAction( android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE ) , ACTION_PAUSE );

    }

    class timerThread extends Thread {
        @Override
        public void run() {
            while (playStatus < STOP) {
                if (player != null) {
                    Log.i("timerThread","============== 들어왔나안왔나");
                    // 하단의 부분이 메인스레드에서 동작하도록 Runnable 객체를 메인스레드에 던져준다
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 플레이어가 도중에 종료되면 예외가 발생하기 때문에 예외처리를 해준다.
                            // try로 해주면 퍼포먼스가 안좋아지기 때문에 if로 처리
                            try {
                                seekBar_player.setProgress(player.getCurrentPosition());
                                tvDurationNow_player.setText(TimeUtil.covertMiliToTime(player.getCurrentPosition()));
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                    });
                }
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

//    private void playMusic() {
//        player.start();
//        playStatus = PLAY;
//    }
//    private void playPause() {
//        player.pause();
//        playStatus = PAUSE;
//    }
//    private void playRestart() {
//        player.seekTo(player.getCurrentPosition());
//        player.start();
//        playStatus = PLAY;
//    }

}