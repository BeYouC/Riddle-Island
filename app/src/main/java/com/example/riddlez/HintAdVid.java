package com.example.riddlez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.riddle.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class HintAdVid extends AppCompatActivity {
    private RewardedVideoAd HintVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint_ad_vid);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        MobileAds.initialize(getApplicationContext(),
                getString(R.string.admob_app_id));
        // Get reference to singleton RewardedVideoAd object
        HintVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        //setting up the listerner for different stages of videos
        HintVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(getBaseContext(),
                        "Ad opened.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(),
                        "Ad loaded.", Toast.LENGTH_SHORT).show();
                startVid();
            }


            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(getBaseContext(),
                        "Ad started.", Toast.LENGTH_SHORT).show();
                //HintVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(getBaseContext(),
                        "Ad closed.", Toast.LENGTH_SHORT).show();
                finish();
            }
            //make sure to use a reward system
            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(getBaseContext(),
                        "Ad triggered reward.", Toast.LENGTH_SHORT).show();
                Data.putDataHint(2, HintAdVid.this);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(),
                        "Ad left application.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(),
                        "Ad failed to load.", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
        HintVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
        //threadRun();
        Log.i("threaded", "Am I visited after 5 seconds or before?");
        //startVid();

    }
    public void startVid(){
        if (HintVideoAd.isLoaded()) {
            HintVideoAd.show();
        }
    }

    @Override
    public void onResume() {
        HintVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        HintVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        HintVideoAd.destroy(this);
        super.onDestroy();
    }
}
