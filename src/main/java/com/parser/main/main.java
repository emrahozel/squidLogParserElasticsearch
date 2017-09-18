package com.parser.main;

import com.maxmind.geoip.LookupService;
import com.parser.config.properties;
import com.parser.service.ClientProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by emrahozel on 18.09.2017.
 */
public class main {

    static LookupService cl;

    public static void main(String[] args) throws IOException {
        properties prop = new properties();
        ClientProvider.instance().prepareClient();
        System.out.println("Starting Squid Access Log Parsing...");
        cl = new LookupService("src/main/resources/GeoIP.dat", LookupService.GEOIP_MEMORY_CACHE);
        File file = new File(prop.logFile);
        if ((file.exists()) && (file.canRead())) {
            for (;;)
            {
                prop = new properties();
                if (file.exists()) {
                    if (prop.lastLine < file.length()) {
                        System.out.println("Start Reading.....");
                        readFile(file, Long.valueOf(prop.lastLine), prop.indexName, prop.typeName);
                        prop.updateLastLine(file.length());
                        System.out.println("End Of File");
                    }
                    else if (file.length() < prop.lastLine) {
                        System.out.println("Start Reading.....");
                        readFile(file, Long.valueOf(0L), prop.indexName, prop.typeName);
                        prop.updateLastLine(file.length());
                        System.out.println("End Of File");
                    }
                }
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        ClientProvider.instance().closeClient();
    }

    public static void readFile(File file, Long fileLength, String indexName, String typeName) {
        String line = null;
        try (BufferedReader in = new BufferedReader(new java.io.FileReader(file))) {
            in.skip(fileLength);
            Map<String, Object> map = new HashMap();

            while ((line = in.readLine()) != null) {
                String[] str = line.trim().split("\\s+");
                if (str[6].startsWith("http")) {
                    map.put("timestamp", new Date((long) Double.parseDouble(str[0]) * 1000L));
                    map.put("src_ip", str[2]);
                    map.put("status_code", str[3].split("/")[1]);
                    map.put("bytes", Long.valueOf(Long.parseLong(str[4])));
                    map.put("method", str[5]);
                    String url = str[6];
                    map.put("dst_host", url.split("/")[2]);
                    map.put("request_url", url);
                    map.put("user", str[7]);
                    String dst_ip = str[8].split("/")[1];
                    map.put("dst_ip", dst_ip);
                    map.put("content", str[9]);
                    map.put("location", cl.getCountry(dst_ip).getCode());
                    ClientProvider.instance().getClient().prepareIndex(indexName, typeName).setSource(map).execute().actionGet();
                }
                cl.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
