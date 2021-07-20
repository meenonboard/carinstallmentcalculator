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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class CarModelList extends ListActivity implements Runnable{
	 /** Called when the activity is first created. */
	private Context warpper= null;
	private String XML_PATH = "";
	private String BRAND = "";

	private ListActivity _activity;
	private ArrayList<HashMap<String, String>> _mylist;
	private ListAdapter _adapter;
	private XMLServices _service;
	
	ProgressDialog _dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carmodelview);
        
        warpper = this.getApplicationContext();
        _activity = this;
        
        //Needed for the listItems
        _mylist = new ArrayList<HashMap<String, String>>(); 
        _service = new XMLServices();
        
        SetLoadingThread();
    }
    
	private void SetLoadingThread() {
		_dialog = ProgressDialog.show(CarModelList.this, "", 
		        "Loading. Please wait...", true);
		
		 Thread thread = new Thread(this);
		 thread.start();
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	
    }

	public void run() {
		// TODO Auto-generated method stub
		 //Get the xml string from the server
        XML_PATH = _service.getXML(getResources().getString(R.string.brand_datasource));
        Document doc = _service.XMLfromString(XML_PATH);

        //TODO : Protect from no element found in XML
        //int numResults = XMLfunctions.numResults(doc);
        NodeList nodes = doc.getElementsByTagName("result");

		//fill in the list items from the XML document
		for (int i = 0; i < nodes.getLength(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();	
			Element e = (Element)nodes.item(i);
			map.put("id", _service.getValue(e, "id"));
        	map.put("Brand", "" + _service.getValue(e, "Brand"));
        	map.put("Path", "" + _service.getValue(e, "Path"));
        	map.put("Logo", "" + getResources().getIdentifier(_service.getValue(e, "Logo"), "drawable", "com.PonRod"));
        	map.put("PriceRange", "" + _service.getValue(e, "PriceRange"));
        	_mylist.add(map);		
		}	
		
		 //Make a new listadapter
   		_adapter = new SimpleAdapter(this, _mylist , R.layout.listadapterwithpicture, 
                    new String[] { "Logo","Brand","PriceRange" }, 
                    new int[] { R.id.imageLogoView,R.id.item_title,R.id.item_subtitle});
		
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
	               		Intent newIntent = new Intent(warpper, ModelModelList.class);
	       				Bundle bundle = new Bundle();
	       				bundle.putString("path",XML_PATH);
	       				bundle.putString("brand",BRAND);
	       				newIntent.putExtras(bundle);
	       				startActivity(newIntent);
	       				finish();
	       			}
	       		});

	        }
	};
}
