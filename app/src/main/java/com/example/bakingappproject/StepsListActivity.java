package com.example.bakingappproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingappproject.adapter.RecipeStepsAdapter;
import com.example.bakingappproject.model.IngredientsModel;
import com.example.bakingappproject.model.RecipeModel;
import com.example.bakingappproject.model.StepsModel;
import com.example.bakingappproject.widget.WidgetProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsListActivity extends AppCompatActivity implements RecipeStepsAdapter.StepsClickListener, StepDetailsFragment.OnStepClickListener {

    public static final String INTENT_EXTRA = "recipe";
    public static final String WIDGET_PREF = "widget_prefs";
    public static final String ID_PREF = "id";
    public static final String NAME_PREF = "name";

    private boolean isTwoPane;
    private int mRecipeId;
    private List<StepsModel> stepsList;
    private String mRecipeName;

    @BindView(R.id.step_list_rv)
    RecyclerView mRecyclerView;
    private ArrayList<Object> objects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        ButterKnife.bind(this);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        objects = new ArrayList<>();


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        assert intent != null;
        if (intent.hasExtra(INTENT_EXTRA)) {
            RecipeModel recipe = getIntent().getParcelableExtra(INTENT_EXTRA);
            assert recipe != null;
            mRecipeId = recipe.getId();
            mRecipeName = recipe.getName();
            List<IngredientsModel> ingredients = recipe.getIngredients();
            stepsList = recipe.getSteps();
            String mRecipeName = recipe.getName();
            objects.addAll(ingredients);
            objects.addAll(stepsList);
            setTitle(mRecipeName);
        }


        if (findViewById(R.id.step_detail_container) != null) {
            isTwoPane = true;
        }

        initViews();

    }

    private void initViews() {
        RecipeStepsAdapter mAdapter = new RecipeStepsAdapter(objects, (RecipeStepsAdapter.StepsClickListener) this);
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void closeOnError() {
        finish();
    }


    @Override
    public void onStepClick(StepsModel steps) {
        if (steps != null) {
            if (isTwoPane) {
                StepDetailsFragment fragment = new StepDetailsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(this, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.EXTRA, steps);
                intent.putExtra(RecipeDetailActivity.EXTRA_NAME, mRecipeName);
                intent.putParcelableArrayListExtra(RecipeDetailActivity.EXTRA_LIST,
                        (ArrayList<? extends Parcelable>) stepsList);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.widget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.widget:
                addToPrefsForWidget();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToPrefsForWidget() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ID_PREF, mRecipeId);
        editor.putString(NAME_PREF, mRecipeName);
        editor.apply();
        WidgetProvider.updateWidget(this);
    }


    @Override
    public void onPreviousStepClick(StepsModel steps) {

    }

    @Override
    public void onNextStepClick(StepsModel steps) {

    }
}
