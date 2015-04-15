package org.beyondpn.redis.migration.command;

import com.beust.jcommander.Parameters;
import redis.clients.jedis.Jedis;

/**
 * Created by yangjianhua on 2015/4/13.
 */
@Parameters(commandNames = "clean", separators = "=", commandDescription = "clean all keys from redis")
public class CommandClean extends Command {
    @Override
    public void doExecute(Jedis jedis) {
        jedis.flushDB();
    }
}