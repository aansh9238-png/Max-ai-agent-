package com.example.maxai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout chatContainer;
    private ScrollView scrollView;
    private EditText etInput;
    private ImageButton btnSend, btnMic, btnSettings;
    private GlowingOrbView orbView;
    private TextView tvStatus, tvGreeting, navHome, navMemory, navSettings;
    private GeminiHelper geminiHelper;
    private TTSHelper ttsHelper;
    private AppLauncher appLauncher;

    private static final int MIC_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatContainer = findViewById(R.id.chatContainer);
        scrollView = findViewById(R.id.scrollView);
        etInput = findViewById(R.id.etInput);
        btnSend = findViewById(R.id.btnSend);
        btnMic = findViewById(R.id.btnMic);
        btnSettings = findViewById(R.id.btnSettings);
        orbView = findViewById(R.id.orbView);
        tvStatus = findViewById(R.id.tvStatus);
        tvGreeting = findViewById(R.id.tvGreeting);
        navHome = findViewById(R.id.navHome);
        navMemory = findViewById(R.id.navMemory);
        navSettings = findViewById(R.id.navSettings);

        geminiHelper = new GeminiHelper(this);
        ttsHelper = new TTSHelper(this);
        appLauncher = new AppLauncher(this);

        SharedPreferences prefs = getSharedPreferences("max_prefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "Friend");
        tvGreeting.setText("Hello " + userName + "!");

        btnSend.setOnClickListener(v -> {
            String text = etInput.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                etInput.setText("");
            }
        });

        btnMic.setOnClickListener(v -> startVoiceInput());

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        navMemory.setOnClickListener(v ->
                startActivity(new Intent(this, MemoryActivity.class)));

        navSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void sendMessage(String userMsg) {
        addUserMessage(userMsg);
        tvStatus.setText("Thinking...");

        geminiHelper.sendMessage(userMsg, response -> {
            runOnUiThread(() -> {
                addMaxMessage(response);
                tvStatus.setText("Speaking...");
                ttsHelper.speak(response, new TTSHelper.SpeechListener() {
                    @Override
                    public void onStart() {}

                    @Override
                    public void onDone() {
                        runOnUiThread(() -> tvStatus.setText("Tap mic to speak"));
                    }
                });
            });
        });
    }

    private void addUserMessage(String msg) {
        TextView tv = new TextView(this);
        tv.setText(msg);
        tv.setTextColor(0xFFFFFFFF);
        tv.setBackgroundColor(0xFF1E2838);
        tv.setPadding(20, 12, 20, 12);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        params.setMargins(80, 8, 8, 8);
        tv.setLayoutParams(params);
        chatContainer.addView(tv);
        scrollToBottom();
    }

    private void addMaxMessage(String msg) {
        TextView tv = new TextView(this);
        tv.setText(msg);
        tv.setTextColor(0xFFE8EAF0);
        tv.setBackgroundColor(0xFF121826);
        tv.setPadding(20, 12, 20, 12);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.START;
        params.setMargins(8, 8, 80, 8);
        tv.setLayoutParams(params);
        chatContainer.addView(tv);
        scrollToBottom();
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        try {
            startActivityForResult(intent, MIC_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this, "Microphone not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MIC_REQUEST && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                sendMessage(results.get(0));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsHelper.shutdown();
    }
}
