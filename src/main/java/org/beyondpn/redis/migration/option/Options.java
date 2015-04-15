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

    @Parameter(names = "--configFile", converter = FileConverter.class, required = true)
    private File configFile;

    @Parameter(names = "--help", help = true)
    private boolean help;

    public File getConfigFile() {
        return configFile;
    }

    public boolean isHelp() {
        return help;
    }
}
