package com.example.maxai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("max_prefs", MODE_PRIVATE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);
        layout.setBackgroundColor(0xFF0A0E1A);

        TextView title = new TextView(this);
        title.setText("Settings");
        title.setTextColor(0xFF2196F3);
        title.setTextSize(26);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);

        TextView nameLabel = new TextView(this);
        nameLabel.setText("Name: " + prefs.getString("user_name", ""));
        nameLabel.setTextColor(0xFFE8EAF0);
        nameLabel.setTextSize(16);
        nameLabel.setPadding(0, 10, 0, 10);
        layout.addView(nameLabel);

        TextView personalityLabel = new TextView(this);
        personalityLabel.setText("Personality Mode");
        personalityLabel.setTextColor(0xFFE8EAF0);
        personalityLabel.setTextSize(16);
        personalityLabel.setPadding(0, 20, 0, 10);
        layout.addView(personalityLabel);

        Spinner spinner = new Spinner(this);
        String[] modes = {"Girlfriend", "Boss", "Hyper Energetic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, modes);
        spinner.setAdapter(adapter);

        String currentPersonality = prefs.getString("personality", "Girlfriend");
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(currentPersonality)) {
                spinner.setSelection(i);
                break;
            }
        }
        layout.addView(spinner);

        Button btnSave = new Button(this);
        btnSave.setText("Save");
        btnSave.setBackgroundColor(0xFF2196F3);
        btnSave.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(0, 30, 0, 10);
        btnSave.setLayoutParams(btnParams);
        btnSave.setOnClickListener(v -> {
            String selected = modes[spinner.getSelectedItemPosition()];
            prefs.edit().putString("personality", selected).apply();
            finish();
        });
        layout.addView(btnSave);

        Button btnBack = new Button(this);
        btnBack.setText("Back");
        btnBack.setBackgroundColor(0xFF121826);
        btnBack.setTextColor(0xFFE8EAF0);
        btnBack.setOnClickListener(v -> finish());
        layout.addView(btnBack);

        setContentView(layout);
    }
}
