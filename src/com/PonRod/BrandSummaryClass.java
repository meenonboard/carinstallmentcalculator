package com.PonRod;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class BrandSummaryClass extends Activity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.result_list);
	        
	        WebView myWebView = (WebView) findViewById(R.id.webPromo);
	        
	        WebSettings webSettings = myWebView.getSettings();
	        webSettings.setJavaScriptEnabled(true);
	        
	        myWebView.loadUrl("http://getbootstrap.com/examples/signin/");
	 }
}
