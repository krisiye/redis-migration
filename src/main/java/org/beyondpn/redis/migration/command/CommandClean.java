/*
 * Copyright 2015 BeyondPN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.beyondpn.redis.migration.command;

import com.beust.jcommander.Parameters;
import org.beyondpn.redis.migration.log.Logger;
import org.beyondpn.redis.migration.log.LoggerFactory;
import org.beyondpn.redis.migration.util.StopWatch;
import redis.clients.jedis.Jedis;

/**
 * <strong>clean</strong> command. Would flush redis db.
 * <p>
 * Created by yangjianhua on 2015/4/13.
 */
@Parameters(commandNames = "clean", separators = "=", commandDescription = "clean all keys from redis")
public class CommandClean extends Command {

    private Logger logger = LoggerFactory.getLogger(CommandClean.class);

    @Override
    public void doExecute(Jedis jedis) {
        boolean cleanEnable = "true".equals(super.getProperty("command.clean.enable"));
        if (cleanEnable) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            jedis.flushDB();
            stopWatch.stop();
            logger.info(String.format("redis db flushed (execute time : %d ms)", stopWatch.getMills()));
        } else {
            logger.info("clean command is disabled. nothing changed.");
        }
    }
}