package com.mezi.flickrapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Created by umangjeet on 21/10/16.
 */

public class ImageWorker implements Callable {

    ImageView iv;
    String url;

    public ImageWorker(String urlString, ImageView imgView)
    {
        this.url = urlString;
        this.iv = imgView;
    }

    @Override
    public Bitmap call() throws Exception {
        return exec();
    }

    private Bitmap exec() throws IOException {

        if(url == null || TextUtils.isEmpty(url))
            return null;
        URL imageURL = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) imageURL.openConnection();
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setInstanceFollowRedirects(true);
        Log.d("mezi", "http " + url);
        InputStream is = conn.getInputStream();
        final Bitmap bmp = BitmapFactory.decodeStream(is);
        conn.disconnect();
        if(iv != null)
        {
            iv.post(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(bmp);
                }
            });


        }
        return bmp;
    }
}
