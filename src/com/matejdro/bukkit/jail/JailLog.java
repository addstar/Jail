package com.matejdro.bukkit.jail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Jail Log Class
 * @author Luke
 */
public class JailLog {
    
    public JailLog()
    {
        
    }
    
    public void logToFile(Player player, Integer time, String reason, String sender, String name)
    {
        try{
            File dataFolder = Jail.instance.getDataFolder();

            if(!dataFolder.exists())
            {
                dataFolder.mkdir();
            }

            File jailLogFile = new File(Jail.instance.getDataFolder(), "jailLog.txt");
            if(!jailLogFile.exists())
            {
                jailLogFile.createNewFile();
            }
            
            FileWriter fw = new FileWriter(jailLogFile, true);
            PrintWriter pw = new PrintWriter(fw);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            
            if(player != null){
            	pw.println("[" + dateFormat.format(date) + "] " + ChatColor.BLUE + player.getName() + ChatColor.WHITE + " jailed by " + ChatColor.BLUE + sender + ChatColor.WHITE + ". Reason: " + ChatColor.BLUE + reason);
            }else{
            	pw.println("[" + dateFormat.format(date) + "] " + ChatColor.BLUE + name + ChatColor.WHITE + " jailed by " + ChatColor.BLUE + sender + ChatColor.WHITE + ". Reason: " + ChatColor.BLUE + reason);
            }
            pw.flush();
            pw.close();
            
        }catch(IOException e){}
    }
}
