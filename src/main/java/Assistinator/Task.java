package Assistinator;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsUndone() {
        isDone = false;
    }

    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }

    public abstract String toFileString();

    /**
     * Checks if task description contains keyword
     * @param keyword search keyword
     * @return whether description contains keyword
     */
    public boolean contains(String keyword){
        return this.description.contains(keyword);
    }
}
