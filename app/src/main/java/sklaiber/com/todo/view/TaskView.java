package sklaiber.com.todo.view;

public interface TaskView {
    int getTaskId();

    void displayName(String name);
    void displayDescription(String description);

    void showTaskNotFoundMessage();
    void showTaskSavedMessage();

    String getName();
    String getDescription();

    void showTaskNameIsRequired();
}
