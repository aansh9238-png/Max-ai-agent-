package com.example.maxai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {

    private EditText etApiKey, etName;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("max_prefs", MODE_PRIVATE);

        if (prefs.contains("api_key")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_setup);

        etApiKey = findViewById(R.id.etApiKey);
        etName = findViewById(R.id.etName);
        Button btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            String apiKey = etApiKey.getText().toString().trim();
            String name = etName.getText().toString().trim();

            if (apiKey.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            prefs.edit()
                    .putString("api_key", apiKey)
                    .putString("user_name", name)
                    .putString("personality", "Girlfriend")
                    .apply();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
