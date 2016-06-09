package sklaiber.com.todo.presentation;

import sklaiber.com.todo.model.Task;
import sklaiber.com.todo.repository.TaskRepository;
import sklaiber.com.todo.view.TaskView;

public class TaskPresenterImpl implements TaskPresenter {

    private TaskView view;
    private TaskRepository taskRepository;
    private Task t;

    public TaskPresenterImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void loadTaskDetails() {
        int taskId = view.getTaskId();
        t = taskRepository.getTask(taskId);

        if (t == null) {
            view.showTaskNotFoundMessage();
        } else {
            view.displayName(t.getName());
            view.displayDescription(t.getDescription());
        }
    }

    @Override
    public void setView(TaskView view) {
        this.view = view;
        loadTaskDetails();
    }

    @Override
    public void saveTask() {
        if (t != null) {
            if (view.getName().trim().equals("") || view.getDescription().trim().equals("")) {
                view.showTaskNameIsRequired();
            } else {
                t.setName(view.getName());
                t.setDescription(view.getDescription());
                taskRepository.save(t);
                view.showTaskSavedMessage();
            }
        }
    }
}
