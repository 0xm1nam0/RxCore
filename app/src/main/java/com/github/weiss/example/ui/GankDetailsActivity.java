package com.github.weiss.example.ui;

import android.app.Activity;
import android.content.Intent;

import androidx.core.view.ViewCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.weiss.core.utils.ImageLoaderUtil;
import com.github.weiss.example.BaseActivity;
import com.github.weiss.example.R;
import com.github.weiss.example.util.JsHandler;
import com.github.weiss.example.util.SnackbarUtil;
import com.github.weiss.example.view.webview.CommonWebChromeClient;
import com.github.weiss.example.view.webview.CommonWebView;
import com.github.weiss.example.view.webview.CommonWebViewClient;
import com.minamo.utils.ClipboardUtils;

import butterknife.BindView;

/**
 * gank.IO详情web页面
 * <p/>
 * Tips:做这个界面遇到个问题,设置界面共享元素动画时候
 * Actiivty的跳转,退出界面崩溃,原因是WebView所在父View
 * NestedScrollView没有设置android:transitionGroup="true"属性
 * <p/>
 * StackOverFlow的答案:
 * Switching the ScrollView's transitionGroup from false (the default value)
 * to true makes it work because then the ScrollView is being faded in.
 * The ScrollView has a maximum size, while its contents can be enormous.
 */
public class GankDetailsActivity extends BaseActivity {

/*    @Bind(R.id.circle_progress)
    CircleProgressView mCircleProgressView;*/

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.web_view)
    CommonWebView mCommonWebView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.detail_image)
    ImageView mImageView;

    @BindView(R.id.detail_title)
    TextView mTitle;

    @BindView(R.id.detail_source)
    TextView mSource;

    private static final String KEY_URL = "key_url";

    private static final String KEY_TITLE = "key_title";

    private static final String KEY_IMG = "key_img";

    private static final String KEY_USER = "key_user";

    public static final String TRANSIT_PIC = "picture";

    private String url, title;

    private String imgUrl;

    private String user;


    @Override
    public int getLayoutId() {

        return R.layout.activity_gank_details;
    }

/*    @Override
    public void initPresenter() {

    }*/

    @Override
    public void initView() {

        ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);
        Intent intent = getIntent();
        if (intent != null)
            parseIntent(intent);

        initWebSetting();
        initToolBar();

        mProgressBar.setVisibility(View.GONE);
        mCommonWebView.setWebChromeClient(new CommonWebChromeClient(mProgressBar, mProgressBar));
        mCommonWebView.setWebViewClient(new CommonWebViewClient(GankDetailsActivity.this));
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
                MainActivityFragment.startContainerActivity(this,"Android");
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
        imgUrl = intent.getStringExtra(KEY_IMG);
        user = intent.getStringExtra(KEY_USER);
//        LogUtils.d(imgUrl);
        ImageLoaderUtil.loadGifImg(mImageView, imgUrl);
/*        Glide.with(GankDetailsActivity.this)
                .load(imgUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);*/

        mTitle.setText(title);
        mSource.setText("by: " + user);

        if (TextUtils.isEmpty(url)) {
            finish();
        }
    }

    private void initWebSetting() {

        JsHandler jsHandler = new JsHandler(this, mCommonWebView);
        mCommonWebView.addJavascriptInterface(jsHandler, "JsHandler");
    }

    public static Intent start(Activity activity, String url, String title, String imgUrl, String user) {

        Intent intent = new Intent();
        intent.setClass(activity, GankDetailsActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_IMG, imgUrl);
        intent.putExtra(KEY_USER, user);
        // activity.startActivity(intent);

        return intent;
    }

    public static boolean start(Activity activity, String url) {

        Intent intent = new Intent();
        intent.setClass(activity, GankDetailsActivity.class);
        intent.putExtra(KEY_URL, url);
        activity.startActivity(intent);

        return true;
    }


    public void hideProgress() {

/*        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();*/
    }


    private void share() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自「Gank.IO」的分享:" + url);
        startActivity(Intent.createChooser(intent, title));
    }
}
