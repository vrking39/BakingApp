package com.example.bakingappproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingappproject.R;
import com.example.bakingappproject.databinding.IngredientsListItemBinding;
import com.example.bakingappproject.databinding.RecipeStepItemBinding;
import com.example.bakingappproject.model.IngredientsModel;
import com.example.bakingappproject.model.StepsModel;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> dataSet;
    private StepsClickListener mListener;

    public RecipeStepsAdapter(List<Object> dataSet, StepsClickListener mListener) {
        this.dataSet = dataSet;
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet.get(position) instanceof IngredientsModel) {
            return 0;
        } else if (dataSet.get(position) instanceof StepsModel){
            return 1;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            return new IngredientsViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ingredients_list_item, viewGroup, false));
        } else {
            return new StepsViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recipe_step_item, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof IngredientsViewHolder) {
            IngredientsViewHolder viewHolder = (IngredientsViewHolder) holder;
            IngredientsModel ingredients = (IngredientsModel) dataSet.get(position);
            if (ingredients != null) {
                viewHolder.bind(ingredients);
            }
        } else {
            StepsViewHolder viewHolder = (StepsViewHolder) holder;
            StepsModel steps = (StepsModel) dataSet.get(position);

            if(steps != null) {
                // description
                viewHolder.bind(steps);
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public interface StepsClickListener {
        void onStepClick(StepsModel steps);
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        private IngredientsListItemBinding ingredientsBinding;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            ingredientsBinding = DataBindingUtil.bind(itemView);
        }

        void bind(IngredientsModel ingredients) {
            ingredientsBinding.setIngredient(ingredients);
            ingredientsBinding.executePendingBindings();
        }
    }

    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecipeStepItemBinding stepItemBinding;

        StepsViewHolder(View itemView) {
            super(itemView);
            stepItemBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        void bind(StepsModel steps) {
            stepItemBinding.setStep(steps);
            stepItemBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            StepsModel steps = (StepsModel) dataSet.get(position);
            mListener.onStepClick(steps);
        }
    }
}