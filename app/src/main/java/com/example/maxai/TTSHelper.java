package com.example.maxai;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TTSHelper {

    private TextToSpeech tts;
    private boolean isReady = false;

    public interface SpeechListener {
        void onStart();
        void onDone();
    }

    public TTSHelper(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
                isReady = true;
            }
        });
    }

    public void speak(String text) {
        if (isReady && text != null && !text.isEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void speak(String text, SpeechListener listener) {
        if (!isReady || text == null || text.isEmpty()) return;

        tts.setOnUtteranceProgressListener(new android.speech.tts.UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                if (listener != null) listener.onStart();
            }

            @Override
            public void onDone(String utteranceId) {
                if (listener != null) listener.onDone();
            }

            @Override
            public void onError(String utteranceId) {
                if (listener != null) listener.onDone();
            }
        });

        java.util.HashMap<String, String> params = new java.util.HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "max_speech");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
