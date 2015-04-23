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
import org.beyondpn.redis.migration.meta.MetaService;
import org.beyondpn.redis.migration.meta.VersionMeta;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <strong>info</strong> command, would print current migration information.
 * <p>
 * Created by yangjianhua on 2015/4/13.
 */
@Parameters(commandNames = "info", separators = "=", commandDescription = "print migration info")
public class CommandInfo extends Command {

    private Logger logger = LoggerFactory.getLogger(CommandInfo.class);

    @Override
    protected void doExecute(Jedis jedis) {
        MetaService metaService = new MetaService(jedis);
        List<VersionMeta> versionMetaList = metaService.selectVersionMeta();

        int versionLen = "Version".length();
        int descLen = "Description".length();
        int scriptNameLen = "Script".length();
        int hashLen = String.valueOf(Long.MAX_VALUE).length();
        int onLen = String.valueOf("yyyy-MM-dd HH:mm:ss").length();
        int millsLen = String.valueOf(Long.MAX_VALUE).length();
        int succLen = "Success".length();
        for (VersionMeta versionMeta : versionMetaList) {
            if (descLen < versionMeta.getDescription().length()) {
                descLen = versionMeta.getDescription().length();
            }
            if (scriptNameLen < versionMeta.getScript().length()) {
                scriptNameLen = versionMeta.getScript().length();
            }
        }

        //print title
        logger.info(new PrettyPrint().append('+').append(versionLen, '-').append('+').append(descLen, '-')
                .append('+').append(scriptNameLen, '-').append('+').append(hashLen, '-').append("+").append(onLen, '-')
                .append('+').append(millsLen, '-').append('+').append(succLen, '-').append('+').toString());
        logger.info(new PrettyPrint().append("|").append("Version", versionLen, ' ').append("|").append("Description", descLen, ' ')
                .append("|").append("Script", scriptNameLen, ' ').append("|").append("Hash", hashLen, ' ').append("|")
                .append("Execute on", onLen, ' ').append("|").append("Mills", millsLen, ' ').append('|').append("State", succLen, ' ').append('|').toString());
        logger.info(new PrettyPrint().append('+').append(versionLen, '-').append('+').append(descLen, '-')
                .append('+').append(scriptNameLen, '-').append('+').append(hashLen, '-').append("+").append(onLen, '-')
                .append('+').append(millsLen, '-').append('+').append(succLen, '-').append('+').toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (VersionMeta m : versionMetaList) {
            logger.info(new PrettyPrint().append("|").append(String.valueOf(m.getVersion()), versionLen, ' ').append("|")
                    .append(m.getDescription(), descLen, ' ').append("|").append(m.getScript(), scriptNameLen, ' ').append("|")
                    .append(String.valueOf(m.getHash()), hashLen, ' ').append("|")
                    .append(dateFormat.format(m.getExecuteOn()), onLen, ' ').append("|").append(String.valueOf(m.getExecuteMills()), millsLen, ' ').append('|')
                    .append(m.getSuccess() == 1 ? "Success" : "Fail", succLen, ' ').append('|').toString());
        }
        logger.info(new PrettyPrint().append('+').append(versionLen, '-').append('+').append(descLen, '-')
                .append('+').append(scriptNameLen, '-').append('+').append(hashLen, '-').append("+").append(onLen, '-')
                .append('+').append(millsLen, '-').append('+').append(succLen, '-').append('+').toString());
    }

    private class PrettyPrint {

        private StringBuilder builder = new StringBuilder();

        private PrettyPrint append(char ch) {
            builder.append(ch);
            return this;
        }

        private PrettyPrint append(String string) {
            builder.append(string);
            return this;
        }

        private PrettyPrint append(String content, int length, char padding) {
            builder.append(padding);
            int contentLength = content == null ? 0 : content.length();
            if (contentLength != 0) {
                builder.append(content);
            }
            for (int i = 0; i < length - contentLength + 2; i++) {
                builder.append(padding);
            }
            return this;
        }

        private PrettyPrint append(int length, char padding) {
            return append(null, length, padding);
        }

        public String toString() {
            return builder.toString();
        }
    }
}
