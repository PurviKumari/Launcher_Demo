package com.app.launcherapphomescreenpracticalapp.AppList;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.app.launcherapphomescreenpracticalapp.R;
import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {
    private PackageManager manager;
    private List<AppDetail> apps;
    private GridView apps_list;
    private ImageView appIcon;
    private TextView appLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        bind_view();
        loadApps();
        loadListView();

        apps_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(position).app_name.toString());
                AppListActivity.this.startActivity(i);
            }
        });

    }

    private void bind_view() {
        apps_list = findViewById(R.id.apps_list);
        apps_list = (GridView)findViewById(R.id.apps_list);
    }

    private void loadListView(){

        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item,apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).app_icon);

                appLabel = (TextView)convertView.findViewById(R.id.item_app_name);
                appLabel.setText(apps.get(position).app_label);

                return convertView;
            }
        };

        apps_list.setAdapter(adapter);
    }

    private void loadApps(){
        manager = getPackageManager();
        apps = new ArrayList<AppDetail>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            AppDetail app = new AppDetail();
            app.app_label = ri.loadLabel(manager);
            app.app_name = ri.activityInfo.packageName;
            app.app_icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }


}