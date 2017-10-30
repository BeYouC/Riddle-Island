package com.example.riddlez;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.riddle.R;

/**
 * Created by vince on 7/20/2017.
 */

public class AdventureListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_adventure);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        // v: obtain intent data from before
        final Intent oldAdventureIntent = getIntent();
        TextView nameText = (TextView) findViewById(R.id.adventure_name_two);
        nameText.setText(oldAdventureIntent.getExtras().getString("ADVENTURE_NAME"));

        // v2: calling old intent array number, storing as variable for multiple uses later
        final int arrayNum = oldAdventureIntent.getExtras().getInt("ARRAY_NUMBER");

        //v2: somehow found a way to calculate array size by calling Data class.
/* TODO: we can pass in array size now. Fix button size for last row now */
        int size = (Data.updateAdventureNumber(arrayNum)).length; // v: total buttons needed for specific adventure
        int maxSize = 4; // v: maximum amount of buttons in a line
        int maxSizeFinal = maxSize; // v2: used to calculate number text for buttons
        double rowsD = Math.ceil((double) size / maxSize); // v: the amount of LL rows needed. Change to double
        int rows = (int) Math.round(rowsD); // v: convert to int for display
        //D1: counter is used to know which button to enable or disable
        int counter = 0;

        //D1: getting the stored data for riddle num
        int currentRiddleNum = Data.getDataRiddle(this);

        //D1: getting the stored data for Adventure num
        int currentAdventureNum = Data.getDataAdventure(this);

        Button[] tv = new Button[size];
        Button temp;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adventure_list_one);
        LinearLayout linearLayoutTwo = (LinearLayout) findViewById(R.id.adventure_list_two);
        LinearLayout linearLayoutThree = (LinearLayout) findViewById(R.id.adventure_list_three);
        LinearLayout linearLayoutFour = (LinearLayout) findViewById(R.id.adventure_list_four);
        LinearLayout linearLayoutFive = (LinearLayout) findViewById(R.id.adventure_list_five);

        for (int i = 0; i < rows; i++) {
            if (i == rows - 1) {
                maxSize = (size - maxSize * (rows - 1));
            }
            for (int j = 1; j <= maxSize; j++) {
                temp = new Button(this);

                final int textNumber = i * maxSizeFinal + j - 1;
                temp.setText("" + textNumber); //arbitrary task
                // add the textview to the linearlayout
                if (i == 0) {
                    linearLayout.addView(temp);
                } else if (i == 1) {
                    linearLayoutTwo.addView(temp);
                } else if (i == 2) {
                    linearLayoutThree.addView(temp);
                } else if (rows == 3) {
                    linearLayoutThree.addView(temp);
                } else if (rows == 4) {
                    linearLayoutFour.addView(temp);
                } else if (rows == 5) {
                    linearLayoutFive.addView(temp);
                }

                tv[j] = temp;

                // v: creates border between buttons and sets weight
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) temp.getLayoutParams();
                params.setMargins(5, 0, 5, 0); //left, top, right, bottom
                params.weight = 1;
                temp.setLayoutParams(params);

                // v: rounds off the button, format, etc
                temp.setBackgroundResource(R.drawable.button_background);
                temp.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        // Set a click listener on that View
                        Intent adventureIntent = new Intent(view.getContext(), RiddleMainActivity.class);
                        // v: if -1, we in trouble! (gets sent if error with ARRAY_NUMBER call)
                        adventureIntent.putExtra("ADVENTURE_NUMBER", arrayNum);
                        adventureIntent.putExtra("QUESTION_NUMBER", textNumber);
                        adventureIntent.putExtra("FROM_ARCHIVES", true);
                        startActivity(adventureIntent);
                    }
                });
                //D1: setting the buttons to be accessed depending on the user's current progress in the riddle
/*TODO: v2: Check if what I did is correct. Set to >= and changed second "if" to "else if". Also counter can be replaced by textNumber?*/
                if (arrayNum >= currentAdventureNum) {
                    temp.setEnabled(false);
                }
            }
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

