package co.lateralview.myapp.infraestructure.manager.implementation;

import java.util.ArrayList;
import java.util.List;

import co.lateralview.myapp.infraestructure.manager.interfaces.TaskManager;

public class TaskManagerImpl implements TaskManager
{
	private List<PendingTask> mPendingTasksQueue = new ArrayList<>();

	public void callPendingTasks()
	{
		if (!mPendingTasksQueue.isEmpty())
		{
			for (PendingTask pendingTask : mPendingTasksQueue)
			{
				pendingTask.callPendingTask();
			}

			clearQueue();
		}
	}

	public void addTask(PendingTask task)
	{
		if (!mPendingTasksQueue.contains(task))
		{
			mPendingTasksQueue.add(task);
		}
	}

	public void removeTasks(String tag)
	{
		List<PendingTask> pendingTasks = new ArrayList<>();

		for (PendingTask pendingTask : mPendingTasksQueue)
		{
			if (!pendingTask.getTag().equals(tag))
			{
				pendingTasks.add(pendingTask);
			}
		}

		mPendingTasksQueue.clear();
		mPendingTasksQueue.addAll(pendingTasks);
	}

	public void clearQueue()
	{
		mPendingTasksQueue.clear();
	}

}
