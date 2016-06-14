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
        if (view == null) {
            throw new ViewNotFoundException();
        }

        int taskId = view.getTaskId();
        t = taskRepository.getTask(taskId);

        if (t == null) {
            view.showTaskNotFoundMessage();
        } else {
            view.displayTitle(t.getTitle());
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
            if (view.getTitle().trim().equals("") || view.getDescription().trim().equals("")) {
                view.showTaskTitleIsRequired();
            } else {
                t.setTitle(view.getTitle());
                t.setDescription(view.getDescription());
                taskRepository.save(t);
                view.showTaskSavedMessage();
            }
        }
    }
}
