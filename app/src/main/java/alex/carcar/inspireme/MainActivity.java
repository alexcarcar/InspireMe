package alex.carcar.inspireme;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import alex.common.AlexFile;
import alex.common.AlexView;
import alex.common.voice.AlexVoice;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> quotes;
    TextView quote, commentQuote, mainComments;
    Random random;
    Button sayButton, sayNextButton, inspireButton, commentSave, commentCancel, commentClear;
    FloatingActionButton addButton;
    LinearLayout flashLayout;
    View[] flashScreen, mainScreen, commentScreen;
    EditText comment;
    int currentQuote;
    String currentComments, currentFilename;

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
        addButton = findViewById(R.id.addButton);
        comment = findViewById(R.id.comment);
        mainComments = findViewById(R.id.mainComments);
        mainComments.setOnClickListener(v -> AlexVoice.say(currentComments));
        mainScreen = new View[]{sayButton, sayNextButton, inspireButton, quote, addButton, mainComments};

        sayButton.setOnClickListener(v -> sayQuote());
        sayNextButton.setOnClickListener(v -> onNext());
        inspireButton.setOnClickListener(v -> pickQuote());
        addButton.setOnClickListener(v -> addThoughts());
        pickQuote();

        // COMMENT SCREEN
        comment = findViewById(R.id.comment);
        commentQuote = findViewById(R.id.commentQuote);
        commentSave = findViewById(R.id.commentSave);
        commentCancel = findViewById(R.id.commentCancel);
        commentClear = findViewById(R.id.commentClear);
        commentScreen = new View[]{commentQuote, comment, commentSave, commentCancel, commentClear};
        commentCancel.setOnClickListener(v -> AlexView.hideAndShow(commentScreen, mainScreen));
        commentClear.setOnClickListener(v -> comment.setText(""));
        commentSave.setOnClickListener(v -> saveComment());

        // FLASH SCREEN
        flashLayout = findViewById(R.id.flashLayout);
        flashScreen = new View[]{flashLayout};
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::showFlashScreen, 1000);
    }

    private void saveComment() {
        currentComments = comment.getText().toString().trim();
        mainComments.setText(currentComments);
        AlexFile.saveString(this, currentFilename, currentComments);
        AlexView.hideAndShow(commentScreen, mainScreen);
        AlexView.toggleKeyboard(this, mainComments);
    }

    private void addThoughts() {
        comment.setText(currentComments);
        commentQuote.setText(quotes.get(currentQuote));
        AlexView.hideAndShow(mainScreen, commentScreen);
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
            currentQuote = random.nextInt(quotes.size());
            currentFilename = "quotes-" + currentQuote;
            currentComments = AlexFile.readAsString(this, currentFilename);
            quote.setText(quotes.get(currentQuote));
            mainComments.setText(currentComments);
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