package co.lateralview.myapp.ui.util;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import co.lateralview.myapp.R;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public class ToolbarUtils
{
	public static void initializeToolbar(AppCompatActivity activity, boolean backEnabled, @Nullable String title)
	{
		Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

		if (toolbar != null)
		{
			activity.setSupportActionBar(toolbar);

			setToolbarTitle(activity, title != null && !title.isEmpty() ? title : "");

			activity.getSupportActionBar().setDisplayHomeAsUpEnabled(backEnabled);
			activity.getSupportActionBar().setHomeButtonEnabled(backEnabled);
		}
	}

	public static void setActionBarVisibility(Activity activity, int visibility)
	{
		Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

		if (toolbar != null)
		{
			toolbar.setVisibility(visibility);
		}
	}

	public static void setToolbarTitle(AppCompatActivity activity, String title)
	{
		if (activity.getSupportActionBar() != null)
		{
			activity.getSupportActionBar().setTitle(title);
		}
	}

}
