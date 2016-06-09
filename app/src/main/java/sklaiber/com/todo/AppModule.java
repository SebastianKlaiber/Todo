package sklaiber.com.todo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sklaiber.com.todo.repository.InMemoryTaskRepositoryImpl;
import sklaiber.com.todo.repository.TaskRepository;

@Module
public class AppModule {
    @Provides @Singleton
    public TaskRepository provideTaskRepository() {
        return new InMemoryTaskRepositoryImpl();
    }
}
