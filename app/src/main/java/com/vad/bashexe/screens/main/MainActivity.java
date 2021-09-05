package com.vad.bashexe.screens.main;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vad.bashexe.R;
import com.vad.bashexe.temperaturepaath.TemperaturePaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST = 11235;
    private TextView textViewTerm;
    private EditText terminal;
    private final String[] permission = {"android.permission.READ_EXTERNAL_STORAGE"};
    private boolean request;

    private Map<String, String> listPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        terminal = (EditText) findViewById(R.id.editTextTextMultiLine);
        textViewTerm = (TextView) findViewById(R.id.text_view_term);
        requestPermissions(permission, READ_REQUEST);

        listPaths = new HashMap<>();

        for(Field field: TemperaturePaths.class.getDeclaredFields()){
            int modifiers = field.getModifiers();
            if(Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)){
                listPaths.put(field.getName(), String.valueOf(field.getType()));
            }
        }
    }

    public void execute(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //String result = runExecute(terminal.getText().toString())+"\n end";
                        //String result = cat("sys/devices/virtual/thermal/thermal_zone0/temp");
                        String result = parsingListPaths(listPaths);
                        textViewTerm.setText(result);
                        System.out.println(result);
                    }
                });

            }
        }).start();
    }

    private String runExecute(String resource) {
        Process process;
        String line = "";
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec(resource);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();

    }

    private String cat(String file) {
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(file));

            while((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result.toString();
    }

    private String parsingListPaths(Map<String, String> listPaths) {
        StringBuilder result = new StringBuilder();
        for(String key: listPaths.keySet()){
            result.append(key).append(" ").append(cat(listPaths.get(key)));
        }

        return result.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==READ_REQUEST){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                request = true;
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            }else{
                request=false;
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}