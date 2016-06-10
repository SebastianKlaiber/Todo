package sklaiber.com.todo.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import sklaiber.com.todo.R;
import sklaiber.com.todo.view.fragment.NewTaskFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigateToMainFragment();
    }

    private void navigateToMainFragment() {
        NewTaskFragment f = NewTaskFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, f, NewTaskFragment.class.getSimpleName())
                .commit();
    }
}
