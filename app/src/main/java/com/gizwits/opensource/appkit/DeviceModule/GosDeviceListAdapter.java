package com.gizwits.opensource.appkit.DeviceModule;

import java.util.List;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.opensource.appkit.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class GosDeviceListAdapter extends BaseAdapter {

    Handler handler = new Handler();
    protected static final int UNBOUND = 99;


    private boolean isDelay1Open;
    private boolean isDelay2Open;

    private DelayIconListener listener;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    Context context;
    List<GizWifiDevice> deviceList;

    public GosDeviceListAdapter(Context context, List<GizWifiDevice> deviceList) {
        super();
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_gos_device_list, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }


        final GizWifiDevice device = deviceList.get(position);


        String LAN, noLAN, unbind;
        LAN = (String) context.getText(R.string.lan);
        noLAN = (String) context.getText(R.string.no_lan);
        unbind = (String) context.getText(R.string.unbind);
        String deviceAlias = device.getAlias();
        String devicePN = device.getProductName();

        if (device.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOnline
                || device.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled) {
            if (device.isBind()) {// 已绑定设备
                holder.getTvDeviceMac().setText(device.getMacAddress());
                if (device.isLAN()) {
                    holder.getTvDeviceStatus().setText(LAN);
                } else {
                    holder.getTvDeviceStatus().setText(noLAN);
                }
                if (TextUtils.isEmpty(deviceAlias)) {
                    holder.getTvDeviceName().setText(devicePN);
                } else {
                    holder.getTvDeviceName().setText(deviceAlias);
                }


                if (!device.getRemark().isEmpty()) {
                    int number = Integer.parseInt(device.getRemark());
                    int[] value = new int[5];
                    value[0] = number % 10000 % 1000 % 100 % 10; //个位
                    value[1] = number / 10 % 1000 % 100 % 10;//十位
                    Log.e("youo", "getRemark:" + device.getRemark());
                    Log.e("youo", " value[0] :" + value[0]);
                    Log.e("youo", " value[1] :" + value[1]);

                    if (value[0] == 1) {
                        holder.getCbDelay2().setChecked(true);
                        isDelay1Open = true;
                    } else if (value[0] == 2) {
                        holder.getCbDelay2().setChecked(false);
                        isDelay1Open = false;
                    }

                    if (value[1] == 1) {
                        holder.getCbDelay1().setChecked(true);
                        isDelay2Open = true;
                    } else if (value[1] == 2) {
                        holder.getCbDelay1().setChecked(false);
                        isDelay2Open = false;
                    }


                holder.getCbDelay1().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (listener != null) {
                            listener.Delay1Listener(device, isChecked);

                        }
                    }
                });


                holder.getCbDelay2().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (listener != null) {
                            listener.Delay2Listener(device, isChecked);
                        }
                    }
                });

                }
            } else {// 未绑定设备
                holder.getTvDeviceMac().setText(device.getMacAddress());
                holder.getTvDeviceStatus().setText(unbind);
                if (TextUtils.isEmpty(deviceAlias)) {
                    holder.getTvDeviceName().setText(devicePN);
                } else {
                    holder.getTvDeviceName().setText(deviceAlias);
                }
            }
        } else {// 设备不在线

            holder.getTvDeviceMac().setText(device.getMacAddress());
            holder.getTvDeviceMac().setTextColor(
                    context.getResources().getColor(R.color.gray));
            holder.getTvDeviceStatus().setText("");
            holder.getTvDeviceStatus().setTextColor(
                    context.getResources().getColor(R.color.gray));
            holder.getImgRight().setVisibility(View.GONE);
            holder.getLlLeft().setBackgroundResource(
                    R.drawable.btn_getcode_shape_gray);
            if (TextUtils.isEmpty(deviceAlias)) {
                holder.getTvDeviceName().setText(devicePN);
            } else {
                holder.getTvDeviceName().setText(deviceAlias);
            }
            holder.getTvDeviceName().setTextColor(
                    context.getResources().getColor(R.color.gray));
        }
        holder.getDelete2().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = UNBOUND;
                message.obj = device.getDid().toString();
                handler.sendMessage(message);
            }
        });
        return view;
    }


    public void setDelayListener(DelayIconListener listener) {
        this.listener = listener;
    }


    public interface DelayIconListener {

        void Delay1Listener(GizWifiDevice device, boolean isDelay1);

        void Delay2Listener(GizWifiDevice device, boolean isDelay2);


    }
}


class Holder {
    View view;

    public Holder(View view) {
        this.view = view;
    }

    private TextView tvDeviceMac, tvDeviceStatus, tvDeviceName;

    private RelativeLayout delete2;

    private ImageView imgRight;

    private LinearLayout llLeft;

    private CheckBox cbDelay1, cbDelay2;

    public LinearLayout getLlLeft() {
        if (null == llLeft) {
            llLeft = (LinearLayout) view.findViewById(R.id.llLeft);
        }
        return llLeft;
    }

    public ImageView getImgRight() {
        if (null == imgRight) {
            imgRight = (ImageView) view.findViewById(R.id.imgRight);
        }
        return imgRight;
    }

    public RelativeLayout getDelete2() {
        if (null == delete2) {
            delete2 = (RelativeLayout) view.findViewById(R.id.delete2);
        }
        return delete2;
    }

    public TextView getTvDeviceMac() {
        if (null == tvDeviceMac) {
            tvDeviceMac = (TextView) view.findViewById(R.id.tvDeviceMac);
        }
        return tvDeviceMac;
    }

    public TextView getTvDeviceStatus() {
        if (null == tvDeviceStatus) {
            tvDeviceStatus = (TextView) view.findViewById(R.id.tvDeviceStatus);
        }
        return tvDeviceStatus;
    }

    public TextView getTvDeviceName() {
        if (null == tvDeviceName) {
            tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
        }
        return tvDeviceName;
    }

    public CheckBox getCbDelay1() {
        if (null == cbDelay1) {
            cbDelay1 = (CheckBox) view.findViewById(R.id.cbDelay1);
        }
        return cbDelay1;
    }

    public CheckBox getCbDelay2() {
        if (null == cbDelay2) {
            cbDelay2 = (CheckBox) view.findViewById(R.id.cbDelay2);
        }
        return cbDelay2;
    }

}


