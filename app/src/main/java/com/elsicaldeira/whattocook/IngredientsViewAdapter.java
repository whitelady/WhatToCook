package com.elsicaldeira.whattocook;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Elsi on 15/08/2015.
 */
public class IngredientsViewAdapter extends RecyclerView.Adapter<IngredientsViewAdapter.IngredientViewHolder> {
    private ArrayList<String> ingredientsDataSet;

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
       // CardView recipeCard;
        TextView ingredientsTxt;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            this.ingredientsTxt = (TextView) itemView.findViewById(R.id.ingredientsTxt);
        }

    }

    public IngredientsViewAdapter(ArrayList<String> ingredients) {
        this.ingredientsDataSet = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_tab, parent, false);

        IngredientViewHolder myViewHolder = new IngredientViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final IngredientViewHolder holder, final int i) {
        holder.ingredientsTxt.setText(ingredientsDataSet.get(i));
    }

    @Override
    public int getItemCount() {
        return ingredientsDataSet.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
