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

package org.beyondpn.redis.migration;

import com.beust.jcommander.JCommander;
import org.beyondpn.redis.migration.command.Command;
import org.beyondpn.redis.migration.command.CommandClean;
import org.beyondpn.redis.migration.command.CommandInfo;
import org.beyondpn.redis.migration.command.CommandMigrate;
import org.beyondpn.redis.migration.option.Options;

/**
 * Created by yangjianhua on 2015/4/13.
 */
public class Main {

    public static void main(String[] args) {
        Options options = new Options();
        JCommander jc = new JCommander(options);

        CommandClean clean = new CommandClean();
        jc.addCommand(clean);
        CommandMigrate migrate = new CommandMigrate();
        jc.addCommand(migrate);
        CommandInfo info = new CommandInfo();
        jc.addCommand(info);

        jc.parse(args);

        if (options.isHelp()) {
            jc.usage();
        } else {
            JCommander cmd = jc.getCommands().get(jc.getParsedCommand());
            Command command = (Command) cmd.getObjects().get(0);
            command.setOptions(options);
            command.execute();
        }
    }

}
