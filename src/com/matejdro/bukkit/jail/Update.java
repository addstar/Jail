package com.matejdro.bukkit.jail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Update {
	// The project's unique ID
    private final int projectID;

    // Keys for extracting file information from JSON response
    private static final String API_NAME_VALUE = "name";

    // Static information for querying the API
    private static final String API_QUERY = "/servermods/files?projectIds=";
    private static final String API_HOST = "https://api.curseforge.com";
    private boolean needed = false;
    
    /**
     * Check for updates anonymously (keyless)
     *
     * @param projectID The BukkitDev Project ID, found in the "Facts" panel on the right-side of your project page.
     */
    public Update(int projectID) {
    	this.projectID = projectID;
    }
    
    public void query() {
        URL url = null;

        try {
            // Create the URL to query using the project's ID
            url = new URL(API_HOST + API_QUERY + projectID);
        } catch (MalformedURLException e) {
            return;
        }

        try {
            // Open a connection and query the project
            URLConnection conn = url.openConnection();

            // Add the user-agent to identify the program
            conn.addRequestProperty("User-Agent", "Jail Update Checker");

            // Read the response of the query
            // The response will be in a JSON format, so only reading one line is necessary.
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();

            // Parse the array of files from the query's response
            JSONArray array = (JSONArray) JSONValue.parse(response);

            if (array.size() > 0) {
                // Get the newest file's details
                JSONObject latest = (JSONObject) array.get(array.size() - 1);

                // Get the version's title
                String versionName = (String) latest.get(API_NAME_VALUE);
                String ver = versionName.split(" ")[1].replaceAll("v", "");
                
                if(!Jail.instance.getDescription().getVersion().contains(ver)) this.needed = true;
                
                if(needed) {
                	Bukkit.getLogger().info("There is an update for Jail, the newest version is: " + ver);
                }
            }
        } catch (IOException e) {
            // There was an error reading the query

            e.printStackTrace();
            return;
        }
    }
    
    /** Returns true if there is an update needed, false if not. */
    public boolean updateNeeded() {
    	return this.needed;
    }
}
