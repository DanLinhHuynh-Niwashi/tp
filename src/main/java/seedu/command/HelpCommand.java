package seedu.command;


import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends Command{
    public static final String COMMAND_WORD = "help";
    public static final String COMMAND_GUIDE = "help: Print this guide.";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {};

    private List<Command> commands;
    /**
     * Executes the help command and returns a list of command guide messages.
     *
     * @return A list of strings containing the messages generated by the command execution.
     */
    @Override
    public List<String> execute() {
        List<String> messages = new ArrayList<>();

        for (Command command: commands) {
            messages.add(command.getCommandGuide());
        }

        return messages;
    }

    /**
     * Sets the list of commands for the help guide.
     *
     * @param commands List of available commands.
     */
    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    /**
     * Gets the mandatory keywords for the command.
     *
     * @return An array of strings containing the mandatory keywords associated with this command.
     */
    @Override
    protected String[] getMandatoryKeywords() {
        return COMMAND_MANDATORY_KEYWORDS;
    }

    /**
     * Gets the extra keywords for the command.
     *
     * @return An array of strings containing the extra keywords associated with this command.
     */
    @Override
    protected String[] getExtraKeywords() {
        return COMMAND_EXTRA_KEYWORDS;
    }

    /**
     * Gets the word for the command.
     *
     * @return A string representing the command word.
     */
    @Override
    protected String getCommandWord() {
        return COMMAND_WORD;
    }

    /**
     * Gets the guide for the command.
     *
     * @return A string representing the command guide.
     */
    @Override
    protected String getCommandGuide() {
        return COMMAND_GUIDE;
    }
}
