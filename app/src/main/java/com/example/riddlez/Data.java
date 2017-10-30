package com.example.riddlez;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.example.android.riddle.R;

/**
 * Created by eorud on 8/13/2017.
 */

public class Data {
    private static int totalHints = 15;
    private static int totalProgress= 100;

    public static String[][] updateAdventureNumber(int adventure_number) {

        switch (adventure_number) {
            case 1:
                String[][] data1 = {{"What has a head, a tail, is brown, and has no legs?", "PENNY"},
                        {"The more you take, the more you leave behind. What am I?", "FOOTSTEPS"},
                        {"What has many keys, but can't even open a single door?", "PIANO"},
                        {"Tall I am young, Short I am old, While with life I glow, Wind is my foe. What am I?", "CANDLE"},
                        {"What belongs to you, but other people use it more than you?", "YOUR NAME"},
                        {"What can you catch but not throw?", "COLD"},
                        {"What has rivers with no water, forests but no trees and cities with no buildings?", "MAP"}};
                return data1;
            case 2:
                String[][] data2 = {{"What is black when you buy it, red when you use it, and gray when you throw it away?", "CHARCOAL"},
                       {"When does Christmas come before Thanksgiving?", "DICTIONARY"},
                        {"What has six faces, But does not wear makeup. It also has twenty-one eyes, But cannot see?", "DICE"},
                        {"I am not alive, but I grow; I don't have lungs, but I need air; I don't have a mouth, but water kills me. What am I?", "FIRE"},
                        {"If you have me, you want to share me. If you share me, you haven’t got me. What am I?", "SECRET"},
                        {"What is easy to get into, but hard to get out of?", "TROUBLE"},
                        {"I am a ship that can be made to ride the greatest waves. I am not built by tool, but built by hearts and minds. What am I?", "FRIENDSHIP"}};
                return data2;
            case 3:
                String[][] data3 = {{"There was a green house. Inside the green house there was a white house Inside the white house there was a red house. Inside the red house there were lots of babies. What am I?", "WATERMELON"},
                        {"What has no beginning, end, or middle?", "Donut"},
                        {"You throw away the outside and cook the inside. Then you eat the outside and throw away the inside. What did you eat?", "Corn"},
                        {"What goes in the water black and comes out red?", "LOBSTER"},
                        {"What kind of room has no doors or windows?", "MUSHROOM"},
                        {"A box without hinges, key, or lid, yet golden treasure inside is hid. (From the Hobbit)", "EGG"}};
                return data3;
            default:
                return null;
        }
    }

    public static int getDataAdventure(Context context){
        Log.i("work", "working");
        SharedPreferences adventureSave = context.getSharedPreferences(
                context.getString(R.string.adventureNum), Context.MODE_PRIVATE);
        return adventureSave.getInt(context.getString(R.string.adventureNum), 1);
    }
    public static void putDataAdventure(int adventure_number, Context context){
        SharedPreferences adventureSave = context.getSharedPreferences(
                context.getString(R.string.adventureNum), Context.MODE_PRIVATE);
        SharedPreferences.Editor adventureEditor = adventureSave.edit();
        adventureEditor.putInt(context.getString(R.string.adventureNum), adventure_number);
        adventureEditor.apply();

    }
    public static int getDataRiddle(Context context){
        SharedPreferences riddleSave = context.getSharedPreferences(
                context.getString(R.string.riddleNum), Context.MODE_PRIVATE);
        return riddleSave.getInt(context.getString(R.string.riddleNum), 0);
    }
    public static void putDataRiddle(int riddle_number, Context context){
        SharedPreferences riddleSave = context.getSharedPreferences(
                context.getString(R.string.riddleNum), Context.MODE_PRIVATE);
        SharedPreferences.Editor riddleEditor = riddleSave.edit();
        riddleEditor.putInt(context.getString(R.string.riddleNum), riddle_number);
        riddleEditor.apply();

    }
    public static int getDataHint(Context context){
        SharedPreferences countSave = context.getSharedPreferences(
                context.getString(R.string.hintSave), Context.MODE_PRIVATE);
        return countSave.getInt(context.getString(R.string.hintSave), 15);
    }
    public static void putDataHint(int count_number, Context context){
        SharedPreferences countSave = context.getSharedPreferences(
                context.getString(R.string.hintSave), Context.MODE_PRIVATE);
        SharedPreferences.Editor countEditor = countSave.edit();
        countEditor.putInt(context.getString(R.string.hintSave), count_number);
        countEditor.apply();

    }
    //for health bar
    public static int getDataProgress(Context context){
        SharedPreferences progressSave = context.getSharedPreferences(
                context.getString(R.string.progressSave), Context.MODE_PRIVATE);
        //Log.i("HInt", progressSave.getInt(context.getString(R.string.progressSave), getTotalProgress()) + "gottems ");
        return progressSave.getInt(context.getString(R.string.progressSave), 100);
    }
    //updaing the health bar
    public static void putDataProgress(int count_number, Context context){
        //Log.i("HInt", "passed in Data " + count_number);
        SharedPreferences progressSave = context.getSharedPreferences(
                context.getString(R.string.progressSave), Context.MODE_PRIVATE);
        SharedPreferences.Editor progressEditor = progressSave.edit();
        progressEditor.putInt(context.getString(R.string.progressSave), count_number);
        progressEditor.apply();

    }
    public  static int getTotalHints(){
        return totalHints;
    }
    //D2: getting the total health
    public  static int getTotalProgress(){
        return totalProgress;
    }
    //get the total adventure num
    public static int getCurrentAdventureNum(){
        return 3;
    }

}
