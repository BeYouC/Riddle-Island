package com.example.riddlez;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.example.android.riddle.R;

import java.util.ArrayList;

/**
 * Created by vince on 7/17/2017.
 */

public class ArchivesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archives_list);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        ArrayList<Arch> words = new ArrayList<Arch>();
/*TODO: testing lower code outside */
        words.clear();
        words.add(new Arch("Adventure One", R.drawable.archives_one, 1));
        words.add(new Arch("Adventure Two", R.drawable.riddle_background_one, 2));
        words.add(new Arch("Adventure Three", R.drawable.option, 3));
        // words.add(new Word("son", "angsi", R.drawable.family_son));
        // words.add(new Word("daughter", "tune", R.drawable.family_daughter));


        ArchivesAdapter adapter = new ArchivesAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.archives_id_list);
        listView.setAdapter(adapter);

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
