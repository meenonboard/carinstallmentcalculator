package com.PonRod;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.PonRod.Controllers.SeriesCalculationModeController;
import com.PonRod.Models.CalculationModel;
import com.PonRod.Models.CarModelListModel;
import com.PonRod.Models.SeriesModel;
import com.PonRod.Models.SeriesModelListModel;
import com.PonRod.SeriesModelView.MyOnSeriesSelectedListener;

import android.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView.FixedViewInfo;

public class BrandSeriesListClass extends Activity implements SeriesCalculationModeController{
	
	private boolean swap = true;
	private Button btnfixed ;
	private Button btnpercent; 
	private Button btnBack; 
	private LinearLayout fixedline;
	private LinearLayout percentline;
	
	private String BRAND ="";
	private String MODEL ="";
	private String SERIES ="";
	private String LOGO ="";
	private String IMAGES ="";
	private String SERIESPATH = "";
	private String ModelXMLPath ="";
	private String CURRENCYFORMAT = "###,###.##";
	private static String MY_AD_UNIT_ID ="a14f144466d1565";
	private String[][] SERIESPRICE;  
	private com.google.ads.AdView ads;
	private CalculationServices _calculationService;
	private ArrayAdapter <CharSequence> _adapter;
	private SeriesModelListModel _targetModel;
	private Spinner _modelSpinner;
	
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.series_list);
	     
	     //Prepare static data 
	     Bundle bundle = this.getIntent().getExtras();
	     SERIESPATH = bundle.getString("path");
	     BRAND = bundle.getString("brand");
	     MODEL = bundle.getString("model");
	     IMAGES = bundle.getString("Image");
	     ModelXMLPath = bundle.getString("returnToModelPath");
	     LOGO = bundle.getString("logo");
	     //returnToModelPath

	     //PromotionCode = bundle.getString("promotion");
	     //LastUpdate = bundle.getString("update");
	        
	     SetDownpaymentList();
	     SetLeasingMonthList();
	     
	     //Set Brand + Model text
	     TextView brandLabel = (TextView) findViewById(R.id.carFullBrand);
	     brandLabel.setText(BRAND);
	     TextView seriesLabel = (TextView) findViewById(R.id.carFullSeries);
	     seriesLabel.setText(MODEL);
	     
	     //Prepare calculation mode
	     btnfixed = (Button) findViewById(R.id.btnFixedMode);
         btnpercent = (Button) findViewById(R.id.btnPercentMode);
         btnBack = (Button) findViewById(R.id.btnBack);
         fixedline = (LinearLayout) findViewById(R.id.fixed_line);
         percentline = (LinearLayout) findViewById(R.id.percent_line);
         
         //Prepare event handling
         btnfixed.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ControlDownPaymentMode(v);
			}
		 });
         btnpercent.setOnClickListener(new OnClickListener() {
 			
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				ControlDownPaymentMode(v);
 			}
 		 });
         btnBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		} );
         
	     //Set image 
	     int resID = getResources().getIdentifier(IMAGES, "drawable", "com.PonRod");
	     if(resID!=0)
	     {
		     ImageView img = (ImageView)findViewById(R.id.carFullImage);
	         img.setImageResource(resID);
	     }
	      
	     //Populate series list 
	     _adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
	     _adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     
	   //Prepare model list data
	     this.CreateCarSeriesList(SERIESPATH);
	     
	     ArrayList<SeriesModel> nodes = this.GetCarSeriesList();
	    	
	     SERIESPRICE = new String[nodes.size()][2];
	     
		 //fill in the list items from the XML document
	     for (int i = 0; i < nodes.size(); i++) {
			SeriesModel e = (SeriesModel)nodes.get(i);
			_adapter.add(e.getSeriesName());	
		    //Store to local
		    SERIESPRICE[i][0] = e.getSeriesName();
		    SERIESPRICE[i][1] = e.getSelectPrice();		
	     }	
			
	     _modelSpinner = (Spinner) findViewById(R.id.model_spinner);
	     _modelSpinner.setAdapter(_adapter);
	     _modelSpinner.setOnItemSelectedListener(new OnSeriesSelectedListener());
	 }

	@Override
	public void onBackPressed() {
		Intent newIntent = new Intent(this.getApplicationContext(), BrandModelListClass.class);
		Bundle bundle = new Bundle();
		bundle.putString("path",ModelXMLPath);
		bundle.putString("brand",BRAND);
		bundle.putString("logo",LOGO);
		newIntent.putExtras(bundle);
		startActivity(newIntent);
		finish();
	}
		
	public void CreateCarSeriesList(String url) {
		// TODO Auto-generated method stub
		_targetModel = new SeriesModelListModel(url, BRAND, MODEL, CalculationModel.Mode.FIXED,IMAGES);
		
	}

	public ArrayList<SeriesModel> GetCarSeriesList() {
		// TODO Auto-generated method stub
		return _targetModel.models;
	}

	public void ControlDownPaymentMode(View currentButton) {
		// TODO Auto-generated method stub
		if(swap){
			
			fixedline.setVisibility(View.VISIBLE);
			percentline.setVisibility(View.GONE);
		}else
		{
			fixedline.setVisibility(View.GONE);
			percentline.setVisibility(View.VISIBLE);
		}
		
		swap = !swap;
		
	}

	public void SetDownpaymentList() {
		Spinner percentSpinner = (Spinner) findViewById(R.id.downpayment_spinner);
        ArrayAdapter<CharSequence> percentAdapter = ArrayAdapter.createFromResource(
                this, R.array.percent_array, android.R.layout.simple_spinner_item);
        percentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        percentSpinner.setAdapter(percentAdapter);
	}

	public void SetLeasingMonthList() {
		Spinner monthSpinner = (Spinner) findViewById(R.id.month_spinner);
	    ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
	             this, R.array.months_array, android.R.layout.simple_spinner_item);
	    monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    monthSpinner.setAdapter(monthAdapter);
	}

    private class OnSeriesSelectedListener implements OnItemSelectedListener {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				String selected = parent.getItemAtPosition(pos).toString();
				//Update price
				for (String[] series : SERIESPRICE) {
				    // do some work here on intValue
					if(series[0].equals(selected))
					{
						 TextView salePrice = (TextView)findViewById(R.id.txtFullPrice);
						 CalculationServices service = new CalculationServices();
					     salePrice.setText(service.FormatDecimal(CURRENCYFORMAT, Double.valueOf(series[1])));
					}
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		}
}
