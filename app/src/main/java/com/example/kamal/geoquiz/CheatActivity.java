package com.example.kamal.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private TextView apiTextView;

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

        LinearLayout cheatLinearLayout = (LinearLayout) findViewById(R.id.cheat_activity_layout);

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

                // Check for backwards compatibility
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(
                            mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        apiTextView = new TextView(CheatActivity.this);
        apiTextView.setText("API Level " + Build.VERSION.SDK_INT);
        apiTextView.setTextSize(12);
        apiTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        apiTextView.setPadding(6, 6, 6, 6);
        cheatLinearLayout.addView(apiTextView);
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