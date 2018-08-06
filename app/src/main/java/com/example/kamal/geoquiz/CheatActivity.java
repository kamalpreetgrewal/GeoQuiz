package com.example.kamal.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    // Define a key for extra that stores the answer.
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.com.kamal.geoquiz.answer_is_true";

    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.com.kamal.geoquiz.answer_shown";
    private static final String DID_CHEAT = "cheated";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private boolean mIsCheater;

    // Encapsulate the intent that starts this activity and call this method in calling activity
    public static Intent newIntent(Context context, boolean answerIsTrue) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerTextView = (TextView) findViewById(R.id.answer_textview);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        /*
          If device is rotated, the answer text is not shown and implies that the user
          did not see the answer, so it is important to save orientation changes.
         */
        if(savedInstanceState != null) {
            mIsCheater = savedInstanceState.getBoolean(DID_CHEAT);
            mAnswerIsTrue = savedInstanceState.getBoolean(EXTRA_ANSWER_IS_TRUE);
            if (mAnswerIsTrue && mIsCheater) {
                showAnswerText();
            }
        }

        // Show the answer on clicking 'Show Answer' button after retrieving answer from intent
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAnswerText();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(DID_CHEAT, mIsCheater);
        savedInstanceState.putBoolean(EXTRA_ANSWER_IS_TRUE, mAnswerIsTrue);
    }

    /**
     * This method sets the result to be sent to QuizActivity.
     * @param isAnswerShown
     */
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, intent);
    }

    /**
     * This method keeps the answer intact on screen if the user keeps rotating the device
     * after pressing the 'Show Answer' button and also does not clear the fact that the user
     * has cheated and orientation change won't change it.
     */
    private void showAnswerText() {
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
        setAnswerShownResult(true);
        mIsCheater = true;
    }
}