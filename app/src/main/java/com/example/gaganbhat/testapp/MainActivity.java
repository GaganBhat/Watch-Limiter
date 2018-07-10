package com.example.gaganbhat.testapp;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int ADMIN_INTENT = 15;
    private static final String description = "ez Admin";
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    private NumberPicker timePicker;
    private int minutes = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDevicePolicyManager = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReciever.class);

        Button btnEnableAdmin = (Button) findViewById(R.id.btnEnableAdmin);
        Button btnDisableAdmin = (Button) findViewById(R.id.btnDisableAdmin);
        Button btnLock = (Button) findViewById(R.id.btnLock);
        Button btnStartTimer = (Button) findViewById(R.id.btnStartTimer);

        timePicker = (NumberPicker) findViewById(R.id.numberPicker);
        timePicker.setMinValue(1);
        timePicker.setMaxValue(30);
        timePicker.setValue(10);
        timePicker.setOnValueChangedListener(onValueChangeListener);

        btnEnableAdmin.setOnClickListener(this);
        btnDisableAdmin.setOnClickListener(this);
        btnStartTimer.setOnClickListener(this);
        btnLock.setOnClickListener(this);
        Log.d("MINUTE VAR = ", String.valueOf(minutes));

    }

    NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker timePicker, int i, int i1) {
            minutes = timePicker.getValue();
        }
    };


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnableAdmin:
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,description);
                startActivityForResult(intent, ADMIN_INTENT);
                break;

            case R.id.btnDisableAdmin:
                mDevicePolicyManager.removeActiveAdmin(mComponentName);
                Toast.makeText(getApplicationContext(), "Admin registration removed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnLock:
                boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
                if (isAdmin) {
                    mDevicePolicyManager.lockNow();
                }else{
                    Toast.makeText(getApplicationContext(), "Not Registered as admin", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnStartTimer:
//                new CountDownTimer((long) Utils.minutesToMillis(minutes), (long) Utils.millisToMinutes(minutes) / 10)
                new CountDownTimer((long) ((long) (minutes * 60) * 1000.0), 60000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (minutes > 1) {
                            Toast.makeText(getApplicationContext(), "MINUTES REMAINING = " + (millisUntilFinished / 1000.0) / 60.0 + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onFinish(){
                        Toast.makeText(getApplicationContext(), "FINISHED TIMER OF " + (minutes * 60) * 1000.0 + "millis", Toast.LENGTH_SHORT).show();
                        mDevicePolicyManager.lockNow();
                    }

                }.start();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }


}