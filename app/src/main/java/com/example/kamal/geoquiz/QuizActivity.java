package com.example.kamal.geoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextview;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextview = (TextView) findViewById(R.id.question_textview);
        mQuestionTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex++;
                updateQuestion();
            }
        });
        // Initialise the textview with first question from the question bank.
        updateQuestion();

        /**
         * Retrieve the inflated objects from resources and assign them to button variables and
         * then implement interfaces for the two buttons
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

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the index of the question and update question view.
                if (mCurrentIndex == mQuestionBank.length-1) {
                    Toast.makeText(QuizActivity.this, R.string.no_next_question,
                            Toast.LENGTH_SHORT).show();
                } else {
                    mCurrentIndex++;
                    updateQuestion();
                }
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
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
    }

    // This method checks if the answer is correct/incorrect.
    private void checkAnswer(boolean userPressedButton) {
        boolean isAnswerTrue = mQuestionBank[mCurrentIndex].getAnswerTrue();

        int messageResId = 0;

        /**
         * If the option selected and answer match, "Correct" is shown in toast
         * message, otherwise "Incorrect" is displayed.
         */
        if (userPressedButton == isAnswerTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
