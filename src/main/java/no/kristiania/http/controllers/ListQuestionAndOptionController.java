package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.db.option.Option;
import no.kristiania.db.option.OptionDao;
import no.kristiania.db.question.Question;
import no.kristiania.db.question.QuestionDao;

import java.sql.SQLException;

public class ListQuestionAndOptionController implements HttpController {
    private QuestionDao questionDao;
    private OptionDao optionDao;

    public ListQuestionAndOptionController(QuestionDao questionDao, OptionDao optionDao) {
        this.questionDao = questionDao;
        this.optionDao = optionDao;
    }

    @Override
    public String getPath() {
        return "/api/question";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String response = "";

        for (Question question : questionDao.listAll()){
            response += "<div  class='contentContainer' id='questionDiv'><form method='POST' action='/api/newAnswer'>" +
                    "<h2><input type='hidden' checked='true' name='questionId' value='" + question.getQuestionId() + "'>" + question.getQuestionTitle() + "</h2>" +
                    "<p class='descriptionDiv'>" + question.getQuestionDescription() + "</p><br>";

            for (Option option : optionDao.listAll()){
                if(question.getQuestionId() == option.getQuestionId()){
                    response +=
                            "<div class='optionsInput'><input class='checkbox' type='checkbox' name='optionId' value='" + option.getOptionId() + "'>" +
                            option.getOptionId() + ". " + option.getOptionName() + "</div>"
                    ;
                }
            }
        response += "<br><button class='optionsAddButton'>Submit answer</button></form></div><br><br>";
        }

        return new HttpMessage("HTTP/1.1 200 OK", response);
    }
}
