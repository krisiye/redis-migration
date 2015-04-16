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

package org.beyondpn.redis.migration.meta;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yangjianhua on 2015/4/15.
 */
public class MetaService {

    private static final String META_VERSION_KEY = "beyondpn.redis.migration.version";
    private static final String META_KEY_PREFIX = "beyondpn.redis.migration.v";

    private Jedis jedis;

    public MetaService(Jedis jedis) {
        this.jedis = jedis;
    }

    public List<VersionMeta> selectVersionMeta() {
        int curVersion = this.getCurVersion();
        Pipeline p = jedis.pipelined();
        for (int version = 1; version <= curVersion; version++) {
            p.hgetAll(META_KEY_PREFIX + version);
        }
        List list = p.syncAndReturnAll();
        List<VersionMeta> metaList = new ArrayList<>();
        list.forEach(t -> {
                    Map<String, String> metaMap = (Map<String, String>) t;
                    VersionMeta meta = new VersionMeta();
                    meta.setVersion(Integer.parseInt(metaMap.get("version")));
                    meta.setScript(metaMap.get("script"));
                    meta.setDescription(metaMap.get("desc"));
                    meta.setHash(Long.parseLong(metaMap.get("hash")));
                    meta.setExecuteOn(new Date(Long.parseLong(metaMap.get("on"))));
                    meta.setExecuteMills(Integer.parseInt(metaMap.get("mills")));
                    meta.setSuccess(Integer.parseInt(metaMap.get("succ")));
                    metaList.add(meta);
                }
        );
        return metaList;
    }

    public void setVersionMeta(VersionMeta versionMeta) {
        int curVersion = this.getCurVersion();
        if (versionMeta.getVersion() > curVersion + 1) {
            throw new IllegalStateException("version must be sequential and begin at 1, expect : " + curVersion + 1
                    + " , but got : " + versionMeta.getVersion());
        }
        String key = META_KEY_PREFIX + versionMeta.getVersion();
        Pipeline p = jedis.pipelined();
        p.hset(key, "version", String.valueOf(versionMeta.getVersion()));
        p.hset(key, "script", versionMeta.getScript());
        p.hset(key, "desc", versionMeta.getDescription());
        p.hset(key, "hash", String.valueOf(versionMeta.getHash()));
        p.hset(key, "on", String.valueOf(versionMeta.getExecuteOn().getTime()));
        p.hset(key, "mills", String.valueOf(versionMeta.getExecuteMills()));
        p.hset(key, "succ", String.valueOf(versionMeta.getSuccess()));
        p.set(META_VERSION_KEY, String.valueOf(versionMeta.getVersion()));
        p.sync();
    }

    private int getCurVersion() {
        String curVersionStr = jedis.get(META_VERSION_KEY);
        return curVersionStr == null ? 0 : Integer.parseInt(curVersionStr);
    }

}
