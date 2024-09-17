package assistinator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * API of the storage class
 */
public class Storage {
    private String filePath;

    /**
     * Initialise a storage class
     * @param filePath File path to tasks
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves tasks to file in initialised file path
     * @param tasks Task list
     */
    public void saveTasks(ArrayList<Task> tasks) {
        try {
            String content = tasks.stream()
                    .map(Task::toFileString)
                    .collect(Collectors.joining(System.lineSeparator()));
            Files.writeString(Paths.get(filePath), content);
        } catch (IOException e) {
            System.out.println("An error occurred while saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads task list from file
     * @return task list
     * @throws AssitinatorException If file not in provided file path
     */
    public ArrayList<Task> loadTasks() throws AssitinatorException {
        try {
            return Files.lines(Paths.get(filePath))
                    .map(line -> {
                        String[] parts = line.split("\\|");
                        String type = parts[0].trim();
                        return getTask(parts, type);
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new AssitinatorException("File not found");
        }
    }

    /**
     * Converts lines in files to tasks
     * @param parts Task information
     * @param type Type of task
     * @return Task
     */
    public Task getTask(String[] parts, String type) {
        boolean isDone = TaskStatus.isDone(parts[1].trim());
        String description = parts[2].trim();

        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            String deadline = parts[3].trim();
            task = new Deadline(description, deadline);
            break;
        case "E":
            String start = parts[3].trim();
            String end = parts[4].substring(parts[4].indexOf(' ') + 1);
            task = new Event(
                    description,
                    start,
                    end
            );
            break;
        default:
            throw new IllegalArgumentException("Unknown task type: " + type);
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
