package co.lateralview.myapp.ui.presenter;

import co.lateralview.myapp.ui.activities.main.MainPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule
{
	@Provides
	public MainPresenter providesMainPresenter()
	{
		return new MainPresenter();
	}
}
