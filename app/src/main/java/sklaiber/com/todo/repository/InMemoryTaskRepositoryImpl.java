package sklaiber.com.todo.repository;

import sklaiber.com.todo.model.Task;

public class InMemoryTaskRepositoryImpl implements TaskRepository {

    private Task t;

    /**
     * Gets the task from memory.
     *
     * @param id The id of the task.
     * @return a {@link Task}
     */
    @Override
    public Task getTask(int id) {
        // Normally this would go to a DB/etc and fetch the task with an ID.
        // Here though, we're just doing something in memory, so we're kind of just
        // faking it out.
        if (t == null) {
            t = new Task();
            t.setId(id);
            t.setName("Name");
            t.setDescription("Description");
        }
        return t;
    }

    /**
     * Save's the in-memory task.
     *
     * @param t The task.
     */
    @Override
    public void save(Task t) {
        if (this.t == null) {
            this.t = getTask(0);
        }
        this.t.setId(t.getId());
        this.t.setName(t.getName());
        this.t.setDescription(t.getDescription());
    }
}
