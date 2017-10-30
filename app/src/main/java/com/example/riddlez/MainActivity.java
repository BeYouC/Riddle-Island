package com.example.riddlez;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.example.android.riddle.R;


/*D1:8/11/17; v2:8/14/17;  */

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    //D2: interstital ad
    public static InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //interstial add is set here
        mInterstitialAd = new InterstitialAd(this);
        //D2: set below to something else for actual app
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
            }
        });
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Button archives = (Button) findViewById(R.id.archives);
        Button contButton = (Button)findViewById(R.id.continueAdven);
        //setting the onclick for the continue button
        //get the stored data using sharedPreference then go to that adventure/riddle
        contButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //if u finished all the adventures
                if (Data.getDataAdventure(MainActivity.this) > Data.getCurrentAdventureNum()) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Adventure Finished")
                            .setMessage("You have safely Arrived Home")
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
                else {
                    Intent riddleIslandIntent = new Intent(view.getContext(), RiddleIslandActivity.class);
                    startActivity(riddleIslandIntent);
                }
            }
        });

// Set a click listener on that View
        archives.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent archivesIntent = new Intent(MainActivity.this, ArchivesActivity.class);
                startActivity(archivesIntent);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void popUp(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.mainpagemenu);//uses a menu to list out the options
        popup.show();
    }

    //D1: function called with n you click on one of the items in the pop up menu
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reset:
                Data.putDataAdventure(1, MainActivity.this);
                Data.putDataRiddle(0, MainActivity.this);
                Data.putDataHint(Data.getTotalHints(), MainActivity.this);
                Data.putDataProgress(Data.getTotalProgress(), MainActivity.this);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }


}
