package com.example.women_safetyapp;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telecom.PhoneAccountHandle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

//**********Screen 2************
public class MainActivity2 extends AppCompatActivity {
    Button add_contacts, EMERGENCY;
    private static final int REQUEST_LOCATION=1;
    String no,msg,name;
    StringBuffer buffer1[],buffer2[];
    DBHandler DB;
    LocationManager locationManager;
    String latitude,longitude,locationn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        String name1 = intent.getStringExtra("N1");
        this.setTitle("Hello: " + name1);
        add_contacts = findViewById(R.id.b1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        add_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, register.class);
                startActivity(intent);
            }
        });

        // protected void onCreate (Bundle savedInstanceState){
        // super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main2);
        EMERGENCY = findViewById(R.id.b2);
        EMERGENCY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DB = new DBHandler(MainActivity2.this);
                Cursor cursor = DB.getdata();
                buffer1 = new StringBuffer[cursor.getCount()];
                buffer2 = new StringBuffer[cursor.getCount()];
                for (int i = 0; i < cursor.getCount(); i++) {
                    buffer1[i] = new StringBuffer();
                    buffer2[i] = new StringBuffer();
                }
                int i = 0;
                while (cursor.moveToNext()) {
                    buffer1[i].append(cursor.getString(0));
                    buffer2[i].append(cursor.getString(1));
                    i++;
                }

                // Check if the buffer1 array is null or empty
                if (buffer1 == null || buffer1.length == 0) {
                    Toast.makeText(MainActivity2.this, "No emergency contacts available.", Toast.LENGTH_SHORT).show();
                    return;
                }

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    onGPS();
                } else {
                    getLocation();
                }
                //Toast.makeText(getApplicationContext(),buffer[0]+"\n"+buffer[1],Toast.LENGTH_LONG).show();
                if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.SEND_SMS},
                            100);
                }

                // Check if there are any contacts in the database
                String firstContactNumber = buffer2[0].toString();

                // Check if the device has multiple SIM cards
                if (hasMultipleSIM(MainActivity2.this)) {
                    // Get the default SIM card ID
                    String defaultSimId = getDefaultSIM(MainActivity2.this);

                    // Get the PhoneAccountHandle for the default SIM card
                    PhoneAccountHandle defaultPhoneAccountHandle = getPhoneAccountHandleBySimId(MainActivity2.this, defaultSimId);

                    if (defaultPhoneAccountHandle != null) {
                        // Create an intent to initiate a phone call with the default SIM
                        Intent callIntent = new Intent(Intent.ACTION_CALL)
                                .setData(Uri.parse("tel:" + firstContactNumber))
                                .putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, defaultPhoneAccountHandle);
                        startActivity(callIntent);
                    } else {
                        // If no default PhoneAccountHandle found, request the CALL_PHONE permission and initiate the call
                        ActivityCompat.requestPermissions(MainActivity2.this,
                                new String[]{Manifest.permission.CALL_PHONE}, 200);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + firstContactNumber));
                        startActivity(intent);
                    }
                } else {
                    // If single SIM device, initiate the call directly
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + firstContactNumber));
                    startActivity(callIntent);
                }
            }
        });
    }

    // Method to check if the device has multiple SIM cards
    private boolean hasMultipleSIM(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            return subscriptionManager.getActiveSubscriptionInfoCount() > 1;
        } else {
            // If the method is not available (pre-Lollipop), assume single SIM
            return false;
        }
    }

    // Method to get the default SIM card ID
    private String getDefaultSIM(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return telephonyManager.getSimSerialNumber();
        } else {
            // If the method is not available (pre-Lollipop), return null
            return null;
        }
    }

    // Method to get the PhoneAccountHandle by SIM ID
    private PhoneAccountHandle getPhoneAccountHandleBySimId(Context context, String simId) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (PhoneAccountHandle handle : telecomManager.getCallCapablePhoneAccounts()) {
                String accountId = handle.getId();
                if (accountId.contains(simId)) {
                    return handle;
                }
            }
        }
        return null;
    }
    private void onGPS() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void getLocation()
    {
        if(ActivityCompat.checkSelfPermission(MainActivity2.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity2.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        else
        {
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null)
            {
                double lat=location.getLatitude();
                double longi=location.getLongitude();
                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                locationn=latitude+","+longitude;
            }
            else
            {

            }
        }
    }
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            sendSMS();
        }
        else
        {

        }
    }


    private void sendSMS() {
        for (int i = 0; i < buffer1.length; i++) {
            name = new String(buffer1[i]);
            no = new String(buffer2[i]);

            msg = "Hey " + name + ", I am in danger and need help. Please urgently reach me out. Here are my coordinates: \n" + "http://maps.google.com/?q=" + locationn;
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(no, null, msg, null, null);
        }
        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
    }




}

