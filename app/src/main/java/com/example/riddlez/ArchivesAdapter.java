package com.example.riddlez;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.riddle.R;

import java.util.ArrayList;

/**
 * Created by vince on 7/17/2017.
 */

public class ArchivesAdapter extends ArrayAdapter<Arch> {


    public ArchivesAdapter(Activity context, ArrayList<Arch> words) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, words);
        /*SharedPreferences adventureStatus = getContext().getSharedPreferences(
                Resources.getSystem().getString(R.string.save1), Context.MODE_PRIVATE
        );
        int currentAdventureNum = adventureStatus.getInt(Resources.getSystem().getString(R.string.adventureNum), 0);*/
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        // Get the {@link AndroidFlavor} object located at this position in the list
         final Arch currentAdventureList = getItem(position);
        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView adventureTextView = (TextView) listItemView.findViewById(R.id.adventure_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        adventureTextView.setText(currentAdventureList.getAdventureName());
        // Find the TextView in the list_item.xml layout with the ID version_number


        RelativeLayout button = (RelativeLayout) listItemView.findViewById(R.id.arch_background);
        button.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent numbersIntent = new Intent(view.getContext(), AdventureListActivity.class);
                numbersIntent.putExtra("ARRAY_NUMBER", currentAdventureList.getArrayNumber());
                numbersIntent.putExtra("ADVENTURE_NAME", currentAdventureList.getAdventureName());
                view.getContext().startActivity(numbersIntent);
            }
        });
        //D1: getting the saved data for which adventure the player is in
        /*Context context = sharedPreferece.getContext();
        SharedPreferences adventureStatus = context.getSharedPreferences(
                Resources.getSystem().getString(R.string.save1), Context.MODE_PRIVATE
        );
        int currentAdventureNum = adventureStatus.getInt(Resources.getSystem().getString(R.string.adventureNum), 0);*/
        /*if(currentAdventureList.getArrayNumber() > currentAdventureNum){
            button.setEnabled(false);
        }*/
        ImageView layout =(ImageView) listItemView.findViewById(R.id.image);
        layout.setImageResource(currentAdventureList.getImageResourceId());


        return listItemView;

    }

}

