package sklaiber.com.todo;

import javax.inject.Singleton;

import dagger.Component;
import sklaiber.com.todo.view.fragment.TaskFragment;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {
    void inject(TaskFragment target);
}
