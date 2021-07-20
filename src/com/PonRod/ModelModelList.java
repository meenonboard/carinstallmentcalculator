package com.PonRod;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ModelModelList extends ListActivity implements Runnable {
	private Context warpper= null;
	private String XML_PATH = "";
	private String BRAND = "";
	private String MODEL ="";
	private String IMAGE ="";
	private String ModelPathParam ="";
	private String PromotionCode ="";
	private String LastUpdate ="";
	private String ModelXMLPath ="";

	private ListActivity _activity;
	private ArrayList<HashMap<String, String>> _mylist;
	private ListAdapter _adapter;
	private XMLServices _service;
	
	ProgressDialog _dialog;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.carmodelview);
	        
	        //Get param "path"
	        Bundle bundle = this.getIntent().getExtras();
	        ModelPathParam = bundle.getString("path");
	        BRAND = bundle.getString("brand");
	        
	        warpper = this.getApplicationContext();
	        _activity = this;
	        
	        //Needed for the listItems
	        _mylist = new ArrayList<HashMap<String, String>>(); 
	        _service = new XMLServices();
	        
	        SetLoadingThread();
	    }
	 
	 @Override
	public void onBackPressed() {
		// your code.
		Intent newIntent = new Intent(this.getApplicationContext(), CarModelList.class);
		startActivity(newIntent);
		finish();
	}
	 
	 private void SetLoadingThread() {
			_dialog = ProgressDialog.show(ModelModelList.this, "", 
			        "Loading. Please wait...", true);
			
			 Thread thread = new Thread(this);
			 thread.start();
	}
	
	 public void run() {
		// TODO Auto-generated method stub
		   //Needed for the listItems

        //Get the xml string from the server
        String xml = _service.getXML(ModelPathParam);
        Document doc = _service.XMLfromString(xml);

        //TODO : Protect from no element found in XML
        //int numResults = XMLfunctions.numResults(doc);
        NodeList nodes = doc.getElementsByTagName("result");

		//fill in the list items from the XML document
		for (int i = 0; i < nodes.getLength(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();	
			Element e = (Element)nodes.item(i);
			map.put("id", _service.getValue(e, "id"));
        	map.put("Model", "" + _service.getValue(e, "Model"));
        	map.put("Path", "" + _service.getValue(e, "Path"));
        	map.put("Image", "" + _service.getValue(e, "Image"));
        	map.put("Promotion", "" + _service.getValue(e, "Promotion"));
        	map.put("Update", "" + _service.getValue(e, "Update"));
        	if(_service.getValue(e, "FlagUpdate").toString().equals("1"))
        		map.put("FlagUpdate", "" + getResources().getIdentifier("price_update", "drawable", "com.PonRod"));
        	else
        		map.put("FlagUpdate", "" + getResources().getIdentifier("price_update_black", "drawable", "com.PonRod"));
        	//Put previous path
        	map.put("ModelXMLPath", "" + ModelPathParam);
        	_mylist.add(map);		
		}	
		//Make a new listadapter
		_adapter = new SimpleAdapter(this, _mylist , R.layout.listadapterwithupdate, 
                 new String[] { "Model","FlagUpdate" }, 
                 new int[] { R.id.item_content_title,R.id.imageUpdateView});
		 
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
		        		MODEL = o.get("Model");
		        		XML_PATH = o.get("Path");
		        		IMAGE = o.get("Image");
		        		PromotionCode = o.get("Promotion");
		        		LastUpdate = o.get("Update");
		        		ModelXMLPath = o.get("ModelXMLPath");

		        		Intent newIntent = new Intent(warpper, SeriesModelView.class);
						Bundle bundle = new Bundle();
						bundle.putString("path",XML_PATH);
						bundle.putString("brand",BRAND);
						bundle.putString("model",MODEL);
						bundle.putString("image",IMAGE);
						bundle.putString("promotion",PromotionCode);
						bundle.putString("update",LastUpdate);
						bundle.putString("modelxmlpath",ModelXMLPath);
						newIntent.putExtras(bundle);
						startActivity(newIntent);
						finish();
					}
				});

	        }
	};
}
