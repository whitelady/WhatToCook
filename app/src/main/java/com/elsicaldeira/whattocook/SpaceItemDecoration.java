package com.elsicaldeira.whattocook;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Elsi on 21/08/2015.
 * Based on code found here: http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;
    private final int mHorizontalSpaceWidth;

    public SpaceItemDecoration(int mVerticalSpaceHeight, int mHorizontalSpaceWidth) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        this.mHorizontalSpaceWidth = mHorizontalSpaceWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        Log.d(Util.RECIPE_TAG,"Rect: " + outRect.bottom);
        outRect.bottom = mVerticalSpaceHeight;
        outRect.left = mHorizontalSpaceWidth;
        outRect.right = mHorizontalSpaceWidth;
        if(parent.getChildAdapterPosition(view) == 0)
            outRect.top = mVerticalSpaceHeight;
    }
}
