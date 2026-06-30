package com.example.maxai;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Set;

public class MemoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences memPrefs = getSharedPreferences("max_memory", MODE_PRIVATE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);
        layout.setBackgroundColor(0xFF0A0E1A);

        TextView title = new TextView(this);
        title.setText("Memory");
        title.setTextColor(0xFF2196F3);
        title.setTextSize(26);
        title.setPadding(0, 0, 0, 20);
        layout.addView(title);

        Button btnBack = new Button(this);
        btnBack.setText("Back");
        btnBack.setBackgroundColor(0xFF121826);
        btnBack.setTextColor(0xFFE8EAF0);
        btnBack.setOnClickListener(v -> finish());
        layout.addView(btnBack);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout memoryList = new LinearLayout(this);
        memoryList.setOrientation(LinearLayout.VERTICAL);
        memoryList.setPadding(0, 20, 0, 0);

        Set<String> keys = memPrefs.getAll().keySet();
        if (keys.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No memories yet. Start chatting with Max!");
            empty.setTextColor(0xFF7A8499);
            empty.setTextSize(14);
            memoryList.addView(empty);
        } else {
            for (String key : keys) {
                TextView item = new TextView(this);
                item.setText(key + ": " + memPrefs.getString(key, ""));
                item.setTextColor(0xFFE8EAF0);
                item.setPadding(0, 10, 0, 10);
                memoryList.addView(item);
            }
        }

        scrollView.addView(memoryList);
        layout.addView(scrollView);
        setContentView(layout);
    }
}
