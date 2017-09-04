package com.elsicaldeira.whattocook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Elsi on 13/08/2015.
 * Class Recipe: Holds recipe info from
 * API request.
 * last modification: 06/09/2015.
 */
public class Recipe implements Serializable {
    private String title;
    private String publisher;
    private String f2furl;
    private String sourceurl;
    private String recipeId;
    private String imageurl;
    private String socialRank;
    private String publisherurl;
    private String recipeDate;
    private int favId;
    private ArrayList<String> ingredients;


    public Recipe (String title, String publisher, String f2furl, String sourceurl, String recipeId,String imageurl,String socialRank,String publisherurl,ArrayList<String>ingredients){
        this.title = title;
        this.publisher = publisher;
        this.f2furl = f2furl;
        this.sourceurl = sourceurl;
        this.recipeId = recipeId;
        this.imageurl = imageurl;
        this.socialRank = socialRank;
        this.publisherurl = publisherurl;
        this.ingredients = ingredients;
       // this.recipeDate = new Date();
    }

    /**
     * getters
     */

    public String getTitle() {
        return title;
    }
    public String getPublisher() {
        return publisher;
    }
    public String getF2furl() {
        return f2furl;
    }
    public String getSourceurl() {
        return sourceurl;
    }
    public String getRecipeId() {
        return recipeId;
    }
    public String getImageurl() {
        return imageurl;
    }
    public String getSocialRank() {
        return "rating:" + socialRank;
    }
    public String getPublisherurl() {
        return publisherurl;
    }
    public String getDate() {
        return recipeDate;
    }
    public ArrayList<String> getIngredients() {
        return ingredients;
    }
    public int getFavId() {
        return favId;
    }

    /**
     * setters
     */

    public void setTitle(String title) {
        this.title = title;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setF2furl(String f2furl) {
        this.f2furl = f2furl;
    }
    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }
    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    public void setSocialRank(String socialRank) {
        this.socialRank = socialRank;
    }
    public void setPublisherurl(String publisherurl) {
        this.publisherurl = publisherurl;
    }


    public void setFavId(int favId) {
        this.favId = favId;
    }
    public void setDate(String Date) {this.recipeDate = Date;}

}
