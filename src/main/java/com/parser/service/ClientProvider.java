package com.parser.service;

import com.parser.config.properties;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientProvider {
    private static ClientProvider instance = null;
    private static Object lock = new Object();
    private TransportClient client;

    public ClientProvider() {}

    public static ClientProvider instance() {
        if (instance == null) {
            synchronized (lock) {
                if (null == instance) {
                    instance = new ClientProvider();
                }
            }
        }
        return instance;
    }

    public void prepareClient()
    {
        properties prop = new properties();
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY, new Class[0])
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(prop.elkHost), prop.elkPort))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(prop.elkHost), prop.elkPort));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void closeClient() {
        client.close();
    }

    public TransportClient getClient() {
        return client;
    }

    public void printThis() {
        System.out.println(this);
    }
}