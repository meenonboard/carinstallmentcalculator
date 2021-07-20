package com.PonRod;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CustomCalculateModelView extends Activity implements Runnable{
	public String CURRENCYFORMAT = "###,###.##";
	private static String MY_AD_UNIT_ID ="a14f144466d1565";
	public String[][] SERIESPRICE;  
	private com.google.ads.AdView ads;
	
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
	private CalculationServices _calculationService;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customcarpriceinputview);

        new XMLServices();
		_resultDialog = new Dialog(CustomCalculateModelView.this);
		  //set up dialog
		 _resultDialog.setContentView(R.layout.calculate_result);
		 _resultDialog.setTitle("Result");
		 _resultDialog.setCancelable(true);
		 
        // Create the adView
        ads = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);
        RelativeLayout relateLayout =(RelativeLayout) findViewById(R.id.customImageDisplayLayout);
        relateLayout.addView(ads);
        ads.loadAd(new AdRequest());
        
        //Set down payment
        Spinner percentSpinner = (Spinner) findViewById(R.id.customPercentSpin);
        ArrayAdapter<CharSequence> percentAdapter = ArrayAdapter.createFromResource(
                this, R.array.percent_array, android.R.layout.simple_spinner_item);
        percentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        percentSpinner.setAdapter(percentAdapter);
        
        //Set month payment
        Spinner monthSpinner = (Spinner) findViewById(R.id.customMonthSpin);
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.months_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        
        //Set Radio button
        RadioButton radio_percent = (RadioButton) findViewById(R.id.customRadioPercent);
        RadioButton radio_amount = (RadioButton) findViewById(R.id.customRadioAmount);
        radio_percent.setOnClickListener(radio_listener);
        radio_amount.setOnClickListener(radio_listener);
        
        //Set calculate button 
        Button calculateBtn = (Button)findViewById(R.id.customCalculateBtn);
        calculateBtn.setOnClickListener(calculate_listener);       
	}
	
	private OnClickListener radio_listener = new OnClickListener() {
        public void onClick(View v) {
            // Perform action on clicks
            RadioButton rb = (RadioButton) v;
            Toast.makeText(CustomCalculateModelView.this, rb.getText(), Toast.LENGTH_SHORT).show();
            Spinner percent = (Spinner) findViewById(R.id.customPercentSpin);
            EditText amount = (EditText) findViewById(R.id.customAmountDown);
            if(rb.getId() == R.id.customRadioPercent){
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
       	  CalculationServices services = new CalculationServices();
       	  
       	  RadioButton radio_percent = (RadioButton) findViewById(R.id.customRadioPercent);
       	  EditText amount = (EditText) findViewById(R.id.customAmountDown);
       	  EditText totalPrice = (EditText) findViewById(R.id.customSalePrice);
       	  EditText interest = (EditText) findViewById(R.id.customInterest_value);
       	  Spinner percent = (Spinner) findViewById(R.id.customPercentSpin);
       	  Spinner monthToPay = (Spinner) findViewById(R.id.customMonthSpin);
       	  monthToPayString = monthToPay.getSelectedItem().toString();
       	  price = totalPrice.getText().toString();
       	  //0. Update price
       	  if(price.equals(""))
       	  {
       		  warningMessage = "Please input a car sale price";
   			  isValidate = false;
       	  }
       	  
       	  //1. Interest
       	  if(!interest.getText().toString().equals(""))
       		  interestRate = interest.getText().toString();
       	  else
       	  {
       		  warningMessage = "Please input interest rate in percent";
   			  isValidate = false;
       	  }
       	  
       	  //2. downpayment - Check RadioMode
       	  if(!price.equals(""))
       	  {
       		 if(radio_percent.isChecked())
          	  {
          		  percentString = percent.getSelectedItem().toString();
          		  downpayment = services.GetDownPaymentByPercent(price, percentString).toString();	  
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
       	  }
       	  
       	  if(isValidate)
       	  {
       		  SetLoadingThread();
    	  }
       	  else
          	Toast.makeText(CustomCalculateModelView.this, warningMessage, Toast.LENGTH_SHORT).show();
       }       	
      };
      
      
    private void SetLoadingThread() {
    		_dialog = ProgressDialog.show(CustomCalculateModelView.this, "", 
    		        "Calculating, Please wait...", true);
    		
    		 Thread thread = new Thread(this);
    		 thread.start();
    }
    	
    private Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	 //set up button
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

	public void run() {
		// TODO Auto-generated method stub
		
     	  //Calculate
		 _calculationService = new CalculationServices();

 		 amountAfterDeduct = Double.valueOf(price)-Double.valueOf(downpayment);

 		 Double totalInterest = _calculationService.GetTotalInterest(interestRate, monthToPayString, amountAfterDeduct.toString());
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
		 
         textNew_1.setText("Custom calculation");
    	 textNew_4.setText(getString(R.string.series_price) + " " + _calculationService.FormatDecimal(CURRENCYFORMAT, Double.valueOf(price)));
    	 textNew_5.setText(getString(R.string.downpayment) + " " + _calculationService.FormatDecimal(CURRENCYFORMAT, (Double.valueOf(downpayment))));
    	 textNew_6.setText(getString(R.string.month) + " " + monthToPayString);
    	 textNew_7.setText(getString(R.string.interest_rate) + " " + interestRate);
         textNew_8.setText(getString(R.string.total_interest) + " " + _calculationService.FormatDecimal(CURRENCYFORMAT, totalInterest));
         textNew_9.setText(getString(R.string.total_payment) + " " + _calculationService.FormatDecimal(CURRENCYFORMAT, totalPayment)  + " " + getString(R.string.Baht));
         textNew_2.setText(_calculationService.FormatDecimal(CURRENCYFORMAT, netPaypermonth) + " " + getString(R.string.pay_monthly));
         textNew_3.setText(getString(R.string.series_name) + " - ");

         //Set image
    	 int resID = getResources().getIdentifier("logo_mobile", "drawable", "com.PonRod");
    	 if(resID!=0)
    	 {
           ImageView img = (ImageView) _resultDialog.findViewById(R.id.carPreviewResult);
           img.setImageResource(resID);
    	 }
    	 handler.sendEmptyMessage(0);
	}
}
