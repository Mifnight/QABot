package cn.buptteam.dao;

/**
 * Created by bitholic on 16/9/6.
 */
public class QAs {
    private Integer id;
    private String question;
    private String answer;

    public QAs() {}

    public QAs(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public QAs(Integer id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
