package com.PonRod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.PonRod.Controllers.BrandListController;
import com.PonRod.Models.*;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class BrandListClass extends ListActivity implements Runnable,BrandListController  {
	 /** Called when the activity is first created. */
	private Context warpper= null;
	private String XML_PATH = "";
	private String BRAND = "";
	private String LOGO = "";
	private ListActivity _activity;
	private ArrayList<HashMap<String, String>> _mylist;
	private SimpleAdapter _adapter;
	ProgressDialog _dialog;	
	BrandListModel _targetModel;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.brand_list);
	        
	        warpper = this.getApplicationContext();
	        _activity = this;

	        SetLoadingThread();
	 }
	 
	 //Controller Methods
	public void SetLoadingThread() {
			_dialog = ProgressDialog.show(BrandListClass.this, "", 
			        "Loading. Please wait...", true);
			
			 Thread thread = new Thread(this);
			 thread.start();
	}
	 
	 //Controller Methods
	public void CreateBrandlist(String url) {
			_targetModel = new BrandListModel(url);
	}

	 //Controller Methods
	public ArrayList<BrandModel> GetBrandList() {
			// TODO Auto-generated method stub
			return _targetModel.brands;
	}

	public void run() {
				//Download brand
		        this.CreateBrandlist(getResources().getString(R.string.brand_datasource));
		        
		        ArrayList<BrandModel> nodes = this.GetBrandList();
		        
		        //Needed for the listItems
		        _mylist = new ArrayList<HashMap<String, String>>(); 
		        
		    	for (int i = 0; i < nodes.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();	
					BrandModel e = (BrandModel)nodes.get(i);
					map.put("id", e.getID());
		        	map.put("Brand", "" + e.getBrandName());
		        	map.put("Path", "" + e.getPath());
		        	map.put("Logo", "" + getResources().getIdentifier(e.getLogo(), "drawable", "com.PonRod"));
		        	map.put("PriceRange", "" + e.getPrice());
		        	_mylist.add(map);		
				}	   
    	
				 //Make a new listadapter
		   		_adapter = new SimpleAdapter(this, _mylist , R.layout.brand_item, 
		                    new String[] { "Brand","PriceRange" }, 
		                    new int[] { R.id.item_title,R.id.item_subtitle});
				
		   	  
		   	   
		   	   handler.sendEmptyMessage(0);
	}
	
	
	 private Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	               _dialog.dismiss();
	            
	               _activity.setListAdapter(_adapter);
	               
	               final ListView lv = getListView();
	               lv.setTextFilterEnabled(true);	
	               lv.setOnItemClickListener(new OnItemClickListener() {
	               	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
	               		@SuppressWarnings("unchecked")
	       				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);
	               		BRAND = o.get("Brand");
	               		XML_PATH = o.get("Path");
	               		LOGO  = o.get("Logo");
	               		Intent newIntent = new Intent(warpper, BrandModelListClass.class);
	       				Bundle bundle = new Bundle();
	       				bundle.putString("path",XML_PATH);
	       				bundle.putString("brand",BRAND);
	       				bundle.putString("logo",LOGO);
	       				newIntent.putExtras(bundle);
	       				startActivity(newIntent);
	       				finish();
	       			}
	       		});
	               
	        }
	};
}
 

