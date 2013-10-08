package com.graywolf336.jail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.matejdro.bukkit.jail.Setting;
import com.matejdro.bukkit.jail.Settings;

/**
 * Handles all things related to configs and saving/loading the {@link com.graywolf336.jail.beans.Jail jails}, {@link com.graywolf336.jail.beans.Cell cells}, and {@link com.graywolf336.jail.beans.Prisoner prisoners}.
 * 
 * @author graywolf336
 * @since 2.x.x
 * @version 2.0.0
 *
 */
public class JailIO {
	private JailMain plugin;
    private YamlConfiguration global, jails;
    private HashMap<Integer, String[]> jailStickParameters;
    
    public JailIO(JailMain plugin) {
    	this.plugin = plugin;
    	this.global = new YamlConfiguration();
    	this.jails = new YamlConfiguration(); 
    	this.jailStickParameters = new HashMap<Integer, String[]>();
    	
    	loadSettings();
    }
    
    /** Gets the global configuration. */
    public YamlConfiguration getGlobalConfig() {
    	return this.global;
    }
    
    /** Returns the config which has settings per jail. */
    public YamlConfiguration getJailsConfig() {
    	return this.jails;
    }
    
	private void loadSettings() {    	
		try {
			if (!new File(plugin.getDataFolder(),"global.yml").exists())
				global.save(new File(plugin.getDataFolder(),"global.yml"));
			if (!new File(plugin.getDataFolder(),"jails.yml").exists())
				jails.save(new File(plugin.getDataFolder(),"jails.yml"));
			
			global.load(new File(plugin.getDataFolder(),"global.yml"));
			jails.load(new File(plugin.getDataFolder(),"jails.yml"));
			
			for (Setting s : Setting.values())
				if (global.get(s.getString()) == null)
					global.set(s.getString(), s.getDefault());
			
			loadJailStickParameters();
			
			global.save(new File(plugin.getDataFolder(),"global.yml"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			plugin.getLogger().severe("Unable to create and load the settings, please check the files exist.");
		} catch (IOException e) {
			e.printStackTrace();
			plugin.getLogger().severe("Unable to save or load the settings, please verify everything before reporting.");
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			plugin.getLogger().severe("Unable to load the configuration, please report this error.");
		}
	}
	    
	private void loadJailStickParameters() {
		for (String i : Settings.getGlobalString(Setting.JailStickParameters).split(";")) {
			jailStickParameters.put(Integer.parseInt(i.substring(0, i.indexOf(","))), i.split(","));
		}
	}
}
