package com.app.launcherapphomescreenpracticalapp;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.launcherapphomescreenpracticalapp.AppList.AppListActivity;
import com.app.launcherapphomescreenpracticalapp.Widget.WeatherDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class HomeScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ProgressBar prog_bar;
    TextView text_bet_per;
    private Context mContext;

    private int mProgressStatus = 0;

    Button app_launcher;
    TextView date_day;
    ImageView bettery_usb;

    private String dateTime;
    ViewGroup widget_home_layout;
    Spinner spinner;

    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;
    static final int APPWIDGET_HOST_ID = 2037;
    ArrayAdapter<String> adapterItems;
    String[] items = {"Beijing", "Berlin", "Cardiff", "Edinburgh", "London", "Nottingham"};
    public Intent pickIntent;
    Button app_widget;
    LinearLayout main_l1, country_list;
    TextView city, county, temp, disc;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }

        bindview();
        bettry_info();
        date_day_info();
        weather_info();

        app_launcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, AppListActivity.class));
            }
        });

    }

    private void bindview() {
        prog_bar = findViewById(R.id.prog_bar);
        text_bet_per = findViewById(R.id.text_bet_per);
        app_launcher = findViewById(R.id.app_launcher);
        date_day = findViewById(R.id.date_day);
        bettery_usb = findViewById(R.id.bettery_usb);
        widget_home_layout = (ViewGroup) findViewById(R.id.widget_home_layout);
        spinner = findViewById(R.id.spinner);
        app_widget = findViewById(R.id.app_widget);
        main_l1 = findViewById(R.id.main_l1);
        country_list = findViewById(R.id.country_list);
        city = findViewById(R.id.city);
        county = findViewById(R.id.county);
        temp = findViewById(R.id.temp);
        disc = findViewById(R.id.disc);
    }

    private void date_day_info() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = day + "/" + (month + 1) + "/" + year;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE ,LLLL ,dd");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        date_day.setText(dateTime);
    }

    private void bettry_info() {
        mContext = getApplicationContext();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(mBroadcastReceiver, iFilter);
        text_bet_per = (TextView) findViewById(R.id.text_bet_per);
        prog_bar = (ProgressBar) findViewById(R.id.prog_bar);
    }

    private void weather_info() {
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item_country, items);
        spinner.setOnItemSelectedListener(this);

        spinner.setAdapter(adapterItems);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String items = parent.getItemAtPosition(position).toString();
        String country_name = "beijing";
        if (position == 0) {
            country_name = "beijing";
        }
        if (position == 1) {
            country_name = "berlin";
        }
        if (position == 2) {
            country_name = "cardiff";
        }
        if (position == 3) {
            country_name = "edinburgh";
        }
        if (position == 4) {
            country_name = "london";
        }
        if (position == 5) {
            country_name = "nottingham";
        }
        loadApiList(country_name);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void add_widgets() {
        app_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWidget();
            }
        });
    }

    void selectWidget() {
        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
        pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        addEmptyData(pickIntent);
        startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
    }

    void addEmptyData(Intent pickIntent) {
        ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<AppWidgetProviderInfo>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == R.id.REQUEST_PICK_APPWIDGET) {
                configureWidget(data);
            } else if (requestCode == R.id.REQUEST_CREATE_APPWIDGET) {
                createWidget(data);
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    private void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, R.id.REQUEST_CREATE_APPWIDGET);
        } else {
            createWidget(data);
        }
    }

    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        widget_home_layout.addView(hostView);

        hostView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                new AlertDialog.Builder(HomeScreen.this)
//                        .setTitle("Options")
//                        .setMessage("Do you want to delete widget?")
//                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                removeWidget(hostView);
//                                Toast.makeText(HomeScreen.this, "Widget Deleted", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dismissDialog(i);
//                            }
//                        }).show();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeScreen.this);
                alertDialog.setTitle("Options");
                alertDialog.setMessage("Do you want to delete widget?");
                alertDialog.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeWidget(hostView);
                                country_list.setVisibility(View.INVISIBLE);
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            }
        });


