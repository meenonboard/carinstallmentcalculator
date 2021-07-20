package com.PonRod.Controllers;

import java.util.ArrayList;

import com.PonRod.Models.CarModelModel;

public interface ModelListController {
	
	void CreateCarModelList(String url);
	
	ArrayList<CarModelModel> GetCarModelList();
	
	void SetLoadingThread();
}
