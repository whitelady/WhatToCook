package com.elsicaldeira.whattocook;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

/**
 * Created by Elsi on 15/08/2015.
 */
public class RecipeViewAdapter extends RecyclerView.Adapter<RecipeViewAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> recipeDataSet;

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
       // CardView recipeCard;
        TextView recipeTitle;
        TextView recipeRating;
        ImageView recipeImg;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            this.recipeTitle = (TextView) itemView.findViewById(R.id.recipeTitle);
            this.recipeRating = (TextView) itemView.findViewById(R.id.recipeRating);
            this.recipeImg = (ImageView) itemView.findViewById(R.id.recipeImg);
        }

    }

    public RecipeViewAdapter(ArrayList<Recipe> recipes) {
        this.recipeDataSet = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        RecipeViewHolder myViewHolder = new RecipeViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int i) {
        Log.d(Util.RECIPE_TAG,"on bind view holder " + recipeDataSet.get(i).getTitle());
        holder.recipeTitle.setText(recipeDataSet.get(i).getTitle());
        holder.recipeRating.setText(recipeDataSet.get(i).getSocialRank());
        Uri uri = Uri.parse(recipeDataSet.get(i).getImageurl());
        Context context = holder.recipeImg.getContext();
        Glide.with(context)
                .load(uri)
                .apply(centerCropTransform()
                        .placeholder(R.drawable.placeholder_loading)
                        .error(R.drawable.placeholder_error)
                        .priority(Priority.HIGH))
                .into(holder.recipeImg);

        //holder.recipeImg.setImageResource(recipeDataSet.get(i).getImage());

    }
    public String getRecipeId(int pos){
        return recipeDataSet.get(pos).getRecipeId();
    }
    @Override
    public int getItemCount() {
        return recipeDataSet.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
