package com.example.test.childlocationtracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
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

                String[] words = str.split("\\s+");
                /*for (int k = 0; k <words.length; k++) {
                    words[i] = words[i].replaceAll("[^\\w]", "");
                }*/

                //str += "n";
                if(words[0].equals("get"))
                   {
                       Intent j = new Intent(context, ExtractLocation.class);
                       context.startService(j);
                       SmsManager smsManager = SmsManager.getDefault();
                       if(ExtractLocation.ServiceStatus.equals("Started"))
                       smsManager.sendTextMessage("+64223616617", null, "locReply"+" "+ExtractLocation.Latitude+" "+ExtractLocation.Longitude, null, null);
                    }

                if(words[0].equals("locReply"))
                {
                    Intent mapIntent = new Intent(context, MapsActivity.class);
                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mapIntent.putExtra("coordinates", words[1]+" "+words[2]);
                    context.startActivity(mapIntent);
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
