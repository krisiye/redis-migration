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

package org.beyondpn.redis.migration.option;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

import java.io.File;

/**
 * Created by yangjianhua on 2015/4/13.
 */
@Parameters(separators = "=")
public class Options {

    @Parameter(names = "--configFile", converter = FileConverter.class)
    private File configFile = new File(getClass().getClassLoader().getResource("conf/redis-migration.properties").getFile());

    @Parameter(names = "--help", help = true)
    private boolean help;

    public File getConfigFile() {
        return configFile;
    }

    public boolean isHelp() {
        return help;
    }
}
