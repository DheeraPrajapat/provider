package com.marius.valeyou_admin.data.beans.faq;

public class FaqModel {


    /**
     * id : 3
     * question : Where can I download earlier versions?
     * answer : You have access to all previous free versions of all extensions on this website.
     */

    private int id;
    private String question;
    private String answer;
    private boolean isClicked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
