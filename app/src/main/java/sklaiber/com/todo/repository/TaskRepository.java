package sklaiber.com.todo.repository;

import sklaiber.com.todo.model.Task;

public interface TaskRepository {
    Task getTask(int id);
    void save(Task t);
}
