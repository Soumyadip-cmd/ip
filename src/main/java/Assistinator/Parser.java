package Assistinator;

public class Parser {
    /**
     * Parses command from user input
     * @param input User input from UI
     * @return Command to be used by
     */
    public Command parseCommand(String input) {
        String commandType = input.split(" ")[0].toUpperCase();
        return Command.fromString(commandType);
    }

    /**
     * Parses the input string to extract and return an index.
     * This method expects the input to be in the format "command index",
     * where index is a positive integer.
     *
     * @param input The input string to parse.
     * @return The parsed index as an integer, adjusted to be zero-based (subtracts 1 from the parsed number).
     * @throws AssitinatorExceptions If the input format is invalid or the index cannot be parsed.
     */
    public int parseIndex(String input) throws AssitinatorExceptions {
        try {
            return Integer.parseInt(input.split(" ")[1]) - 1;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new AssitinatorExceptions("Please provide a valid task number");
        }
    }

    /**
     * Parses the input and creates a Task object based on the specified command type.
     *
     * @param type The type of command (TODO, DEADLINE, or EVENT).
     * @param input The input string containing the task details.
     * @return A Task object created based on the input and command type.
     * @throws AssitinatorExceptions If the task type is invalid or if there's an error in parsing the specific task type.
     */
    public Task parseTask(Command type, String input) throws AssitinatorExceptions {
        switch (type) {
            case TODO:
                return parseTodo(input);
            case DEADLINE:
                return parseDeadline(input);
            case EVENT:
                return parseEvent(input);
            default:
                throw new AssitinatorExceptions("Invalid task type");
        }
    }

    private Todo parseTodo(String input) throws AssitinatorExceptions {
        String description = input.substring(input.indexOf(' ') + 1);
        if (description.equalsIgnoreCase("todo")) {
            throw new AssitinatorExceptions("Please follow format: todo {task description}");
        }
        return new Todo(description);
    }

    private Deadline parseDeadline(String input) throws AssitinatorExceptions {
        String[] parts = input.split(" /");
        if (parts.length != 2 || parts[1].equals("by")) {
            throw new AssitinatorExceptions("Please follow format: deadline {task description} /by {deadline}");
        }
        String description = parts[0].substring(parts[0].indexOf(' ') + 1);
        String deadline = parts[1].substring(parts[1].indexOf(' ') + 1);
        return new Deadline(description, deadline);
    }

    private Event parseEvent(String input) throws AssitinatorExceptions {
        String[] parts = input.split(" /");
        if (parts.length != 3 || parts[1].equals("from") || parts[2].equals("to")) {
            throw new AssitinatorExceptions("Please follow format: event {task description} /from {start} /to {end}");
        }
        String description = parts[0].substring(parts[0].indexOf(' ') + 1);
        String start = parts[1].substring(parts[1].indexOf(' ') + 1);
        String end = parts[2].substring(parts[2].indexOf(' ') + 1);
        return new Event(description, start, end);
    }
}
