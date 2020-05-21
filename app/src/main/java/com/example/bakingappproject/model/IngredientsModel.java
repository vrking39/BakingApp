package com.example.bakingappproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class IngredientsModel implements Parcelable {
    public static final Creator<IngredientsModel> CREATOR = new Creator<IngredientsModel>() {
            @Override
            public IngredientsModel createFromParcel(Parcel in) {
                return new IngredientsModel(in);
            }

            @Override
            public IngredientsModel[] newArray(int size) {
                return new IngredientsModel[size];
            }
        };
        @SerializedName("quantity")
        private float quantity;
        @SerializedName("measure")
        private String measure;
        @SerializedName("ingredient")
        private String ingredient;

    protected IngredientsModel(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @NonNull
    @Override
    public String toString() {
        return "Ingredients{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}

