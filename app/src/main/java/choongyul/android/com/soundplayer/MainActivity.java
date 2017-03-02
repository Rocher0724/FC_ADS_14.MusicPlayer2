package choongyul.android.com.soundplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import choongyul.android.com.soundplayer.domain.Common;
import choongyul.android.com.soundplayer.util.fragment.PagerAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static choongyul.android.com.soundplayer.App.ACTION_PAUSE;
import static choongyul.android.com.soundplayer.App.ACTION_PLAY;
import static choongyul.android.com.soundplayer.App.ACTION_RESTART;
import static choongyul.android.com.soundplayer.App.APP_RESTART;
import static choongyul.android.com.soundplayer.App.PAUSE;
import static choongyul.android.com.soundplayer.App.PLAY;
import static choongyul.android.com.soundplayer.App.STOP;
import static choongyul.android.com.soundplayer.App.playStatus;
import static choongyul.android.com.soundplayer.App.player;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Observer {

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter adapter;
    LinearLayout libraryLO;
    LinearLayout detailLO;
    ImageView imgAlbum_player, imgPlay_player, imgff_player, imgVol_player;
    TextView tvThick_Player,tvThin_Player, tvDurationNow_player, tvDurationMax;
    SeekBar seekBar_player;
    List<?> datas;
    int position;
    Server server;
    Thread thread;
    Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVersion(REQ_PERMISSION);
        // 레이아웃 가져오기
        libraryLO = (LinearLayout) findViewById(R.id.libraryLO);
        detailLO = (LinearLayout) findViewById(R.id.detailLO);

        //위젯 가져오기
        imgAlbum_player = (ImageView) findViewById(R.id.imgAlbum_player);
        imgPlay_player = (ImageView) findViewById(R.id.imgPlay_player);
        imgff_player = (ImageView) findViewById(R.id.imgff_player);
        imgVol_player = (ImageView) findViewById(R.id.imgVol_player);
        tvThick_Player = (TextView) findViewById(R.id.tvThick_player);
        tvThin_Player = (TextView) findViewById(R.id.tvThin_player);
        tvDurationNow_player = (TextView) findViewById(R.id.tvDurationNow_player);
        tvDurationMax = (TextView) findViewById(R.id.tvDurationMax_player);
        seekBar_player = (SeekBar) findViewById(R.id.seekBar_player);

        // 리스너 세팅
        imgPlay_player.setOnClickListener(clickListener);
        imgff_player.setOnClickListener(clickListener);
        imgVol_player.setOnClickListener(clickListener);
        seekBar_player.setOnSeekBarChangeListener(seekBarListener);


        // 서비스 설정
        service = new Intent(this, SoundService.class);

        // 옵저버 패턴을 위한 서버설정
//        this.server = server;
//        this.name = name;
//        server.addObserver(this);

        // 툴바 가져오기
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        // 네비게이션 드로워 설정
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 컨텐트 영역
        // 1. 탭 레이아웃
        //탭 레이아웃 정의
        tabLayout = (TabLayout) findViewById(R.id.tab);
        // 가로축 스크롤하기
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                                                    // 리소스 가져오는 방법
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_albums)));
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_songs)));
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_artist)));
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_genre)));
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_recent)));


        // 2. 뷰페이저
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // 아답터 설정 필요
        adapter = new PagerAdapter(getSupportFragmentManager());

        // 프래그먼트 초기화
        adapter.add(ListFragment.newInstance(1,ListFragment.TYPE_SONG));
        adapter.add(ListFragment.newInstance(2,ListFragment.TYPE_ALBUM));
//        adapter.add(new OneFragment());
//        adapter.add(new TwoFragment());
        adapter.add(new ThreeFragment());
        adapter.add(new FourFragment());
        adapter.add(new FiveFragment());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 툴바 우측 옵션 클릭리스너
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_sort:
                libraryLO.setVisibility(VISIBLE);
                detailLO.setVisibility(GONE);
                Toast.makeText(this, "search ins Selected!!", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                Toast.makeText(this, "search ins Selected!!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Navi Drawer 메뉴가 클릭되면 호출
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_library) {
            detailLO.setVisibility(GONE);
            libraryLO.setVisibility(VISIBLE);
        } else if (id == R.id.menu_sleep) {
        } else if (id == R.id.menu_nowplaying) {
        } else if (id == R.id.menu_playlist) {

        } else if (id == R.id.action_settings) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setShuffle() {
        // 리스트 섞기
    }

    @Override
    public void update() {
        datas = server.datas;
        position = server.getPosition();
        initPlayerSetting();
    }

    //student의 생성자와 같은 역할을 하는 것. 한번만 실행되면 된다.
    public void observer(Server server) {
        this.server = server;
        server.addObserver(this);

    }

    private void initPlayerSetting() {

        // 나갔다 들어온경우 (APP_RESTART = true) 일경우 필요한것들만 세팅해준다.
        if( !APP_RESTART ){
            // 음악을 이동할 경우 플레이어에 세팅된 값을 해제한 후 로직을 실행한다.
            if (player != null ) { // player != null 뷰페이져 이동시에  // playStatus != PLAY 나갔다 들어왔을때
                // 플레이 상태를 STOP으로 변경
                playStatus = STOP;
                // 아이콘을 플레이 버튼으로 변경
                player.release();

                imgPlay_player.setImageResource(android.R.drawable.ic_media_play);
            }

            initPlayer();
            initController();
            playMusic();

        } else {


            APP_RESTART = false;
            initController();
            imgPlay_player.setImageResource(android.R.drawable.ic_media_pause);
            threadStart();
        }


//            if (App.playStatus != PLAY) {
//                playMusic();
//            }

    }

    private void initPlayer() {
        Uri musicUri = ((Common) datas.get(position)).getMusic_uri();
        player = MediaPlayer.create(this, musicUri); // 시스템파일 - context, 음원파일Uri . player를 최초 실행시키는 방법이다.
        player.setLooping(false);
        // 미디어 플레이어에 완료체크 리스너를 등록한다. Todo 일단 재생만 하려고 뺴놨다
//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                nextMusic();
//            }
//        });
    }

    private void initController() {
        // seekbar 최고길이 설정
        seekBar_player.setMax(player.getDuration());
        // seekbar 현재 값 0으로 설정
        seekBar_player.setProgress(0);
        // 전체 플레이 시간 설정
        tvDurationMax.setText(covertMiliToTime(player.getDuration()));
        // 현재 플레이시간 0으로 설정
        tvDurationNow_player.setText("0");
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgPlay_player:
                    playMusic();
                    break;
                case R.id.imgff_player:
                    nextMusic();
                    break;
                case R.id.imgVol_player:
                    musicVolChange();
                    break;
            }
        }
    };

    // 볼륨체인지
    private void musicVolChange() {
//        if (position > 0) {
//            mViewPager.setCurrentItem(position-1);
//        } else {
//            mViewPager.setCurrentItem(datas.size());
//        }
    }
