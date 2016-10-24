package com.example.picviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.centerm.picviewer.IPagerAdapter;
import com.centerm.picviewer.VerticalViewPager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		VerticalViewPager viewPager = (VerticalViewPager) this.findViewById(R.id.vp);
		String[] photos;
		ArrayList<String> lphotos = new ArrayList<String>();

		AssetManager assetManager = getAssets();
		try {
			photos = assetManager.list("pic");
			for (int i=0; i<photos.length; i++) {
				lphotos.add("pic/"+photos[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//photos = this.getIntent().getStringArrayListExtra("photoList");
		int index = this.getIntent().getIntExtra("index", 0);

		IPagerAdapter adapter = new IPagerAdapter(this.getSupportFragmentManager(), lphotos);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(index);
		viewPager.setOnPageChangeListener(null);
	}

	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {

		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
