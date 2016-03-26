package com.example.helinlin.clock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by helinlin on 2015/5/29.
 */
public class AlomView extends LinearLayout{
   public AlomView(Context context,AttributeSet attrs,int defStyle)
   {
       super(context,attrs,defStyle);
       Init();
   }
    public  AlomView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
        Init();
    }
    public AlomView(Context context)
    {
        super(context);
        Init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 查找资源
        addAlom = (Button)findViewById(R.id.addAlom);
        alomList = (ListView)findViewById(R.id.alomList);
        alomArray = new ArrayAdapter<AlomData>(getContext(),R.layout.support_simple_spinner_dropdown_item);
        alomList.setAdapter(alomArray);
        ReadAlomList();
        addAlom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlom();
            }
        });

        alomList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("操作选项").setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                              DelAlomItem(position);
                            break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("取消",null).show();
                return true;
            }
        });
    }
    private void DelAlomItem(int postion)
    {
        alomArray.remove(alomArray.getItem(postion));
        saveAlomList();
    }
    private void saveAlomList()
    {
        SharedPreferences.Editor editor =
                getContext().getSharedPreferences(AlomView.class.getName(), Context.MODE_PRIVATE).edit();
        StringBuffer stringbuf = new StringBuffer();
        for (int i = 0;i < alomArray.getCount();i++)
        {
            stringbuf.append(alomArray.getItem(i).GetTime()).append(",");
        }
        editor.putString(KEY_ALOM_LIST,stringbuf.toString().substring(0,stringbuf.length() - 1));
        editor.commit();
    }
    private void ReadAlomList()
    {
        SharedPreferences sp = getContext().getSharedPreferences(AlomView.class.getName(),Context.MODE_PRIVATE);
        String context = sp.getString(KEY_ALOM_LIST,null);
        if (context != null)
        {
            String[] timeStr;
            timeStr = context.split(",");
            for (String string:timeStr)
            {
                alomArray.add(new AlomData(Long.parseLong(string)));
            }
        }
    }
    private void AddAlom(){
        Calendar systemTime = Calendar.getInstance();
        new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                if (true == timeFlag)
                {
                    timeFlag = false;
                    return;
                }
                System.out.print("-------\r\n");
                Calendar alomTime = Calendar.getInstance();
                alomTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                alomTime.set(Calendar.MINUTE,minute);
                // 判断闹钟是否有效
                if (Calendar.getInstance().getTimeInMillis() >= alomTime.getTimeInMillis())
                {
                    alomTime.setTimeInMillis(alomTime.getTimeInMillis() + 24*60*60*1000);
                }
                alomArray.add(new AlomData(alomTime.getTimeInMillis()));
                timeFlag = true;
                saveAlomList();         // 保存闹钟列表
                m_alarmanger.setRepeating(
                        AlarmManager.RTC_WAKEUP,alomTime.getTimeInMillis(),5*60*1000,
                        PendingIntent.getBroadcast(getContext(),0,new Intent(getContext(),AlomRecive.class),0));
            }
        },systemTime.get(Calendar.HOUR_OF_DAY),systemTime.get(Calendar.MINUTE),true).show();
    }
    private void Init(){
        m_alarmanger = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
    }
    // 变量资源
    private  Button addAlom;
    private ListView alomList;
    private  ArrayAdapter<AlomData> alomArray;
    private static  final  String KEY_ALOM_LIST= "alomList";
    private boolean timeFlag;
    private AlarmManager m_alarmanger;
    // 数据结构
    private static class AlomData
    {
        public AlomData(long time){
            this.time = time;
            date = Calendar.getInstance();
            date.setTimeInMillis(time);
            timeStr = String.format("%04d-%02d-%02d %02d:%02d:%02d",
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DATE),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE),
                    date.get(Calendar.SECOND));
        }
        private long GetTime(){
            return time;
        }
        private  String GetTimeString(){
            return timeStr;
        }
        @Override
        public String toString() {
          return GetTimeString();
        }

        private long time = 0;
        private  String timeStr = "";
        private Calendar date;
    }

}
