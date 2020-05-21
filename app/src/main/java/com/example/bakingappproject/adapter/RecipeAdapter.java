package com.example.bakingappproject.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingappproject.R;
import com.example.bakingappproject.model.RecipeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<RecipeModel> mRecipes;
    private RecipeClickListener mListener;

    public RecipeAdapter(RecipeClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.recipes_item, viewGroup, false);

        return new RecipeViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int i) {
        final RecipeModel recipeItem = mRecipes.get(i);
        holder.tv_recipe_name.setText(recipeItem.getName());
    }

    @Override
    public int getItemCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

    public void setData(List<RecipeModel> mRecipeList) {
        mRecipes = mRecipeList;
        notifyDataSetChanged();
    }

    public interface RecipeClickListener {
        void onRecipeClick(RecipeModel recipe);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView tv_recipe_name;
        @BindView(R.id.iv_recipe_image)
        AppCompatImageView iv_recipe;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            RecipeModel recipe = mRecipes.get(position);
            mListener.onRecipeClick(recipe);
        }
    }
}