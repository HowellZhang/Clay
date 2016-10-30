package com.example.howell.clayexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/28.
 */
public class SelectMenuItemPopUpWindow extends Activity {

    private static final String TAG ="SelectMenuItemPopUpWindow";
    private LinearLayout  ll_show;
    private GridView   gv_util_show;
    private List<Map<String,Object>> data_list;
    private SimpleAdapter  simpleAdapter;
    private int[] icon={R.mipmap.label,R.mipmap.history,R.mipmap.download,R.mipmap.saoyisao,
                          R.mipmap.unwatched ,R.mipmap.share,R.mipmap.setting,R.mipmap.finish}; ;
    private String[] iconName={"书签","历史","下载","扫一扫","无痕","分享","设置","退出"};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(TAG,"LoginSuccess");
        setView();
        findView();
        data_list=new ArrayList<Map<String,Object>>();
        //添加数据
        Log.i(TAG,"Login datalist Success");
        data_list=getData();
        //生成新的适配器
        Log.i(TAG,"Login 适配器 Success");
        setAdapter();
        gv_util_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int tid= (int) id;
                Log.i(TAG,"id:"+id);
                switch (tid){
                    case 0://书签
                        break;
                    case 1://历史
                        break;
                    case 2://下载
                        break;
                    case 3://扫一扫
                        Log.i(TAG,"saoyisao success");
                        startActivityForResult(new Intent(MainActivity.mContext, ScannerActivity.class), 1);
                        finish();
                        break;
                    case 4://无痕
                        break;
                    case 5://分享
                        break;
                    case 6://设置
                        break;
                    case 7://退出
                        startActivity(new Intent(SelectMenuItemPopUpWindow.this,MainActivity.class));
                        finish();
                        break;
                }
            }
        });

    }

    private void setAdapter() {
        String [] from ={"image","text"};
        int[]  to={R.id.iv_item_image,R.id.tv_item_text};
        simpleAdapter=new SimpleAdapter(this,data_list,R.layout.gridview_util_item,from,to);
        gv_util_show.setAdapter(simpleAdapter);
    }

    private void setView() {
        setContentView(R.layout.gridview_util_show);
    }

    private void findView() {
        ll_show= (LinearLayout) findViewById(R.id.ll_gridview_show);
        gv_util_show= (GridView) findViewById(R.id.gv_utillist);
    }

    private List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
