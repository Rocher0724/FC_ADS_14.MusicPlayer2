package choongyul.android.com.soundplayer.domain;

import android.net.Uri;

import choongyul.android.com.soundplayer.util.TimeUtil;

/**
 * Created by myPC on 2017-02-01.
 */

public class Music extends Common{

    // music info
    private String id;
    private String title;
    private String artist;
    private String artist_key;
    private String album_id;
    private String genre_id;
    private Uri album_image_uri;
    private String duration; // 길이
    Uri music_uri;
    // add info
    private int order; // 순서
    private boolean favor; // 선택된 음악만 넣을 때?


    public void setIs_music(String is_music) {
        this.is_music = is_music;
    }

    private String is_music; // 음악만 가져옴
    private String content_type; //
    private String year; //



    public String getComposer() {
        return composer;
    }
    private String composer; // 작곡가

    @Override
    public String getDurationCovert() {
        return TimeUtil.covertMiliToTime(Long.parseLong(duration));
    }
    @Override
    public String getDuration() {
        return duration;
    }

    public String getIs_music() {
        return is_music;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public Uri getAlbum_image_uri() {
        return album_image_uri;
    }

    public void setAlbum_image_uri(Uri album_image_uri) {
        this.album_image_uri = album_image_uri;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public boolean isFavor() {
        return favor;
    }

    public void setFavor(boolean favor) {
        this.favor = favor;
    }

    public String getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(String genre_id) {
        this.genre_id = genre_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Uri getMusic_uri() {
        return music_uri;
    }

    public void setMusic_uri(Uri uri) {
        this.music_uri = uri;
    }

    private String artist_id;
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

    @Override
    public String getThickTV() {
        return title;
    }

    @Override
    public String getThinTV() {
        return artist;
    }

    @Override
    public Uri getImageUri() {
        return album_image_uri;
    }
}
