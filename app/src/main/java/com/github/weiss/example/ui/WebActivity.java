package com.github.weiss.example.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.github.weiss.core.utils.ClipboardUtils;
import com.github.weiss.example.BaseActivity;
import com.github.weiss.example.R;
import com.github.weiss.example.util.JsHandler;
import com.github.weiss.example.util.SnackbarUtil;
import com.github.weiss.example.view.webview.CommonWebChromeClient;
import com.github.weiss.example.view.webview.CommonWebView;
import com.github.weiss.example.view.webview.CommonWebViewClient;

import butterknife.BindView;

/**
 * gank.IO详情web页面
 */
public class WebActivity extends BaseActivity {

//    @Bind(R.id.circle_progress)
//    CircleProgressView mCircleProgressView;

    @BindView(R.id.progress_bar)
    ProgressBar mBar;

    @BindView(R.id.web_view)
    CommonWebView mCommonWebView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private static final String KEY_URL = "key_url";

    private static final String KEY_TITLE = "key_title";

    private String url, title;


    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

/*
    @Override
    public void initPresenter() {

    }*/

    @Override
    public void initView() {

        Intent intent = getIntent();
        if (intent != null)
            parseIntent(intent);

        initWebSetting();
        initToolBar();
//        hideProgress();
        mCommonWebView.setWebChromeClient(new CommonWebChromeClient(mBar, mBar));
        mCommonWebView.setWebViewClient(new CommonWebViewClient(WebActivity.this));
        mCommonWebView.loadUrl(url);
    }


    public void initToolBar() {

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                share();
                return true;

            case R.id.action_copy:
                ClipboardUtils.copyText(url);
                SnackbarUtil.showMessage(mCommonWebView, "已复制到剪贴板");
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        parseIntent(intent);
    }

    private void parseIntent(Intent intent) {

        url = intent.getStringExtra(KEY_URL);
        title = intent.getStringExtra(KEY_TITLE);

        if (TextUtils.isEmpty(url)) {
            finish();
        }
    }

    private void initWebSetting() {

        JsHandler jsHandler = new JsHandler(this, mCommonWebView);
        mCommonWebView.addJavascriptInterface(jsHandler, "JsHandler");
    }

    public static boolean start(Activity activity, String url, String title) {

        Intent intent = new Intent();
        intent.setClass(activity, WebActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_TITLE, title);
        activity.startActivity(intent);

        return true;
    }

    public static boolean start(Activity activity, String url) {

        return start(activity, url, null);
    }


/*    public void hideProgress()
    {

        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();
    }*/


    private void share() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自「Gank.IO」的分享:" + url);
        startActivity(Intent.createChooser(intent, title));
    }
}
