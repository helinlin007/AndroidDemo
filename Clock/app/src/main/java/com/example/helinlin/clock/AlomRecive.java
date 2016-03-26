package com.example.helinlin.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by helinlin on 2015/6/17.
 */
public class AlomRecive extends BroadcastReceiver{
    public void onReceive(Context context,Intent intent){
     System.out.print("闹钟");
    }
}
