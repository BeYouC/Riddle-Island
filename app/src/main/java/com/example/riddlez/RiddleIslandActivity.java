package com.example.riddlez;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.view.MotionEventCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.Button;

        import com.example.android.riddle.R;

/**
 * Created by vince on 8/19/2017.
 */

public class RiddleIslandActivity extends AppCompatActivity {

    Button holderButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_island);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        for (int ab = 1; ab < 4; ++ab) {

            holderButton = updateButton(ab);

            if(Data.getDataAdventure(RiddleIslandActivity.this)<ab){ // v3: if adventure is not yet unlocked
                holderButton.setEnabled(false);
            } else {
                implementOnClick(holderButton, ab);
            }
        }


    }

    public Button updateButton(int ab){
        switch (ab) {
            case 1:
                holderButton = (Button) findViewById(R.id.island_one);
                return holderButton;
            case 2:
                holderButton = (Button) findViewById(R.id.island_two);
                return holderButton;
            case 3:
                holderButton = (Button) findViewById(R.id.island_three);
                return holderButton;
            default:
                holderButton = (Button) findViewById(R.id.island_one);
                return holderButton;
        }
    }

    public void implementOnClick(final Button button, final int ab) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // v2: pass in adventure number and question number
                final int currentAdventureNum = Data.getDataAdventure(RiddleIslandActivity.this);
                final int currentRiddleNum = Data.getDataRiddle(RiddleIslandActivity.this);

                Intent adventureIntent = new Intent(view.getContext(), RiddleMainActivity.class);
                adventureIntent.putExtra("ADVENTURE_NUMBER", ab);
                adventureIntent.putExtra("QUESTION_NUMBER", 0);
                //riddleIsland.getInt(view.getContext().getString(R.string.riddleIslandNumber), 1)


                if (currentAdventureNum == ab) {  // if its the current adventure, start from saved riddle number
                    adventureIntent.putExtra("QUESTION_NUMBER", currentRiddleNum);
                    startActivity(adventureIntent);
                } else { // v3: set both to true as just replaying, don't save.
                    adventureIntent.putExtra("RERUN_ADVENTURE", true);
                    adventureIntent.putExtra("FROM_ARCHIVES", true);
                    startActivity(adventureIntent);
                }
            }
        });
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


