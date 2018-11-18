package com.mzw.appwidgetdemof;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mzw.appwidgetdemof.tools.ConstantParameter;
import com.mzw.appwidgetdemof.tools.NotificationsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Calendar calendar = Calendar.getInstance();
    private TextView dateTextView;
    private EditText nicknameView;
    private GridView gridView;
    private Context mContext;
    private String lunarStr = "";//阴历 月日
    private String solarStr = "";//阳历 月日

    private List<Tempbean> mTempbeanList = new ArrayList<Tempbean>();
    private JSONObject mJSONObject;

    private GridAdapter mGridAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    int arg1 = msg.arg1;
                    String text = "";
                    if(arg1 == 1){
                        text = "添加成功";
                        dateTextView.setText("");
                        nicknameView.setText("");
                    }else{
                        text = "删除成功";
                    }
                    Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
                    getBirthday();
                    break;
                case 1:
                    if(mGridAdapter != null){
                        toJson();
                        while (mTempbeanList != null && mTempbeanList.size() % 3 != 0){
                            mTempbeanList.add(new Tempbean("",""));
                        }
                        mGridAdapter.notifyDataSetChanged();
                    }
                    break;
                case -1:
                    Toast.makeText(mContext,"操作失败请重试...",Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void toJson() {//封装成 json 存储
        mJSONObject = new JSONObject();
        Collections.sort(mTempbeanList,new Comparator<Tempbean>(){
            public int compare(Tempbean arg0, Tempbean arg1) {
                return arg0.date.compareTo(arg1.date);
            }
        });
        for(int i = 0; i < mTempbeanList.size(); i++) {
            Tempbean tempbean = mTempbeanList.get(i);
            try {
                if(!TextUtils.isEmpty(tempbean.date)){
                    mJSONObject.put(tempbean.date, tempbean.name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        gridView = findViewById(R.id.id_gridView);
        dateTextView = findViewById(R.id.textView4);
        nicknameView = findViewById(R.id.nicknameView);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //主题 样式  0 -- 5
                showDatePickerDialog(MainActivity.this,3,dateTextView,calendar);
            }
        });
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBirthday();
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameView.getText().toString().trim();
                if(!TextUtils.isEmpty(nickname) && !TextUtils.isEmpty(lunarStr)){
                    if(mTempbeanList.size() > 35){
                        Toast.makeText(mContext,"添加数量已达上线！！！",Toast.LENGTH_LONG).show();
                    }else{
//                        putBirthday(lunarStr,nickname);
                        mTempbeanList.add(new Tempbean(lunarStr,nickname));
                        toJson();
                        putBirthday(1);//添加
                    }
                }
            }
        });

        getBirthday();

        mGridAdapter = new GridAdapter(mTempbeanList, mContext, getLayoutInflater());
        gridView.setAdapter(mGridAdapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mTempbeanList.remove(position);
                toJson();
                putBirthday(0);//删除
                return true;
            }
        });

        checkSettings();
    }



    //选择时间
    public void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new MyDatePickerDialog(activity
                ,  themeResId
                // 绑定监听器(How the parent is notified that the date is set.)
                ,new MyDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,int month, int day,String lunarStr,String str) {
                // 此处得到选择的时间，可以进行你想要的操作
                MainActivity.this.lunarStr = str;
                String _month = ""+month;
                String _day = ""+day;
                if(month < 10){
                    _month = "0"+month;
                }
                if(day < 10){
                    _day = "0"+day;
                }
                MainActivity.this.solarStr = _month+_day;
                tv.setText(year+ "年" + month+ "月" + day + "日 【" + lunarStr + "】");
            }
        }
                ,calendar
                ,true
                ,true
                ,true).show();
    }

    //提交数据到网络
    private void putBirthday(int arg1) {
        final int finalArg = arg1;
        MyNetwork.putBirthday(mContext, ConstantParameter.MOB_TABLE,mJSONObject.toString(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("---mzw---", "call1: "+call.toString());
                        Log.e("---mzw---", "onFailure1: "+e.getMessage());
                        mHandler.sendEmptyMessage(3);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String _response = response.body().string();
                        Log.i("---mzw---","onResponse1 : " + _response);
                        if(!TextUtils.isEmpty(_response) && _response.startsWith("{")){
                            try {
                                JSONObject json = new JSONObject(_response);
                                json.getString("msg");
                                if(json.getInt("retCode") == 200){
//                                    mHandler.sendEmptyMessage(0);
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.arg1 = finalArg;
                                    mHandler.sendMessage(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    //获取数据
    private void getBirthday() {
        mTempbeanList.clear();
        final SharedPreferences.Editor mSharedPreferences = mContext.getSharedPreferences(ConstantParameter.MOB_TABLE, Context.MODE_PRIVATE).edit();
        mSharedPreferences.clear();
        MyNetwork.getBirthday(mContext,ConstantParameter.MOB_TABLE,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("---mzw---", "call2: "+call.toString());
                        Log.e("---mzw---", "onFailure2: "+e.getMessage());
                        mHandler.sendEmptyMessage(3);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String _response = response.body().string();
//                        Log.i("---mzw---","onResponse2 : " + _response);

                        if(!TextUtils.isEmpty(_response) && _response.startsWith("{")){
                            try {
                                JSONObject json = new JSONObject(_response);
                                json.getString("msg");
                                if(json.getInt("retCode") == 200){
                                    String result = json.getString("result");
                                    if(!TextUtils.isEmpty(result) && result.startsWith("{")){
                                        JSONObject resultJson = new JSONObject(result);
                                        String v = resultJson.getString("v");
                                        if(!TextUtils.isEmpty(v)){
                                            mSharedPreferences.putString(ConstantParameter.birthday_k,v);
                                            JSONObject vJson = new JSONObject(v);
                                            Iterator<String> krys = vJson.keys();

                                            while(krys.hasNext()) {
                                                String date = krys.next();
                                                String name = vJson.getString(date);
                                                mTempbeanList.add(new Tempbean(date,name));
                                                System.out.print(date + " - " + name);
                                            }
                                            mHandler.sendEmptyMessage(1);
                                        }else{
                                            mTempbeanList.clear();
                                            mHandler.sendEmptyMessage(1);
                                        }
                                    }
                                }
                                mSharedPreferences.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    // 检查 设置  （是否开启通知栏权限）
    private void checkSettings() {
        if (!NotificationsUtils.isNotificationEnabled(this)) {
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.show();

            View view = View.inflate(this, R.layout.dialog, null);
            dialog.setContentView(view);

            TextView context = (TextView) view.findViewById(R.id.tv_dialog_context);
            context.setText("检测到您没有打开通知权限！！！\n是否去打开？？");

            TextView confirm = (TextView) view.findViewById(R.id.btn_confirm);
            confirm.setText("是");
            confirm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.cancel();
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);

                        localIntent.setClassName("com.android.settings",
                                "com.android.settings.InstalledAppDetails");

                        localIntent.putExtra("com.android.settings.ApplicationPkgName",
                                MainActivity.this.getPackageName());
                    }
                    startActivity(localIntent);
                }
            });

            TextView cancel = (TextView) view.findViewById(R.id.btn_off);
            cancel.setText("否");
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
    }
}
