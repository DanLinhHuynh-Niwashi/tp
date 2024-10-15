package seedu.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
* The {@code Command} class is an abstract class for all commands.
* It defines the structure for executing commands and provides common functionality such as
* set arguments
*/
public abstract class Command {
    public static final String COMMAND_WORD = ""; // The word associated with the command
    public static final String COMMAND_GUIDE = ""; // A guide or description of the command
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {}; // Keywords for arguments
    public static final String[] COMMAND_EXTRA_KEYWORDS = {}; // Keywords for arguments

    public static final String LACK_ARGUMENTS_ERROR_MESSAGE = "Lack mandatory arguments.";

    protected Map<String, String> arguments = new HashMap<>(); // A map to hold command arguments

    // Abstract methods for subclasses to execute the command
    public abstract List<String> execute();

    protected boolean isArgumentsValid () {
        for (String key: getMandatoryKeywords()) {
            if (!arguments.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the arguments for the command.
     *
     * @param arguments A map of arguments where the key is the argument name
     *                  and the value is the argument value.
     */
    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    /**
     * Gets all argument keys (mandatory and extra) of the command.
     *
     * @return An array of strings containing all argument keys,
     *         including mandatory and extra keywords.
     */
    public String[] getArgumentKeys() {
        return Stream.concat(Arrays.stream(getMandatoryKeywords()),
                        Arrays.stream(getExtraKeywords()))
                .toArray(String[]::new);
    }

    // Abstract methods for subclasses to provide their keywords
    protected abstract String[] getMandatoryKeywords();
    protected abstract String[] getExtraKeywords();
    protected abstract String getCommandWord();
    protected abstract String getCommandGuide();
}
