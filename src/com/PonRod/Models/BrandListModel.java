package com.PonRod.Models;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.PonRod.XMLServices;

public class BrandListModel {
	
	public ArrayList<BrandModel> brands;
	
	private XMLServices xmlService;
	
	public BrandListModel(String url){
		
		//Initial data 
		this.brands = new ArrayList<BrandModel>();
		GetBrandList(url);
	}
	
	private void GetBrandList(String pathURI){
		
		xmlService = new XMLServices();
		
		String XML_PATH = xmlService.getXML(pathURI);
	    Document doc = xmlService.XMLfromString(XML_PATH);
	    
	    //TODO : Protect from no element found in XML
        //int numResults = XMLfunctions.numResults(doc);
        NodeList nodes = doc.getElementsByTagName("result");

		//fill in the list items from the XML document
		for (int i = 0; i < nodes.getLength(); i++) {
			BrandModel Model = new BrandModel();
			Element e = (Element)nodes.item(i);
			
			Model.setBrandName(xmlService.getValue(e, "Brand"));
			Model.setID(xmlService.getValue(e, "id"));
			Model.setPath(xmlService.getValue(e, "Path"));
			Model.setLogo(xmlService.getValue(e, "Logo"));
			Model.setPrice(xmlService.getValue(e, "PriceRange"));
			
			brands.add(Model);	
		}	
	}
	
}
