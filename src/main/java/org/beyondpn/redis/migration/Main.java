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
