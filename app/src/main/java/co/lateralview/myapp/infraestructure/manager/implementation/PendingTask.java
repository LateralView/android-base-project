package co.lateralview.myapp.infraestructure.manager.implementation;

public class PendingTask
{
	private String mTag;
	private ITasksListener mListener;

	public PendingTask(String mTag, ITasksListener mListener)
	{
		this.mTag = mTag;
		this.mListener = mListener;
	}

	public void callPendingTask()
	{
		mListener.callPendingTask();
	}

	public String getTag()
	{
		return mTag;
	}

	public interface ITasksListener
	{
		void callPendingTask();
	}
}