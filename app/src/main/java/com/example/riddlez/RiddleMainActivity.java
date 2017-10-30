package com.example.riddlez;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.riddle.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

public class RiddleMainActivity extends AppCompatActivity{
    private static final String TAG = "TAG";
    //private RewardedVideoAd HintVideoAd;
    public String[][] data;


    TextView question;
    LinearLayout answer;
    LinearLayout choice;
    LinearLayout choice2;
    ProgressBar myBar;
    EditText answerGoes;
    int currentProgress = Data.getTotalProgress();
    int currentRiddle = 0;
    public int totalHints = Data.getTotalHints(); // v2: total hints stored in data
    int hint = totalHints;
    int hintCount = 0;
    int counter = 0;
    int deathHintBonus = 2;  // v2: new hint count after dying
    int adventure_number;
    boolean fromArchives;
    boolean reRunAdventure;
    String correct = "";
    int clicked = 0;
    String possibleAnswer = "";
    String possibleAnswer2 = "";
        Vector idForButton = new Vector();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
/*TODO: made data into global but funny how i have to get return from "updateAdventureNumber. tried to just update in method but no work */
        // v: intent retrieves data sent from archives
        final Intent oldArchiveIntent = getIntent();
// TODO: maybe don't need to take in extra intents? Yes when its from archives.
        currentRiddle = oldArchiveIntent.getExtras().getInt("QUESTION_NUMBER"); // v: currentRiddle now updated
        adventure_number = oldArchiveIntent.getExtras().getInt("ADVENTURE_NUMBER");
        reRunAdventure = oldArchiveIntent.getBooleanExtra("RERUN_ADVENTURE", false);
        fromArchives = oldArchiveIntent.getBooleanExtra("FROM_ARCHIVES", false);
        // v3: how many hints you start with depends on where you open RiddleMainActivity from
        if (reRunAdventure){
            hint = Data.getTotalHints();
        } else if (fromArchives){
            hint = deathHintBonus;
        }
        else {
            // v2: get correct number of hints every time on create (does not reset when exit app)
            hint = Data.getDataHint(this);
        }

