package com.example.test.childlocationtracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class ScreenOnOffBroadcastReceiver extends BroadcastReceiver {
    private static int powerButtonPressCount=0;
    private DBHelper dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper=new DBHelper(context);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            powerButtonPressCount+=1;
            sendSmsOnCondition(dbHelper.getContact("Parent").toString());
            Toast.makeText(context,"Screen Off "+powerButtonPressCount, Toast.LENGTH_LONG).show();

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            powerButtonPressCount+=1;
            sendSmsOnCondition(dbHelper.getContact("Parent").toString());
            Toast.makeText(context,"Screen On "+powerButtonPressCount,Toast.LENGTH_LONG).show();

        }

        // Toast.makeText(context, "BroadcastReceiver :"+screenOff, Toast.LENGTH_SHORT).show();
    }

    public void sendEmergencySMS(String message,String destNumber)
    {
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(destNumber,null,message,null,null);
    }

    public void sendSmsOnCondition(String destNumber)
    {
        if(powerButtonPressCount==4)
        {
            String msg="Emergency"+" "+ExtractLocation.Latitude+" "+ExtractLocation.Longitude+" "+"Emergency";
            sendEmergencySMS(msg,destNumber);
            powerButtonPressCount=0;
        }
    }
}
