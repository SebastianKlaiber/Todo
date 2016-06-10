package sklaiber.com.todo;

import org.junit.Before;
import org.junit.Test;

import sklaiber.com.todo.model.Task;
import sklaiber.com.todo.presentation.TaskPresenter;
import sklaiber.com.todo.presentation.TaskPresenterImpl;
import sklaiber.com.todo.presentation.ViewNotFoundException;
import sklaiber.com.todo.repository.TaskRepository;
import sklaiber.com.todo.view.TaskView;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class PresenterTests {

    TaskRepository mockTaskRepository;
    TaskView mockView;
    TaskPresenter presenter;
    Task task;

    @Before
    public void setup() {
        mockTaskRepository = mock(TaskRepository.class);

        task = new Task();
        task.setId(1);
        task.setName("Mighty");
        task.setDescription("Mouse");
        when(mockTaskRepository.getTask(anyInt())).thenReturn(task);

        mockView = mock(TaskView.class);

        presenter = new TaskPresenterImpl(mockTaskRepository);
    }

    @Test
    public void noInteractionsWithViewShouldTakePlaceIfTaskIsNull() {
        presenter.saveTask();

        // task object is not initialized, lets verify no interactions take place
        verifyZeroInteractions(mockView);
    }

    @Test
    public void shouldBeABleToLoadTheTaskFromTheRepositoryWhenValidTaskIsPresent() {
        when(mockView.getTaskId()).thenReturn(1);

        presenter.setView(mockView );

        // Verify repository interactions
        verify(mockTaskRepository, times(1)).getTask(anyInt());

        // Verify view interactions
        verify(mockView, times(1)).getTaskId();
        verify(mockView, times(1)).displayName("Mighty");
        verify(mockView, times(1)).displayDescription("Mouse");
        verify(mockView, never()).showTaskNotFoundMessage();
    }

    @Test
    public void shouldShowErrorMessageOnViewWhenTaskIsNotPresent() {
        when(mockView.getTaskId()).thenReturn(1);

        // Return null when we ask the repo for a task.
        when(mockTaskRepository.getTask(anyInt())).thenReturn(null);

        presenter.setView(mockView);

        // Verify repository interactions
        verify(mockTaskRepository, times(1)).getTask(anyInt());

        // verify view interactions
        verify(mockView, times(1)).getTaskId();
        verify(mockView, times(1)).showTaskNotFoundMessage();
        verify(mockView, never()).displayName(anyString());
        verify(mockView, never()).displayDescription(anyString());
    }

    @Test
    public void shouldShouldErrorMessageDuringSaveIfNameIsMissing() {
        when(mockView.getTaskId()).thenReturn(1);

        // Load the task
        presenter.setView(mockView);

        verify(mockView, times(1)).getTaskId();

        // Set up the view mock
        when(mockView.getName()).thenReturn(""); // empty string

        presenter.saveTask();

        verify(mockView, times(1)).getName();
        verify(mockView, never()).getDescription();
        verify(mockView, times(1)).showTaskNameIsRequired();

        // Now tell mockView to return a value for first name and an empty last name
        when(mockView.getName()).thenReturn("Foo");
        when(mockView.getDescription()).thenReturn("");

        presenter.saveTask();

        verify(mockView, times(2)).getName(); // Called two times now, once before, and once now
        verify(mockView, times(1)).getDescription();  // Only called once
        verify(mockView, times(2)).showTaskNameIsRequired(); // Called two times now, once before and once now
    }

    @Test
    public void shouldBeAbleToSaveAValidTask() {
        when(mockView.getTaskId()).thenReturn(1);

        // Load the task
        presenter.setView(mockView);

        verify(mockView, times(1)).getTaskId();

        when(mockView.getName()).thenReturn("Foo");
        when(mockView.getDescription()).thenReturn("Bar");

        presenter.saveTask();

        // Called two more times in the saveUser call.
        verify(mockView, times(2)).getName();
        verify(mockView, times(2)).getDescription();

        assertThat(task.getName(), is("Foo"));
        assertThat(task.getDescription(), is("Bar"));

        // Make sure the repository saved the task
        verify(mockTaskRepository, times(1)).save(task);

        // Make sure that the view showed the task saved message
        verify(mockView, times(1)).showTaskSavedMessage();
    }

    @Test
    public void shouldLoadTaskDetailsWhenTheViewIsSet() {
        presenter.setView(mockView);
        verify(mockTaskRepository, times(1)).getTask(anyInt());
        verify(mockView, times(1)).displayName(anyString());
        verify(mockView, times(1)).displayDescription(anyString());
    }

    @Test(expected = ViewNotFoundException.class)
    public void shouldThrowViewNotFoundExceptionWhenViewIsNull() {
        // Null out the view
        presenter.setView(null);

        // Try to load the screen which will force interactions with the view
        presenter.loadTaskDetails();
    }
}