//    다음음악
    private void nextMusic() {
        if (position < datas.size()) {
            position = position+1;

        } else {
            position = 0;
        }
        initPlayerSetting();
    }
    // 음악플레이
    private void playMusic() {
        switch(playStatus) {
            case STOP: // 현재상태 스탑
                playStart();
                break;
            case PLAY:
                playPause();
                break;
            case PAUSE:
                playRestart();
                break;
        }
    }

    private void playStart() {
        playStatus = PLAY;
        service.setAction(ACTION_PLAY);
        startService(service);
        imgPlay_player.setImageResource(android.R.drawable.ic_media_pause);
        threadStart();
    }
    private void playPause() {
        service.setAction(ACTION_PAUSE);
        startService(service);
        imgPlay_player.setImageResource(android.R.drawable.ic_media_play);
    }
    private void playRestart() {
        service.setAction(ACTION_RESTART);
        startService(service);
        player.seekTo(player.getCurrentPosition());
        imgPlay_player.setImageResource(android.R.drawable.ic_media_pause);
    }
    private void threadStart() {
        // sub thread를 생성해서 mediaplayer의 현재 포지션 값으로 seekbar를 변경해준다
        // 매1초마다 sub thread 에서 동작할 로직 정의
        thread = new timerThread();
        // 새로운 스레드 시작
        thread.start();
    }

    @Override
    protected void onDestroy() {
        thread.interrupted();
        super.onDestroy();
    }

//    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            App.position = position;
//            initPlayerSetting();
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };

    // seekbar를 이동하면 미디어가 이동하도록 변경
    SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        boolean k;
        int progress;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            k = fromUser;
            this.progress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (player != null && k) {
                player.seekTo(progress);
            }
        }
    };

    private String covertMiliToTime(long mili){
        long min = mili / 1000 / 60;
        long sec = mili / 1000 % 60;


        return String.format("%02d", min) + ":" + String.format("%02d", sec);
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
                                tvDurationNow_player.setText(covertMiliToTime(player.getCurrentPosition()));
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                    });
                }
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }




/////////////////////////////////////////////////////
    // mainActivity에서 권한체크 메소드로 활용을 하자!
    // === MainActivity에서 선언해야 할 것 ===
    public final int REQ_PERMISSION = 100;
    // === onCreate 에서 불러와야 할것 ===
    // checkVersion();
    public final String PERMISSION_ARRAY[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE
            // TODO 원하는 permission 추가 또는 수정하기
    };

    public void checkVersion(int REQ_PERMISSION) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if( checkPermission(REQ_PERMISSION) ) {
                return;
            }
        } else {
            return;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkPermission(int REQ_PERMISSION) {
        // 1.1 런타임 권한체크 (권한을 추가할때 1.2 목록작성과 2.1 권한체크에도 추가해야한다.)
        boolean permCheck = true;
        for(String perm : PERMISSION_ARRAY) {
            if ( this.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED ) {
                permCheck = false;
                break;
            }
        }

        // 1.2 퍼미션이 모두 true 이면 프로그램 실행
        if(permCheck) {
            // TODO 퍼미션이 승인 되었을때 해야하는 작업이 있다면 여기에서 실행하자.

            return true;
        } else {
            // 1.3 퍼미션중에 false가 있으면 시스템에 권한요청
            this.requestPermissions(PERMISSION_ARRAY, REQ_PERMISSION);
            return false;
        }
    }


    //2. 권한체크 후 콜백 - 사용자가 확인 후 시스템이 호출하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode == REQ_PERMISSION) {

            if( onCheckResult(grantResults)) {
                // TODO 퍼미션이 승인 되었을때 해야하는 작업이 있다면 여기에서 실행하자.

                return;
            } else {
                Toast.makeText(this, "권한을 활성화 해야 모든 기능을 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                // 선택 : 1 종료, 2 권한체크 다시물어보기, 3 권한 획득하지 못한 기능만 정지시키기
                // finish();
            }
        }
    }
    public static boolean onCheckResult(int[] grantResults) {

        boolean checkResult = true;
        // 권한 처리 결과 값을 반복문을 돌면서 확인한 후 하나라도 승인되지 않았다면 false를 리턴해준다.
        for(int result : grantResults) {
            if( result != PackageManager.PERMISSION_GRANTED) {
                checkResult = false;
                break;
            }
        }
        return checkResult;
    }
}
