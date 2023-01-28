package alex.carcar.inspireme;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import alex.common.AlexChoose;
import alex.common.AlexFile;
import alex.common.AlexView;
import alex.common.voice.AlexVoice;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> quotes;
    TextView quote;
    Random random;
    Button sayButton, sayNextButton, inspireButton;
    LinearLayout flashLayout;
    View[] flashScreen, mainScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quotes = AlexFile.readAsList(getResources(), R.raw.quotes);
        random = new Random();
        quote = findViewById(R.id.quote);
        sayButton = findViewById(R.id.sayButton);
        sayNextButton = findViewById(R.id.sayNextButton);
        inspireButton = findViewById(R.id.inspireButton);
        mainScreen = new View[] {sayButton, sayNextButton, inspireButton, quote};

        sayButton.setOnClickListener(v -> sayQuote());
        sayNextButton.setOnClickListener(v -> onNext());
        inspireButton.setOnClickListener(v -> pickQuote());
        pickQuote();

        // FLASH SCREEN
        flashLayout = findViewById(R.id.flashLayout);
        flashScreen = new View[]{flashLayout};
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::showFlashScreen, 1000);
    }

    private void showFlashScreen() {
        String toSpeak = getResources().getString(R.string.flash_screen_tag);
        AlexVoice.say(toSpeak);
        flashLayout.setVisibility(View.VISIBLE);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::hideFlashScreen, 3500);
    }

    private void hideFlashScreen() {
        AlexView.hideAndShow(flashScreen, mainScreen);
    }

    private void pickQuote() {
        if (!quotes.isEmpty()) {
            quote.setText(AlexChoose.getFromList(random, quotes));
        }
    }

    private void sayQuote() {
        AlexVoice.say(quote.getText().toString());
    }

    @Override
    protected void onPause() {
        AlexVoice.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        AlexVoice.start(getApplicationContext());
        super.onResume();
    }

    public void onNext() {
        pickQuote();
        sayQuote();
    }
}