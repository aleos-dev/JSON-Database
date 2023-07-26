package server;

import com.beust.jcommander.Parameter;

public class CommandLineArgs {
    @SuppressWarnings("unused")
    @Parameter(names = {"-t", "type"}, description = "Type of operation parameter")
    private String type;

    @SuppressWarnings("unused")
    @Parameter(names = {"-k", "key"}, description = "Key parameter")
    private String key;

    @SuppressWarnings("unused")
    @Parameter(names = {"-v", "value"}, description = "Value parameter")
    private String value;

    @SuppressWarnings("unused")
    @Parameter(names = {"-in"}, description = "Input file name parameter")
    private String fileName;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return fileName;
    }
}
