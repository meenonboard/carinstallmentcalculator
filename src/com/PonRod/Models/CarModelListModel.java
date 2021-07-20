package com.PonRod.Models;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.PonRod.XMLServices;

public class CarModelListModel {
	public ArrayList<CarModelModel> models;
	public String _brand;
	public String _logo;
	
	private XMLServices xmlService;
	
	public CarModelListModel(String url,String brand,String logo){
		this.models = new ArrayList<CarModelModel>();
		this._brand = brand;
		this._logo = logo;
		GetCarModelList(url);
	}
	
	private void GetCarModelList(String pathURI){
		
		xmlService = new XMLServices();
		
		String XML_PATH = xmlService.getXML(pathURI);
	    Document doc = xmlService.XMLfromString(XML_PATH);
	    
	    //TODO : Protect from no element found in XML
        //int numResults = XMLfunctions.numResults(doc);
        NodeList nodes = doc.getElementsByTagName("result");

		//fill in the list items from the XML document
		for (int i = 0; i < nodes.getLength(); i++) {
			CarModelModel Model = new CarModelModel();
			Element e = (Element)nodes.item(i);
			Model.setModelName(xmlService.getValue(e, "Model"));
			Model.setID(xmlService.getValue(e, "id"));
			Model.setPath(xmlService.getValue(e, "Path"));
			Model.setPromotion(xmlService.getValue(e, "Promotion"));
			Model.setFlagUpdate(xmlService.getValue(e, "FlagUpdate"));
			Model.setImage(xmlService.getValue(e, "Image"));
			models.add(Model);
			
        	//Put previous path
			//Model.setLogo(xmlService.getValue(e, "Image"));
		}	
	}
}
