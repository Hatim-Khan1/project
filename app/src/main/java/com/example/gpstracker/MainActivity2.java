package com.example.gpstracker;

import static android.widget.Toast.LENGTH_LONG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {
    private static final int REQ_USER_CONSENT=200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    EditText lat,longi;
    Button button1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lat = findViewById(R.id.latitude);
        longi=findViewById(R.id.longitude);
        button1=(Button) findViewById(R.id.openInMap);
        startSmartUserConsent();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?daddr=" + lat + "," + longi;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try
                {
                    startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        ;
                    }
                }
            }

            });


    }


    private void startSmartUserConsent() {
        SmsRetrieverClient client =SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_USER_CONSENT){
            if((resultCode==RESULT_OK)&&(data!=null)){
                String message =data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        Pattern otpPattern= Pattern.compile("\\d+\\.\\d+,\\d+\\.\\d+");
        Matcher matcher =otpPattern.matcher(message);
        if(matcher.find()){
            String s = matcher.group(0);

            String lon = s.split(",")[0];
            String lat = s.split(",")[1];


            this.lat.setText("Lat"+lat);
            this.longi.setText("Longitude"+lon);

        }

    }

    private void registerBroadcastReceiver(){
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener= new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
                startActivityForResult(intent,REQ_USER_CONSENT);
            }

            @Override
            public void onFailure() {

            }
        };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }
}