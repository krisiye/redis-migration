package org.beyondpn.redis.migration.command;

import com.beust.jcommander.Parameters;
import org.beyondpn.redis.migration.meta.MetaService;
import org.beyondpn.redis.migration.meta.VersionMeta;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by yangjianhua on 2015/4/13.
 */
@Parameters(commandNames = "info", separators = "=", commandDescription = "clean all keys from redis")
public class CommandInfo extends Command {
    @Override
    protected void doExecute(Jedis jedis) {
        MetaService metaService = new MetaService(jedis);
        List<VersionMeta> versionMetaList = metaService.selectVersionMeta();
        for(VersionMeta versionMeta : versionMetaList){
            System.out.println(versionMeta.getVersion());
        }
    }
}
