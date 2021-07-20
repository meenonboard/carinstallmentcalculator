package com.PonRod;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.PonRod.Controllers.ModelListController;
import com.PonRod.Models.BrandListModel;
import com.PonRod.Models.CarModelListModel;
import com.PonRod.Models.CarModelModel;

public class BrandModelListClass extends ListActivity implements Runnable, ModelListController {

	private Context warpper= null;
	private String BRAND = "";
	private String LOGO = "";
	private String MODEL = "";
	private String IMAGE = "";
	
	private String ModelPathParam ="";
	private String LastUpdate ="";
	private String SERIESPATHPARAM ="";

	private ListActivity _activity;
	private ArrayList<HashMap<String, String>> _mylist;
	private ListAdapter _adapter;
	CarModelListModel _targetModel;
	
	ProgressDialog _dialog;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.model_list);
	        
	        //Get param "path"
	        Bundle bundle = this.getIntent().getExtras();
	        ModelPathParam = bundle.getString("path");
	        BRAND = bundle.getString("brand");
	        LOGO = bundle.getString("logo");
	        
	        warpper = this.getApplicationContext();
	        _activity = this;
	        
	        //Needed for the listItems
	        _mylist = new ArrayList<HashMap<String, String>>(); 
	        
	        //Set logo and brand
	        int resID = getResources().getIdentifier(LOGO, "drawable", "com.PonRod");
	        if(resID!=0)
	        {
		        ImageView img = (ImageView)findViewById(R.id.brandLogo);
	            img.setImageResource(resID);
	        }
	        
	        TextView txtBrandName = (TextView) findViewById(R.id.brandHeader);
	        txtBrandName.setText(BRAND);
	        
	        SetLoadingThread();
	}
	 
	@Override
	public void onBackPressed() {
			// your code.
			Intent newIntent = new Intent(this.getApplicationContext(), BrandListClass.class);
			startActivity(newIntent);
			finish();
	}
	 

	public void SetLoadingThread() {
		// TODO Auto-generated method stub
		_dialog = ProgressDialog.show(BrandModelListClass.this, "", 
		        "Loading. Please wait...", true);
		
		 Thread thread = new Thread(this);
		 thread.start();
	}

	public void run() {
		//Download brand model list
        this.CreateCarModelList(ModelPathParam);
        
        ArrayList<CarModelModel> nodes = this.GetCarModelList();
        
        //Needed for the listItems
        _mylist = new ArrayList<HashMap<String, String>>(); 
        
    	for (int i = 0; i < nodes.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();	
			CarModelModel e = (CarModelModel)nodes.get(i);
			map.put("id", e.getID());
			map.put("Brand", "" + BRAND);
        	map.put("Model", "" + e.getModelName());
        	map.put("Path", "" + e.getPath());
        	map.put("Logo", "" + LOGO);
        	map.put("Image", "" + getResources().getIdentifier(e.getImage(), "drawable", "com.PonRod"));
        	//map.put("Promotion", "" + getResources().getIdentifier(e.getLogo(), "drawable", "com.PonRod"));
        	//map.put("FlagUpdate", "" + getResources().getIdentifier(e.getLogo(), "drawable", "com.PonRod"));

        	_mylist.add(map);		
		}	   
    	
		 //Make a new listadapter
    	_adapter = new SimpleAdapter(this, _mylist , R.layout.model_item, 
                new String[] {"Model" }, 
                new int[] { R.id.brandModel});

   	   handler.sendEmptyMessage(0);
	}

	public void CreateCarModelList(String url) {
		// TODO Auto-generated method stub
		_targetModel = new CarModelListModel(url, BRAND, LOGO);
	}

	public ArrayList<CarModelModel> GetCarModelList() {
		// TODO Auto-generated method stub
		return _targetModel.models;
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
	               		//@SuppressWarnings("unchecked")
	       				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);
	               		BRAND = o.get("Brand");
	               		SERIESPATHPARAM = o.get("Path");
	               		LOGO  = o.get("Logo");
	               		MODEL =  o.get("Model");
	               		IMAGE = o.get("Image");
	               		Intent newIntent = new Intent(warpper, BrandSeriesListClass.class);
	               		Bundle bundle = new Bundle();
	       				bundle.putString("path",SERIESPATHPARAM);
	       				bundle.putString("brand",BRAND);
	       				bundle.putString("logo",LOGO);
	       				bundle.putString("model",MODEL);
	       				bundle.putString("Image",IMAGE);
	       				bundle.putString("returnToModelPath",ModelPathParam);
	       				newIntent.putExtras(bundle);
	       				startActivity(newIntent);
	       				finish();
	       			}
	       		});
	               
	        }
	};

}
