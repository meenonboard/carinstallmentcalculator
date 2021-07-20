package com.PonRod.Models;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.PonRod.XMLServices;
import com.PonRod.Models.CalculationModel.Mode;

public class SeriesModelListModel {
	public ArrayList<SeriesModel> models;
	public String brand;
	public String model;
	public Mode  calculationmode;
	public String imagepath;
	
	private XMLServices xmlService;
	
	public SeriesModelListModel(String url,String brand, String model,Mode  calculationmode,String imagepath){
		this.models = new ArrayList<SeriesModel>();
		this.brand = brand;
		this.model = model;
		this.calculationmode = calculationmode;
		this.imagepath = imagepath;
		GetSeriesModelList(url);
	}
	
	private void GetSeriesModelList(String pathURI){
		xmlService = new XMLServices();
		
		String XML_PATH = xmlService.getXML(pathURI);
	    Document doc = xmlService.XMLfromString(XML_PATH);
	    
	    //TODO : Protect from no element found in XML
        //int numResults = XMLfunctions.numResults(doc);
        NodeList nodes = doc.getElementsByTagName("result");

		//fill in the list items from the XML document
		for (int i = 0; i < nodes.getLength(); i++) {
			SeriesModel Model = new SeriesModel();
			Element e = (Element)nodes.item(i);
			Model.setSeriesName(xmlService.getValue(e, "SubModel"));
			Model.setID(xmlService.getValue(e, "id"));
			Model.setSelectPrice(xmlService.getValue(e, "Price"));
			models.add(Model);
			
        	//Put previous path
			//Model.setLogo(xmlService.getValue(e, "Image"));
		}
	}
}
