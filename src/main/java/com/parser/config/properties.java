package com.parser.config;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by emrahozel on 18.09.2017.
 */
public class properties {

    public long lastLine;
    public String elkHost;
    public int elkPort;
    public String indexName;
    public String typeName;
    public String logFile;
    public Properties prop;
    public final String configFile;

    public properties(){
        configFile = System.getProperty("user.dir") + "/config.properties";
        createConfigFile();
        readConfigFile();
    }

    public void createConfigFile(){
        File file = new File(configFile);
        OutputStream output = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
                output = new FileOutputStream(file);
                prop = new Properties();
                prop.setProperty("lastLine", "0");
                prop.setProperty("elkHost", "localhost");
                prop.setProperty("elkPort", "9300");
                prop.setProperty("indexName", "squid");
                prop.setProperty("typeName", "access");
                prop.setProperty("logFile", "/var/log/squid/access.log");

                prop.store(output, null);
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(properties.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(properties.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(properties.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void readConfigFile() {
        try {
            InputStream input = new FileInputStream(configFile);
            prop = new Properties();
            prop.load(input);
            lastLine = Long.parseLong(prop.getProperty("lastLine"));
            elkHost = prop.getProperty("elkHost");
            elkPort = Integer.parseInt(prop.getProperty("elkPort"));
            indexName = prop.getProperty("indexName");
            typeName = prop.getProperty("typeName");
            logFile = prop.getProperty("logFile");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(properties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(properties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateLastLine(long fileLenght) {
        try {
            InputStream input = new FileInputStream(configFile);
            prop = new Properties();
            prop.load(input);
            OutputStream output = new FileOutputStream(configFile);
            prop.setProperty("lastLine", Long.toString(fileLenght));
            prop.store(output, null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(properties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(properties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
