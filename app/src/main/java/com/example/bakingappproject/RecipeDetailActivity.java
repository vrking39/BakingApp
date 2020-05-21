package com.example.bakingappproject;


import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;


import com.example.bakingappproject.databinding.ActivityStepsDetailsBinding;
import com.example.bakingappproject.model.StepsModel;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements StepDetailsFragment.OnStepClickListener {

    public static final String EXTRA = "step";
    public static final String EXTRA_NAME = "recipe_name";
    public static final String EXTRA_LIST = "recipe_step_list";
    private static final String STEP_LIST = "current_list";
    private static final String STEP_INDEX = "index";

    private List<StepsModel> stepsList;
    int stepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TODO (6) Rename ids and xmls
        ActivityStepsDetailsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_steps_details);

        Toolbar toolbar = binding.detailListToolbar;
        setSupportActionBar(toolbar);

        String mRecipeName = getIntent().getStringExtra(EXTRA_NAME);
        setTitle(mRecipeName);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            StepsModel steps = getIntent().getParcelableExtra((EXTRA));
            stepsList = getIntent().getParcelableArrayListExtra(RecipeDetailActivity.EXTRA_LIST);
            StepDetailsFragment fragment = StepDetailsFragment.newInstance(steps);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            stepsList = savedInstanceState.getParcelableArrayList(STEP_LIST);
            stepIndex = savedInstanceState.getInt(STEP_INDEX);
        }

    }


    @Override
    public void onPreviousStepClick(StepsModel steps) {

        stepIndex = steps.getId();
        if (stepIndex > 0) {
            showStep(stepsList.get(stepIndex - 1));
        } else {
            finish();
        }

    }

    @Override
    public void onNextStepClick(StepsModel steps) {

        stepIndex = steps.getId();
        if (stepIndex < stepsList.size() - 1) {
            showStep(stepsList.get(stepIndex + 1));
        } else {
            finish();
        }

    }

    private void showStep(StepsModel steps) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        StepDetailsFragment fragment = StepDetailsFragment.newInstance(steps);
        transaction.replace(R.id.step_detail_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}