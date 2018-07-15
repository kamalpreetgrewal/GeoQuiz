package com.example.kamal.geoquiz;

/**
 * Created by Kamal on 14-07-2018.
 */

public class Question {

    /**
     *  Two member variables - question text and question answer. The question text is
     *  an int due to the fact that it will refer to a string resource.
     */

    private int mQuestionTextId;
    private Boolean mAnswerTrue;

    public Question(int questionTextId, Boolean answerTrue) {
        mQuestionTextId = questionTextId;
        mAnswerTrue = answerTrue;
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
}
