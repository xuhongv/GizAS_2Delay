package com.gizwits.opensource.appkit.ControlModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.opensource.appkit.ControlModule.adapter.ListViewAdapter;
import com.gizwits.opensource.appkit.ControlModule.bean.TimerListInfBean;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.EventBusMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TimerTaskActivity extends GosControlModuleBaseActivity {


    private ListView listView;

    private Button btnAdd;

    private ListViewAdapter adapter;

    private GizWifiDevice mDevice;


    private List<TimerListInfBean> listInfBeans;
    private List<TimerListInfBean> listInfBeansTemper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer);
        setActionBar(true, true, "定时");

        Intent intent = getIntent();
        mDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        listInfBeansTemper = new ArrayList<>();

        listInfBeans = getIntent().getParcelableArrayListExtra("_listInfBean");
        for (TimerListInfBean bean : listInfBeans) {
            if (bean.getTask_type() == 1 || bean.getTask_type() == 2)
                listInfBeansTemper.add(bean);
        }

        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {


        btnAdd = (Button) findViewById(R.id.btAdd);
        if (listInfBeansTemper.size() > 4) {
            btnAdd.setVisibility(View.GONE);
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (listInfBeansTemper.size() == 0) {
                    Intent intent = new Intent(TimerTaskActivity.this, AddTimerTaskActivity.class);
                    intent.putExtra("_delayType", 1);
                    intent.putExtra("_device", mDevice);
                    startActivity(intent);
                    return;
                }


                List<Integer> integerList = new ArrayList<>();
                integerList.add(1);
                integerList.add(2);
                integerList.add(3);
                integerList.add(4);
                integerList.add(5);

                for (int i = 0; i < listInfBeansTemper.size(); i++) {
                    for (int j = 0; j < integerList.size(); j++) {
                        Log.e("==w", "listInfBeansTemper.get(i).getDelayWaysType():" + listInfBeansTemper.get(i).getDelayWaysType());
                        Log.e("==w", "remove:" + j);
                        if (listInfBeansTemper.get(i).getDelayWaysType() == j) {
                            Collections.replaceAll(integerList, j, 6);
                        }
                    }
                }

                for (int k = 0; k < 5; k++) {
                    if (integerList.get(k) < 6) {
                        Intent intent1 = new Intent(TimerTaskActivity.this, AddTimerTaskActivity.class);
                        intent1.putExtra("_delayType", integerList.get(k));
                        intent1.putExtra("_device", mDevice);
                        startActivity(intent1);
                        finish();
                        return;
                    }
                }
            }
        });


        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListViewAdapter(this, listInfBeansTemper);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("==w", "onItemClick");
            }
        });
        adapter.setListener(new ListViewAdapter.AddTimerTaskItemListener() {
            @Override
            public void onItemPowerClick(boolean isOpen, int type) {

                ConcurrentHashMap<String, Object> hashMap;
                switch (type) {
                    case 1:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK1_ENABLE, isOpen);
                        sendCommand(hashMap);
                        break;
                    case 2:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK2_ENABLE, isOpen);
                        sendCommand(hashMap);
                        break;
                    case 3:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK3_ENABLE, isOpen);
                        sendCommand(hashMap);
                        break;
                    case 4:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK4_ENABLE, isOpen);
                        sendCommand(hashMap);
                        break;
                    case 5:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK5_ENABLE, isOpen);
                        sendCommand(hashMap);
                        break;
                    default:
                        break;
                }
                Log.e("==w", "onItemPowerClick:" + type);
            }

            @Override
            public void onItemRubbishClick(TimerListInfBean bean, int position) {

                ConcurrentHashMap<String, Object> hashMap;
                switch (bean.getDelayWaysType()) {
                    case 1:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK1_TYPE, 0);
                        sendCommand(hashMap);
                        break;
                    case 2:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK2_TYPE, 0);
                        sendCommand(hashMap);
                        break;
                    case 3:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK3_TYPE, 0);
                        sendCommand(hashMap);
                        break;
                    case 4:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK4_TYPE, 0);
                        sendCommand(hashMap);
                        break;
                    case 5:
                        hashMap = new ConcurrentHashMap<>();
                        hashMap.put(KEY_TASK5_TYPE, 0);
                        sendCommand(hashMap);
                        break;
                    default:
                        break;
                }

                listInfBeansTemper.remove(position);
                adapter.notifyDataSetChanged();
                myToast("删除成功！");
            }

        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage messageEvent) {
        if (messageEvent.getEventCode() == 1) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void sendCommand(ConcurrentHashMap<String, Object> hashMap) {
        if (hashMap == null)
            return;
        int sn = 5;
        mDevice.write(hashMap, sn);
        Log.i("==w", "下发命令：" + hashMap.toString());
    }

}
