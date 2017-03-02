package choongyul.android.com.soundplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import static choongyul.android.com.soundplayer.App.ACTION_PAUSE;
import static choongyul.android.com.soundplayer.App.ACTION_PLAY;
import static choongyul.android.com.soundplayer.App.ACTION_RESTART;
import static choongyul.android.com.soundplayer.App.PAUSE;
import static choongyul.android.com.soundplayer.App.PLAY;
import static choongyul.android.com.soundplayer.App.playStatus;
import static choongyul.android.com.soundplayer.App.player;

public class SoundService extends Service {
    public SoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( intent != null) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_PLAY:
                    playMusic();
                    break;
                case ACTION_RESTART:
                    playRestart();
                    break;
                case ACTION_PAUSE:
                    playPause();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playMusic() {
        player.start();
        playStatus = PLAY;
    }
    private void playPause() {
        player.pause();
        playStatus = PAUSE;
    }
    private void playRestart() {
        player.seekTo(player.getCurrentPosition());
        player.start();
        playStatus = PLAY;
    }

}