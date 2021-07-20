package com.PonRod;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SeriesModelView extends Activity implements Runnable  {
	private String BRAND ="";
	private String MODEL ="";
	private String SERIES ="";
	private String IMAGES ="";
	private String XML_PATH = "";
	private String PromotionCode ="";
	private String LastUpdate ="";
	private String ModelXMLPath ="";
	private String CURRENCYFORMAT = "###,###.##";
	private static String MY_AD_UNIT_ID ="a14f144466d1565";
	private String[][] SERIESPRICE;  
	private com.google.ads.AdView ads;
	private XMLServices _service;
	private CalculationServices _calculationService;
	private ArrayAdapter <CharSequence> _adapter;
	
	ProgressDialog _dialog;
	
	/**Variable for result dialog**/
	private String price = "";
	private String warningMessage = "";
	private String downpayment = "";
	private String interestRate = "";
	private String percentString ="";
	private String monthToPayString = "";
	private Boolean isValidate = true;
	private Double amountAfterDeduct = 0.0;
	private Dialog _resultDialog; 
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.carpriceinputview);
	        _service = new XMLServices();
			_resultDialog = new Dialog(SeriesModelView.this);
			  //set up dialog
			 _resultDialog.setContentView(R.layout.calculate_result);
			 _resultDialog.setCancelable(true);
			 _resultDialog.setTitle("Result");
			
	        // Create the adView
	        ads = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);

	        ads.setGravity(Gravity.CENTER);
	        ads.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	        
	        RelativeLayout relateLayout =(RelativeLayout) findViewById(R.id.imageDisplayLayout);
	        relateLayout.addView(ads);
	        ads.loadAd(new AdRequest());
	        
	        //Get param "path"
	        Bundle bundle = this.getIntent().getExtras();
	        XML_PATH = bundle.getString("path");
	        BRAND = bundle.getString("brand");
	        MODEL = bundle.getString("model");
	        IMAGES = bundle.getString("image");
	        PromotionCode = bundle.getString("promotion");
	        LastUpdate = bundle.getString("update");
	        ModelXMLPath = bundle.getString("modelxmlpath");
	        
	        String xml = _service.getXML(XML_PATH);
	        Document doc = _service.XMLfromString(xml);

	        //TODO : Protect from no element found in XML
	        //int numResults = XMLfunctions.numResults(doc);
	        NodeList nodes = doc.getElementsByTagName("result");
	        SERIESPRICE = new String[nodes.getLength()][2];
	        _adapter =
				  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
	        _adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				 //fill in the list items from the XML document
			for (int i = 0; i < nodes.getLength(); i++) {
					Element e = (Element)nodes.item(i);
					_adapter.add(_service.getValue(e, "SubModel"));	
		        	//Store to local
		        	SERIESPRICE[i][0] = _service.getValue(e, "SubModel");
		        	SERIESPRICE[i][1] = _service.getValue(e, "Price");		
			}	
			
        	Spinner spinner = (Spinner) findViewById(R.id.seriesSpin);
	        spinner.setAdapter(_adapter);
	        spinner.setOnItemSelectedListener(new MyOnSeriesSelectedListener());
	        
	        //Set image
	        int resID = getResources().getIdentifier(IMAGES, "drawable", "com.PonRod");
	        if(resID!=0)
	        {
		        ImageView img = (ImageView)findViewById(R.id.carPreview);
	            img.setImageResource(resID);
	        }
	        
	        //Set promotion image
	        int resPromotionId = getResources().getIdentifier("promotion", "drawable", "com.PonRod");
	        if(resID!=0 && PromotionCode.equals("1"))
	        {
		        ImageView promotionImage = (ImageView)findViewById(R.id.imagePromotionView);
		        Bitmap bmp = BitmapFactory.decodeResource(getResources(),resPromotionId);
		        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
		        promotionImage.setBackgroundDrawable(bitmapDrawable);
	        }
	        
	        //Set Model text
	        TextView model = (TextView) findViewById(R.id.modelText);
	        model.setText(BRAND + " " + MODEL);
	        
	        //Set Update text
	        TextView lastUpdateText = (TextView) findViewById(R.id.lastUpdateText);
	        lastUpdateText.setText(LastUpdate);
	        
	        //Set down payment
	        Spinner percentSpinner = (Spinner) findViewById(R.id.percentSpin);
	        ArrayAdapter<CharSequence> percentAdapter = ArrayAdapter.createFromResource(
	                this, R.array.percent_array, android.R.layout.simple_spinner_item);
	        percentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        percentSpinner.setAdapter(percentAdapter);
	        
	        //Set month payment
	        Spinner monthSpinner = (Spinner) findViewById(R.id.monthSpin);
	        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
	                this, R.array.months_array, android.R.layout.simple_spinner_item);
	        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        monthSpinner.setAdapter(monthAdapter);
	        
	        //Set Radio button
	        RadioButton radio_percent = (RadioButton) findViewById(R.id.radioPercent);
	        RadioButton radio_amount = (RadioButton) findViewById(R.id.radioAmount);
	        radio_percent.setOnClickListener(radio_listener);
	        radio_amount.setOnClickListener(radio_listener);
	        
	        //Set calculate button 
	        Button calculateBtn = (Button)findViewById(R.id.calculateBtn);
	        calculateBtn.setOnClickListener(calculate_listener);
	  }
	
	@Override
	public void onBackPressed() {
	    // your code.
		Intent newIntent = new Intent(this.getApplicationContext(), ModelModelList.class);
		Bundle bundle = new Bundle();
		bundle.putString("path",ModelXMLPath);
		bundle.putString("brand",BRAND);
		newIntent.putExtras(bundle);
		startActivity(newIntent);
		finish();
	}
	
	private OnClickListener radio_listener = new OnClickListener() {
         public void onClick(View v) {
             // Perform action on clicks
             RadioButton rb = (RadioButton) v;
             Toast.makeText(SeriesModelView.this, rb.getText(), Toast.LENGTH_SHORT).show();
             Spinner percent = (Spinner) findViewById(R.id.percentSpin);
             EditText amount = (EditText) findViewById(R.id.amountDown);
             if(rb.getId() == R.id.radioPercent){
            	 percent.setVisibility(View.VISIBLE);
            	 amount.setVisibility(View.INVISIBLE);
             }else {
            	 percent.setVisibility(View.INVISIBLE);
            	 amount.setVisibility(View.VISIBLE);
			}
         }
      };
      
    private OnClickListener calculate_listener = new OnClickListener() {
          public void onClick(View v) {
              // Perform action on clicks
        	  //Control value null 
        	  isValidate = true;
        	  _calculationService = new CalculationServices();
        	  
        	  RadioButton radio_percent = (RadioButton) findViewById(R.id.radioPercent);
        	  EditText amount = (EditText) findViewById(R.id.amountDown);
        	  EditText interest = (EditText) findViewById(R.id.interest_value);
        	  Spinner percent = (Spinner) findViewById(R.id.percentSpin);
        	  Spinner monthToPay = (Spinner) findViewById(R.id.monthSpin);
        	  Spinner series = (Spinner) findViewById(R.id.seriesSpin);
        	  SERIES = series.getSelectedItem().toString();
        	  monthToPayString = monthToPay.getSelectedItem().toString();
        	  
			  //0. Update price
			  for (String[] seriesPrice : SERIESPRICE) {
				    // do some work here on intValue
					if(seriesPrice[0].equals(SERIES))
					{
						price = seriesPrice[1];
					}
		      }
        	  //1. downpayment - Check RadioMode 
        	  if(radio_percent.isChecked())
        	  {
        		  percentString = percent.getSelectedItem().toString();
        		  downpayment = _calculationService.GetDownPaymentByPercent(price, percentString).toString();	  
        	  }
        	  else
        	  {
        		  downpayment = amount.getText().toString();
        		  if(downpayment.equals("")){
        			  warningMessage = "Please input downpayment as you select in Baht";
        			  isValidate = false;
        		  }
        		  else if(Double.valueOf(downpayment) > Double.valueOf(price)){
        			  warningMessage = "Please input downpayment not to over a total sale price";
        			  isValidate = false;
        		  }
        	  }
        	  //2. Interest
        	  if(!interest.getText().toString().equals(""))
        		  interestRate = interest.getText().toString();
        	  else
        	  {
        		  warningMessage = "Please input interest rate in percent";
    			  isValidate = false;
        	  }
        	  if(isValidate)
        	  {
            	  //Calculate
      	          SetLoadingThread();
     		}
        	  else
           		 Toast.makeText(SeriesModelView.this, warningMessage, Toast.LENGTH_SHORT).show();
        }
        	
       };
	  
    public class MyOnSeriesSelectedListener implements OnItemSelectedListener {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				String selected = parent.getItemAtPosition(pos).toString();
				//Update price
				for (String[] series : SERIESPRICE) {
				    // do some work here on intValue
					if(series[0].equals(selected))
					{
						 TextView salePrice = (TextView)findViewById(R.id.salePrice);
						 CalculationServices service = new CalculationServices();
					     salePrice.setText(service.FormatDecimal(CURRENCYFORMAT, Double.valueOf(series[1])));
					}
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		}

  	private void SetLoadingThread() {
		_dialog = ProgressDialog.show(SeriesModelView.this, "", 
		        "Calculating, Please wait...", true);
		
		 Thread thread = new Thread(this);
		 thread.start();
	}
	public void run() {
		// TODO Auto-generated method stub
		  //Get the xml string from the server
		 _calculationService = new CalculationServices();
		 amountAfterDeduct = Double.valueOf(price)-Double.valueOf(downpayment);

		 Double totalInterest = _calculationService.GetTotalInterest(interestRate,monthToPayString,amountAfterDeduct.toString());
		 Double totalCalculateCost = amountAfterDeduct + totalInterest;
		 Double totalPayment = totalCalculateCost + Double.valueOf(downpayment);
		 Double netPaypermonth = _calculationService.GetMonthlyPayment(totalCalculateCost,monthToPayString);
  	  
		
         //there are a lot of settings, for dialog, check them all out!

        //set up text
		 TextView textNew_1 = (TextView) _resultDialog.findViewById(R.id.brandModelResult);
		 TextView textNew_2 = (TextView) _resultDialog.findViewById(R.id.priceTextResult);
		 TextView textNew_3 = (TextView) _resultDialog.findViewById(R.id.modelResult);
		 TextView textNew_4 = (TextView) _resultDialog.findViewById(R.id.priceResult);
		 TextView textNew_5 = (TextView) _resultDialog.findViewById(R.id.downPaymentResult);
		 TextView textNew_6 = (TextView) _resultDialog.findViewById(R.id.monthResult);
		 TextView textNew_7 = (TextView) _resultDialog.findViewById(R.id.interestResult);
		 TextView textNew_8 = (TextView) _resultDialog.findViewById(R.id.totalInterestResult);
		 TextView textNew_9 = (TextView) _resultDialog.findViewById(R.id.totalPaymentResult);
		
		 
    	 textNew_1.setText(BRAND + " " + MODEL);
    	 textNew_4.setText(getString(R.string.series_price) + " " + _calculationService.FormatDecimal(CURRENCYFORMAT, Double.valueOf(price)));
    	 textNew_5.setText(getString(R.string.downpayment) + " " + _calculationService.FormatDecimal(CURRENCYFORMAT, (Double.valueOf(downpayment))));
    	 textNew_6.setText(getString(R.string.month) + " " + monthToPayString);
    	 textNew_7.setText(getString(R.string.interest_rate) + " " + interestRate);
         textNew_8.setText(getString(R.string.total_interest) + " " + _calculationService.FormatDecimal(CURRENCYFORMAT, totalInterest));
         textNew_9.setText(getString(R.string.total_payment) + " " + _calculationService.FormatDecimal(CURRENCYFORMAT, totalPayment)  + " " + getString(R.string.Baht));
         textNew_2.setText(_calculationService.FormatDecimal(CURRENCYFORMAT, netPaypermonth) + " " + getString(R.string.pay_monthly));
         textNew_3.setText(getString(R.string.series_name) + " " + SERIES);
         //Set image
	     int resID = getResources().getIdentifier(IMAGES, "drawable", "com.PonRod");
	     if(resID!=0)
	     {
           ImageView img = (ImageView) _resultDialog.findViewById(R.id.carPreviewResult);
           img.setImageResource(resID);
	     }
		
		 handler.sendEmptyMessage(0);
	}
	 private Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	 //set up button
                Button button = (Button) _resultDialog.findViewById(R.id.btnBackResult);
                button.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	_resultDialog.dismiss();
                    }
                });

                //now that the dialog is set up, it's time to show it    
                _resultDialog.show();
		           
                //Hidden Loading progress
	            _dialog.dismiss();
	        }
	 };
	 
}
