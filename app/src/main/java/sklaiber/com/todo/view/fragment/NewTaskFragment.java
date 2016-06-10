package sklaiber.com.todo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private Button mTaskSave;

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
    public void displayName(String name) {
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
        Toast.makeText(getActivity(), R.string.task_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getName() {
        return mTaskName.getText().toString();
    }

    @Override
    public String getDescription() {
        return mTaskDescription.getText().toString();
    }

    @Override
    public void showTaskNameIsRequired() {
        Toast.makeText(getActivity(), R.string.task_name_required_message, Toast.LENGTH_SHORT).show();
    }
}
