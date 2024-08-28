package Assistinator;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class Storage {
    private final String FILE_PATH;

    public Storage(String filePath) {
        this.FILE_PATH = filePath;
    }

    public void saveTasks(ArrayList<Task> tasks) {
        try {
            FileWriter writer = new FileWriter(FILE_PATH);
            for (Task task : tasks) {
                writer.write(task.toFileString() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving tasks: " + e.getMessage());
        }
    }

    public ArrayList<Task> loadTasks() throws AssitinatorExceptions {
        try {
            ArrayList<Task> tasks = new ArrayList<>();
            File file = new File(FILE_PATH);
            Scanner s = new Scanner(file);
            while (s.hasNext()) {
                String[] parts = s.nextLine().split("\\|");
                String type = parts[0].trim();
                Task task = getTask(parts, type);
                tasks.add(task);
            }
            return tasks;
        } catch (FileNotFoundException e) {
            throw new AssitinatorExceptions("File not found");
        }
    }

    public Task getTask(String[] parts, String type) {
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                task = new Deadline(description, parts[3].trim());
                break;
            case "E":
                task = new Event(
                        description,
                        parts[3].trim(),
                        parts[4].substring(parts[4].indexOf(' ') + 1)
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
