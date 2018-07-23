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

    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionTextview;
    private Button mCheatButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // Save the activity instance state for any runtime changes
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
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
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
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
    }

    // This method checks if the answer is correct/incorrect.
    private void checkAnswer(boolean userPressedButton) {
        Question question = mQuestionBank[mCurrentIndex];
        question.setAnswered(true);
        setAnswerButtons();

        boolean isAnswerTrue = question.getAnswerTrue();
        int messageResId;

        /*
          If the option selected and answer match, "Correct" is shown in toast
          message, otherwise "Incorrect" is displayed.
         */
        if (userPressedButton == isAnswerTrue) {
            messageResId = R.string.correct_toast;
            mScore += 1;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method enables/disables true/false answer buttons. If the question has been
     * answered once, the buttons are disabled to prevent multiple answers to the same
     * question.
     */
    private void setAnswerButtons() {
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        if (currentQuestion.getAnswered()) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }
}