package sklaiber.com.todo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class ToDoApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        component = DaggerAppComponent.builder().appModule(new AppModule()).build();
    }

    public AppComponent getComponent() {
        return component;
    }
}