        data = Data.updateAdventureNumber(adventure_number);
        question = (TextView) findViewById(R.id.question);
        answer = (LinearLayout) findViewById(R.id.answer);
        choice = (LinearLayout) findViewById(R.id.choice);
        choice2 = (LinearLayout) findViewById(R.id.choice2);
        myBar = (ProgressBar) findViewById(R.id.healthBar);
        //D2: updating health bar
        currentProgress = Data.getDataProgress(this);
        /*if(currentProgress == deathHintBonus || currentProgress == Data.getTotalHints()){
            currentProgress = Data.getTotalProgress();
        }*/
        myBar.setProgress(currentProgress);
        //if the person is dead but try to play riddle
        if(currentProgress == 0){
            //interstial add
            if (MainActivity.mInterstitialAd.isLoaded()) {
                MainActivity.mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            deathPopUp();
        }
        else {
            delete();
            update(currentRiddle);
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
    //D1: the function called when you click the top right "Option" button

    //"updating" the whole screen to move on to the next riddle
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void update(final int question) {
        // D2: Load a reward based video ad
// TODO: I don't think needed. Will screw up saved state when calling from archives
        //save the answer on to a variable
        final String theAnswer = data[question][1];
        //D1: making adjustment to the textsize so that it fits the screen
/* TODO: manually setting text size like this may not work if say on tablet? i could be wrong. Also delete log? */
        if (data[question][0].length() < 80) {
            this.question.setText(data[question][0]);
            this.question.setTextSize(30);
            Log.i("textSize", "" + this.question.getTextSize());

        } else {
            this.question.setText(data[question][0]);
            this.question.setTextSize(23);
        }

        //D1: changing the "remove" button so that it has a different functionality than before
        int i = 0;
        Button removeIt = (Button) findViewById(R.id.remove);
        // if the length of the answer is greater than 8 use EDITTEXT rather than Button to get the riddle right
        if (theAnswer.length() > 8 || theAnswer.indexOf(" ") >= 0) {
            removeIt.setText("Words");
            removeIt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    choice.removeAllViews();
                    choice2.removeAllViews();
                    TextView answerRelated = new TextView(RiddleMainActivity.this);
                    answerRelated.setTextColor(Color.WHITE);
                    answerRelated.setTextSize(20);
                    TextView answerRelated2 = new TextView(RiddleMainActivity.this);
                    answerRelated2.setTextColor(Color.WHITE);
                    answerRelated2.setTextSize(20);
                    Button thisButton = (Button) findViewById(v.getId());
                    //the first time the button is clicked
                    if (clicked == 0) {
                        int z = 0;
                        List<String> myList = new ArrayList<String>();
                        //adding the answer to an array
                        while (z < theAnswer.length()) {
                            myList.add(String.valueOf(theAnswer.charAt(z)));
                            z++;
                        }
                        //adding additional random words to the array
                        Random r = new Random();
                        int random = r.nextInt(4 - 1) + 2;
                        z = 0;
                        while (z < random) {
                            int randomLetter = r.nextInt(27 - 1) + 1;
                            myList.add(getCharForNumber(randomLetter));
                            z++;
                        }
                        //shuffling the array
                        Collections.shuffle(myList);
                        z = 0;
                        //the first half of the array is saved in to a string called possible answer
                        while (z < myList.size() / 2) {
                            if (myList.get(z).equals(" ")) {
                                possibleAnswer += "space   ";
                            } else {
                                possibleAnswer += myList.get(z) + "   ";
                            }
                            z++;
                        }
                        //the second half of the array is saved in to a string called possible answer2
                        while (z < myList.size()) {
                            if (myList.get(z).equals(" ")) {
                                possibleAnswer2 += "space   ";
                            } else {
                                possibleAnswer2 += myList.get(z) + "   ";
                            }
                            z++;
                        }
                        //set the choice linearlayout to exhibit the possible Answer sting
                        answerRelated.setText(possibleAnswer);
                        choice.addView(answerRelated);
                        //set the choice2 linearlayout to exhibit the possible Answer2 sting
                        answerRelated2.setText(possibleAnswer2);
                        choice2.addView(answerRelated2);
                        thisButton.setText("Length");
                        myList.clear();
                    }
                    //if the button is clicked every even times, show the same  possibleanswer and possibleanswer2 strings
                    else if (clicked % 2 == 0) {
                        answerRelated.setText(possibleAnswer);
                        choice.addView(answerRelated);
                        answerRelated2.setText(possibleAnswer2);
                        choice2.addView(answerRelated2);
                        thisButton.setText("Length");
                    }
                    //if the button is clieed eery odd times, show the length of the words to the player
                    else {
                        answerRelated.setText("" + theAnswer.length());
                        choice.addView(answerRelated);
                        thisButton.setText("Words");
                    }
                    clicked++;

                }
            });
            //putting the EDITTEXT for the player to type in the answers
            answerGoes = new EditText(this);
            answerGoes.setTextColor(Color.WHITE);
            answerGoes.setHint("Answer Here");
            answerGoes.setHintTextColor(Color.GRAY);
            answerGoes.setInputType(TYPE_TEXT_FLAG_CAP_CHARACTERS);
            LinearLayout.LayoutParams editDetails = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            answer.addView(answerGoes, editDetails);
        } else {
            //if the length of the answer does not contain space and is less than 9 then change the riddle back to its original format
            Button remove = (Button) findViewById(R.id.remove);
            remove.setText("remove");
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(v);
                }
            });
            List<String> myList = new ArrayList<String>();
            int widthAnswer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            int widthChoice = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            while (i < theAnswer.length()) {
                TextView answerBox = new TextView(this);
                answerBox.setId(i);
                myList.add(String.valueOf(theAnswer.charAt(i)));
                LinearLayout.LayoutParams textDetails = new LinearLayout.LayoutParams(
                        widthAnswer,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                answerBox.setBackgroundResource(R.drawable.draw);
                answerBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                answerBox.setTextSize(30);
                answerBox.setTextColor(Color.WHITE);
                answer.addView(answerBox, textDetails);
                i++;

            }
            i = 0;
            //add in random words
            while (i < 10 - theAnswer.length()) {
                Random r = new Random();
                int random = r.nextInt(27 - 1) + 1;
                myList.add(getCharForNumber(random));
                i++;
            }
            Collections.shuffle(myList);
            i = 0;
            //making the first 5 buttons for the answer choices
            //TODO: make simple using if statements
            //Dynamically creating buttons that are used to enter answers to riddles
            while (i < 5) {
                Button choose = new Button(this);
                //setting the ID's for the buttons
                choose.setId(i + 10);
                //setting the width and height for the buttons
                LinearLayout.LayoutParams buttonDetails = new LinearLayout.LayoutParams(
                        widthChoice,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                //setting the onclick for the buttons
                choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button pressed = (Button) findViewById(v.getId());
                        //obtain the letter in the button
                        String setVal = pressed.getText().toString();
                        if (counter < theAnswer.length()) {
                            //vector idForButton is used to store all the pressed Buttons' ID which can be used when you click the "remove" Button later on
                            idForButton.add(v.getId());
                            //obtain the textView where the answer goes
                            TextView currText = (TextView) findViewById(counter);
                            currText.setText(setVal);
                            correct += setVal;
                            counter++;
                            pressed.setEnabled(false);

                        }


                    }
                });
                choose.setText(myList.get(i));
                choice.addView(choose, buttonDetails);
                i++;
            }
            //setting the next 5 buttons
            while (i < 10) {
                Button choose = new Button(this);
                choose.setId(i + 10);
                LinearLayout.LayoutParams buttonDetails = new LinearLayout.LayoutParams(
                        widthChoice,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button pressed = (Button) findViewById(v.getId());
                        String setVal = pressed.getText().toString();
                        if (counter < theAnswer.length()) {
                            idForButton.add(v.getId());
                            TextView currText = (TextView) findViewById(counter);
                            currText.setText(setVal);
                            correct += setVal;
                            counter++;
                            pressed.setEnabled(false);

                        }
                    }
                });
                choose.setText(myList.get(i));
                choice2.addView(choose, buttonDetails);
                i++;
            }
        }

    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 64)) : null;
    }

    //D1: remove all views for 3 different linear layout
    public void delete() {
        answer.removeAllViews();
        choice.removeAllViews();
        choice2.removeAllViews();

    }

    //D1: remove the words from the answer key and enable the disabled buttons again using idForButton
    public void remove(View view) {
        if (counter == 0) {
            TextView currText = (TextView) findViewById(counter);
            currText.setText("");
            correct = "";
        } else if (counter > 0) {
            counter--;
            TextView currText = (TextView) findViewById(counter);
            currText.setText("");
            correct = correct.substring(0, correct.length() - 1);
        }
        if (!idForButton.isEmpty()) {
            int tempId = (int) idForButton.remove(idForButton.size() - 1);
            Button button = (Button) findViewById(tempId);
            button.setEnabled(true);
        }
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "hello");
                updateGUI(intent); // or whatever method used to update your GUI fields
            }
        };



    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            if(intent.getExtras().getBoolean("FINISHED")){
                Intent m = new Intent(RiddleMainActivity.this, MainActivity.class);
                startActivity(m);
            }
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);
            //change the countdown
            alertDialog.setMessage("seconds remaining: " + millisUntilFinished / 1000);
            /*final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RiddleMainActivity.this)
                    .setTitle("You try coming back to your senses")
                    .setMessage("");
            alertDialogBuilder.setCancelable(false); // cannot get out of builder screen

            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/

        }
    }
    //reset everything if we got the answer right
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void checkButton(final View view) {
        if (correct.equals(data[currentRiddle][1]) || (answerGoes != null && answerGoes.getText().toString().toUpperCase().equals(data[currentRiddle][1]))) {
            correct = "";
            counter = 0;
            hintCount = 0; // hintCount used to check if first ask for riddle
            clicked = 0;
            idForButton.clear();
            possibleAnswer = "";
            possibleAnswer2 = "";
            //move on to the next riddle
            if (currentRiddle < data.length - 1) {
                delete();
                currentRiddle++;
                if (!fromArchives){
                    // v2: update riddle number
                    Data.putDataRiddle(currentRiddle, this);
                } else if (!reRunAdventure){
                    hint = deathHintBonus;  // v3: resets hint count if from archives
                }
                update(currentRiddle);


            }
            //if the adventure is done
            else {
                // TODO: v2: reset hint upon completion
// rerRunAdventure is first as both are true if from RiddleIslandActivity.
                if (reRunAdventure) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Well Done!")
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent toRiddleIslandIntent = new Intent(view.getContext(), RiddleIslandActivity.class);
                                    startActivity(toRiddleIslandIntent);
                                }
                            })
                            .show();
                }
                else if (fromArchives) {
                    Toast toast = Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent returnIntent = new Intent(view.getContext(), ArchivesActivity.class);
                    startActivity(returnIntent);
                } else {
                    hint = totalHints;
                    adventure_number++;
                    Data.putDataHint(hint, this);
                    // v2: saving new adventure number into permanent data
                    Data.putDataAdventure(adventure_number, this);
                    Data.putDataRiddle(0, this);
                    Data.putDataProgress(Data.getTotalProgress(), this);
                    data = Data.updateAdventureNumber(adventure_number); // v2: updated in adventureeditor
                    //D2: checking if the Data is Null, if it is go back to the main page
                    //when the last adventure is done
                    if (data == null) {
                        Intent i = new Intent(RiddleMainActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(this);
                        builder.setTitle("Well Done")
                                .setMessage("Next Adventure Awaits")
                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO: delete
                                        data = Data.updateAdventureNumber(adventure_number); // v2: updated in adventureeditor
                                        //starting of the next adventure
                                        hint = totalHints;
                                        delete();
                                        currentRiddle = 0;
                                        update(currentRiddle);

                                    }
                                })
                                .show();
                    }
                }

            }
        }
        //if you got the answer wrong
        else {
            if (currentProgress > 4) {
                currentProgress -= 4;
                myBar.setProgress(currentProgress);
                Data.putDataProgress(currentProgress, this);
            }
            //no more health bar left
            else {
                //D2: set the current health bar to 0
                currentProgress = 0;
                myBar.setProgress(0);
                Data.putDataProgress(currentProgress, this);
                deathPopUp();

            }
        }


    }
    public static AlertDialog.Builder alertDialogBuilder;
    public static AlertDialog alertDialog;
    //D2: changed the code to be in a function so I can call if from onCreate
    //popup message when you are dead
    public void deathPopUp(){
        // v2: here we build by inflating a view to contain multiple buttons. Build displays option to watch video or wait
        AlertDialog.Builder choiceBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View deathView = inflater.inflate(R.layout.death_window, (ViewGroup) findViewById(R.id.windowView));
        TextView titleText = (TextView) deathView.findViewById(R.id.title);
        titleText.setText("You Fainted!");
        TextView descriptionText = (TextView) deathView.findViewById(R.id.description);
        descriptionText.setText("Your mind clears again as you wake up by...");

        Button waitButton = (Button) deathView.findViewById(R.id.waitButton);
        Button videoButton = (Button) deathView.findViewById(R.id.videoButton);
        choiceBuilder.setCancelable(false);
        choiceBuilder.setView(deathView);
// TODO: right now we have onClick set, need to actually display videos code
        videoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                /*if (MainActivity.mRewardedVideoAd.isLoaded()) {
                    MainActivity.mRewardedVideoAd.show();
                }*/
                Intent intent = new Intent(RiddleMainActivity.this, loadingScreen.class);
                startActivity(intent);
            }
        });


        waitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(RiddleMainActivity.this, BroadcastService.class));
                startService(new Intent(RiddleMainActivity.this, BroadcastService.class));
                Log.i(TAG, "Started service");
                // Code here executes on main thread after user presses button
                alertDialogBuilder = new AlertDialog.Builder(RiddleMainActivity.this)
                        .setTitle("You try coming back to your senses")
                        .setMessage("");
                alertDialogBuilder.setCancelable(false); // cannot get out of builder screen

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        final AlertDialog alertDialog = choiceBuilder.create();
        alertDialog.show();
    }
    //ask for hint
    public void askRinie(View view) {

        if (hint > 0) {
            hint--;
            //copied from stackoverflow
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("you have " + hint + " hints left");
            // two variables: hint and hintCount. hint is saved, hintCount reset each new riddle.
            //the first and later hints are combined
            if (hintCount < data[currentRiddle][1].length()) {
                String answerFromHint = Character.toString(data[currentRiddle][1].charAt(hintCount));
                if (hintCount == 0) {
                    builder.setMessage("First Letter is " + answerFromHint)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .show();
                } else {
                    builder.setMessage("Next letter is " + answerFromHint)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .show();
                }
                hintCount++;
                // v2: saves amount of hints used
                //TODO: MAYBE wrong
                if (reRunAdventure){

                }else{
                    // v2: saves amount of hints used
                    Data.putDataHint(hint, this);
                }



            }
            //if the length of the hint is same as the answer
            else {
                Toast.makeText(this, "answer has been given", Toast.LENGTH_LONG).show();
            }
        }
        //all the hints has been used
        //D2: now have the option of watching videos for more hints
        else {
            //D2: displaying the video when you run out of hint
            //Toast.makeText(this, "Rinie is out of power", Toast.LENGTH_LONG).show();
            final AlertDialog.Builder choiceBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View hintView = inflater.inflate(R.layout.more_hint, (ViewGroup) findViewById(R.id.moreHintView));
            TextView titleText = (TextView) hintView.findViewById(R.id.titleHint);
            titleText.setText("oh Rinie help!");
            TextView descriptionText = (TextView) hintView.findViewById(R.id.descriptionHint);
            descriptionText.setText("Rinie is out of power");
            Button videoButton = (Button) hintView.findViewById(R.id.videoHintButton);
            choiceBuilder.setCancelable(true);
            choiceBuilder.setView(hintView);
            //the video button to display ad
            videoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(RiddleMainActivity.this, HintAdVid.class);
                    //i.putExtra("PROGRESS_NUMBER", Data.getTotalProgress());
                    startActivity(i);

                    // Code here executes on main thread after user presses button
                    /*if (HintVideoAd.isLoaded()) {
                        HintVideoAd.show();
                    }*/
                }
            });
            final AlertDialog alertDialog = choiceBuilder.create();
            alertDialog.show();
        }
    }


}



