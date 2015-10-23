package com.example.test.childlocationtracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by 21502476 on 17/10/2015.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Parse the SMS.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null)
        {
            // Retrieve the SMS.
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++)
            {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // In case of a particular App / Service.
               // if(msgs[i].getOriginatingAddress().equals("0223144902"))
                //{
                //str += "SMS from " + msgs[i].getOriginatingAddress();
                //str += " :";
                str += msgs[i].getMessageBody().toString();
                //str += "n";
                   if(str.equals("get"))
                   {
                        Intent j = new Intent(context, ExtractLocation.class);
                        context.startService(j);
                    }
               // }
            }
            // Display the SMS as Toast.
            //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            //Starts Service
            //Intent i = new Intent(context, ExtractLocation.class);
            //context.startService(i);
        }
    }
}
