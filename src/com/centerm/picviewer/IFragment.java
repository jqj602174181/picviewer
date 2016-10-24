package com.centerm.picviewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class IFragment extends Fragment {
	public ImageViewTouch img;
    private String path;
    private int pos;
    private Timer clickTimer;
    private Context context = null;
    private static String ACTION = "com.centerm.picviewer.fragment.stop";
    private MessageReceiver receiver = new MessageReceiver();
    private boolean workStatus = false;

    LruCache<String, Bitmap> cache;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public IFragment()
    {
        super();
    }
    
    public void setArguments(String path, int pos, LruCache<String, Bitmap> cache) {
    	this.path = path;
        this.pos = pos;
        this.cache = cache;
        Log.i("IFragment",path+","+pos);
    }
    
    static public IFragment newIFragment(String path, int pos, LruCache<String, Bitmap> cache) {
    	IFragment fr = new IFragment();
    	fr.setArguments(path, pos, cache);
    	return fr;
    }
    
    @Override
    public void onAttach(Activity activity){
    	this.context = activity;
    	super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.v("browser", ""+pos+" onCreateview");
        RelativeLayout relativeLayout = new RelativeLayout(this.getActivity());
        relativeLayout.setBackgroundColor(Color.BLACK);
        //BrowserImageView bimg = new BrowserImageView(this.getActivity());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ImageViewTouch bimg = new ImageViewTouch(this.getActivity());
        bimg.setScaleType(ScaleType.MATRIX);
        bimg.setClickable(true);
        relativeLayout.addView(bimg, lp);
        img = bimg;
        return relativeLayout;
    }
    
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Log.v("browser", ""+pos+" onStart");
        registerMessageReceiver();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.v("browser", ""+pos+" onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub  
    	Log.v("browser", ""+pos+" onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        Log.v("browser", ""+pos+" onResume");
        workStatus = true;
        super.onResume();
        setBitmap();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        Log.v("browser", ""+pos+" onStop");
        workStatus = false;
        final String action = ACTION;
		Intent intent = new Intent();
		intent.setAction(action);
		context.sendBroadcast(intent);
        super.onStop();

    }
    
    public void registerMessageReceiver() {
		receiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION);
		context.registerReceiver(receiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String wsAction = intent.getAction();
			if(wsAction.equals(ACTION))
			{
				if(workStatus){
					setBitmap();
				}else{
					img = null;
				}
			}
		}
	}

    public void setBitmap() {
        Bitmap bitmap = IFragment.this.getBitmapFromMemCache(path);
        if(null == bitmap)
        {
            LoadBitmapTask task = new LoadBitmapTask();
            task.execute();
        } else {
            //BitmapSize bs = img.new BitmapSize(bitmap.getWidth(), bitmap.getHeight());
            //img.setBitmapSize(bs);
            img.setImageBitmap(bitmap);
        }

    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            cache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return cache.get(key);
    }

    class LoadBitmapTask extends AsyncTask<Void, Void, Bitmap>
    {
    	private Bitmap getImageFromAssetsFile(String fileName)
    	{
    		Bitmap image = null;
    		AssetManager am = getResources().getAssets();
    		try
    		{
    			Log.i("IFragment", fileName);
    			InputStream is = am.open(fileName);
    			image = BitmapFactory.decodeStream(is);
    			is.close();
    		}
    		catch (IOException e)
    		{
    			e.printStackTrace();
    		}
    		return image;
    	}
        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Bitmap bitmap = null;
            /*try {
                FileInputStream fin = new FileInputStream(path);
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(fin, null, options);
                IFragment.this.addBitmapToMemoryCache(path, bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } */
            bitmap = getImageFromAssetsFile(path);
            IFragment.this.addBitmapToMemoryCache(path, bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //BitmapSize bs = img.new BitmapSize(result.getWidth(), result.getHeight());
            //img.setBitmapSize(bs);
            img.setImageBitmap(result);
        }

    }

}
