package com.matejdro.bukkit.jail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;

/**
 * Jail Log Class
 * @author Luke
 */
public class JailLog {
    public void logToFile(String name, Integer time, String reason, String sender) {
        try{
            File dataFolder = Jail.instance.getDataFolder();

            if(!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File jailLogFile = new File(Jail.instance.getDataFolder(), "jailLog.txt");
            if(!jailLogFile.exists()) {
                jailLogFile.createNewFile();
            }
            
            FileWriter fw = new FileWriter(jailLogFile, true);
            PrintWriter pw = new PrintWriter(fw);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            
        	pw.println("[" + dateFormat.format(date) + "] " + name.toLowerCase() + " jailed by " + sender.toLowerCase() + " for " + time.toString() + " minutes with " + (reason.isEmpty() ? "no reason given." : "a reason of '" + reason + "'."));
            
            pw.flush();
            pw.close();
            
        }catch(IOException e){
        	Bukkit.getLogger().warning("Failed to log a jailing to file because: " + e.getMessage());
        }
    }
}
