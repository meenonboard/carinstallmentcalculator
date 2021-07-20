package com.PonRod;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * Writen by Jiraphat Jokdee 
 * Copyrights 2012 : PonRod Applicaiton 
 * Chiang mai,Thailand 
 * **/
public class PonRodHome extends Activity {
	private ConnectivityManager connectivityManager;
	private ImageServices _imageServiceInstance;
	private XMLServices _xmlservice;
	private String adsImageURI = "";
	private String adsImageLink = "";

	ProgressDialog _dialog;
	AlertDialog alertDialog;
	Bitmap adsImage;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        //Get wifi manager
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        _imageServiceInstance = new ImageServices();
        _xmlservice = new XMLServices();
        
        if(connectivityManager.getActiveNetworkInfo()!=null 
        		&& connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting())
        {
    		try
    		{
            	if(!_imageServiceInstance.isImageConnectionReachAble(getResources().getString(R.string.ads_datasource)))
            	{
            		//TODO : Show something on that error and  return to main page
        			alertDialog = new AlertDialog.Builder(this).create();  
        		    alertDialog.setTitle("Oops!");  
        		    alertDialog.setMessage("Your internet connection has problems, or our server has shut down");  
  
        		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {  
        		      public void onClick(DialogInterface dialog, int which) {
        		    	  finish();
        		        return;  
        		    } });   
        		    
          		    alertDialog.show();
            	}
            	else
            	{
                	//Get main ads link
                	String realAdsXMLURL = "";
          		    String XML_PATH = _xmlservice.getXML(getResources().getString(R.string.ads_datasource));
                    Document doc = _xmlservice.XMLfromString(XML_PATH);

                    //TODO : Protect from no element found in XML
                    //int numResults = XMLfunctions.numResults(doc);
                    NodeList nodes = doc.getElementsByTagName("result");

            		//fill in the list items from the XML document
            		for (int i = 0; i < nodes.getLength(); i++) {;	
            			Element e = (Element)nodes.item(i);
            			adsImageURI = _xmlservice.getValue(e, "Image");
                    	adsImageLink = _xmlservice.getValue(e, "Link");
            		}
            		
            		//URI for check connectivity of Ads
                	if(!_imageServiceInstance.isImageConnectionReachAble(adsImageURI))
                	{
                		//TODO : Show something on that error and  return to main page
                		alertDialog = new AlertDialog.Builder(this).create();  
            		    alertDialog.setTitle("Oops!");  
            		    alertDialog.setMessage("Your internet connection has problems, please contact your operator");  
            		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {  
            		      public void onClick(DialogInterface dialog, int which) {
            		    	  finish();
            		        return;  
            		    } });   
              		    alertDialog.show();
                	}
                	else
                	{
	                	adsImage = _imageServiceInstance.DownloadImageFile(adsImageURI);
	           			//Load and set image + set external link
	           	    	ImageButton adsbutton = (ImageButton) findViewById(R.id.btnAds);
	           			BitmapDrawable bitmapDrawable = new BitmapDrawable(adsImage);
	           			adsbutton.setBackgroundDrawable(bitmapDrawable);
	           			
	           			adsbutton.setOnClickListener(new OnClickListener() {
	           		  			
	           		  	public void onClick(View v) {
	           		  				// TODO Auto-generated method stub
	           		  				Uri uriUrl = Uri.parse(adsImageLink);
	           		  		        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
	           		  		        startActivity(launchBrowser);
	           		  			}
	           		  	});
	           			
	           			Handler handler = new Handler(); 
	           			    handler.postDelayed(new Runnable() { 
	           			         public void run() { 
	           			        	 startActivity(new Intent(PonRodHome.this,MainMenuView.class));
	           			        	 finish();
	           			         } 
	           			    }, 5000);
	                	} 
            	}
    		}
    		catch(Exception e)
    		{		
    			//Download failed, throw the error
    			Log.e("DownloadError", e.getMessage());
    			e.printStackTrace();
    		}
        }
        else
        {
        	//Redirect
        	startActivity(new Intent(PonRodHome.this,MainMenuView.class));
        	finish();
        }
    } 
}