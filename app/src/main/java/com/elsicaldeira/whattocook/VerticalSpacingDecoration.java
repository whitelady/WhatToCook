package com.elsicaldeira.whattocook;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Elsi on 21/08/2015.
 * Based on code found here: http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
 */
public class VerticalSpacingDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    public VerticalSpacingDecoration(int spacing) {
        this.spacing = spacing;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = spacing;
        if(parent.getChildAdapterPosition(view) == 0)
            outRect.top = spacing;
    }
}
