package com.example.kamal.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";

    // It is key for question for the key-value pair in the bundle.
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_ANSWERED_ARRAY = "questionansweredarray";
    private static final String KEY_CHEATER = "cheater";
    private static final String KEY_CHEATED_ARRAY = "answercheatedarray";

    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionTextview;
    private Button mCheatButton;
    private TextView mCheatCountTextView;

    // This array stores the questions.
    private Question[] mQuestionBank = new Question[] {
      new Question(R.string.question_india, true),
      new Question(R.string.question_australia, true),
      new Question(R.string.question_africa, false),
      new Question(R.string.question_americas, true),
      new Question(R.string.question_asia, true),
      new Question(R.string.question_mideast, false),
      new Question(R.string.question_oceans, true)
    };

    // This variable is used to keep track of questions in question bank.
    private int mCurrentIndex = 0;
    // This variable will store percentage score for the quiz.
    private double mScore = 0.0;
    // This variable will keep a track of the number of times the user has cheated.
    private int mCheatCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // Save the activity instance state for any runtime changes
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);

            boolean[] mAnsweredArray = savedInstanceState.getBooleanArray(KEY_ANSWERED_ARRAY);
            for (int i = 0; i < mAnsweredArray.length; i++) {
                mQuestionBank[i].setAnswered(mAnsweredArray[i]);
            }

            boolean[] mCheatedArray = savedInstanceState.getBooleanArray(KEY_CHEATED_ARRAY);
            for (int i = 0; i < mCheatedArray.length; i++) {
                mQuestionBank[i].setCheated(mCheatedArray[i]);
            }
        }

        mQuestionTextview = (TextView) findViewById(R.id.question_textview);
        // Change to next question when question text is clicked
        mQuestionTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex++;
                updateQuestion();
            }
        });

        /*
          Retrieve the inflated objects from resources and assign them to button variables and
          then implement interfaces for the two buttons.
         */
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        ImageButton nextButton = (ImageButton) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the index of the question and update question view.
                if (mCurrentIndex == mQuestionBank.length-1) {
                    Toast.makeText(QuizActivity.this, R.string.no_next_question,
                            Toast.LENGTH_SHORT).show();
                    // Compute percentage and show result upto 2 decimal places.
                    mScore = (mScore/mQuestionBank.length) * 100;
                    Toast.makeText(QuizActivity.this, "Your percentage score is " +
                            String.format("%.2f", mScore) + ".", Toast.LENGTH_SHORT).show();
                } else {
                    mCurrentIndex++;
                    updateQuestion();
                }
            }
        });

        ImageButton prevButton = (ImageButton) findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement the index and display previous question in the array.
                if (mCurrentIndex > 0) {
                    mCurrentIndex--;
                    updateQuestion();
                } else {
                    Toast.makeText(QuizActivity.this, R.string.no_prev_question,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].getAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mCheatCountTextView = (TextView) findViewById(R.id.cheat_count_textview);

        // Initialise the textview with first question from the question bank.
        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    /**
     * If the user has not seen the answer despite navigating to CheatActivity, the
     * data returned is null which crashed the application. Also if the user cheats,
     * then the Cheat button is disabled in order to not let the user waste his cheat
     * count and the cheat counter variable is incremented.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean temp = false;
        if (data != null) {
            temp = CheatActivity.wasAnswerShown(data);
            mCheatButton.setEnabled(false);
        } else {
            mCheatButton.setEnabled(true);
        }
        if (temp) {
            mCheatCount++;
        }
        mQuestionBank[mCurrentIndex].setCheated(temp);
        setCheatCountMessage();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);

        boolean[] mAnsweredArray = new boolean[mQuestionBank.length];
        for (int i = 0; i < mAnsweredArray.length; i++) {
            mAnsweredArray[i] = mQuestionBank[i].getAnswered();
        }
        savedInstanceState.putBooleanArray(KEY_ANSWERED_ARRAY, mAnsweredArray);

        boolean[] mCheatedArray = new boolean[mQuestionBank.length];
        for (int i = 0; i < mCheatedArray.length; i++) {
            mCheatedArray[i] = mQuestionBank[i].getCheated();
            Log.d("cheat", Boolean.toString(mCheatedArray[i]));
        }
        savedInstanceState.putBooleanArray(KEY_CHEATED_ARRAY, mCheatedArray);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    // This method updates question on clicking next button.
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getQuestionTextId();
        mQuestionTextview.setText(question);
        setAnswerButtons();
        mCheatButton.setEnabled(true);
        // If the user has cheated three times, the cheat button is disabled.
        if (mCheatCount >= 3) {
            mCheatButton.setEnabled(false);
        }
        setCheatCountMessage();
    }

    // This method checks if the answer is correct/incorrect.
    private void checkAnswer(boolean userPressedButton) {
        mQuestionBank[mCurrentIndex].setAnswered(true);
        boolean isAnswerTrue = mQuestionBank[mCurrentIndex].getAnswerTrue();
        int messageResId;

        /*
          If user cheats, show a judgement message otherwise if the option selected and answer
          match, "Correct" is shown as message, otherwise "Incorrect" is displayed.
         */
        if (mQuestionBank[mCurrentIndex].getCheated()) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedButton == isAnswerTrue) {
                messageResId = R.string.correct_toast;
                mScore += 1;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        setAnswerButtons();
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method enables/disables true/false answer buttons. If the question has been
     * answered once, the buttons are disabled to prevent multiple answers to the same
     * question.
     */
    private void setAnswerButtons() {
        if (mQuestionBank[mCurrentIndex].getAnswered()) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    /**
     * This method sets the message to be displayed below the Cheat button depending
     * on the number of chances left.
     */
    private void setCheatCountMessage() {
        switch (mCheatCount) {
            case 1:
                mCheatCountTextView.setText("You got 2 chances.");
                break;
            case 2:
                mCheatCountTextView.setText("Last chance left.");
                break;
            case 3:
                mCheatCountTextView.setText("Enough cheating!");
                break;
            default:
                mCheatCountTextView.setText("You get 3 chances to cheat.");
                break;
        }
    }
}