package sklaiber.com.todo.view;

public interface TaskView {
    int getTaskId();

    void displayTitle(String name);
    void displayDescription(String description);

    void showTaskNotFoundMessage();
    void showTaskSavedMessage();

    String getTitle();
    String getDescription();

    void showTaskTitleIsRequired();
}
