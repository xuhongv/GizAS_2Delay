package com.gizwits.opensource.appkit.ControlModule;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.ControlModule.bean.TimerListInfBean;
import com.gizwits.opensource.appkit.utils.HexStrUtils;

import android.util.Log;
import android.content.Context;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class GosControlModuleBaseActivity extends GosBaseActivity {




    protected static TimerListInfBean timer1Bean = new TimerListInfBean();
    protected static TimerListInfBean  timer2Bean = new TimerListInfBean();
    protected static TimerListInfBean timer3Bean = new TimerListInfBean();
    protected static TimerListInfBean timer4Bean = new TimerListInfBean();
    protected static TimerListInfBean timer5Bean = new TimerListInfBean();

    /*
     * ===========================================================
     * 以下key值对应开发者在云端定义的数据点标识名
     * ===========================================================
     */
    // 数据点"开关1"对应的标识名
    protected static final String KEY_ONOFF1 = "OnOff1";
    // 数据点"开关2"对应的标识名
    protected static final String KEY_ONOFF2 = "OnOff2";
    // 数据点"任务1启动开关"对应的标识名
    protected static final String KEY_TASK1_ENABLE = "Task1_Enable";
    // 数据点"任务1周期动作1"对应的标识名
    protected static final String KEY_TASK1_CYCLE_ONOFF1 = "Task1_cycle_onoff1";
    // 数据点"任务1周期动作2"对应的标识名
    protected static final String KEY_TASK1_CYCLE_ONOFF2 = "Task1_cycle_onoff2";
    // 数据点"任务2启动开关"对应的标识名
    protected static final String KEY_TASK2_ENABLE = "Task2_Enable";
    // 数据点"任务2周期动作1"对应的标识名
    protected static final String KEY_TASK2_CYCLE_ONOFF1 = "Task2_cycle_onoff1";
    // 数据点"任务2周期动作2"对应的标识名
    protected static final String KEY_TASK2_CYCLE_ONOFF2 = "Task2_cycle_onoff2";
    // 数据点"任务3启动开关"对应的标识名
    protected static final String KEY_TASK3_ENABLE = "Task3_Enable";
    // 数据点"任务3周期动作1"对应的标识名
    protected static final String KEY_TASK3_CYCLE_ONOFF1 = "Task3_cycle_onoff1";
    // 数据点"任务3周期动作2"对应的标识名
    protected static final String KEY_TASK3_CYCLE_ONOFF2 = "Task3_cycle_onoff2";
    // 数据点"任务4启动开关"对应的标识名
    protected static final String KEY_TASK4_ENABLE = "Task4_Enable";
    // 数据点"任务4周期动作1"对应的标识名
    protected static final String KEY_TASK4_CYCLE_ONOFF1 = "Task4_cycle_onoff1";
    // 数据点"任务4周期动作2"对应的标识名
    protected static final String KEY_TASK4_CYCLE_ONOFF2 = "Task4_cycle_onoff2";
    // 数据点"任务5启动开关"对应的标识名
    protected static final String KEY_TASK5_ENABLE = "Task5_Enable";
    // 数据点"任务5周期动作1"对应的标识名
    protected static final String KEY_TASK5_CYCLE_ONOFF1 = "Task5_cycle_onoff1";
    // 数据点"任务5周期动作2"对应的标识名
    protected static final String KEY_TASK5_CYCLE_ONOFF2 = "Task5_cycle_onoff2";
    // 数据点"任务1开关动作"对应的标识名
    protected static final String KEY_TASK1_ONOFF = "Task1_onoff";
    // 数据点"任务1类型"对应的标识名
    protected static final String KEY_TASK1_TYPE = "Task1_type";
    // 数据点"任务1每周重复"对应的标识名
    protected static final String KEY_TASK1_WEEK_REPEAT = "Task1_Week_Repeat";
    // 数据点"任务2开关动作"对应的标识名
    protected static final String KEY_TASK2_ONOFF = "Task2_onoff";
    // 数据点"任务2类型"对应的标识名
    protected static final String KEY_TASK2_TYPE = "Task2_type";
    // 数据点"任务2每周重复"对应的标识名
    protected static final String KEY_TASK2_WEEK_REPEAT = "Task2_Week_Repeat";
    // 数据点"任务3开关动作"对应的标识名
    protected static final String KEY_TASK3_ONOFF = "Task3_onoff";
    // 数据点"任务3类型"对应的标识名
    protected static final String KEY_TASK3_TYPE = "Task3_type";
    // 数据点"任务3每周重复"对应的标识名
    protected static final String KEY_TASK3_WEEK_REPEAT = "Task3_Week_Repeat";
    // 数据点"任务4开关动作"对应的标识名
    protected static final String KEY_TASK4_ONOFF = "Task4_onoff";
    // 数据点"任务4类型"对应的标识名
    protected static final String KEY_TASK4_TYPE = "Task4_type";
    // 数据点"任务4每周重复"对应的标识名
    protected static final String KEY_TASK4_WEEK_REPEAT = "Task4_Week_Repeat";
    // 数据点"任务5开关动作"对应的标识名
    protected static final String KEY_TASK5_ONOFF = "Task5_onoff";
    // 数据点"任务5类型"对应的标识名
    protected static final String KEY_TASK5_TYPE = "Task5_type";
    // 数据点"任务5每周重复"对应的标识名
    protected static final String KEY_TASK5_WEEK_REPEAT = "Task5_Week_Repeat";
    // 数据点"任务1日数值"对应的标识名
    protected static final String KEY_TASK1_DAY = "Task1_day";
    // 数据点"任务1分钟值"对应的标识名
    protected static final String KEY_TASK1_MINUTE = "Task1_minute";
    // 数据点"任务1周期分钟1"对应的标识名
    protected static final String KEY_TASK1_CYCLE_MINUTE1 = "Task1_cycle_minute1";
    // 数据点"任务1周期分钟2"对应的标识名
    protected static final String KEY_TASK1_CYCLE_MINUTE2 = "Task1_cycle_minute2";
    // 数据点"任务2日数值"对应的标识名
    protected static final String KEY_TASK2_DAY = "Task2_day";
    // 数据点"任务2分钟值"对应的标识名
    protected static final String KEY_TASK2_MINUTE = "Task2_minute";
    // 数据点"任务2周期分钟1"对应的标识名
    protected static final String KEY_TASK2_CYCLE_MINUTE1 = "Task2_cycle_minute1";
    // 数据点"任务2周期分钟2"对应的标识名
    protected static final String KEY_TASK2_CYCLE_MINUTE2 = "Task2_cycle_minute2";
    // 数据点"任务3日数值"对应的标识名
    protected static final String KEY_TASK3_DAY = "Task3_day";
    // 数据点"任务3分钟值"对应的标识名
    protected static final String KEY_TASK3_MINUTE = "Task3_minute";
    // 数据点"任务3周期分钟1"对应的标识名
    protected static final String KEY_TASK3_CYCLE_MINUTE1 = "Task3_cycle_minute1";
    // 数据点"任务3周期分钟2"对应的标识名
    protected static final String KEY_TASK3_CYCLE_MINUTE2 = "Task3_cycle_minute2";
    // 数据点"任务4日数值"对应的标识名
    protected static final String KEY_TASK4_DAY = "Task4_day";
    // 数据点"任务4分钟值"对应的标识名
    protected static final String KEY_TASK4_MINUTE = "Task4_minute";
    // 数据点"任务4周期分钟1"对应的标识名
    protected static final String KEY_TASK4_CYCLE_MINUTE1 = "Task4_cycle_minute1";
    // 数据点"任务4周期分钟2"对应的标识名
    protected static final String KEY_TASK4_CYCLE_MINUTE2 = "Task4_cycle_minute2";
    // 数据点"任务5日数值"对应的标识名
    protected static final String KEY_TASK5_DAY = "Task5_day";
    // 数据点"任务5分钟值"对应的标识名
    protected static final String KEY_TASK5_MINUTE = "Task5_minute";
    // 数据点"任务5周期分钟1"对应的标识名
    protected static final String KEY_TASK5_CYCLE_MINUTE1 = "Task5_cycle_minute1";
    // 数据点"任务5周期分钟2"对应的标识名
    protected static final String KEY_TASK5_CYCLE_MINUTE2 = "Task5_cycle_minute2";
    // 数据点"厂商"对应的标识名
    protected static final String KEY_COMPANYNAME = "CompanyName";
    // 数据点"产品型号"对应的标识名
    protected static final String KEY_MODUL = "Modul";

    /*
     * ===========================================================
     * 以下数值对应开发者在云端定义的可写数值型数据点增量值、数据点定义的分辨率、seekbar滚动条补偿值
     * _ADDITION:数据点增量值
     * _RATIO:数据点定义的分辨率
     * _OFFSET:seekbar滚动条补偿值
     * APP与设备定义的协议公式为：y（APP接收的值）=x（设备上报的值）* RATIO（分辨率）+ADDITION（增量值）
     * 由于安卓的原生seekbar无法设置最小值，因此代码中增加了一个补偿量OFFSET
     * 实际上公式中的：x（设备上报的值）=seekbar的值+补偿值
     * ===========================================================
     */
    // 数据点"任务1开关动作"对应seekbar滚动条补偿值
    protected static final int TASK1_ONOFF_OFFSET = 0;
    // 数据点"任务1开关动作"对应数据点增量值
    protected static final int TASK1_ONOFF_ADDITION = 0;
    // 数据点"任务1开关动作"对应数据点定义的分辨率
    protected static final int TASK1_ONOFF_RATIO = 1;

    // 数据点"任务1类型"对应seekbar滚动条补偿值
    protected static final int TASK1_TYPE_OFFSET = 0;
    // 数据点"任务1类型"对应数据点增量值
    protected static final int TASK1_TYPE_ADDITION = 0;
    // 数据点"任务1类型"对应数据点定义的分辨率
    protected static final int TASK1_TYPE_RATIO = 1;

    // 数据点"任务1每周重复"对应seekbar滚动条补偿值
    protected static final int TASK1_WEEK_REPEAT_OFFSET = 0;
    // 数据点"任务1每周重复"对应数据点增量值
    protected static final int TASK1_WEEK_REPEAT_ADDITION = 0;
    // 数据点"任务1每周重复"对应数据点定义的分辨率
    protected static final int TASK1_WEEK_REPEAT_RATIO = 1;

    // 数据点"任务2开关动作"对应seekbar滚动条补偿值
    protected static final int TASK2_ONOFF_OFFSET = 0;
    // 数据点"任务2开关动作"对应数据点增量值
    protected static final int TASK2_ONOFF_ADDITION = 0;
    // 数据点"任务2开关动作"对应数据点定义的分辨率
    protected static final int TASK2_ONOFF_RATIO = 1;

    // 数据点"任务2类型"对应seekbar滚动条补偿值
    protected static final int TASK2_TYPE_OFFSET = 0;
    // 数据点"任务2类型"对应数据点增量值
    protected static final int TASK2_TYPE_ADDITION = 0;
    // 数据点"任务2类型"对应数据点定义的分辨率
    protected static final int TASK2_TYPE_RATIO = 1;

    // 数据点"任务2每周重复"对应seekbar滚动条补偿值
    protected static final int TASK2_WEEK_REPEAT_OFFSET = 0;
    // 数据点"任务2每周重复"对应数据点增量值
    protected static final int TASK2_WEEK_REPEAT_ADDITION = 0;
    // 数据点"任务2每周重复"对应数据点定义的分辨率
    protected static final int TASK2_WEEK_REPEAT_RATIO = 1;

    // 数据点"任务3开关动作"对应seekbar滚动条补偿值
    protected static final int TASK3_ONOFF_OFFSET = 0;
    // 数据点"任务3开关动作"对应数据点增量值
    protected static final int TASK3_ONOFF_ADDITION = 0;
    // 数据点"任务3开关动作"对应数据点定义的分辨率
    protected static final int TASK3_ONOFF_RATIO = 1;

    // 数据点"任务3类型"对应seekbar滚动条补偿值
    protected static final int TASK3_TYPE_OFFSET = 0;
    // 数据点"任务3类型"对应数据点增量值
    protected static final int TASK3_TYPE_ADDITION = 0;
    // 数据点"任务3类型"对应数据点定义的分辨率
    protected static final int TASK3_TYPE_RATIO = 1;

    // 数据点"任务3每周重复"对应seekbar滚动条补偿值
    protected static final int TASK3_WEEK_REPEAT_OFFSET = 0;
    // 数据点"任务3每周重复"对应数据点增量值
    protected static final int TASK3_WEEK_REPEAT_ADDITION = 0;
    // 数据点"任务3每周重复"对应数据点定义的分辨率
    protected static final int TASK3_WEEK_REPEAT_RATIO = 1;

    // 数据点"任务4开关动作"对应seekbar滚动条补偿值
    protected static final int TASK4_ONOFF_OFFSET = 0;
    // 数据点"任务4开关动作"对应数据点增量值
    protected static final int TASK4_ONOFF_ADDITION = 0;
    // 数据点"任务4开关动作"对应数据点定义的分辨率
    protected static final int TASK4_ONOFF_RATIO = 1;

    // 数据点"任务4类型"对应seekbar滚动条补偿值
    protected static final int TASK4_TYPE_OFFSET = 0;
    // 数据点"任务4类型"对应数据点增量值
    protected static final int TASK4_TYPE_ADDITION = 0;
    // 数据点"任务4类型"对应数据点定义的分辨率
    protected static final int TASK4_TYPE_RATIO = 1;

    // 数据点"任务4每周重复"对应seekbar滚动条补偿值
    protected static final int TASK4_WEEK_REPEAT_OFFSET = 0;
    // 数据点"任务4每周重复"对应数据点增量值
    protected static final int TASK4_WEEK_REPEAT_ADDITION = 0;
    // 数据点"任务4每周重复"对应数据点定义的分辨率
    protected static final int TASK4_WEEK_REPEAT_RATIO = 1;

    // 数据点"任务5开关动作"对应seekbar滚动条补偿值
    protected static final int TASK5_ONOFF_OFFSET = 0;
    // 数据点"任务5开关动作"对应数据点增量值
    protected static final int TASK5_ONOFF_ADDITION = 0;
    // 数据点"任务5开关动作"对应数据点定义的分辨率
    protected static final int TASK5_ONOFF_RATIO = 1;

    // 数据点"任务5类型"对应seekbar滚动条补偿值
    protected static final int TASK5_TYPE_OFFSET = 0;
    // 数据点"任务5类型"对应数据点增量值
    protected static final int TASK5_TYPE_ADDITION = 0;
    // 数据点"任务5类型"对应数据点定义的分辨率
    protected static final int TASK5_TYPE_RATIO = 1;

    // 数据点"任务5每周重复"对应seekbar滚动条补偿值
    protected static final int TASK5_WEEK_REPEAT_OFFSET = 0;
    // 数据点"任务5每周重复"对应数据点增量值
    protected static final int TASK5_WEEK_REPEAT_ADDITION = 0;
    // 数据点"任务5每周重复"对应数据点定义的分辨率
    protected static final int TASK5_WEEK_REPEAT_RATIO = 1;

    // 数据点"任务1日数值"对应seekbar滚动条补偿值
    protected static final int TASK1_DAY_OFFSET = 0;
    // 数据点"任务1日数值"对应数据点增量值
    protected static final int TASK1_DAY_ADDITION = 0;
    // 数据点"任务1日数值"对应数据点定义的分辨率
    protected static final int TASK1_DAY_RATIO = 1;

    // 数据点"任务1分钟值"对应seekbar滚动条补偿值
    protected static final int TASK1_MINUTE_OFFSET = 0;
    // 数据点"任务1分钟值"对应数据点增量值
    protected static final int TASK1_MINUTE_ADDITION = 0;
    // 数据点"任务1分钟值"对应数据点定义的分辨率
    protected static final int TASK1_MINUTE_RATIO = 1;

    // 数据点"任务1周期分钟1"对应seekbar滚动条补偿值
    protected static final int TASK1_CYCLE_MINUTE1_OFFSET = 0;
    // 数据点"任务1周期分钟1"对应数据点增量值
    protected static final int TASK1_CYCLE_MINUTE1_ADDITION = 0;
    // 数据点"任务1周期分钟1"对应数据点定义的分辨率
    protected static final int TASK1_CYCLE_MINUTE1_RATIO = 1;

    // 数据点"任务1周期分钟2"对应seekbar滚动条补偿值
    protected static final int TASK1_CYCLE_MINUTE2_OFFSET = 0;
    // 数据点"任务1周期分钟2"对应数据点增量值
    protected static final int TASK1_CYCLE_MINUTE2_ADDITION = 0;
    // 数据点"任务1周期分钟2"对应数据点定义的分辨率
    protected static final int TASK1_CYCLE_MINUTE2_RATIO = 1;

    // 数据点"任务2日数值"对应seekbar滚动条补偿值
    protected static final int TASK2_DAY_OFFSET = 0;
    // 数据点"任务2日数值"对应数据点增量值
    protected static final int TASK2_DAY_ADDITION = 0;
    // 数据点"任务2日数值"对应数据点定义的分辨率
    protected static final int TASK2_DAY_RATIO = 1;

    // 数据点"任务2分钟值"对应seekbar滚动条补偿值
    protected static final int TASK2_MINUTE_OFFSET = 0;
    // 数据点"任务2分钟值"对应数据点增量值
    protected static final int TASK2_MINUTE_ADDITION = 0;
    // 数据点"任务2分钟值"对应数据点定义的分辨率
    protected static final int TASK2_MINUTE_RATIO = 1;

    // 数据点"任务2周期分钟1"对应seekbar滚动条补偿值
    protected static final int TASK2_CYCLE_MINUTE1_OFFSET = 0;
    // 数据点"任务2周期分钟1"对应数据点增量值
    protected static final int TASK2_CYCLE_MINUTE1_ADDITION = 0;
    // 数据点"任务2周期分钟1"对应数据点定义的分辨率
    protected static final int TASK2_CYCLE_MINUTE1_RATIO = 1;

    // 数据点"任务2周期分钟2"对应seekbar滚动条补偿值
    protected static final int TASK2_CYCLE_MINUTE2_OFFSET = 0;
    // 数据点"任务2周期分钟2"对应数据点增量值
    protected static final int TASK2_CYCLE_MINUTE2_ADDITION = 0;
    // 数据点"任务2周期分钟2"对应数据点定义的分辨率
    protected static final int TASK2_CYCLE_MINUTE2_RATIO = 1;

    // 数据点"任务3日数值"对应seekbar滚动条补偿值
    protected static final int TASK3_DAY_OFFSET = 0;
    // 数据点"任务3日数值"对应数据点增量值
    protected static final int TASK3_DAY_ADDITION = 0;
    // 数据点"任务3日数值"对应数据点定义的分辨率
    protected static final int TASK3_DAY_RATIO = 1;

    // 数据点"任务3分钟值"对应seekbar滚动条补偿值
    protected static final int TASK3_MINUTE_OFFSET = 0;
    // 数据点"任务3分钟值"对应数据点增量值
    protected static final int TASK3_MINUTE_ADDITION = 0;
    // 数据点"任务3分钟值"对应数据点定义的分辨率
    protected static final int TASK3_MINUTE_RATIO = 1;

    // 数据点"任务3周期分钟1"对应seekbar滚动条补偿值
    protected static final int TASK3_CYCLE_MINUTE1_OFFSET = 0;
    // 数据点"任务3周期分钟1"对应数据点增量值
    protected static final int TASK3_CYCLE_MINUTE1_ADDITION = 0;
    // 数据点"任务3周期分钟1"对应数据点定义的分辨率
    protected static final int TASK3_CYCLE_MINUTE1_RATIO = 1;

    // 数据点"任务3周期分钟2"对应seekbar滚动条补偿值
    protected static final int TASK3_CYCLE_MINUTE2_OFFSET = 0;
    // 数据点"任务3周期分钟2"对应数据点增量值
    protected static final int TASK3_CYCLE_MINUTE2_ADDITION = 0;
    // 数据点"任务3周期分钟2"对应数据点定义的分辨率
    protected static final int TASK3_CYCLE_MINUTE2_RATIO = 1;

    // 数据点"任务4日数值"对应seekbar滚动条补偿值
    protected static final int TASK4_DAY_OFFSET = 0;
    // 数据点"任务4日数值"对应数据点增量值
    protected static final int TASK4_DAY_ADDITION = 0;
    // 数据点"任务4日数值"对应数据点定义的分辨率
    protected static final int TASK4_DAY_RATIO = 1;

    // 数据点"任务4分钟值"对应seekbar滚动条补偿值
    protected static final int TASK4_MINUTE_OFFSET = 0;
    // 数据点"任务4分钟值"对应数据点增量值
    protected static final int TASK4_MINUTE_ADDITION = 0;
    // 数据点"任务4分钟值"对应数据点定义的分辨率
    protected static final int TASK4_MINUTE_RATIO = 1;

    // 数据点"任务4周期分钟1"对应seekbar滚动条补偿值
    protected static final int TASK4_CYCLE_MINUTE1_OFFSET = 0;
    // 数据点"任务4周期分钟1"对应数据点增量值
    protected static final int TASK4_CYCLE_MINUTE1_ADDITION = 0;
    // 数据点"任务4周期分钟1"对应数据点定义的分辨率
    protected static final int TASK4_CYCLE_MINUTE1_RATIO = 1;

    // 数据点"任务4周期分钟2"对应seekbar滚动条补偿值
    protected static final int TASK4_CYCLE_MINUTE2_OFFSET = 0;
    // 数据点"任务4周期分钟2"对应数据点增量值
    protected static final int TASK4_CYCLE_MINUTE2_ADDITION = 0;
    // 数据点"任务4周期分钟2"对应数据点定义的分辨率
    protected static final int TASK4_CYCLE_MINUTE2_RATIO = 1;

    // 数据点"任务5日数值"对应seekbar滚动条补偿值
    protected static final int TASK5_DAY_OFFSET = 0;
    // 数据点"任务5日数值"对应数据点增量值
    protected static final int TASK5_DAY_ADDITION = 0;
    // 数据点"任务5日数值"对应数据点定义的分辨率
    protected static final int TASK5_DAY_RATIO = 1;

    // 数据点"任务5分钟值"对应seekbar滚动条补偿值
    protected static final int TASK5_MINUTE_OFFSET = 0;
    // 数据点"任务5分钟值"对应数据点增量值
    protected static final int TASK5_MINUTE_ADDITION = 0;
    // 数据点"任务5分钟值"对应数据点定义的分辨率
    protected static final int TASK5_MINUTE_RATIO = 1;

    // 数据点"任务5周期分钟1"对应seekbar滚动条补偿值
    protected static final int TASK5_CYCLE_MINUTE1_OFFSET = 0;
    // 数据点"任务5周期分钟1"对应数据点增量值
    protected static final int TASK5_CYCLE_MINUTE1_ADDITION = 0;
    // 数据点"任务5周期分钟1"对应数据点定义的分辨率
    protected static final int TASK5_CYCLE_MINUTE1_RATIO = 1;

    // 数据点"任务5周期分钟2"对应seekbar滚动条补偿值
    protected static final int TASK5_CYCLE_MINUTE2_OFFSET = 0;
    // 数据点"任务5周期分钟2"对应数据点增量值
    protected static final int TASK5_CYCLE_MINUTE2_ADDITION = 0;
    // 数据点"任务5周期分钟2"对应数据点定义的分辨率
    protected static final int TASK5_CYCLE_MINUTE2_RATIO = 1;


    /*
     * ===========================================================
     * 以下变量对应设备上报类型为布尔、数值、扩展数据点的数据存储
     * ===========================================================
     */
    // 数据点"开关1"对应的存储数据
    protected static boolean data_OnOff1;
    // 数据点"开关2"对应的存储数据
    protected static boolean data_OnOff2;
    // 数据点"任务1启动开关"对应的存储数据
    protected static boolean data_Task1_Enable;
    // 数据点"任务1周期动作1"对应的存储数据
    protected static int data_Task1_cycle_onoff1;
    // 数据点"任务1周期动作2"对应的存储数据
    protected static int data_Task1_cycle_onoff2;
    // 数据点"任务2启动开关"对应的存储数据
    protected static boolean data_Task2_Enable;
    // 数据点"任务2周期动作1"对应的存储数据
    protected static int data_Task2_cycle_onoff1;
    // 数据点"任务2周期动作2"对应的存储数据
    protected static int data_Task2_cycle_onoff2;
    // 数据点"任务3启动开关"对应的存储数据
    protected static boolean data_Task3_Enable;
    // 数据点"任务3周期动作1"对应的存储数据
    protected static int data_Task3_cycle_onoff1;
    // 数据点"任务3周期动作2"对应的存储数据
    protected static int data_Task3_cycle_onoff2;
    // 数据点"任务4启动开关"对应的存储数据
    protected static boolean data_Task4_Enable;
    // 数据点"任务4周期动作1"对应的存储数据
    protected static int data_Task4_cycle_onoff1;
    // 数据点"任务4周期动作2"对应的存储数据
    protected static int data_Task4_cycle_onoff2;
    // 数据点"任务5启动开关"对应的存储数据
    protected static boolean data_Task5_Enable;
    // 数据点"任务5周期动作1"对应的存储数据
    protected static int data_Task5_cycle_onoff1;
    // 数据点"任务5周期动作2"对应的存储数据
    protected static int data_Task5_cycle_onoff2;
    // 数据点"任务1开关动作"对应的存储数据
    protected static int data_Task1_onoff;
    // 数据点"任务1类型"对应的存储数据
    protected static int data_Task1_type;
    // 数据点"任务1每周重复"对应的存储数据
    protected static int data_Task1_Week_Repeat;
    // 数据点"任务2开关动作"对应的存储数据
    protected static int data_Task2_onoff;
    // 数据点"任务2类型"对应的存储数据
    protected static int data_Task2_type;
    // 数据点"任务2每周重复"对应的存储数据
    protected static int data_Task2_Week_Repeat;
    // 数据点"任务3开关动作"对应的存储数据
    protected static int data_Task3_onoff;
    // 数据点"任务3类型"对应的存储数据
    protected static int data_Task3_type;
    // 数据点"任务3每周重复"对应的存储数据
    protected static int data_Task3_Week_Repeat;
    // 数据点"任务4开关动作"对应的存储数据
    protected static int data_Task4_onoff;
    // 数据点"任务4类型"对应的存储数据
    protected static int data_Task4_type;
    // 数据点"任务4每周重复"对应的存储数据
    protected static int data_Task4_Week_Repeat;
    // 数据点"任务5开关动作"对应的存储数据
    protected static int data_Task5_onoff;
    // 数据点"任务5类型"对应的存储数据
    protected static int data_Task5_type;
    // 数据点"任务5每周重复"对应的存储数据
    protected static int data_Task5_Week_Repeat;
    // 数据点"任务1日数值"对应的存储数据
    protected static int data_Task1_day;
    // 数据点"任务1分钟值"对应的存储数据
    protected static int data_Task1_minute;
    // 数据点"任务1周期分钟1"对应的存储数据
    protected static int data_Task1_cycle_minute1;
    // 数据点"任务1周期分钟2"对应的存储数据
    protected static int data_Task1_cycle_minute2;
    // 数据点"任务2日数值"对应的存储数据
    protected static int data_Task2_day;
    // 数据点"任务2分钟值"对应的存储数据
    protected static int data_Task2_minute;
    // 数据点"任务2周期分钟1"对应的存储数据
    protected static int data_Task2_cycle_minute1;
    // 数据点"任务2周期分钟2"对应的存储数据
    protected static int data_Task2_cycle_minute2;
    // 数据点"任务3日数值"对应的存储数据
    protected static int data_Task3_day;
    // 数据点"任务3分钟值"对应的存储数据
    protected static int data_Task3_minute;
    // 数据点"任务3周期分钟1"对应的存储数据
    protected static int data_Task3_cycle_minute1;
    // 数据点"任务3周期分钟2"对应的存储数据
    protected static int data_Task3_cycle_minute2;
    // 数据点"任务4日数值"对应的存储数据
    protected static int data_Task4_day;
    // 数据点"任务4分钟值"对应的存储数据
    protected static int data_Task4_minute;
    // 数据点"任务4周期分钟1"对应的存储数据
    protected static int data_Task4_cycle_minute1;
    // 数据点"任务4周期分钟2"对应的存储数据
    protected static int data_Task4_cycle_minute2;
    // 数据点"任务5日数值"对应的存储数据
    protected static int data_Task5_day;
    // 数据点"任务5分钟值"对应的存储数据
    protected static int data_Task5_minute;
    // 数据点"任务5周期分钟1"对应的存储数据
    protected static int data_Task5_cycle_minute1;
    // 数据点"任务5周期分钟2"对应的存储数据
    protected static int data_Task5_cycle_minute2;
    // 数据点"厂商"对应的存储数据
    protected static int data_CompanyName;
    // 数据点"产品型号"对应的存储数据
    protected static int data_Modul;

    /*
     * ===========================================================
     * 以下key值对应设备硬件信息各明细的名称，用与回调中提取硬件信息字段。
     * ===========================================================
     */
    protected static final String WIFI_HARDVER_KEY = "wifiHardVersion";
    protected static final String WIFI_SOFTVER_KEY = "wifiSoftVersion";
    protected static final String MCU_HARDVER_KEY = "mcuHardVersion";
    protected static final String MCU_SOFTVER_KEY = "mcuSoftVersion";
    protected static final String WIFI_FIRMWAREID_KEY = "wifiFirmwareId";
    protected static final String WIFI_FIRMWAREVER_KEY = "wifiFirmwareVer";
    protected static final String PRODUCT_KEY = "productKey";

    private Toast mToast;

    @SuppressWarnings("unchecked")
    protected void getDataFromReceiveDataMap(ConcurrentHashMap<String, Object> dataMap) {
        // 已定义的设备数据点，有布尔、数值和枚举型数据

        if (dataMap.get("data") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
            Log.i("liang", "2接收到数据dataMap.get(\"map\")	:" + map);
            Log.i("liang", "2接收到数据dataMap.get(\"data\")	:" + dataMap);
            for (String dataKey : map.keySet()) {


                if (dataKey.equals(KEY_ONOFF1)) {
                    data_OnOff1 = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_ONOFF2)) {
                    data_OnOff2 = (Boolean) map.get(dataKey);
                }

                //任务一
                if (dataKey.equals(KEY_TASK1_ENABLE)) {
                    data_Task1_Enable = (Boolean) map.get(dataKey);
                    timer1Bean.setTask_Enable(data_Task1_Enable);
                }
                if (dataKey.equals(KEY_TASK1_CYCLE_ONOFF1)) {
                    data_Task1_cycle_onoff1 = (Integer) map.get(dataKey);
                    timer1Bean.setTask_cycle_onoff1(data_Task1_cycle_onoff1);
                }
                if (dataKey.equals(KEY_TASK1_CYCLE_ONOFF2)) {
                    data_Task1_cycle_onoff2 = (Integer) map.get(dataKey);
                    timer1Bean.setTask_cycle_onoff2(data_Task1_cycle_onoff2);
                }


                if (dataKey.equals(KEY_TASK2_ENABLE)) {
                    data_Task2_Enable = (Boolean) map.get(dataKey);
                    timer2Bean.setTask_Enable(data_Task2_Enable);
                }
                if (dataKey.equals(KEY_TASK2_CYCLE_ONOFF1)) {
                    data_Task2_cycle_onoff1 = (Integer) map.get(dataKey);
                    timer2Bean.setTask_cycle_onoff1(data_Task2_cycle_onoff1);
                }
                if (dataKey.equals(KEY_TASK2_CYCLE_ONOFF2)) {
                    data_Task2_cycle_onoff2 = (Integer) map.get(dataKey);
                    timer2Bean.setTask_cycle_onoff2(data_Task2_cycle_onoff2);
                }


                if (dataKey.equals(KEY_TASK3_ENABLE)) {
                    data_Task3_Enable = (Boolean) map.get(dataKey);
                    timer3Bean.setTask_Enable(data_Task3_Enable);
                }
                if (dataKey.equals(KEY_TASK3_CYCLE_ONOFF1)) {
                    data_Task3_cycle_onoff1 = (Integer) map.get(dataKey);
                    timer3Bean.setTask_cycle_onoff1(data_Task3_cycle_onoff1);
                }
                if (dataKey.equals(KEY_TASK3_CYCLE_ONOFF2)) {
                    data_Task3_cycle_onoff2 = (Integer) map.get(dataKey);
                    timer3Bean.setTask_cycle_onoff2(data_Task3_cycle_onoff2);
                }


                if (dataKey.equals(KEY_TASK4_ENABLE)) {
                    data_Task4_Enable = (Boolean) map.get(dataKey);
                    timer4Bean.setTask_Enable(data_Task4_Enable);
                }
                if (dataKey.equals(KEY_TASK4_CYCLE_ONOFF1)) {
                    data_Task4_cycle_onoff1 = (Integer) map.get(dataKey);
                    timer4Bean.setTask_cycle_onoff1(data_Task4_cycle_onoff1);
                }
                if (dataKey.equals(KEY_TASK4_CYCLE_ONOFF2)) {
                    data_Task4_cycle_onoff2 = (Integer) map.get(dataKey);
                    timer4Bean.setTask_cycle_onoff2(data_Task4_cycle_onoff2);
                }


                if (dataKey.equals(KEY_TASK5_ENABLE)) {
                    data_Task5_Enable = (Boolean) map.get(dataKey);
                    timer5Bean.setTask_Enable(data_Task5_Enable);
                }
                if (dataKey.equals(KEY_TASK5_CYCLE_ONOFF1)) {
                    data_Task5_cycle_onoff1 = (Integer) map.get(dataKey);
                    timer5Bean.setTask_cycle_onoff1(data_Task5_cycle_onoff1);
                }
                if (dataKey.equals(KEY_TASK5_CYCLE_ONOFF2)) {
                    data_Task5_cycle_onoff2 = (Integer) map.get(dataKey);
                    timer5Bean.setTask_cycle_onoff2(data_Task5_cycle_onoff2);
                }


                if (dataKey.equals(KEY_TASK1_ONOFF)) {

                    data_Task1_onoff = (Integer) map.get(dataKey);
                    timer1Bean.setTask_onoff(data_Task1_onoff);
                }
                if (dataKey.equals(KEY_TASK1_TYPE)) {
                    data_Task1_type = (Integer) map.get(dataKey);
                    timer1Bean.setTask_type(data_Task1_type);
                }
                if (dataKey.equals(KEY_TASK1_WEEK_REPEAT)) {
                    data_Task1_Week_Repeat = (Integer) map.get(dataKey);
                    timer1Bean.setTask_Week_Repeat(data_Task1_Week_Repeat);
                }


                if (dataKey.equals(KEY_TASK2_ONOFF)) {

                    data_Task2_onoff = (Integer) map.get(dataKey);
                    timer2Bean.setTask_onoff(data_Task2_onoff);
                }
                if (dataKey.equals(KEY_TASK2_TYPE)) {

                    data_Task2_type = (Integer) map.get(dataKey);
                    timer2Bean.setTask_type(data_Task2_type);
                }
                if (dataKey.equals(KEY_TASK2_WEEK_REPEAT)) {

                    data_Task2_Week_Repeat = (Integer) map.get(dataKey);
                    timer2Bean.setTask_Week_Repeat(data_Task2_Week_Repeat);
                }


                if (dataKey.equals(KEY_TASK3_ONOFF)) {

                    data_Task3_onoff = (Integer) map.get(dataKey);
                    timer3Bean.setTask_onoff(data_Task3_onoff);
                }
                if (dataKey.equals(KEY_TASK3_TYPE)) {

                    data_Task3_type = (Integer) map.get(dataKey);
                    timer3Bean.setTask_type(data_Task3_type);
                }
                if (dataKey.equals(KEY_TASK3_WEEK_REPEAT)) {

                    data_Task3_Week_Repeat = (Integer) map.get(dataKey);
                    timer3Bean.setTask_Week_Repeat(data_Task3_Week_Repeat);
                }


                if (dataKey.equals(KEY_TASK4_ONOFF)) {

                    data_Task4_onoff = (Integer) map.get(dataKey);
                    timer4Bean.setTask_onoff(data_Task4_onoff);
                }
                if (dataKey.equals(KEY_TASK4_TYPE)) {

                    data_Task4_type = (Integer) map.get(dataKey);
                    timer4Bean.setTask_type(data_Task4_type);
                }
                if (dataKey.equals(KEY_TASK4_WEEK_REPEAT)) {

                    data_Task4_Week_Repeat = (Integer) map.get(dataKey);
                    timer4Bean.setTask_Week_Repeat(data_Task4_Week_Repeat);
                }


                if (dataKey.equals(KEY_TASK5_ONOFF)) {

                    data_Task5_onoff = (Integer) map.get(dataKey);
                    timer5Bean.setTask_onoff(data_Task5_onoff);
                }
                if (dataKey.equals(KEY_TASK5_TYPE)) {

                    data_Task5_type = (Integer) map.get(dataKey);
                    timer5Bean.setTask_type(data_Task5_type);
                }
                if (dataKey.equals(KEY_TASK5_WEEK_REPEAT)) {

                    data_Task5_Week_Repeat = (Integer) map.get(dataKey);
                    timer5Bean.setTask_Week_Repeat(data_Task5_Week_Repeat);
                }


                if (dataKey.equals(KEY_TASK1_DAY)) {

                    data_Task1_day = (Integer) map.get(dataKey);
                    timer1Bean.setTask_day(data_Task1_day);
                }
                if (dataKey.equals(KEY_TASK1_MINUTE)) {

                    data_Task1_minute = (Integer) map.get(dataKey);
                    timer1Bean.setTask_minute(data_Task1_minute);

                    Log.e("==w", "适配器前 data_Task1_minute ；" +data_Task1_minute);
                    Log.e("==w", "适配器前 getTask_minute ；" + timer1Bean.getTask_minute());


                }
                if (dataKey.equals(KEY_TASK1_CYCLE_MINUTE1)) {

                    data_Task1_cycle_minute1 = (Integer) map.get(dataKey);
                    timer1Bean.setTask_cycle_minute1(data_Task1_cycle_minute1);
                }
                if (dataKey.equals(KEY_TASK1_CYCLE_MINUTE2)) {
                    data_Task1_cycle_minute2 = (Integer) map.get(dataKey);
                    timer1Bean.setTask_cycle_minute2(data_Task1_cycle_minute2);
                }


                if (dataKey.equals(KEY_TASK2_DAY)) {

                    data_Task2_day = (Integer) map.get(dataKey);
                    timer2Bean.setTask_day(data_Task2_day);
                }
                if (dataKey.equals(KEY_TASK2_MINUTE)) {

                    data_Task2_minute = (Integer) map.get(dataKey);
                    timer2Bean.setTask_minute(data_Task2_minute);
                }
                if (dataKey.equals(KEY_TASK2_CYCLE_MINUTE1)) {

                    data_Task2_cycle_minute1 = (Integer) map.get(dataKey);
                    timer2Bean.setTask_cycle_minute1(data_Task2_cycle_minute1);
                }
                if (dataKey.equals(KEY_TASK2_CYCLE_MINUTE2)) {

                    data_Task2_cycle_minute2 = (Integer) map.get(dataKey);
                    timer2Bean.setTask_cycle_minute2(data_Task2_cycle_minute2);
                }


                if (dataKey.equals(KEY_TASK3_DAY)) {

                    data_Task3_day = (Integer) map.get(dataKey);
                    timer3Bean.setTask_day(data_Task3_day);
                }
                if (dataKey.equals(KEY_TASK3_MINUTE)) {

                    data_Task3_minute = (Integer) map.get(dataKey);
                    timer3Bean.setTask_minute(data_Task3_minute);
                }
                if (dataKey.equals(KEY_TASK3_CYCLE_MINUTE1)) {

                    data_Task3_cycle_minute1 = (Integer) map.get(dataKey);
                    timer3Bean.setTask_cycle_minute1(data_Task3_cycle_minute1);
                }
                if (dataKey.equals(KEY_TASK3_CYCLE_MINUTE2)) {

                    data_Task3_cycle_minute2 = (Integer) map.get(dataKey);
                    timer3Bean.setTask_cycle_minute2(data_Task3_cycle_minute2);
                }


                if (dataKey.equals(KEY_TASK4_DAY)) {

                    data_Task4_day = (Integer) map.get(dataKey);
                    timer4Bean.setTask_day(data_Task4_day);
                }
                if (dataKey.equals(KEY_TASK4_MINUTE)) {

                    data_Task4_minute = (Integer) map.get(dataKey);
                    timer4Bean.setTask_minute(data_Task4_minute);
                }
                if (dataKey.equals(KEY_TASK4_CYCLE_MINUTE1)) {

                    data_Task4_cycle_minute1 = (Integer) map.get(dataKey);
                    timer4Bean.setTask_cycle_minute1(data_Task4_cycle_minute1);
                }
                if (dataKey.equals(KEY_TASK4_CYCLE_MINUTE2)) {

                    data_Task4_cycle_minute2 = (Integer) map.get(dataKey);
                    timer4Bean.setTask_cycle_minute2(data_Task4_cycle_minute2);
                }


                if (dataKey.equals(KEY_TASK5_DAY)) {

                    data_Task5_day = (Integer) map.get(dataKey);
                    timer5Bean.setTask_day(data_Task5_day);
                }
                if (dataKey.equals(KEY_TASK5_MINUTE)) {

                    data_Task5_minute = (Integer) map.get(dataKey);
                    timer5Bean.setTask_minute(data_Task5_minute);
                }
                if (dataKey.equals(KEY_TASK5_CYCLE_MINUTE1)) {

                    data_Task5_cycle_minute1 = (Integer) map.get(dataKey);
                    timer5Bean.setTask_cycle_minute1(data_Task5_cycle_minute1);
                }
                if (dataKey.equals(KEY_TASK5_CYCLE_MINUTE2)) {

                    data_Task5_cycle_minute2 = (Integer) map.get(dataKey);
                    timer5Bean.setTask_cycle_minute2(data_Task5_cycle_minute2);
                }


                if (dataKey.equals(KEY_COMPANYNAME)) {

                    data_CompanyName = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_MODUL)) {

                    data_Modul = (Integer) map.get(dataKey);
                }
            }
        }

        StringBuilder sBuilder = new StringBuilder();

        // 已定义的设备报警数据点，设备发生报警后该字段有内容，没有发生报警则没内容
        if (dataMap.get("alerts") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");
            for (String alertsKey : map.keySet()) {
                if ((Boolean) map.get(alertsKey)) {
                    sBuilder.append("报警:" + alertsKey + "=true" + "\n");
                }
            }
        }

        // 已定义的设备故障数据点，设备发生故障后该字段有内容，没有发生故障则没内容
        if (dataMap.get("faults") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("faults");
            for (String faultsKey : map.keySet()) {
                if ((Boolean) map.get(faultsKey)) {
                    sBuilder.append("故障:" + faultsKey + "=true" + "\n");
                }
            }
        }

        if (sBuilder.length() > 0) {
            sBuilder.insert(0, "[设备故障或报警]\n");
            myToast(sBuilder.toString().trim());
        }

        // 透传数据，无数据点定义，适合开发者自行定义协议自行解析
        if (dataMap.get("binary") != null) {
            byte[] binary = (byte[]) dataMap.get("binary");
            Log.i("", "Binary data:" + HexStrUtils.bytesToHexString(binary));
        }
    }

    GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        /** 用于设备订阅 */
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            GosControlModuleBaseActivity.this.didSetSubscribe(result, device, isSubscribed);
        }

        ;

        /** 用于获取设备状态 */
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                   java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
            GosControlModuleBaseActivity.this.didReceiveData(result, device, dataMap, sn);
        }

        ;

        /** 用于设备硬件信息 */
        public void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                       java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
            GosControlModuleBaseActivity.this.didGetHardwareInfo(result, device, hardwareInfo);
        }

        ;

        /** 用于修改设备信息 */
        public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
            GosControlModuleBaseActivity.this.didSetCustomInfo(result, device);
        }

        ;

        /** 用于设备状态变化 */
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            GosControlModuleBaseActivity.this.didUpdateNetStatus(device, netStatus);
        }

        ;

    };

    /**
     * 设备订阅回调
     *
     * @param result       错误码
     * @param device       被订阅设备
     * @param isSubscribed 订阅状态
     */
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
    }

    /**
     * 设备状态回调
     *
     * @param result  错误码
     * @param device  当前设备
     * @param dataMap 当前设备状态
     * @param sn      命令序号
     */
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                  java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
    }

    /**
     * 设备硬件信息回调
     *
     * @param result       错误码
     * @param device       当前设备
     * @param hardwareInfo 当前设备硬件信息
     */
    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                      java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
    }

    /**
     * 修改设备信息回调
     *
     * @param result 错误码
     * @param device 当前设备
     */
    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
    }

    /**
     * 设备状态变化回调
     */
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void myToast(String string) {
        if (mToast != null) {
            mToast.setText(string);
        } else {
            mToast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    protected void hideKeyBoard() {
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    /**
     * Description:显示格式化数值，保留对应分辨率的小数个数，比如传入参数（20.3656，0.01），将返回20.37
     *
     * @param date 传入的数值
     * @param
     * @return
     */
    protected String formatValue(double date, Object scale) {
        if (scale instanceof Double) {
            DecimalFormat df = new DecimalFormat(scale.toString());
            return df.format(date);
        }
        return Math.round(date) + "";
    }

}