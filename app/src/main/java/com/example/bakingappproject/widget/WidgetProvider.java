package com.example.bakingappproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;


import com.example.bakingappproject.MainActivity;
import com.example.bakingappproject.R;
import com.example.bakingappproject.StepsListActivity;
import com.example.bakingappproject.model.IngredientsModel;


import java.util.ArrayList;
import java.util.List;


public class WidgetProvider extends AppWidgetProvider {

    static List<IngredientsModel> ingredients = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(StepsListActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        String text = sharedPreferences.getString(StepsListActivity.NAME_PREF, "no recipe");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.appwidget_text, text);
        Intent intent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, intent);


        Intent clickIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    public static void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        for (int appWidgetId : appWidgetIds) {
            WidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences sharedPreferences = context.getSharedPreferences(StepsListActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(StepsListActivity.NAME_PREF);
        editor.remove(StepsListActivity.ID_PREF);
        editor.apply();
    }
}
