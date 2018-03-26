package com.gizwits.opensource.appkit.ControlModule.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.gizwits.opensource.appkit.ControlModule.bean.TimerListInfBean;
import com.gizwits.opensource.appkit.R;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private List<TimerListInfBean> timerListInfBeans;

    public ListViewAdapter(Context context, List<TimerListInfBean> timerListInfBeans) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.timerListInfBeans = timerListInfBeans;
    }

    @Override
    public int getCount() {
        return timerListInfBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return timerListInfBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = mLayoutInflater.inflate(R.layout.item_listview_task, null);
            viewHoler.mCbStatus = (CheckBox) convertView.findViewById(R.id.cbStatus);
            viewHoler.mTvTimer = (TextView) convertView.findViewById(R.id.tvTimer);
            viewHoler.mTvWay = (TextView) convertView.findViewById(R.id.tvWay);
            viewHoler.mTvSelectDay = (TextView) convertView.findViewById(R.id.tvSelectDay);
            viewHoler.mSwOpenOff = (Switch) convertView.findViewById(R.id.swOpenOff);
            viewHoler.mBtRubbish = (Button) convertView.findViewById(R.id.btRubbish);
            convertView.setTag(viewHoler);

        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }


        viewHoler.mTvTimer.setText(parseMinuteNumber(timerListInfBeans.get(position).getTask_minute()));
        Log.e("==w", "适配器；" + timerListInfBeans.get(position).getTask_minute());

        return convertView;
    }


    private String parseMinuteNumber(int temperMinute) {

        int hours = temperMinute / 60;
        int minute = temperMinute % 60;
        if (minute < 10) {
            return hours + ":0" + minute;
        }
        return hours + ":" + minute;
    }


    private class ViewHoler {
        private CheckBox mCbStatus;
        private TextView mTvTimer;
        private TextView mTvWay;
        private TextView mTvSelectDay;
        private Switch mSwOpenOff;
        private Button mBtRubbish;
    }
}
