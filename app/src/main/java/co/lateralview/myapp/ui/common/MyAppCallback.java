package co.lateralview.myapp.ui.common;

public abstract class MyAppCallback<R, E>
{
	public void onStart()
	{
	}

	public void onFinish()
	{
	}

	public abstract void onSuccess(R response);

	public abstract void onError(E error);
}
