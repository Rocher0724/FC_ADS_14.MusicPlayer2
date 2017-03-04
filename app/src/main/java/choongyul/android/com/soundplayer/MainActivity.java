package choongyul.android.com.soundplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import choongyul.android.com.soundplayer.util.TimeUtil;
import choongyul.android.com.soundplayer.util.fragment.PagerAdapter;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static choongyul.android.com.soundplayer.App.ACTION_PAUSE;
import static choongyul.android.com.soundplayer.App.ACTION_PLAY;
import static choongyul.android.com.soundplayer.App.ACTION_STOP;
import static choongyul.android.com.soundplayer.App.APP_RESTART;
import static choongyul.android.com.soundplayer.App.ARG_LIST_TYPE;
import static choongyul.android.com.soundplayer.App.ARG_POSITION;
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
    // 음악데이터
    List<?> datas;
    int position;
    Server server;
    Intent service;
    String list_type = "";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( savedInstanceState != null ) {
            return;
        }

        // 볼륨키로 음악볼륨 바꾸도록 설정
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

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

        //서버 설정
        server = Server.getInstance();
        server.addObserver(this);
        // 핸들러 설정
        handler = new Handler();

        if(playStatus == ACTION_PLAY) {
            initPlayerSetting();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 툴바 우측 옵션 클릭리스너
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        Log.e("MainActivity", "box를 클릭하여 어댑터에서 메인액티비티로 넘어왔다. ");

        datas = server.datas;
        position = server.getPosition();
        list_type = server.getTypeFlag();
        initPlayerSetting();
    }

    private void initPlayerSetting() {

        // 나갔다 들어온경우 (APP_RESTART = true) 일경우 필요한것들만 세팅해준다.
        Log.e("MainActivity", "APP_RESTART =  " + APP_RESTART);

        if( !APP_RESTART ){
            // 음악을 이동할 경우 플레이어에 세팅된 값을 해제한 후 로직을 실행한다.

            // 플레이 상태를 STOP으로 변경
            playStatus = ACTION_STOP;
            if (player != null ) { // player != null 뷰페이져 이동시에  // playStatus != PLAY 나갔다 들어왔을때
                // 아이콘을 플레이 버튼으로 변경
                player.release();
                Log.e("MainActivity", "플레이어 릴리즈! ");

                imgPlay_player.setImageResource(android.R.drawable.ic_media_play);
            }
            Log.e("MainActivity", "플레이어세팅직전! ");

            initPlayer();
            initController();
            playMusic();
            Log.e("Mainactivity", "playStatus = " + playStatus);

        } else {
            APP_RESTART = false;
            initController();
            imgPlay_player.setImageResource(android.R.drawable.ic_media_pause);
        }

    }

    private void initPlayer() {
        // 서비스로 이관


//        Uri musicUri = ((Common) datas.get(position)).getMusic_uri();
//        player = MediaPlayer.create(this, musicUri); // 시스템파일 - context, 음원파일Uri . player를 최초 실행시키는 방법이다.
//        player.setLooping(false);
        // 미디어 플레이어에 완료체크 리스너를 등록한다. Todo 일단 재생만 하려고 뺴놨다
//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                nextMusic();
//            }
//        });
    }

    private void initController() {
        Log.e("MainActivity", "식바와 듀레이션 세팅 ");

        Common common = (Common) datas.get(position);
        // seekbar 최고길이 설정
        seekBar_player.setMax(Integer.parseInt(common.getDuration()));
        // seekbar 현재 값 0으로 설정
        seekBar_player.setProgress(0);
        // 전체 플레이 시간 설정
        tvDurationMax.setText(common.getDurationCovert());
        // 현재 플레이시간 0으로 설정
        tvDurationNow_player.setText("00:00");
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
//        if (position < datas.size()) {
//            position = position+1;
//
//        } else {
//            position = 0;
//        }
//        initPlayerSetting();
    }
    // 음악플레이
    private void playMusic() {
        switch(playStatus) {
            case ACTION_STOP: // 현재상태 스탑
                playStart();
                break;
            case ACTION_PLAY:
                playPause();
                break;
            case ACTION_PAUSE:
                playStart();
                break;
        }
    }
    private void playStart() {
        Intent intent = new Intent(this, SoundService.class);
        intent.setAction(ACTION_PLAY);
        intent.putExtra(ARG_POSITION, position);
        intent.putExtra(ARG_LIST_TYPE,list_type);
        startService(intent);
        Log.e("MainActivity", " 서비스로 출발");


//        playStatus = PLAY;
//        service.setAction(ACTION_PLAY);
//        startService(service);
//        imgPlay_player.setImageResource(android.R.drawable.ic_media_pause);
//        threadStart();
    }

    private void playPause() {
        Intent intent = new Intent(this, SoundService.class);
        intent.setAction(ACTION_PAUSE);
        startService(intent);
        Log.e("MainActivity", " pause를 위해 서비스로 출발");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // seekbar를 이동하면 미디어가 이동하도록 변경
    SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        boolean seekBarByUser;
        int progress;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            seekBarByUser = fromUser;
            this.progress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (player != null && seekBarByUser) {
                player.seekTo(progress);
            }
        }
    };




//////////////////////////////////////////////////////////////////////////////////////////////////
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


    @Override
    public void startPlayer() {
        imgPlay_player.setImageResource(android.R.drawable.ic_media_pause);
    }

    @Override
    public void stopPlayer() {
        imgPlay_player.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void playerSeekBarCounter(Message msg) {
        final Message message = msg;
        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             tvDurationNow_player.setText(TimeUtil.covertMiliToTime(message.arg1));
                             seekBar_player.setProgress(message.arg1);
                         }
                     });

//        tvDurationNow_player.setText(TimeUtil.covertMiliToTime(msg.arg1));
//        seekBar_player.setProgress(msg.arg1);
    }

    @Override
    public void pausePlayer() {
        imgPlay_player.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    protected void onDestroy(){
        server.remove(this);
        super.onDestroy();
    }
}
