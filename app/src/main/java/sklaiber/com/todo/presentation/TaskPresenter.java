package sklaiber.com.todo.presentation;

import sklaiber.com.todo.view.TaskView;

public interface TaskPresenter {
    void loadTaskDetails();
    void setView(TaskView view);
    void saveTask();
}
