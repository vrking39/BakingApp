package com.example.bakingappproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeModel implements Parcelable {

    public static final Creator<RecipeModel> CREATOR = new Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel in) {
            return new RecipeModel(in);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private List<IngredientsModel> ingredients;
    @SerializedName("steps")
    private List<StepsModel> steps;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    protected RecipeModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients, IngredientsModel.CREATOR);
        steps = new ArrayList<>();
        in.readTypedList(steps,StepsModel.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientsModel> getIngredients() {
        return ingredients;
    }


    public List<StepsModel> getSteps() {
        return steps;
    }

}