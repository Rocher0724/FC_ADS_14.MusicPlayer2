package choongyul.android.com.soundplayer.domain;

import android.net.Uri;

import java.util.List;

import choongyul.android.com.soundplayer.util.TimeUtil;

/**
 * Created by myPC on 2017-02-28.
 */
public class Artist extends Common {
    private String id;
    private String title;

    private String artist;
    private String duration;

    private String artist_key;

    private String number_of_tracks;

    private String number_of_albums;
    public List<Music> musics;
    private String album_id;
    private Uri album_image_uri;
    private Uri music_uri;

    @Override
    public Uri getMusic_uri() {
        return music_uri;
    }


    public void setMusic_uri(Uri music_uri) {
        this.music_uri = music_uri;
    }

    @Override
    public String getDurationCovert() {
        return TimeUtil.covertMiliToTime(Long.parseLong(duration));
    }
    @Override
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getAlbum_image_uri() {
        return album_image_uri;
    }

    public void setAlbum_image_uri(Uri album_image_uri) {
        this.album_image_uri = album_image_uri;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    @Override
    public String getThickTV() {
        return album_id;
    }

    @Override
    public String getThinTV() {
        return number_of_tracks + " songs";
    }

    @Override
    public Uri getImageUri() {
        return album_image_uri;
    }

    public String getId() {
        return id;
    }

    public String getNumber_of_albums() {
        return number_of_albums;
    }

    public String getNumber_of_tracks() {
        return number_of_tracks;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist_key() {
        return artist_key;
    }

    public void setArtist_key(String artist_key) {
        this.artist_key = artist_key;
    }

    public void setNumber_of_tracks(String number_of_tracks) {
        this.number_of_tracks = number_of_tracks;
    }

    public void setNumber_of_albums(String number_of_albums) {
        this.number_of_albums = number_of_albums;
    }

    public void setId(String id) {
        this.id = id;
    }


}
