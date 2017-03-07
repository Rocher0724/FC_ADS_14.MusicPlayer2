package choongyul.android.com.soundplayer;

import android.os.Message;

/**
 * Created by myPC on 2017-02-28.
 */

public interface Observer {
    //옵저버를 제공하는 서버측에서
    public void update();

    public void startPlayer();
    public void pausePlayer();
    public void stopPlayer();
    public void playerSeekBarCounter(Message msg);
    public void nextSongPlay();
    public void restartPlayer();
}
