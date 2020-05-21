package com.example.bakingappproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bakingappproject.adapter.RecipeAdapter;
import com.example.bakingappproject.data.RetrofitClient;
import com.example.bakingappproject.data.Service;
import com.example.bakingappproject.model.RecipeModel;
import com.example.bakingappproject.utils.SpacingItemDecorUtils;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeClickListener {

    private Context mContext;
    private RecipeAdapter adapter;
    private List<RecipeModel> mRecipeList;

    public static final String TAG = RecipeFragment.class.getSimpleName();
    private static final String RECIPES_KEY = "recipes";

    @BindView(R.id.recipeRecycler)
    RecyclerView mRecipeRecycler;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);

        mContext = getActivity();
        adapter = new RecipeAdapter(this);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecipeRecycler.setLayoutManager(layoutManager);
        mRecipeRecycler.setHasFixedSize(true);
        mRecipeRecycler.addItemDecoration(new SpacingItemDecorUtils((int)
                getResources().getDimension(R.dimen.margin_medium)));
        mRecipeRecycler.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        mRecipeRecycler.setAdapter(adapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_KEY)) {
            mSwipeRefresh.setRefreshing(true);
            loadRecipes();
        }


        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        loadRecipes();

        return view;

    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRecipes();
                mSwipeRefresh.setRefreshing(false);
            }
        }, 3000);
    }
//TODO (5) Review and Modify
    private void loadRecipes() {
        // set refreshing if this method is called by our BroadcastReceiver
        Service service = RetrofitClient.getClient().create(Service.class);
        Call<List<RecipeModel>> call = service.getRecipes();
        call.enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.isSuccessful()) {
                    mRecipeList = response.body();
                    adapter.setData(mRecipeList);
                    adapter.notifyDataSetChanged();
                    mSwipeRefresh.setRefreshing(false);
                } else {
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRecipeClick(RecipeModel recipe) {
        Intent intent = new Intent(mContext, StepsListActivity.class);
        intent.putExtra(StepsListActivity.INTENT_EXTRA, recipe);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


}