//        hostView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    ClipData data = ClipData.newPlainText("", "");
//                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                    view.startDrag(data, shadowBuilder, view, 0);
//                    return true;
//                } else {
//                    return false;
//                }
//
//            }
//        });

//        mainlayout.setOnDragListener(new View.OnDragListener() {
//            Drawable enterShape = getResources().getDrawable(R.drawable.example_appwidget_preview);
//            Drawable normalShape = getResources().getDrawable(R.drawable.example_appwidget_preview);
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                int action = event.getAction();
//                switch (event.getAction()) {
//                    case DragEvent.ACTION_DRAG_STARTED:
//                        // do nothing
//                        break;
//                    case DragEvent.ACTION_DRAG_ENTERED:
//                        v.setBackgroundDrawable(enterShape);
//                        break;
//                    case DragEvent.ACTION_DRAG_EXITED:
//                        v.setBackgroundDrawable(normalShape);
//                        break;
//                    case DragEvent.ACTION_DROP:
//                        // Dropped, reassign View to ViewGroup
//                        View view = (View) event.getLocalState();
//                        ViewGroup owner = (ViewGroup) view.getParent();
//                        owner.removeView(view);
//                        GridLayout container = (GridLayout) v;
//                        container.addView(view);
//                        view.setVisibility(View.VISIBLE);
//                        break;
//                    case DragEvent.ACTION_DRAG_ENDED:
//                        v.setBackgroundDrawable(normalShape);
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppWidgetHost.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }

    public void removeWidget(AppWidgetHostView hostView) {
        mAppWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());
        widget_home_layout.removeView(hostView);
    }

    private void loadApiList(String country_name) {

        String JSON_URL = "https://weather.bfsah.com/" + country_name;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.i("===", response);

                        try {
                            JSONObject obj = new JSONObject(response);
//                            Log.i("===", obj.toString());


                            WeatherDetails weatherDetails = new WeatherDetails(obj.getString("city"), obj.getString("country"), obj.getInt("temperature"), obj.getString("description"));

                            String temp_11 = String.valueOf(weatherDetails.getTemperature()) + "°C";

                            city.setText(weatherDetails.getCity());
                            county.setText(weatherDetails.getCounty());
                            temp.setText(temp_11);
                            disc.setText(weatherDetails.getDescription());

//                            String temp = String.valueOf(hero.getTemperature()) + "°C";
//
//                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//                            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.weather_widget);
//                            ComponentName thisWidget = new ComponentName(getApplicationContext(), WeatherWidget.class);
//                            remoteViews.setTextViewText(R.id.city, hero.getCity());
//                            remoteViews.setTextViewText(R.id.country, hero.getCountry());
//                            remoteViews.setTextViewText(R.id.temp, temp);
//                            remoteViews.setTextViewText(R.id.disc, hero.getDescription());
//                            appWidgetManager.updateAppWidget(thisWidget, remoteViews);


                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.i("catch", "onErrorResponse: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        NetworkResponse networkResponse = error.networkResponse;
                        String result = new String(networkResponse.data);
//                        Log.i("===", "onErrorResponse: " + result);
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                        city.setText("-------");
                        county.setText("-------");
                        temp.setText("-------");
                        disc.setText("-------");
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            text_bet_per.setText("Battery Scale : " + scale);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            text_bet_per.setText(text_bet_per.getText() + "\nBattery Level : " + level);
            float percentage = level / (float) scale;
            mProgressStatus = (int) ((percentage) * 100);
            text_bet_per.setText("" + mProgressStatus + "%");

            prog_bar.setProgress(mProgressStatus);

            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

            if (plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                bettery_usb.setVisibility(View.VISIBLE);
            } else {
                bettery_usb.setVisibility(View.INVISIBLE);
            }
        }
    };

}