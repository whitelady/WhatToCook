package com.elsicaldeira.whattocook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Elsi on 19/08/2015.
 */
public class IngredientsTab extends Fragment {
    public static final String ARG_PAGE = "INGR";

    private String tempText;
    private String recipeId;

    RecyclerView lista;


    public static IngredientsTab newInstance(String texto) {
       Bundle args = new Bundle();
        args.putString(ARG_PAGE, texto);
        IngredientsTab fragment = new IngredientsTab();
        fragment.setArguments(args);
        return fragment;
    }

   /* public void setIngredientsText(String texto) {
        TextView viewIngredient = (TextView) getView().findViewById(R.id.ingredientsTxt);
        viewIngredient.setText(texto);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeId = getArguments().getString(ARG_PAGE);
       // ingredientsList = new ArrayList<String>();
        Log.i(Util.RECIPE_TAG, "ingcreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredients_list, container, false);
        lista = (RecyclerView)view.findViewById(R.id.ingredientsList);
        final FragmentActivity c = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        lista.setLayoutManager(layoutManager);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL );
        //lista.addItemDecoration(dividerItemDecoration);

        //lista.addItemDecoration(dividerItemDecoration);
        //lista.addItemDecoration(new SpaceItemDecoration(0,0));
        //lista.addItemDecoration(new VerticalSpacingDecoration(5));
        //dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.linedecoration));
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(ContextCompat.getDrawable(getContext(),R.drawable.linedecoration));
        lista.addItemDecoration(dividerItemDecoration);
        lista.setHasFixedSize(true);
        return view;
    }

    // To update fragment in ViewPager, we should implement a public method for the fragment,
    // and do updating stuff in this method.
    public void updateList(ArrayList<String> list) {
        Log.d(Util.RECIPE_TAG, "updateList");
        ArrayList<String> ingredientsList = list;
        RecyclerView.Adapter ingAdapter = new IngredientsViewAdapter(ingredientsList);
        lista.setAdapter(ingAdapter);
    }

}
