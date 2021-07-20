package com.PonRod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuView  extends Activity  {
	private static int EXIT_MENU =0;
	private static int CONTACT_MENU = 1;
	private ConnectivityManager connectivityManager;
	private Activity _activity;
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.new_main_menu);
	        _activity = this;
	        //Get wifi manager
	          connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

	          //Bind event to button
	          Button btnStart = (Button) findViewById(R.id.btnBrand);
	          Button btnCalculator = (Button) findViewById(R.id.btnPrice);
	          Button btnNewList =(Button) findViewById(R.id.btnFixedMode);
	          Button btnSum =(Button) findViewById(R.id.btnSummary);
	          
	          btnSum.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(MainMenuView.this,BrandSummaryClass.class));
				}
			});
	          	
	          btnNewList.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(MainMenuView.this,BrandListClass.class));
				}
			});
	          
	          btnStart.setOnClickListener(new OnClickListener() {
	  			
	  			public void onClick(View v) {
	  				// TODO Auto-generated method stub
	  		        //Check internet connectivity 
	  		        if(connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnected())
	  		        {
	  		        	startActivity(new Intent(MainMenuView.this,CarModelList.class));
	  		        }
	  		        else
	  		        {
	  		        	Toast.makeText(_activity, "Internet connection required", 500).show();
	  		        }
	  			}
	  		});
	          
	          btnCalculator.setOnClickListener(new OnClickListener() {
	  			
	  			public void onClick(View v) {
	  				// TODO Auto-generated method stub
	  		        	startActivity(new Intent(MainMenuView.this,CustomCalculateModelView.class));
	  			}
	  		});
	   }
	   
	   @Override
		public boolean onCreateOptionsMenu(Menu menu){
			menu.add(0,EXIT_MENU,0,"Exit").setIcon(R.drawable.ic_menu_exit);
			menu.add(0,CONTACT_MENU,0,"About").setIcon(R.drawable.ic_menu_tag);
			return true;
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem menuitem){
			switch (menuitem.getItemId()) {
			case 0:
				this.finish();
				break;
			case 1:
				startActivity(new Intent(MainMenuView.this,About.class));
				break;
			default:
				break;
			}
			return true;
		}  
}
