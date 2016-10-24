package com.centerm.picviewer;

import java.util.List;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.util.Log;

public class IPagerAdapter extends FragmentPagerAdapter{
    List<String> pathList;
    LruCache<String, Bitmap> cache;

    public IPagerAdapter(FragmentManager fm, List<String> pathList) {
        super(fm);
        this.pathList = pathList;

        /*init LruCache*/
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);Log.v("diskcache","mem : " + maxMemory);
        final int cacheSize = 1 * 1024 * 1024;//maxMemory / 8;
        cache = new LruCache<String, Bitmap>(cacheSize);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        IFragment f = IFragment.newIFragment(pathList.get(position),position, cache);
        return f;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pathList.size();
    }

}
