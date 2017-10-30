package com.example.riddlez;


/**
 * Created by vince on 7/17/2017.
 */

public class Arch {
    // name of adventure title
    private String mAdventureName;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mArrayNumber;

    public Arch(String vAdventureName, int vImageResourceId, int vArrayNumber)

    {
        mAdventureName = vAdventureName;
        mImageResourceId = vImageResourceId;
        mArrayNumber = vArrayNumber;
    }

    public String getAdventureName() {
        return mAdventureName;
    }
    public int getImageResourceId(){
        return mImageResourceId;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;}

    public int getArrayNumber(){
        return mArrayNumber;
    }
}
