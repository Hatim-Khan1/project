package com.example.gpstracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    Button sendBtn, micOn;
    EditText txtphoneNo;
    String phoneNo;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = (Button) findViewById(R.id.button);
        micOn=(Button) findViewById(R.id.mic);
        txtphoneNo = (EditText) findViewById(R.id.pNumber);
        micOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message="Mic_ON";
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)
                        ==PackageManager.PERMISSION_GRANTED){
                    sendSMSMessage();
                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},
                            100);
                }

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                message="Location";
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)
                ==PackageManager.PERMISSION_GRANTED){
                    sendSMSMessage();
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("phoneNo", phoneNo);
                    startActivity(intent);

                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},
                            100);
                }
            }
        });
    }

    protected void sendSMSMessage() {
        phoneNo = txtphoneNo.getText().toString();
        if(!phoneNo.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null,message, null, null);

            Toast.makeText(this,"SMS sent",Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this,"Enter Phone Number",Toast.LENGTH_LONG).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==100&& grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
            sendSMSMessage();
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

        }
    }
    private void sendDirectSMS(){
         String SENT = "SMS_SENT", DELIVERED="SMS_DELIVERED";

    }
}






