package com.example.vidit.locateme;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by vidit on 07-05-2016.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mrequestQueue;
    private ImageLoader imageLoader;
    private VolleySingleton(){
        mrequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        imageLoader = new ImageLoader(mrequestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap> cache = new LruCache<>((int)(Runtime.getRuntime().maxMemory()/1024)/8);
            @Override
            public Bitmap getBitmap(String s) {
                return cache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                cache.put(s,bitmap);
            }
        });
    }
    public static VolleySingleton getmInstance(){
        if(sInstance == null){
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }
    public RequestQueue getRequestQueue(){
        return mrequestQueue;
    }
    public ImageLoader getImageLoader(){
        return  imageLoader;
    }

}
