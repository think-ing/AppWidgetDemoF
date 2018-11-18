package com.mzw.appwidgetdemof;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mzw.appwidgetdemof.tools.Lunar;

import java.util.List;

/**
 * Created by think on 2018/11/11.
 */

public class GridAdapter extends BaseAdapter {
    private List<Tempbean> mTempbeanList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public GridAdapter(List<Tempbean> mTempbeanList, Context mContext, LayoutInflater layoutInflater) {
        this.mTempbeanList = mTempbeanList;
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return mTempbeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTempbeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tempbean bean = mTempbeanList.get(position);
        ViewHolder mViewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.grid_item,parent,false);
            mViewHolder = new ViewHolder();
            mViewHolder.date_view = convertView.findViewById(R.id.date_view);
            mViewHolder.nick_view = convertView.findViewById(R.id.nick_view);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag();
        }
        mViewHolder.date_view.setText(Lunar.toString(bean.date));
        mViewHolder.nick_view.setText(bean.name);

        return convertView;
    }

    class ViewHolder{
        TextView date_view;
        TextView nick_view;
    }

}
