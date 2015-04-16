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
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.beyondpn.redis.migration.meta.MetaService;
import org.beyondpn.redis.migration.meta.VersionMeta;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;

/**
 * Created by yangjianhua on 2015/4/13.
 */
@Parameters(commandNames = "migrate", separators = "=", commandDescription = "clean all keys from redis")
public class CommandMigrate extends Command {

    private static final String PREFIX = "V";
    private static final String SEPARATOR = "__";

    @Override
    protected void doExecute(Jedis jedis) {

        File groovyDir = new File(super.getProperty("groovy.dir"));
        if (groovyDir.exists()) {
            File[] scripts = groovyDir.listFiles(pathname ->
                    pathname.isFile() && pathname.getName().startsWith(PREFIX)
                            && (pathname.getName().endsWith(".groovy") || pathname.getName().endsWith(".gvy")));
            Arrays.sort(scripts, (o1, o2) -> o1.getName().compareTo(o2.getName()));

            try {
                GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine(groovyDir.getAbsolutePath());
                Binding binding = new Binding();
                binding.setProperty("jedis", jedis);

                MetaService metaService = new MetaService(jedis);
                List<VersionMeta> metaList = metaService.selectVersionMeta();

                for (File script : scripts) {
                    String name = script.getName();
                    int version = Integer.parseInt(name.substring(1, name.indexOf(SEPARATOR)));
                    long hash = this.calcHash(script);
                    if (metaList.size() >= version) {
                        //check file hash
                        VersionMeta meta = metaList.get(version - 1);
                        if (meta.getSuccess() == 1 && meta.getHash() != hash) {
                            throw new IllegalStateException("file change : " + script.getName());
                        }
                        continue;
                    }

                    VersionMeta versionMeta = new VersionMeta();
                    versionMeta.setVersion(version);
                    versionMeta.setScript(name);
                    versionMeta.setDescription(name.substring(name.indexOf("__") + 2));
                    versionMeta.setExecuteOn(new Date());
                    versionMeta.setExecuteMills(0);
                    versionMeta.setSuccess(0);
                    versionMeta.setHash(hash);
                    metaService.setVersionMeta(versionMeta);

                    groovyScriptEngine.run(name, binding);

                    versionMeta.setExecuteMills((int) (System.currentTimeMillis() - versionMeta.getExecuteOn().getTime()));
                    versionMeta.setSuccess(1);
                    metaService.setVersionMeta(versionMeta);
                }
            } catch (IOException | ResourceException | ScriptException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private long calcHash(File script) {
        try {
            FileChannel fileChannel = new RandomAccessFile(script, "r").getChannel();
            if (fileChannel.size() > Integer.MAX_VALUE) {
                throw new RuntimeException("too big script file : " + script.getAbsolutePath());
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            byteBuffer.clear();
            fileChannel.read(byteBuffer);
            fileChannel.close();
            CRC32 crc32 = new CRC32();
            byteBuffer.flip();
            crc32.update(byteBuffer);
            return crc32.getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
