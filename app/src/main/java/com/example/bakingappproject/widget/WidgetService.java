package com.example.bakingappproject.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingappproject.R;
import com.example.bakingappproject.model.IngredientsModel;

import java.util.List;

public class WidgetService extends RemoteViewsService {

    private List<IngredientsModel> ingredients;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteListFactory(getApplicationContext());
    }

    private class RemoteListFactory implements RemoteViewsFactory {

        Context mContext;

        RemoteListFactory(Context applicationContext) {
            this.mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingredients = WidgetProvider.ingredients;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredients == null) return 0;
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);
            IngredientsModel ingredient = ingredients.get(position);

            String measure = String.valueOf(ingredient.getQuantity());
            String widget_ingredients = ingredient.getIngredient();
            views.setTextViewText(R.id.widget_ing, widget_ingredients  + "   " + measure);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
