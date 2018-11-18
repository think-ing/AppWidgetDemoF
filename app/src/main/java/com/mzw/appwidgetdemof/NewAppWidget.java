package com.mzw.appwidgetdemof;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.mzw.appwidgetdemof.tools.ConstantParameter;
import com.mzw.appwidgetdemof.tools.DateUtil;
import com.mzw.appwidgetdemof.tools.Lunar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private List<DateBean> mDateList;
    private Date receive_date;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Lunar lunar = new Lunar(calendar);
        String str = lunar.birthday(context);
        if(!TextUtils.isEmpty(str)){
            //今天是生日
            Log.i("---mzw---","今天是" + str + "的生日");
            sendNotification(context,"今天是重要的日子：" + str);
        }
        getData(context,new Date());
        for (int appWidgetId : appWidgetIds) {
            drawWidget(context, appWidgetId);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        getData(context,new Date());
        drawWidget(context, appWidgetId);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void redrawWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, NewAppWidget.class));
        for (int appWidgetId : appWidgetIds) {
            drawWidget(context, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收广播处理
        String action = intent.getAction();
        Log.i("---mzw---","action : "+action);
        Date _receive_date = new Date(intent.getLongExtra("receive_date", 0));
        Log.i("---mzw---","_receive_date : "+ConstantParameter.sdf_a.format(_receive_date));
        Date mDate = new Date();
        if(ConstantParameter.back_today.equals(action)){
            //回到今天
            mDate = new Date();
        }else if(ConstantParameter.month_previous.equals(action)){
            //上一月
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(_receive_date);
            calendar.add(Calendar.MONTH, -1);
            mDate = calendar.getTime();
        }else if(ConstantParameter.month_next.equals(action)){
            //下一月
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(_receive_date);
            calendar.add(Calendar.MONTH, 1);
            mDate = calendar.getTime();
        }else{
            //点击 天
//            //判断点击的是否是 本月
//            if(ConstantParameter.sdf_tomonth.format(_receive_date).equals(ConstantParameter.sdf_tomonth.format(new Date()))){
//                //是 本月
//            }else{
//                //不是本月\
//            }
            mDate = _receive_date;
        }
        getData(context,mDate);
        redrawWidgets(context);
        super.onReceive(context, intent);
    }

    //获取数据
    private void getData(Context context,Date d) {
        Log.i("---mzw---",""+ConstantParameter.sdf_a.format(d));
        mDateList = new ArrayList<DateBean>();
        receive_date = d;
        if(receive_date == null){
            receive_date = new Date();
        }
        Date date = DateUtil.getMonthStart(receive_date);
        Date monthEnd = DateUtil.getMonthEnd(receive_date);
        while (!date.after(monthEnd)) {
            Calendar calendar= Calendar.getInstance();
            calendar.setTime(date);
            Lunar lunar = new Lunar(calendar);
            mDateList.add(new DateBean(date,ConstantParameter.sdf_year.format(date),ConstantParameter.sdf_month.format(date),ConstantParameter.sdf_day.format(date),lunar.toString(),1,lunar.festival(context)));
            date = DateUtil.getNext(date);
        }

        //如果一号不是周一，取前几天到周一
        while(!"星期一".equals(DateUtil.dateToWeek(mDateList.get(0).date))){
            Date date1 = DateUtil.getPrevious(mDateList.get(0).date);
            Calendar calendar= Calendar.getInstance();
            calendar.setTime(date1);
            Lunar lunar = new Lunar(calendar);
            mDateList.add(0,new DateBean(date1, ConstantParameter.sdf_year.format(date1),ConstantParameter.sdf_month.format(date1),ConstantParameter.sdf_day.format(date1),lunar.toString(),1,lunar.festival(context)));
        }
        //如果月末不是周日，取后几天到周日
        while(!"星期日".equals(DateUtil.dateToWeek(mDateList.get(mDateList.size()-1).date))){
            Date date2 = DateUtil.getNext(mDateList.get(mDateList.size()-1).date);
            Calendar calendar= Calendar.getInstance();
            calendar.setTime(date2);
            Lunar lunar = new Lunar(calendar);
            mDateList.add(new DateBean(date2,ConstantParameter.sdf_year.format(date2),ConstantParameter.sdf_month.format(date2),ConstantParameter.sdf_day.format(date2),lunar.toString(),1,lunar.festival(context)));
        }
    }

    //填充布局
    private void drawWidget(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews mRemoteView = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        mRemoteView.removeAllViews(R.id.id_calendar);
        RemoteViews mRowView = null;
        RemoteViews mDayView = null;
        for(int i =0; i < mDateList.size(); i++){
            DateBean mDateBean = mDateList.get(i);

            if (mRowView == null || i % 7 == 0){
                mRowView = new RemoteViews(context.getPackageName(), R.layout.calendar_row);
            }
            mDayView = new RemoteViews(context.getPackageName(), R.layout.calendar_day);

            //获取当前时间 用来区分当天，当月，其他月
            //当月
            if(ConstantParameter.sdf_year.format(receive_date).equals(mDateBean.yangli_year) &&
                    ConstantParameter.sdf_month.format(receive_date).equals(mDateBean.yangli_month)){
                mDayView.setTextColor(R.id.id_yangli_text, context.getResources().getColor(R.color.this_month_text));
                mDayView.setTextColor(R.id.id_yinli_text, context.getResources().getColor(R.color.this_month_text));
            }else{
                //其他
                mDayView.setTextColor(R.id.id_yangli_text, context.getResources().getColor(R.color.other_month_text));
                mDayView.setTextColor(R.id.id_yinli_text, context.getResources().getColor(R.color.other_month_text));
            }

            //将农历拆分为 月 和 日
            String yinli_month = mDateBean.yinli.substring(0,2);
            String yinli_day = mDateBean.yinli.substring(2,4);
            //如果是初一 则显示月分
            if("初一".equals(yinli_day)){
                mDayView.setTextViewText(R.id.id_yinli_text, yinli_month);
                mDayView.setTextColor(R.id.id_yinli_text, context.getResources().getColor(R.color.yinli_month_text));
            }else{
                mDayView.setTextViewText(R.id.id_yinli_text, yinli_day);
//                views.setTextColor(yinliId, context.getResources().getColor(R.color.other_month_text));
            }
            //特殊节日
            if(!TextUtils.isEmpty(mDateBean.festival)){
                if("消费者权益日".equals(mDateBean.festival)){
                    mDayView.setTextViewText(R.id.id_yinli_text, "打假");
                }else{
                    mDayView.setTextViewText(R.id.id_yinli_text, mDateBean.festival);
                }
                mDayView.setTextColor(R.id.id_yinli_text, context.getResources().getColor(R.color.yinli_month_text));
            }
            mDayView.setTextViewText(R.id.id_yangli_text,mDateBean.yangli_day);

            //当天
            if(ConstantParameter.sdf_year.format(new Date()).equals(mDateBean.yangli_year) &&
                    ConstantParameter.sdf_month.format(new Date()).equals(mDateBean.yangli_month) &&
                    ConstantParameter.sdf_day.format(new Date()).equals(mDateBean.yangli_day)){
                mDayView.setInt(R.id.id_itemLayout, "setBackgroundResource", R.drawable.btn_b);
            }else{
                mDayView.setInt(R.id.id_itemLayout, "setBackgroundResource", R.drawable.btn_a);
            }

            //监听每天点击事件
            mDayView.setOnClickPendingIntent(R.id.id_itemLayout,
                    PendingIntent.getBroadcast(context, 0,
                            new Intent(context, NewAppWidget.class)
                                    .setAction(ConstantParameter.itemLayout+"_"+i)
                                    .putExtra("receive_date",mDateBean.date.getTime()),
                            PendingIntent.FLAG_UPDATE_CURRENT));

            mRowView.addView(R.id.id_calendar_row,mDayView);
            if (mRowView != null && i % 7 == 6){
                mRemoteView.addView(R.id.id_calendar,mRowView);
            }
        }

        //上月
        mRemoteView.setOnClickPendingIntent(R.id.id_month_previous,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, NewAppWidget.class)
                                .setAction(ConstantParameter.month_previous)
                                .putExtra("receive_date",receive_date.getTime()),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        //下月
        mRemoteView.setOnClickPendingIntent(R.id.id_month_next,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, NewAppWidget.class)
                                .setAction(ConstantParameter.month_next)
                                .putExtra("receive_date",receive_date.getTime()),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        //返回今天
        mRemoteView.setOnClickPendingIntent(R.id.id_today_view_a,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, NewAppWidget.class)
                                .setAction(ConstantParameter.back_today)
                                .putExtra("receive_date",new Date().getTime()),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        mRemoteView.setViewVisibility(R.id.id_back_today, View.GONE);

        // 当前时间
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(receive_date);
        Lunar lunar = new Lunar(calendar);
        lunar.festival(null);

        mRemoteView.setTextViewText(R.id.id_today_view_a,ConstantParameter.sdf.format(receive_date));
        mRemoteView.setTextViewText(R.id.id_today_view_b,lunar.cyclical() + " " + lunar.animalsYear() + "年 " + lunar.toString());

        appWidgetManager.updateAppWidget(appWidgetId, mRemoteView);
    }


    //重要日子 发送通知
    private void sendNotification(Context mContext,String str) {
        // 获取通知服务对象NotificationManager
        NotificationManager notiManager = (NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        // 创建Notification对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setTicker("birthday");            // 通知弹出时状态栏的提示文本
        builder.setContentInfo("别忘记哦！！！");  //
        builder.setContentTitle("重大通知")    // 通知标题
                .setContentText(str)  // 通知内容
                .setSmallIcon(R.mipmap.f000);    // 通知小图标
        builder.setDefaults(Notification.DEFAULT_SOUND);    // 设置声音/震动等
        Notification notification = builder.build();
        notiManager.notify(Integer.parseInt(ConstantParameter.sdf_hhmm.format(new Date())), notification);
    }
}

