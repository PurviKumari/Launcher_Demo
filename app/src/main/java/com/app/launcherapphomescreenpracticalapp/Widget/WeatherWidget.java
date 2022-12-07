package com.app.launcherapphomescreenpracticalapp.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import com.app.launcherapphomescreenpracticalapp.R;


public class WeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetcity = context.getString(R.string.widgetcity);
        CharSequence widgetcountry = context.getString(R.string.widgetcountry);
        CharSequence widgettemp = context.getString(R.string.widgettemp);
        CharSequence widgetdisc = context.getString(R.string.widgetdisc);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setTextViewText(R.id.city, widgetcity);
        views.setTextViewText(R.id.country, widgetcountry);
        views.setTextViewText(R.id.temp, widgettemp);
        views.setTextViewText(R.id.disc, widgetdisc);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }



}