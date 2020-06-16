package com.developer_ngapak.resepkita.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {

    String name,detail,category,ingredient,recipe,img,search,key,id;

    public Food() {
    }

    public Food(String name, String category, String detail, String ingredient, String recipe, String img, String search, String id) {
        this.name = name;
        this.detail = detail;
        this.category = category;
        this.ingredient = ingredient;
        this.recipe = recipe;
        this.img = img;
        this.id = id;
    }

    protected Food(Parcel in) {
        name = in.readString();
        detail = in.readString();
        category = in.readString();
        ingredient = in.readString();
        recipe = in.readString();
        img = in.readString();
        search = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String image) {
        this.img = image;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(detail);
        parcel.writeString(category);
        parcel.writeString(ingredient);
        parcel.writeString(recipe);
        parcel.writeString(img);
        parcel.writeString(search);
    }
}
