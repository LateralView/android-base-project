package co.lateralview.myapp.ui.util;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.lateralview.myapp.R;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public class ToolbarUtils
{
	public static void setupActionBar(final Activity activity, boolean backEnabled, String title)
	{
		setupActionBar(activity, backEnabled, title, R.drawable.ic_back_black);
	}

	public static void setupActionBar(final Activity activity, boolean backEnabled, String title, int backIcon)
	{
		Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
		if (toolbar != null)
		{
			if (backEnabled)
			{
				addLeftIcon(activity, backIcon, new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						onHomePressed(activity);
					}
				});
			}

			if (title != null && !title.isEmpty())
			{
				TextView toolBarTitle = (TextView) activity.findViewById(R.id.toolBar_titleTextView);
				toolBarTitle.setText(title);
				toolBarTitle.setVisibility(View.VISIBLE);
			}

			((AppCompatActivity) activity).setSupportActionBar(toolbar);
		}
	}

	private static void onHomePressed(Activity activity)
	{
		activity.onBackPressed();
	}

	public static void setActionBarVisibility(Activity activity, int visibility)
	{
		Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

		if (toolbar != null)
		{
			toolbar.setVisibility(visibility);
		}
	}

	public static void addLeftIcon(Activity activity, int icon, View.OnClickListener iconListener)
	{
		Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
		if (toolbar != null)
		{
			LinearLayout leftIconsLinearLayout = (LinearLayout) activity.findViewById(R.id.toolBar_leftIconsLinearLayout);

			if (leftIconsLinearLayout != null)
			{
				ImageView newIconImageView = new ImageView(activity);

				newIconImageView.setImageResource(icon);

				int padding = UIUtils.convertDipToPixels(activity, 10);
				newIconImageView.setPadding(padding, padding, padding, padding);

				newIconImageView.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));

				newIconImageView.setOnClickListener(iconListener);
				newIconImageView.setBackground(UIUtils.getDrawable(activity, R.drawable.selector_transparent));

				leftIconsLinearLayout.addView(newIconImageView);
			}
		}
	}

	public static void addRightIcon(Activity activity, int icon, View.OnClickListener iconListener)
	{
		Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
		if (toolbar != null)
		{
			LinearLayout rightIconsLinearLayout = (LinearLayout) activity.findViewById(R.id.toolBar_rightIconsLinearLayout);

			if (rightIconsLinearLayout != null)
			{
				ImageView newIconImageView = new ImageView(activity);

				newIconImageView.setImageResource(icon);

				int padding = UIUtils.convertDipToPixels(activity, 10);
				newIconImageView.setPadding(padding, padding, padding, padding);

				newIconImageView.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));

				newIconImageView.setOnClickListener(iconListener);
				newIconImageView.setBackground(UIUtils.getDrawable(activity, R.drawable.selector_transparent));

				rightIconsLinearLayout.addView(newIconImageView);
			}
		}
	}

	public static void addRightLayout(Activity activity, int layout, View.OnClickListener iconListener)
	{
		Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
		if (toolbar != null)
		{
			LinearLayout rightIconsLinearLayout = (LinearLayout) activity.findViewById(R.id.toolBar_rightIconsLinearLayout);

			View view = activity.getLayoutInflater().inflate(layout, rightIconsLinearLayout, false);

			if (rightIconsLinearLayout != null)
			{
				view.setOnClickListener(iconListener);
				rightIconsLinearLayout.addView(view);
			}
		}
	}
}
