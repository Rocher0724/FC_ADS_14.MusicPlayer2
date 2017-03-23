package choongyul.android.com.soundplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import choongyul.android.com.soundplayer.domain.Album;
import choongyul.android.com.soundplayer.domain.Artist;
import choongyul.android.com.soundplayer.domain.Music;

/**
 * Created by myPC on 2017-02-28.
 */

public class DataLoader {

    private final static Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    // 3. 데이터에서 가져올 데이터 컬럼명을 정의한다.
    // 데이터 컬럼명은 Content URI의 패키지에 들어있다.
    private final static String PROJ[] = {
            MediaStore.Audio.Media._ID             // 0
            ,MediaStore.Audio.Media.ALBUM_ID        // 1
            ,MediaStore.Audio.Media.TITLE           // 2
            ,MediaStore.Audio.Media.ARTIST          // 3
            ,MediaStore.Audio.Media.ARTIST_ID       // 4
            ,MediaStore.Audio.Media.ARTIST_KEY      // 5
            ,MediaStore.Audio.Media.DURATION        // 6
            ,MediaStore.Audio.Media.IS_MUSIC        // 7
            ,MediaStore.Audio.Media.COMPOSER        // 8
            ,MediaStore.Audio.Media.YEAR};          // 9


    //datas는 전역에서 사용되므로 static으로 빼고싶다. 이런경우 public 까지 주지는 않고 get함수를 public static을 선언해준다.
    // datas를 두개의 activity에서 공유하기 위해 static으로 변경
    private static List<Music> musicDatas = new ArrayList<>();
    private static List<Album> albumDatas = new ArrayList<>();
    private static List<Artist> artistDatas = new ArrayList<>();

    // static 변수인 data 를 체크해서 null이면 load를 실행
    public static List<Music> getMusicDatas(Context context) {
        if (musicDatas == null || musicDatas.size() == 0) {
            loadMusic(context);
        }
        return musicDatas;
    }

    public static List<Artist> getArtistDatas(Context context) {
        if (artistDatas == null || artistDatas.size() == 0) {
            loadArtist(context);
        }
        return artistDatas;
    }

    public static List<Album> getAlbumDatas(Context context) {
        if (albumDatas == null || albumDatas.size() == 0) {
            loadAlbum(context);
        }
        return albumDatas;
    }

    // load는 외부에서 호출될일이 없고 get함수를 통해서만 접근된다.
    private static void loadMusic(Context context) {
        // 1. 주소록에 접근하기 위해 ContentResolver를 불러온다.
        ContentResolver resolver = context.getContentResolver();

        // 4. Content Resolver 로 불러온(쿼리한) 데이터를 커서에 담는다.
        // 데이터 URI : MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        Cursor cursor = resolver.query(URI, PROJ, null, null, null);

        if( cursor != null) {
            // 5. 커서에 넘어온 데이터가 있다면 반복문을 돌면서 datas에 담아준다.
            while ( cursor.moveToNext() ) {
                Music music = new Music();

                music.setId         (getString(cursor, PROJ[0]));
                music.setAlbum_id   (getString(cursor, PROJ[1]));
                music.setTitle      (getString(cursor, PROJ[2]));
                music.setArtist     (getString(cursor, PROJ[3]));
                music.setArtist_id  (getString(cursor, PROJ[4]));
                music.setArtist_key (getString(cursor, PROJ[5]));
                music.setDuration   (getString(cursor, PROJ[6]));
                music.setIs_music   (getString(cursor, PROJ[7]));
                music.setComposer   (getString(cursor, PROJ[8]));
                music.setYear       (getString(cursor, PROJ[9]));

                music.setAlbum_image_uri(getAlbumImageUri(music.getAlbum_id()));
                music.setMusic_uri(getMusicUri(music.getId()));

                musicDatas.add(music);
            }
            // * 중요 : 사용 후 close를 호출하지 않으면 메모리 누수가 발생할 수 있다.
            cursor.close();
        }
    }

