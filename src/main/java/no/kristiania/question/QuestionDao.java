package no.kristiania.question;

import no.kristiania.person.Person;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {

    private final DataSource dataSource;

    public QuestionDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public static DataSource createDataSource(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/survey_db");
        dataSource.setUser("servey_dbuser");
        dataSource.setPassword("TvsVM5wRCdh");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        return dataSource;
    }

    public void saveQuestion(Question question) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into question (question_title, question_description) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, question.getQuestionTitle());
                statement.setString(2, question.getQuestionDescription());

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    question.setQuestion_id(rs.getLong("question_id"));
                }
            }
        }
    }

    public Question retrieveQuestion(long question_id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from question where question_id = ?")) {
                statement.setLong(1, question_id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return readFromResultSetQuestion(rs);
                }
            }
        }
    }

    private Question readFromResultSetQuestion(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestion_id(rs.getLong("question_id"));
        question.setQuestionTitle(rs.getString("question_title"));
        question.setQuestionDescription(rs.getString("question_description"));
        return question;
    }

    public List<Question> listAllQuestion() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from question"
            )) {

                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Question> result = new ArrayList<>();

                    while (rs.next()){
                        result.add(readFromResultSetQuestion(rs));
                    }

                    return result;
                }
            }
        }
    }
}
