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
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import choongyul.android.com.soundplayer.domain.Common;
import choongyul.android.com.soundplayer.domain.Music;
import choongyul.android.com.soundplayer.util.TimeUtil;
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

public class SoundService extends Service implements Observer {
    String TAG = "SoundService";
    public static String listType = "";
    public static int position = -1;
    List<?> datas = new ArrayList<>();
    Thread thread;
    Server server;
    public static final int WHAT = 100; // 핸들러 사용을 위한 리턴변수 선언

    public SoundService() {
        server = Server.getInstance();
        server.addObserver(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service", "서비스도착 ");

        if( intent != null && intent.getExtras() != null ) {
            listType = intent.getExtras().getString(ARG_LIST_TYPE);
            position = intent.getExtras().getInt(ARG_POSITION);

//            if(player == null) { // 강사님은 이게 있음. 문제가 생길수 있다.......
                Log.e("Service", "미디어를 세팅해보자");

                initMedia();
//            }
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
        Log.e("Service", "initMedia. 플레이어.create");

        datas = server.datas;

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
        Log.e("Service", "핸들액션 ");

        if( intent == null || intent.getAction() == null )
            return;
        String action = intent.getAction();
        Log.e("Service", "가지고온 action = " + action);

        if( action.equalsIgnoreCase( ACTION_PLAY ) ) {
            // 음원 실행처리
            server.play(); // 서버에 있는 플레이를 실행시켜서 서버에 등록된 모든 플레이를 실행한다.
        } else if ( action.equalsIgnoreCase(ACTION_PAUSE) ) {
            server.pause();
        } else if ( action.equalsIgnoreCase(ACTION_PREVIOUS) ) {

        } else if ( action.equalsIgnoreCase(ACTION_NEXT) ) {

        } else if ( action.equalsIgnoreCase(ACTION_STOP) ) {
            server.stop();
        }
    }

    // Noti.Action -> API Level 19
    // Activity에서 클릭 버튼 생성
    // NotificationCompat - compat 계열은 버전처리를 내부적으로 해주는 문법이다.
    private NotificationCompat.Action generateAction(int icon, String title, String intentAction ) {
        Intent intent = new Intent( getApplicationContext(), SoundService.class );
        intent.setAction( intentAction );
        //Pending Intent : 실행 대상이 되는 인텐트를 지연 시키는 역할 .여기 1은 구분자이다. 사용할때 리턴받아서 구분한다.
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

        builder.setSmallIcon(R.mipmap.icon_play96)
                .setContentTitle( common.getTitle() )
                .setContentText( common.getArtist() );

        // 현 상태가 퍼즈일 경우만 노티 삭제 가능하려면 setOngoing을 true로 놓으면된다.
        if( action_flag == ACTION_PAUSE ) {
            builder.setDeleteIntent(stopIntent)
                    .setOngoing(false); // true로 해야 하지만 앱이 자꾸 죽어서 일단은 false로 했다. 서비스를 죽이기 위해서
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
        if(player == null) {
            Log.e("Service - playerSart", "player는 널이다.");

        }
        player.start();
        buildNotification( generateAction( android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE ) , ACTION_PAUSE );
        thread = new timerThread();
        thread.start();
        Log.e("Service", "playerStart 메소드 - playStatus = " + playStatus);
        playStatus = ACTION_PLAY;

    }

    public void playerPause() {
        Log.e("Service", "playerPause메소드로 도착 ");

        player.pause();
        buildNotification( generateAction( android.R.drawable.ic_media_play,  // 노티에 표시할 아이콘
                                                                     "Play",    // 노티에 표시할 텍스트
                                                            ACTION_PLAY ) ,     // 다음번 클릭에 들어갈 행동동
                                                               ACTION_PLAY );   // buildNotification 에서 특정 액션을 실행시키거나 실행못하게 하고싶을때.

        playStatus = ACTION_PAUSE;
    }

    public void playerStop() {
        Log.e("Service", "playerStop ");
        onDestroy();
//        player.stop();
//        buildNotification( generateAction( android.R.drawable.ic_media_play, "Play", ACTION_PLAY ) , ACTION_PLAY );
    }


    @Override
    public void update() {

    }

    @Override
    public void startPlayer() {
        playerStart();
    }

    @Override
    public void pausePlayer() {
        playerPause();
    }

    @Override
    public void stopPlayer() {
        playerStop();
    }

    @Override
    public void playerSeekBarCounter(Message msg) {
        // 서비스나 노티바에는 seekber가 없어서 아무것도 안써도 된다.
    }

    class timerThread extends Thread {
        @Override
        public void run() {
            while (playStatus == ACTION_PLAY) {
                if (player != null) {
                    Log.e("SeekbarCheck Thread","쓰레드가 돌고있다!!!!");

                    Message msg = new Message();
                    msg.what = WHAT;            //msg의 멤버인 what과 arg1, arg2는 int형 파라미터이다.
                    msg.arg1 = player.getCurrentPosition();
                    server.playerSeekBarCounter(msg);


//                    // 하단의 부분이 메인스레드에서 동작하도록 Runnable 객체를 메인스레드에 던져준다
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // 플레이어가 도중에 종료되면 예외가 발생하기 때문에 예외처리를 해준다.
//                            // try로 해주면 퍼포먼스가 안좋아지기 때문에 if로 처리
//                            try {
//                                seekBar_player.setProgress(player.getCurrentPosition());
//                                tvDurationNow_player.setText(TimeUtil.covertMiliToTime(player.getCurrentPosition()));
//                            } catch (Exception e) { e.printStackTrace(); }
//                        }
//                    });
                }
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            Log.e("SeekbarCheck Thread","쓰레드가 끝났다.");
        }

    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
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


    @Override
    public void onDestroy() {
        server.remove(this);
        playStatus = ACTION_STOP;
        Log.e("Service onDestroy","서비스가 죽었다.");

        super.onDestroy();

    }
}