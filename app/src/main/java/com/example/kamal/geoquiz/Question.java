package com.example.kamal.geoquiz;

/**
 * Created by Kamal on 14-07-2018.
 */

public class Question {

    /**
     *  Three member variables - question text, answer to the question and if the question
     *  has been answered by the user. The question text is an int due to the fact that it
     *  will refer to a string resource.
     */
    private int mQuestionTextId;
    private Boolean mAnswerTrue;
    private Boolean mIsAnswered;

    public Question(int questionTextId, Boolean answerTrue) {
        mQuestionTextId = questionTextId;
        mAnswerTrue = answerTrue;
        mIsAnswered = false;
    }

    public int getQuestionTextId() {
        return mQuestionTextId;
    }

    public void setQuestionTextId(int questionTextId) {
        mQuestionTextId = questionTextId;
    }

    public Boolean getAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(Boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public Boolean getAnswered() {
        return mIsAnswered;
    }

    public void setAnswered(Boolean answered) {
        mIsAnswered = answered;
    }
}
