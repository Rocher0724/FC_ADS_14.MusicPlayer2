package choongyul.android.com.soundplayer.util.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myPC on 2017-02-27.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    public void add(Fragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("fiveFragment","==========111===========================");
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