    private static void loadArtist(Context context) {

        final Uri URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        // 3. 데이터에서 가져올 데이터 컬럼명을 정의한다.
        // 데이터 컬럼명은 Content URI의 패키지에 들어있다.
        String PROJECT[] = {
                MediaStore.Audio.Artists._ID        // 0
                ,MediaStore.Audio.Artists.ARTIST    // 1
                ,MediaStore.Audio.Artists.NUMBER_OF_ALBUMS       // 2
                ,MediaStore.Audio.Artists.NUMBER_OF_TRACKS      // 3
                ,MediaStore.Audio.Artists.ARTIST_KEY    // 4
                };

        ContentResolver resolver = context.getContentResolver();

        // 4. Content Resolver 로 불러온(쿼리한) 데이터를 커서에 담는다.
        // 데이터 URI : MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        Cursor cursor = resolver.query(URI, PROJECT, null, null, null);

        if( cursor != null) {
            // 5. 커서에 넘어온 데이터가 있다면 반복문을 돌면서 datas에 담아준다.
            while ( cursor.moveToNext() ) {
                Artist artist = new Artist();

                artist.setId(getString(cursor, PROJECT[0]));
                artist.setArtist(getString(cursor, PROJECT[1]));
                artist.setNumber_of_albums(getString(cursor, PROJECT[2]));
                artist.setNumber_of_tracks(getString(cursor, PROJECT[3]));
                artist.setArtist_key(getString(cursor, PROJECT[4]));

                artist.setTitle(getTitleByArtistId(artist.getId()));
                artist.setMusic_uri(getMusicUriByArtistId(artist.getId()));
                artist.setDuration(getDurationByArtistId(artist.getId()));
                artist.setAlbum_id(getAlbumIdByArtistId(artist.getId()));
                artist.setAlbum_image_uri(getAlbumUriByArtistId(artist.getId()));

                artistDatas.add(artist);
            }
            // * 중요 : 사용 후 close를 호출하지 않으면 메모리 누수가 발생할 수 있다.
            cursor.close();
        }

    }


    private static void loadAlbum (Context context) {

        final Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // 3. 데이터에서 가져올 데이터 컬럼명을 정의한다.
        // 데이터 컬럼명은 Content URI의 패키지에 들어있다.
        String PROJ[] = {
                MediaStore.Audio.Media.ALBUM            // 0
                ,MediaStore.Audio.Media.ALBUM_ID        // 1
                ,MediaStore.Audio.Media.TITLE           // 2
                ,MediaStore.Audio.Media.ARTIST          // 3
                ,MediaStore.Audio.Media.ARTIST_ID       // 4
                ,MediaStore.Audio.Media.ARTIST_KEY      // 5
                ,MediaStore.Audio.Media.DURATION        // 6
                ,MediaStore.Audio.Media._ID};           // 7

        // 1. 주소록에 접근하기 위해 ContentResolver를 불러온다.
        ContentResolver resolver = context.getContentResolver();

        // 4. Content Resolver 로 불러온(쿼리한) 데이터를 커서에 담는다.
        // 데이터 URI : MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        Cursor cursor = resolver.query(URI, PROJ, null, null, null);

        if( cursor != null) {
            // 5. 커서에 넘어온 데이터가 있다면 반복문을 돌면서 datas에 담아준다.
            while ( cursor.moveToNext() ) {
                Album album = new Album();

                album.setAlbum      (getString(cursor, PROJ[0]));
                album.setAlbum_id   (getString(cursor, PROJ[1]));
                album.setTitle      (getString(cursor, PROJ[2]));
                album.setArtist     (getString(cursor, PROJ[3]));
                album.setArtist_id  (getString(cursor, PROJ[4]));
                album.setArtist_key (getString(cursor, PROJ[5]));
                album.setDuration   (getString(cursor, PROJ[6]));
                album.setId         (getString(cursor, PROJ[7]));

                album.setAlbum_image_uri(getAlbumImageUri(album.getAlbum_id()));
                album.setMusic_uri(getMusicUri(album.getId()));

                albumDatas.add(album);
            }
            // * 중요 : 사용 후 close를 호출하지 않으면 메모리 누수가 발생할 수 있다.
            cursor.close();
        }
    }






    private static String getGenre() {
//        MediaStore.Audio.Genres.getContentUriForAudioId();
        return null;
    }

    private static Uri getMusicUriByArtistId(String artist_id) {
        for( Music music : musicDatas) {
            if( music.getId() == artist_id) {
                return music.getMusic_uri();
            }
        }
        return null;
    }

    private static String getDurationByArtistId(String artist_id) {
        for( Music music : musicDatas) {
            if( music.getId() == artist_id) {
                return music.getDuration();
            }
        }
        return null;
    }

    private static String getTitleByArtistId(String artist_id) {
        for( Music music : musicDatas) {
            if( music.getId() == artist_id) {
                return music.getTitle();
            }
        }
        return null;
    }

    private static String getAlbumIdByArtistId(String artist_id) {
        for( Music music : musicDatas) {
            if( music.getId() == artist_id) {
                return music.getAlbum_id();
            }
        }
        return null;
    }

    private static Uri getAlbumUriByArtistId(String artist_id) {
        for( Music music : musicDatas) {
            if( music.getId() == artist_id) {
                return music.getAlbum_image_uri();
            }
        }
        return null;
    }

    private static String getString(Cursor cursor, String columnName){
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getString(idx);
    }

    private static Uri getMusicUri(String music_id) {
        Uri content_uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(content_uri, music_id);
    }

    // 가장 간단하게 앨범 이미지를 가져오는 방법.
    // 문제점 : 실제 앨범 데이터만 있어서 이미지를 불러오지 못하는 경우가 있다.
    private static Uri getAlbumImageUri(String album_id) {
        Log.e("DataLoader", "getAlbumImageUri ----------------------" + album_id);
        return Uri.parse("content://media/external/audio/albumart/" + album_id);
    }

}