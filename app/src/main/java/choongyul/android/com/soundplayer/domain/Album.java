package choongyul.android.com.soundplayer.domain;

import android.net.Uri;

import choongyul.android.com.soundplayer.util.TimeUtil;

/**
 * Created by myPC on 2017-02-28.
 */

public class Album extends Common  {
    private String album;            // 0
    private String album_id;
    private String title;
    private String artist;
    private String artist_id;
    private String artist_key;
    private String duration;
    private String id;
    private Uri album_image_uri;
    Uri music_uri;


    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_key() {
        return artist_key;
    }

    public void setArtist_key(String artist_key) {
        this.artist_key = artist_key;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getAlbum_image_uri() {
        return album_image_uri;
    }

    public void setAlbum_image_uri(Uri album_image_uri) {
        this.album_image_uri = album_image_uri;
    }

    public Uri getMusic_uri() {
        return music_uri;
    }

    public void setMusic_uri(Uri music_uri) {
        this.music_uri = music_uri;
    }

    @Override
    public String getThickTV() {
        return album;
    }

    @Override
    public String getThinTV() {
        return artist;
    }

    @Override
    public Uri getImageUri() {
        return album_image_uri;
    }

    @Override
    public String getDurationCovert() {
        return TimeUtil.covertMiliToTime(Long.parseLong(duration));
    }

}
