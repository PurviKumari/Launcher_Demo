package com.app.launcherapphomescreenpracticalapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button yes,no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind_view();



        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DefaultHomeApp(MainActivity.this).launchapplicationHomeOrClearDefaultsDialog();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HomeScreen.class));

            }
        });

//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setTitle("Select Application as a Launcher");
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage("Do you want to set this application as default??");
//        alertDialog.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        new DefaultHomeApp(MainActivity.this).launchapplicationHomeOrClearDefaultsDialog();
//
//                    }
//                });
//        alertDialog.setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(MainActivity.this,HomeScreen.class));
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();


    }

    private void bind_view() {
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
    }


}