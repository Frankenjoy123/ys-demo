package com.yunsoo.dataService;

import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Zhe on 2015/1/14.
 */

public class MemcachedTest {

    public static void setSampleData() throws IOException {

        String configEndpoint = "mymemcachedcluster.ojvqph.cfg.usw2.cache.amazonaws.com";
        Integer clusterPort = 11211;

        MemcachedClient client = new MemcachedClient(new InetSocketAddress(configEndpoint, clusterPort));
        // The client will connect to the other cache nodes automatically

        // Store a data item for an hour.  The client will decide which cache host will store this item.
        client.set("theKey", 3600, "This is the data value");
    }
}