package com.example.howell.clayexplorer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.InputStream;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG ="MainActivity" ;
    private WebView  webshow;
    private EditText et_searchNetName;
    private TextView tv_search;
    private TextView tv_refresh;
    private ImageView  iv_goto;
    private ImageView  iv_back;
    private ImageView  iv_historywebList;
    private ImageView  iv_home;
    private  ImageView iv_menu;
    private Toolbar  tb_util;
    private ProgressBar pb_web;
    private LinearLayout  ll_serachUrl;
    private WebSettings mySetting;
    private WebViewClient myClient;
    public static Context mContext;
    private GestureDetector mGesture;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        initView();
        init();
        //tb_util.inflateMenu(R.menu.main_util_toorbar_menu);
        setWebShow();
        Log.i(TAG,"hello");
       // setFloatingMenu();
        setAllListener();
        //设置进度条
        setWebViewProgress();


    }

    private void setWebViewProgress() {
        webshow.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress==100){
                    pb_web.setVisibility(View.INVISIBLE);
                }else {
                    if (View.INVISIBLE==pb_web.getVisibility()){
                        pb_web.setVisibility(View.VISIBLE);
                    }
                    pb_web.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

//    private void setFloatingMenu() {
//        //1 - Create a button to attach the menu:
//        ImageView imageView=new ImageView(this);
//        Resources r=getResources();
//        InputStream inputStream=r.openRawResource(R.mipmap.ic_launcher);
//        BitmapDrawable bmpDraw = new BitmapDrawable(inputStream);
//        imageView.setImageDrawable(bmpDraw);
//        FloatingActionButton actionButton=new FloatingActionButton.Builder(this).setContentView(imageView).build();
//        //2 - Create menu items:
//        Log.i(TAG,"hello dear");
//        SubActionButton.Builder itemBuilder=new SubActionButton.Builder(this);
//        ImageView   imageView1=new ImageView(this);
//        InputStream inputStream1=r.openRawResource(R.mipmap.heart);
//        BitmapDrawable bmpDraw1 = new BitmapDrawable(inputStream1);
//        imageView1.setImageDrawable(bmpDraw1);
//        SubActionButton  button1=itemBuilder.setContentView(imageView1).build();
//        //3 - Create the menu with the items:
//        FloatingActionMenu actionMenu=new FloatingActionMenu.Builder(this).addSubActionView(button1)
//                .attachTo(actionButton)
//                .build();
//    }

    //设置各个控件的监听
    private void setAllListener() {
        webshow.setOnTouchListener(new WebViewListener());
        tv_search.setOnClickListener(this);
        tv_refresh.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        iv_goto.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_historywebList.setOnClickListener(this);
        et_searchNetName.addTextChangedListener(new WebUrlNameChangedListener());
        et_searchNetName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER){
                   InputMethodManager im= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (im.isActive()){//隐藏键盘
                        im.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                    }
                    webshow.loadUrl(url);
                    return true;
                }
                return false;
            }
        });
    }

    private void setWebShow() {
        //设置web属性,能执行js脚本
        webshow.getSettings().setJavaScriptEnabled(true);
        //设置代码格式
        mySetting=this.webshow.getSettings();
        mySetting.setDefaultTextEncodingName("UTF-8");
        //设置默认显示网页
        webshow.loadUrl("http://www.baidu.com/");
        Log.i(TAG,"loadurl success");
        //设置web视图
        webshow.setWebViewClient(myClient);
        Log.i(TAG,"loadwevView success");
    }

    private void init() {
        mContext=this;
        url="";
        mGesture=new GestureDetector(mContext,new GestureListener());
        myClient=new MyWebViewClient();
    }

    //用WebView点链接看了很多页以后为了让WebView支持回退功能，需要覆盖覆盖Activity类的onKeyDown()方法，如果不做任何处理，点击系统回退剪键，整个浏览器会调用finish()而结束自身，而不是回退到上一页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode==KeyEvent.KEYCODE_BACK)&&(webshow.canGoBack())){
            webshow.goBack();//指返回页面的上一层
            return true;
        }
       return false;
    }

    private void initView() {
        webshow= (WebView) findViewById(R.id.web_webshow);
        et_searchNetName= (EditText) findViewById(R.id.et_searchNet);
        tv_search= (TextView) findViewById(R.id.tv_search );
        ll_serachUrl= (LinearLayout) findViewById(R.id.ll_searchUrl);
        tv_refresh= (TextView) findViewById(R.id.tv_refreshUrl);
        iv_back= (ImageView) findViewById(R.id.iv_pre);
        iv_goto= (ImageView) findViewById(R.id.iv_next);
        iv_historywebList= (ImageView) findViewById(R.id.iv_historyweblist);
        iv_home= (ImageView) findViewById(R.id.iv_home);
        iv_menu= (ImageView) findViewById(R.id.iv_menu);
        tb_util= (Toolbar) findViewById(R.id.tb_util);
        pb_web= (ProgressBar) findViewById(R.id.progress_web);

    }

    private void setView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
       //  url=et_searchNetName.getText().toString().trim();
        switch (v.getId()){
            //实现点击搜索功能
            case R.id.tv_search:
                Log.i(TAG, "url:"+url);
                if ((URLUtil.isNetworkUrl(url))&&(URLUtil.isValidUrl(url))){
                    Log.i(TAG,"loading.....");
                    webshow.loadUrl(url);
                }
//                else {
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setTitle("警告")
//                            .setMessage("不是有效的网址")
//                            .create()
//                            .show();
//                }
                break;
            case R.id.tv_refreshUrl:

               // if(!(url.equals("")&&url.equals("http://"))){
                    webshow.reload();
               // }
            case R.id.iv_historyweblist:
                startActivityForResult(new Intent(this,ScannerActivity.class),1);
                break;
            case R.id.iv_home:
                webshow.loadUrl("http://www.baidu.com/");
                break;
            case R.id.iv_next:
                webshow.goForward();
                break;
            case R.id.iv_pre:
                webshow.goBack();
                break;
            case R.id.iv_menu:
                Log.i(TAG,"util_show");
                startActivity(new Intent(MainActivity.this,SelectMenuItemPopUpWindow.class));
                break;

        }
    }


    //自设置web视图
    private class MyWebViewClient extends  WebViewClient{
        MyWebViewClient(){
            Log.i(TAG,"视图构造方法");
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.i(TAG,"overrider success");
            return true;
        }

        /**
         * 当找不到网页时,默认为百度搜索
         * 由于访问错误时被114网页占用,所以无法访问该方法
         * @param view
         * @param errorCode
         * @param description
         * @param failingUrl
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i(TAG,"description"+description);
            Log.i(TAG,"failingUrl"+failingUrl);
            String  fallSearchname=et_searchNetName.getText().toString().trim();
            if (errorCode==WebViewClient.ERROR_HOST_LOOKUP){
                //找不到网址,调用百度搜索
                url= "http://m.baidu.com/s?word=" + fallSearchname;
                Log.i(TAG,"onReceivedError:"+url);
                webshow.loadUrl(url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i(TAG,"pagFinished");
          // ll_serachUrl.setVisibility(View.GONE);//实现搜索功能后自动隐藏搜索栏
            //
            //在每次页面加载完成后查看是否有可以回溯的历史
            changeStatueOfWebToolsButton();
        }

    }

    /**
     * 回溯历史用
     */
    private void changeStatueOfWebToolsButton(){
        if (webshow.canGoBack()){
            iv_back.setEnabled(true);
        }else {
            iv_back.setEnabled(false);
        }

        if (webshow.canGoForward()){
            iv_goto.setEnabled(true);
        }else {
            iv_goto.setEnabled(false);
        }

    }

    /**
     * 用于实现在地址栏输入内容时自带提示功能
     * 判断输入地址的有效性,有效时按钮为"进入",无效时为"取消"
     */
    private class WebUrlNameChangedListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
             url=s.toString();
            if (!(url.startsWith("http://")||url.startsWith("https://"))){
                url="http://"+url;
            }
            Log.i(TAG,"onchangeText:"+url);
//            if (URLUtil.isNetworkUrl(url)&&URLUtil.isValidUrl(url)){
//                changeStatueOfWebGoto(true);
//                Log.i(TAG,"changeStatueOfWebGoto is true");
//            }else {
//                changeStatueOfWebGoto(false);
//            }



        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
//根据输入url有效性进行更改tv_search
    private void changeStatueOfWebGoto(boolean b) {
        if (b){
            tv_search.setText("进入");
        }else {
            tv_search.setText("取消");
        }
    }

    /**
     * 手势运用:页面加载后向上滑动到顶部显示地址栏.向下滑动到底部,隐藏地址栏
     */
    private class GestureListener implements GestureDetector.OnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(webshow.getScrollY()==0){
                Log.i(TAG,"getY==0,Visible");
                //滑倒顶部
                ll_serachUrl.setVisibility(View.VISIBLE);
            }
            if(webshow.getScrollY()>0){
                Log.i(TAG,"getY>0,Gone");
                //滑倒底部
                ll_serachUrl.setVisibility(View.GONE);
            }
            return true;
        }
    }
    /**
     * webView手势监听
     */
    private class WebViewListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId()==R.id.web_webshow){
                return mGesture.onTouchEvent(event);
            }
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,data.getStringExtra("scannerUrl"));
        super.onActivityResult(requestCode, resultCode, data);
            Log.i(TAG,data.getStringExtra("scannerUrl"));
            webshow.loadUrl(data.getStringExtra("scannerUrl"));
    }
}
