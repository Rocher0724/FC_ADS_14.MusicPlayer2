package choongyul.android.com.soundplayer;

import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import choongyul.android.com.soundplayer.util.fragment.PagerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 가져오기
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        // 네비게이션 드로워 설정
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int tabCount = 4;
        // 컨텐트 영역
        // 1. 탭 레이아웃
        //탭 레이아웃 정의
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        // 가로축 스크롤하기
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                                                    // 리소스 가져오는 방법
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_album)));
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_songs)));
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_artists)));
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_genre)));
        tabLayout.addTab( tabLayout.newTab().setText(getResources().getString(R.string.menu_recent)));


        // 2. 뷰페이저
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // 아답터 설정 필요
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_mylist:
                Toast.makeText(this, "mylist ins Selected!!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_search:
                Toast.makeText(this, "search ins Selected!!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings:
                Toast.makeText(this, "Setting ins Selected!!", Toast.LENGTH_SHORT).show();
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

        if (id == R.id.menu_album) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.menu_songs) {
            viewPager.setCurrentItem(1);

        } else if (id == R.id.menu_artists) {
            viewPager.setCurrentItem(2);

        } else if (id == R.id.menu_genre) {
            viewPager.setCurrentItem(3);

        } else if (id == R.id.menu_recent) {
            viewPager.setCurrentItem(4);

        } else if (id == R.id.action_settings) {

        } else if (id == R.id.action_mylist) {

        } else if (id == R.id.action_search) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setShuffle() {
        // 리스트 섞기
    }
}
