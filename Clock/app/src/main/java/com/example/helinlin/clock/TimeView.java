package com.example.helinlin.clock;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.jar.Attributes;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by helinlin on 2015/5/29.
 */
public class TimeView extends LinearLayout {
    private TextView tvtime;
    public TimeView(Context context,AttributeSet attrs,int defStyle)
    {
        super(context,attrs,defStyle);
    }
    public TimeView(Context context){
        super(context);
    }
    public TimeView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvtime = (TextView)findViewById(R.id.tvtime);
        tvtime.setText("hello");
    }
    private void FlushTime(){
        Calendar c = Calendar.getInstance();
        tvtime.setText(String.format("%02d:%02d:%02d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)));
        System.out.print("just test");
    };

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(getVisibility() == View.VISIBLE)
        {
            timeHandle.sendEmptyMessage(0);
        }
        else
        {
            timeHandle.removeMessages(0);
        }
    }

    private android.os.Handler timeHandle  = new android.os.Handler(){
    @Override
    public void handleMessage(Message msg) {
        if (getVisibility() == View.VISIBLE) {
            timeHandle.sendEmptyMessageDelayed(0, 1000);
            FlushTime();
        }
    }
};

}
