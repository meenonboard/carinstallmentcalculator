package com.PonRod;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLServices {
	///Instance a HTTP call and get RAW XML in a text form
	public static String getXML(String URI){
		String line = null;

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URI);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			line = EntityUtils.toString(httpEntity,HTTP.UTF_8);

		} catch (UnsupportedEncodingException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (MalformedURLException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (IOException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		}

		return line;

	}
	
	///Transform XML string to a document form
	public Document XMLfromString(String xml){

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(xml));
		    doc = db.parse(is); 

			} catch (ParserConfigurationException e) {
				System.out.println("XML parse error: " + e.getMessage());
				return null;
			} catch (SAXException e) {
				System.out.println("Wrong XML file structure: " + e.getMessage());
	            return null;
			} catch (IOException e) {
				System.out.println("I/O exeption: " + e.getMessage());
				return null;
			}

	        return doc;

		}

	
	///Get Specific field
	public String getValue(Element element, String string) {
		// TODO Auto-generated method stub
		NodeList n = element.getElementsByTagName(string);		
		return XMLServices.getElementValue(n.item(0));
	}
	
	/** Returns element value
	  * @param elem element (it is XML tag)
	  * @return Element value otherwise empty String
	  */
	 public final static String getElementValue( Node elem ) {
	     Node kid;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
	                 if( kid.getNodeType() == Node.TEXT_NODE  ){
	                     return kid.getNodeValue();
	                 }
	             }
	         }
	     }
	     return "";
	 }
}
