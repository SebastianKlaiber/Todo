package sklaiber.com.todo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import butterknife.ButterKnife;
import sklaiber.com.todo.R;
import sklaiber.com.todo.ToDoApplication;
import sklaiber.com.todo.presentation.TaskPresenter;
import sklaiber.com.todo.view.TaskView;

public class NewTaskFragment extends Fragment implements TaskView {

    @Inject TaskPresenter taskPresenter;

    private EditText mTaskName;
    private EditText mTaskDescription;
    FirebaseAnalytics mFirebaseAnalytics;

    private static final String TASK_ID = "task_id";

    public NewTaskFragment() {
    }

    public static NewTaskFragment newInstance() {
        return new NewTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ToDoApplication)getActivity().getApplication()).getComponent().inject(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_task, container, false);

        ButterKnife.bind(this, v);

        mTaskName = (EditText) v.findViewById(R.id.new_task_task_name);
        mTaskDescription = (EditText) v.findViewById(R.id.new_task_task_desc);

        v.findViewById(R.id.task_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskPresenter.saveTask();
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mTaskName.getText().toString());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        taskPresenter.setView(this);
    }

    @Override
    public int getTaskId() {
        return getArguments() == null ? 0 : getArguments().getInt(TASK_ID, 0);
    }

    @Override
    public void displayTitle(String name) {
        mTaskName.setText(name);
    }

    @Override
    public void displayDescription(String description) {
        mTaskDescription.setText(description);
    }

    @Override
    public void showTaskNotFoundMessage() {
        Toast.makeText(getActivity(), R.string.task_not_found, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showTaskSavedMessage() {
        Snackbar.make(getView(), R.string.task_saved, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public String getTitle() {
        return mTaskName.getText().toString();
    }

    @Override
    public String getDescription() {
        return mTaskDescription.getText().toString();
    }

    @Override
    public void showTaskTitleIsRequired() {
        Toast.makeText(getActivity(), R.string.task_name_required_message, Toast.LENGTH_SHORT).show();
    }
}
