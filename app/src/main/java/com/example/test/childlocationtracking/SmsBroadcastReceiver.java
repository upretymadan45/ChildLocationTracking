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
    private DBHelper dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Parse the SMS.
        dbHelper=new DBHelper(context);
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
                String originatingAddress=msgs[i].getOriginatingAddress();
                //str += "n";
                if(words[0].equalsIgnoreCase("get"))
                   {
                       Intent j = new Intent(context, ExtractLocation.class);
                       context.startService(j);
                       sendMessage(dbHelper.getContact("Parent").toString(), "locReply" + " " + ExtractLocation.Latitude + " " + ExtractLocation.Longitude);
                       abortBroadcast();
                    }

                if(words[0].equalsIgnoreCase("locReply"))
                {
                    Intent mapIntent = new Intent(context, MapsActivity.class);
                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mapIntent.putExtra("coordinates", words[1] + " " + words[2]);
                    abortBroadcast();
                    context.startActivity(mapIntent);
                }

                if(words[0].equalsIgnoreCase("speed"))
                {
                    sendMessage(dbHelper.getContact("Parent").toString(), ExtractLocation.Speed);
                    abortBroadcast();
                }
                if(words[0].equalsIgnoreCase("Insert"))
                {
                    int numberOfWords=words.length;
                    String address="";
                    dbHelper=new DBHelper(context);
                    GetGeofenceDataFromDB gf=new GetGeofenceDataFromDB();
                    if(numberOfWords-4>1)
                    {
                        address=words[1]+" "+words[2];
                    }
                    if(numberOfWords-4==1)
                    {
                        address=words[1];
                    }
                    gf.Address=address;
                    gf.Latitude=words[numberOfWords-3].toString();
                    gf.Longitude=words[numberOfWords-2].toString();
                    gf.Radius=words[numberOfWords-1].toString();
                    dbHelper.addGeofence(gf);
                    abortBroadcast();
                }
                if(words[0].equalsIgnoreCase("Update"))
                {
                    int numberOfWords=words.length;
                    String address="";
                    dbHelper=new DBHelper(context);
                    GetGeofenceDataFromDB gf=new GetGeofenceDataFromDB();
                    if(numberOfWords-5>1)
                    {
                        address=words[1]+" "+words[2];
                    }
                    if(numberOfWords-5==1)
                    {
                        address=words[1];
                    }
                    gf.Address=words[1].toString();
                    gf.Latitude=words[numberOfWords-4].toString();
                    gf.Longitude=words[numberOfWords-3].toString();
                    gf.Radius=words[numberOfWords-2].toString();
                    gf.Id=Integer.parseInt(words[numberOfWords - 1].toString());
                    dbHelper.updateGeofence(gf);
                    abortBroadcast();
                }
                if(words[0].equalsIgnoreCase("Delete"))
                {
                    dbHelper=new DBHelper(context);
                    dbHelper.deleteGeofence(words[1]);
                    abortBroadcast();
                }
                if(words[0].equalsIgnoreCase("startgeofence"))
                {
                    Intent startGeofence=new Intent(context,GeofenceService.class);
                    context.startService(startGeofence);
                    abortBroadcast();
                }
                if(words[0].equalsIgnoreCase("stopgeofence"))
                {
                    Intent startGeofence=new Intent(context,GeofenceService.class);
                    context.stopService(startGeofence);
                    abortBroadcast();
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
    public void sendMessage(String destNumber,String message)
    {
        SmsManager smsManager = SmsManager.getDefault();
        if(ExtractLocation.ServiceStatus.equals("Started"))
            smsManager.sendTextMessage(destNumber, null, message, null, null);
    }
}
