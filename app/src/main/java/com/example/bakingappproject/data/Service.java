package com.example.bakingappproject.data;

import com.example.bakingappproject.model.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("baking.json")
    Call<List<RecipeModel>> getRecipes();
}
