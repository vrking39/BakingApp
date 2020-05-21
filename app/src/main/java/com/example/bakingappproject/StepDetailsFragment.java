package com.example.bakingappproject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bakingappproject.databinding.RecipesStepDetailsBinding;
import com.example.bakingappproject.model.RecipeModel;
import com.example.bakingappproject.model.StepsModel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

public class StepDetailsFragment extends Fragment implements Player.EventListener, View.OnClickListener {

    private static final String EXTRA = "step" ;
    private static final String POSITION = "position";
    private static final String LOG_TAG = StepDetailsFragment.class.getSimpleName();

    PlayerView mPlayerView;
    TextView stepDescription;
    ImageView videoThumbnail;

    private Context mContext;
    private StepsModel steps;
    private RecipeModel mRecipe;
    private boolean isTablet;
    private String videoUrl;
    private SimpleExoPlayer mExoPlayer;
    private long playerPosition;
    private boolean playWhenReady;
    private int screenOrientation;

    private OnStepClickListener mListener;

    public StepDetailsFragment() {
    }

    public static StepDetailsFragment newInstance(StepsModel steps) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA, steps);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            steps = getArguments().getParcelable(EXTRA);
        }
        videoUrl = steps != null ? steps.getVideoURL() : null;
        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(POSITION);
        } else {
            playerPosition = 0;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecipesStepDetailsBinding binding =
                RecipesStepDetailsBinding.inflate(inflater, container, false);
        binding.setSteps(steps);

        mContext = getActivity();
        mPlayerView = binding.exoPlayerView;
        videoThumbnail = binding.ivVideoThumbnail;
        stepDescription = binding.tvStepDescription;
        isTablet = getResources().getBoolean(R.bool.istTwoPane);
        screenOrientation = getResources().getConfiguration().orientation;


        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFullScreenPlayer();
        } else {
            Button nextStep = binding.btnNextStep;
            nextStep.setOnClickListener(this);
            Button previousStep = binding.btnPreviousStep;
            previousStep.setOnClickListener(this);
        }

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener) {
            mListener = (OnStepClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + getString(R.string.error_interface));
        }
    }


    // init exoPlayer
    private void initExoPlayer() {
        if (mExoPlayer == null && !(videoUrl.isEmpty()) ) {
            mPlayerView.setVisibility(View.VISIBLE);
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(mContext, getString(R.string.app_name));
            DataSource.Factory factory = new DefaultDataSourceFactory(mContext, userAgent);
            MediaSource mediaSource =
                    new ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(videoUrl));
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(playerPosition);
            mExoPlayer.setPlayWhenReady(true);
        } else {
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                mPlayerView.setVisibility(View.GONE);
                videoThumbnail.setVisibility(View.VISIBLE);
                stepDescription.setVisibility(View.VISIBLE);
            } else  {
                mPlayerView.setVisibility(View.GONE);
                videoThumbnail.setVisibility(View.VISIBLE);
            }
        }
    }

    void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        Objects.requireNonNull(((AppCompatActivity)
                Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        initExoPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            playerPosition = mExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void setFullScreenPlayer() {
        if (!videoUrl.isEmpty() && !isTablet) {
            hideSystemUI();
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_READY) {
            Log.d(LOG_TAG, "Player is playing");
        } else if (playbackState == Player.STATE_READY) {
            Log.d(LOG_TAG, "Player is paused");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(POSITION, playerPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_next_step:
                mListener.onNextStepClick(steps);
                break;
            case R.id.btn_previous_step:
                mListener.onPreviousStepClick(steps);
                break;
        }
    }

    public interface OnStepClickListener {
        void onPreviousStepClick(StepsModel steps);
        void onNextStepClick(StepsModel steps);
    }
}

