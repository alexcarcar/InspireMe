package alex.carcar.inspireme;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static TextToSpeech tts;

    ArrayList<String> quotes;
    TextView quote;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
            }
        });
        quote = findViewById(R.id.quote);
        readQuotes();
        pickQuote();
    }

    private void readQuotes() {
        quotes = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.quotes);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (reader.ready()) {
                quotes.add(reader.readLine());
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickQuote() {
        if (!quotes.isEmpty()) {
            quote.setText(quotes.get(random.nextInt(quotes.size())));
        }
    }

    public void inspireMe(View view) {
        pickQuote();
    }

    public void onClickVoice(View view) {
        sayQuote();
    }

    private void sayQuote() {
        say(quote.getText().toString());
    }

    private void say(String toSpeak) {
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    public void onNext(View view) {
        pickQuote();
        sayQuote();
    }
}