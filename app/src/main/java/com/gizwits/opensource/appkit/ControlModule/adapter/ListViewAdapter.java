package com.gizwits.opensource.appkit.ControlModule.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gizwits.opensource.appkit.ControlModule.bean.TimerListInfBean;
import com.gizwits.opensource.appkit.R;

import java.math.BigInteger;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private AddTimerTaskItemListener listener;

    public void setListener(AddTimerTaskItemListener listener) {
        this.listener = listener;
    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = mLayoutInflater.inflate(R.layout.item_listview_task, null);
            viewHoler.mCbStatus1 = (CheckBox) convertView.findViewById(R.id.cbStatus1);
            viewHoler.mCbStatus2 = (CheckBox) convertView.findViewById(R.id.cbStatus2);
            viewHoler.mTvTimer = (TextView) convertView.findViewById(R.id.tvTimer);
            viewHoler.mTvWay = (TextView) convertView.findViewById(R.id.tvWay);
            viewHoler.mTvSelectDay = (TextView) convertView.findViewById(R.id.tvSelectDay);
            viewHoler.mSwOpenOff = (Switch) convertView.findViewById(R.id.swOpenOff);
            viewHoler.mBtRubbish = (Button) convertView.findViewById(R.id.btRubbish);
            viewHoler.ll = (LinearLayout) convertView.findViewById(R.id.ll);
            convertView.setTag(viewHoler);

        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }


        //显示时间
        viewHoler.mTvTimer.setText(parseMinuteNumber(timerListInfBeans.get(position).getTask_minute()));


        viewHoler.mSwOpenOff.setChecked(timerListInfBeans.get(position).isTask_Enable());
        viewHoler.mSwOpenOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    Log.e("==w", "setOnCheckedChangeListener imerListInfBeans.get(position).getTask_type():" + timerListInfBeans.get(position).getTask_type());
                    Log.e("==w", "setOnCheckedChangeListener imerListInfBeans.get(position).getDelayWaysType():" + timerListInfBeans.get(position).getDelayWaysType());

                    listener.onItemPowerClick(isChecked, timerListInfBeans.get(position).getDelayWaysType());
                }
            }
        });


        viewHoler.mBtRubbish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemRubbishClick(timerListInfBeans.get(position),position);
                }
            }
        });


        //各路的开关状态
        int taskOnoff = timerListInfBeans.get(position).getTask_onoff();
        Log.e("==w", "taskOff:" + taskOnoff);
        switch (taskOnoff) {
            case 13:
                viewHoler.mCbStatus1.setChecked(true);
                viewHoler.mCbStatus2.setChecked(true);
                break;
            case 14:
                viewHoler.mCbStatus1.setChecked(true);
                viewHoler.mCbStatus2.setChecked(false);
                break;
            case 23:
                viewHoler.mCbStatus1.setChecked(false);
                viewHoler.mCbStatus2.setChecked(true);
                break;
            case 24:
                viewHoler.mCbStatus1.setChecked(false);
                viewHoler.mCbStatus2.setChecked(false);
                break;
            case 5:
                viewHoler.mCbStatus1.setChecked(true);
                viewHoler.mCbStatus2.setChecked(true);
                break;
            case 6:
                viewHoler.mCbStatus1.setChecked(false);
                viewHoler.mCbStatus2.setChecked(false);
                break;
            default:
                break;
        }
        String subTitile = null;
        if (timerListInfBeans.get(position).getTask_type() == 1) {
            subTitile = "单次定时";
            viewHoler.mTvSelectDay.setText(subTitile);

        } else if (timerListInfBeans.get(position).getTask_type() == 2) {
            BigInteger src = new BigInteger(timerListInfBeans.get(position).getTask_Week_Repeat() + "");
            int timerWays = Integer.parseInt(src.toString(2));
            int[] value = new int[7];
            value[0] = timerWays % 10000 % 1000 % 100 % 10; //个位
            value[1] = timerWays / 10 % 1000 % 100 % 10;//十位
            value[2] = timerWays / 100 % 100 % 10;//百位
            value[3] = timerWays / 1000 % 10;//千位
            value[4] = timerWays / 10000 % 10;//万位
            value[5] = timerWays / 100000 % 10;//十万位
            value[6] = timerWays / 1000000 % 10;//百万位
            subTitile = "周";
            if (value[0] == 1) {
                subTitile = subTitile + "一";
            }
            if (value[1] == 1) {
                subTitile = subTitile + "、二";
            }
            if (value[2] == 1) {
                subTitile = subTitile + "、三";
            }
            if (value[3] == 1) {
                subTitile = subTitile + "、四";
            }
            if (value[4] == 1) {
                subTitile = subTitile + "、五";
            }
            if (value[5] == 1) {
                subTitile = subTitile + "、六";
            }
            if (value[6] == 1) {
                subTitile = subTitile + "、日";
            }

            viewHoler.mTvSelectDay.setText(subTitile);
        }


        //显示当前使能状态
        viewHoler.mSwOpenOff.setChecked(timerListInfBeans.get(position).isTask_Enable());


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
        private CheckBox mCbStatus1, mCbStatus2;
        private TextView mTvTimer;
        private TextView mTvWay;
        private TextView mTvSelectDay;
        private Switch mSwOpenOff;
        private Button mBtRubbish;
        private LinearLayout ll;
    }

    public interface AddTimerTaskItemListener {

        void onItemPowerClick(boolean isOpen, int delayWaysType);

        void onItemRubbishClick(TimerListInfBean bean,int position);




    }
}
