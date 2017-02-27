package choongyul.android.com.soundplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import choongyul.android.com.soundplayer.util.fragment.PagerAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter adapter;
    LinearLayout libraryLO;
    LinearLayout detailLO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 레이아웃 가져오기
        libraryLO = (LinearLayout) findViewById(R.id.libraryLO);
        detailLO = (LinearLayout) findViewById(R.id.detailLO);

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

        adapter.add(new OneFragment());
        adapter.add(new TwoFragment());
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
}
