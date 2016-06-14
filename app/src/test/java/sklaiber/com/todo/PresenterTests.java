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

    private TaskRepository mockTaskRepository;
    private TaskView mockView;
    private TaskPresenter presenter;
    private Task task;

    @Before
    public void setup() {
        mockTaskRepository = mock(TaskRepository.class);

        task = new Task();
        task.setId(1);
        task.setTitle("Title");
        task.setDescription("Description");
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
        verify(mockView, times(1)).displayTitle("Title");
        verify(mockView, times(1)).displayDescription("Description");
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
        verify(mockView, never()).displayTitle(anyString());
        verify(mockView, never()).displayDescription(anyString());
    }

    @Test
    public void shouldShouldErrorMessageDuringSaveIfNameIsMissing() {
        when(mockView.getTaskId()).thenReturn(1);

        // Load the task
        presenter.setView(mockView);

        verify(mockView, times(1)).getTaskId();

        // Set up the view mock
        when(mockView.getTitle()).thenReturn(""); // empty string

        presenter.saveTask();

        verify(mockView, times(1)).getTitle();
        verify(mockView, never()).getDescription();
        verify(mockView, times(1)).showTaskTitleIsRequired();

        // Now tell mockView to return a value for name and an empty description
        when(mockView.getTitle()).thenReturn("Foo");
        when(mockView.getDescription()).thenReturn("");

        presenter.saveTask();

        verify(mockView, times(2)).getTitle(); // Called two times now, once before, and once now
        verify(mockView, times(1)).getDescription();  // Only called once
        verify(mockView, times(2)).showTaskTitleIsRequired(); // Called two times now, once before and once now
    }

    @Test
    public void shouldBeAbleToSaveAValidTask() {
        when(mockView.getTaskId()).thenReturn(1);

        // Load the task
        presenter.setView(mockView);

        verify(mockView, times(1)).getTaskId();

        when(mockView.getTitle()).thenReturn("Foo");
        when(mockView.getDescription()).thenReturn("Bar");

        presenter.saveTask();

        // Called two more times in the saveTask call.
        verify(mockView, times(2)).getTitle();
        verify(mockView, times(2)).getDescription();

        assertThat(task.getTitle(), is("Foo"));
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
        verify(mockView, times(1)).displayTitle(anyString());
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
