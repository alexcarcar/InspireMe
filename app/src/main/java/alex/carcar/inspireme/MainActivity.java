package alex.carcar.inspireme;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> quotes;
    TextView quote;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quote = findViewById(R.id.quote);
        random = new Random();
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
}