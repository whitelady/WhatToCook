package com.elsicaldeira.whattocook;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recipeListView;
    static View.OnClickListener favOnClickListener;
    private ArrayList<Recipe> recipes;
    private String imageUrl;
    private String recipeId;
    private String title;
    private String sourceUrl;
    private TextView noFavsMsg;
    private boolean isListView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setFavListView(true);
        isListView = Util.getFavListView();
        setContentView(R.layout.activity_favorites);
        noFavsMsg = (TextView)findViewById(R.id.nofavsMsg);
        favOnClickListener = new FavOnClickListener(this);
        recipeListView = (RecyclerView)findViewById(R.id.recipeList);
        recipeListView.setHasFixedSize(true);
        recipeListView.setItemAnimator(new DefaultItemAnimator());
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(Util.ONE_COLUMN, StaggeredGridLayoutManager.VERTICAL);
       /* if (isListView) {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(Util.ONE_COLUMN, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(Util.TWO_COLUMN, StaggeredGridLayoutManager.VERTICAL);
        }*/
        recipeListView.setLayoutManager(mStaggeredLayoutManager);
        FavoritesUtils favoritesRecipes = FavoritesUtils.get(this);
        recipes = favoritesRecipes.getRecipes();

       if (recipes.size()>0) {
           noFavsMsg.setVisibility(View.GONE);
           adapter = new RecipeFavViewAdapter(recipes);
           recipeListView.setAdapter(adapter);
        }
        else {
           noFavsMsg.setText(getResources().getString(R.string.no_favorites));
           noFavsMsg.setVisibility(View.VISIBLE);
        }
    }


    /**
     * FavOnClickListener
     *  Listener for the RecyclerView
     *
     */

    private class FavOnClickListener implements View.OnClickListener {

        private final Context context;

        private FavOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            seeRecipe(v);
        }

        /**
         * seeRecipe
         * get recipe details to pass to the activity
         * @param v
         */

        private void seeRecipe(View v) {
            int selectedItemPosition = recipeListView.getChildAdapterPosition(v);
            imageUrl = recipes.get(selectedItemPosition).getImageurl();
            Log.i(Util.RECIPE_TAG,"entre a ver receta en favoritos imageUrl " + imageUrl);
            RecyclerView.ViewHolder viewHolder
                    = recipeListView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView titleView = (TextView) viewHolder.itemView.findViewById(R.id.recipeTitle);
            recipeId = recipes.get(selectedItemPosition).getRecipeId();
            title = (String) titleView.getText();
            sourceUrl = recipes.get(selectedItemPosition).getSourceurl();
            Context context = getApplicationContext();
            goActivity(context);
            Log.i(Util.RECIPE_TAG,recipeId);

        }

    }


    public static void refreshList(int i){
        adapter.notifyItemRemoved(i);
    }

    /**
     * goActivity
     * Execute activity of recipe selected
     * @param context
     */

    private void goActivity(Context context) {
        Log.i(Util.RECIPE_TAG,"entre go activity favorites " + recipeId);
        Intent intent;
        intent = new Intent(context,RecipeDetailActivity.class);
        intent.putExtra(Util.RECIPE_ID_TAG,recipeId);
        intent.putExtra(Util.TITLE_TAG,title);
        intent.putExtra(Util.IMAGE_URL_TAG,imageUrl);
        intent.putExtra(Util.SOURCE_URL_TAG, sourceUrl);
        intent.putExtra(Util.FROM_FAV,true);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        this.menu = menu;

        //toggle();
        return true;
    }

    private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isListView) {
            mStaggeredLayoutManager.setSpanCount(Util.TWO_COLUMN);
            item.setIcon(R.drawable.ic_list);
            item.setTitle(R.string.action_list);
            Util.setFavListView(false);
        } else {
            mStaggeredLayoutManager.setSpanCount(Util.ONE_COLUMN);
            item.setIcon(R.drawable.ic_grid);
            item.setTitle(R.string.action_grid);
            Util.setFavListView(true);
        }
        isListView = Util.getFavListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toggle) {
            toggle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
