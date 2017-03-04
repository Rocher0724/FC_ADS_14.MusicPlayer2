package choongyul.android.com.soundplayer.domain;

import android.net.Uri;

/**
 * 리스트 어댑터에서 가져올 파라미터를 여러곳에서 사용할것이므로
 * Created by myPC on 2017-02-28.
 */

public abstract class Common {
    public abstract String getThickTV();
    public abstract String getThinTV();
    public abstract Uri getImageUri();
    public abstract Uri getMusic_uri();
    public abstract String getTitle();
    public abstract String getArtist();
    public abstract String getDuration();
    public abstract String getDurationCovert();




}
