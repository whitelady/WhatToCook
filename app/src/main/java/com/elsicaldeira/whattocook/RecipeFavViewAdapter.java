package com.elsicaldeira.whattocook;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elsi on 15/08/2015.
 */
public class RecipeFavViewAdapter extends RecyclerView.Adapter<RecipeFavViewAdapter.RecipeFavViewHolder> {
    private ArrayList<Recipe> recipeDataSet;

    public static class RecipeFavViewHolder extends RecyclerView.ViewHolder {
       // CardView recipeCard;
        TextView recipeTitle;
        TextView recipeRating;
        ImageView recipeImg;
        ImageButton deleteFav;

        public RecipeFavViewHolder(View itemView) {
            super(itemView);
            this.recipeTitle = (TextView) itemView.findViewById(R.id.recipeTitle);
            this.recipeRating = (TextView) itemView.findViewById(R.id.recipeRating);
            this.recipeImg = (ImageView) itemView.findViewById(R.id.recipeImg);
            this.deleteFav = (ImageButton) itemView.findViewById(R.id.removeFav);
        }

    }

    public RecipeFavViewAdapter(ArrayList<Recipe> recipes) {
        this.recipeDataSet = recipes;
    }

    @Override
    public RecipeFavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);

        view.setOnClickListener(FavoritesActivity.favOnClickListener);

        RecipeFavViewHolder myViewHolder = new RecipeFavViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final RecipeFavViewHolder holder, final int i) {
        holder.recipeTitle.setText(recipeDataSet.get(i).getTitle());
        holder.recipeRating.setText(recipeDataSet.get(i).getSocialRank());
        Uri uri = Uri.parse(recipeDataSet.get(i).getImageurl());
        Context context = holder.recipeImg.getContext();
        holder.deleteFav.setVisibility(View.VISIBLE);
        holder.deleteFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.i(Util.RECIPE_TAG, " entre a borrar favorito ");
                String recipeid = recipeDataSet.get(i).getRecipeId();
                Log.i(Util.RECIPE_TAG, " entre a borrar favorito " + recipeid);
                FavoritesUtils favoritesRecipes = FavoritesUtils.get(Util.getActContext());
                boolean result = favoritesRecipes.deleteRecipe(recipeid);
                if (result) {
                    recipeDataSet.remove(i);
                    FavoritesActivity.refreshList(i);
                    if (recipeDataSet.size() > 0) {
                        Snackbar
                                .make(arg0, R.string.recipe_removed, Snackbar.LENGTH_SHORT)
                                .show();
                    } else {
                        Snackbar
                                .make(arg0, R.string.recipe_removed_empty, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });

        Glide.with(context).load(uri)
                .placeholder(R.drawable.placeholder_loading) // optional
                .error(R.drawable.placeholder_error)         // optional
                .override(300,300)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
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
    public void setRecipes(ArrayList<Recipe> recipes) {
        recipeDataSet = recipes;
    }
}
