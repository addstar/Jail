package com.matejdro.bukkit.jail;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateChecker {
	
	private Jail plugin;
	private URL filesFeed;
	
	private String version;
	private String link;
	
	public UpdateChecker(Jail plugin, String url){
		this.plugin = plugin;
		
		try {
			this.filesFeed = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateNeeded(){
		try {
			InputStream input = this.filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
		
			Node latestFile = document.getElementsByTagName("item").item(0);
			
			NodeList childeren = latestFile.getChildNodes();
			
			this.version = childeren.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
			this.link = childeren.item(3).getTextContent();
			
			if(!plugin.getDescription().getVersion().startsWith(this.version.substring(0, 1)) && !plugin.getDescription().getVersion().endsWith(this.version.substring(this.version.length() - 1))){
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String getVersion(){
		return this.version;
	}
	
	public String getLink(){
		return this.link;
	}
}
