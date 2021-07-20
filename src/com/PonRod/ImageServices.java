package com.PonRod;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageServices {
	public Bitmap  DownloadImageFile(String fileUrl){
        URL myFileUrl =null;          
        try {
             myFileUrl= new URL(fileUrl);
        } catch (MalformedURLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
        }
        try {
             HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
             conn.setDoInput(true);
             conn.connect();
             InputStream is = conn.getInputStream();
             
             return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             return null;
        }
   }
	
	public boolean isImageConnectionReachAble(String urlPath){
		try {

            URL url = new URL(urlPath);

            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		    urlc.setRequestProperty("User-Agent", "Android Application: 1.1");
		    urlc.setRequestProperty("Connection", "close");
		    urlc.setConnectTimeout(1000 * 15); // mTimeout is in seconds
		            urlc.connect();
		    if (urlc.getResponseCode() == 200) {
		            return new Boolean(true);
		    }
		} 
		catch (MalformedURLException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} 
		catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}
		return new Boolean(false);
	}
}
