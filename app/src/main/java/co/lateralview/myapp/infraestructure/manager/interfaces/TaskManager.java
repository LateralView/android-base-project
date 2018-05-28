package co.lateralview.myapp.infraestructure.manager.interfaces;

import co.lateralview.myapp.infraestructure.manager.implementation.PendingTask;

public interface TaskManager {
    void callPendingTasks();

    void addTask(PendingTask task);

    void removeTasks(String tag);

    void clearQueue();
}
