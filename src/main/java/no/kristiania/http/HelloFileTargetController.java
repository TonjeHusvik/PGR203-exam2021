package no.kristiania.http;

import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.HttpServer.query;

public class HelloFileTargetController implements HttpController{
    @Override
    public String getPath() {
        return "/hello";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String yourName = "world";
        if (query != null) {
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(query);
            yourName = queryMap.get("lastName") + ", " + queryMap.get("firstName");
        }
        String responseText = "<p>Hello " + yourName + "!</p>";
        String contentType = "text/html; charset=utf-8";

        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}
