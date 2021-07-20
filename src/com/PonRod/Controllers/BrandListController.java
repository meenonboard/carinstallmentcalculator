package com.PonRod.Controllers;

import java.util.ArrayList;

import com.PonRod.Models.BrandModel;

public interface BrandListController {

	void CreateBrandlist(String url);
	
	ArrayList<BrandModel> GetBrandList();
	
	void SetLoadingThread();
	
}
