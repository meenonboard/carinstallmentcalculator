package com.PonRod.Controllers;

import java.util.ArrayList;

import android.view.View;

import com.PonRod.Models.SeriesModel;

public interface SeriesCalculationModeController {

	void CreateCarSeriesList(String url);
	
	ArrayList<SeriesModel> GetCarSeriesList();
	
	void ControlDownPaymentMode(View currentButton);
	
	void SetDownpaymentList();
	
	void SetLeasingMonthList();
	
}
