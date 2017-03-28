package co.lateralview.myapp.ui.presenter;

public class BasePresenter<T>
{
	protected T mView;

	public void attachView(T view)
	{
		this.mView = view;
	}

	public void detachView()
	{
		if (mView != null)
		{
			mView = null;
		}
	}
	public boolean isViewAttached()
	{
		return mView != null;
	}
}
