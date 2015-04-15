package org.beyondpn.redis.migration.command;

import org.beyondpn.redis.migration.option.Options;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by yangjianhua on 2015/4/13.
 */
public abstract class Command {

    private Properties properties;
    private String redisHost;
    private int redisPort;

    public void setOptions(Options options) {
        File file = options.getConfigFile();
        properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException("config file not exists : " + options.getConfigFile());
        }
        redisHost = getProperty("redis.host");
        redisPort = Integer.parseInt(getProperty("redis.port"));
    }

    protected String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void execute() {
        Jedis jedis = new Jedis(redisHost, redisPort);
        try {
            doExecute(jedis);
        } finally {
            jedis.disconnect();
        }
    }

    protected abstract void doExecute(Jedis jedis);

}
