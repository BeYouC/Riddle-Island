package com.example.riddlez;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.android.riddle.R;

import static com.example.riddlez.RiddleMainActivity.alertDialogBuilder;

public class BroadcastService extends Service {
    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.example.riddlez.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                alertDialogBuilder.setTitle("You slowly open your eyes...")
                .setMessage("only to see that darned riddle again") .show();
                alertDialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // v2: change hint count upon death. Can use if statement to determine count based on watch video or not
                        //Log.i("current", "total progress" + Data.getTotalProgress());
                        Data.putDataHint(2, BroadcastService.this);
                        Data.putDataProgress(Data.getTotalProgress(), BroadcastService.this);
                        Intent i = new Intent(COUNTDOWN_BR);
                        i.putExtra("FINISHED", true);
                        sendBroadcast(i);
                        //alertDialogBuilder.setCancelable(true);
                        //Intent i = new Intent(BroadcastService.this, MainActivity.class);
                        //i.putExtra("PROGRESS_NUMBER", Data.getTotalProgress());
                        //startActivity(i);
                    }
                })
                        .show();
            }
        };

        cdt.start();
    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
