package com.example.riddlez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.riddle.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class loadingScreen extends AppCompatActivity {
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
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
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        //setting up the listerner for different stages of videos
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(getBaseContext(),
                        "Ad opened.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(),
                        "Ad loaded.", Toast.LENGTH_SHORT).show();
                mRewardedVideoAd.show();
            }


            @Override
            public void onRewardedVideoStarted() {
                //mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
                Toast.makeText(getBaseContext(),
                        "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(getBaseContext(),
                        "Ad closed.", Toast.LENGTH_SHORT).show();
                Intent j = new Intent(loadingScreen.this, MainActivity.class);
                startActivity(j);
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(getBaseContext(),
                        "Ad triggered reward.", Toast.LENGTH_SHORT).show();
                //reseting the data and hint after watching a video
                Data.putDataProgress(Data.getTotalProgress(), loadingScreen.this);
                Data.putDataHint(2, loadingScreen.this);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(),
                        "Ad left application.", Toast.LENGTH_SHORT).show();
                Intent j = new Intent(loadingScreen.this, MainActivity.class);
                startActivity(j);
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(),
                        "Ad failed to load.", Toast.LENGTH_SHORT).show();
                Intent j = new Intent(loadingScreen.this, MainActivity.class);
                startActivity(j);
            }

        });
        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
    }
    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
