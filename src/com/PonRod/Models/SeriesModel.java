package com.PonRod.Models;

import com.PonRod.Models.CalculationModel.Mode;

public class SeriesModel {
	private String ID;
	private String BrandName;
	private String ModelName;
	private String SeriesName;
	private String SelectPrice;
	private Mode CalculationMode;
	private String ImagePath;

	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getBrandName() {
		return BrandName;
	}
	public void setBrandName(String brandName) {
		BrandName = brandName;
	}
	
	public String getModelName() {
		return ModelName;
	}
	public void setModelName(String modelName) {
		ModelName = modelName;
	}

	public String getSeriesName() {
		return SeriesName;
	}
	public void setSeriesName(String seriesName) {
		SeriesName = seriesName;
	}

	public String getSelectPrice() {
		return SelectPrice;
	}
	public void setSelectPrice(String selectPrice) {
		SelectPrice = selectPrice;
	}

	public Mode getCalculationMode() {
		if(CalculationMode == null)
			return CalculationMode.FIXED;
		else
			return CalculationMode;
	}
	public void setCalculationMode(Mode calculationMode) {
		CalculationMode = calculationMode;
	}
	
	public String getImagePath() {
		return ImagePath;
	}
	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}
}


