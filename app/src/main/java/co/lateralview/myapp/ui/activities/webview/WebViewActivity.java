package co.lateralview.myapp.ui.activities.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lateralview.myapp.R;
import co.lateralview.myapp.ui.activities.base.BaseActivity;

public class WebViewActivity extends BaseActivity {
    public static final String TAG = "WebViewActivity";

    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_ACTION_BAR_TITLE = "EXTRA_ACTION_BAR_TITLE";
    private static final String EXTRA_BACK_ENABLED = "EXTRA_BACK_ENABLED";

    @BindView(R.id.webViewActivity_webview)
    protected WebView mWebView;
    @BindView(R.id.webViewActivity_progressBar)
    protected ProgressBar mProgressBar;
    private String mUrl;
    private String mActionBarTitle;
    private boolean mBackEnabled = false;

    public static Intent newInstance(Context fromActivity, boolean clearStack, String url) {
        Intent intent = BaseActivity.newActivityInstance(fromActivity, clearStack,
            WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);

        return intent;
    }

    public static Intent newInstance(Context fromActivity, boolean clearStack, String url,
                                     String actionBarTitle, boolean backEnabled) {
        Intent intent = newInstance(fromActivity, clearStack, url);
        intent.putExtra(EXTRA_ACTION_BAR_TITLE, actionBarTitle);
        intent.putExtra(EXTRA_BACK_ENABLED, backEnabled);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);

        getExtras();

        if (mActionBarTitle != null && !mActionBarTitle.isEmpty()) {
            initializeToolbar(mBackEnabled, mActionBarTitle);
        } else {
            setActionBarVisibility(View.GONE);
        }

        initializeWebView();

        startWebView();
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUrl = extras.getString(EXTRA_URL);
            mActionBarTitle = extras.getString(EXTRA_ACTION_BAR_TITLE);
            mBackEnabled = extras.getBoolean(EXTRA_BACK_ENABLED, false);
        }
    }

    private void initializeWebView() {
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void startWebView() {
        if (mUrl != null && !mUrl.isEmpty()) {
            mWebView.loadUrl(mUrl);
        }
    }
}
