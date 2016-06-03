package by.olkhovik.chat.servlets;

import by.olkhovik.chat.Constants;
import by.olkhovik.chat.models.*;
import by.olkhovik.chat.storage.MessageStorage;
import by.olkhovik.chat.storage.Portion;
import by.olkhovik.chat.storage.UserStorage;
import by.olkhovik.chat.utils.MessageHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eugene on 31.05.16.
 */

@WebServlet(value = "/chat")
public class ChatServlet extends HttpServlet {

    private MessageStorage messageStorage;
    private UserStorage userStorage;

    @Override
    public void init() throws ServletException {
        messageStorage = new MessageStorage();
        userStorage = new UserStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getQueryString();

        if(query == null || query.isEmpty()){
            resp.sendRedirect("/homepage.html");
            return;
        }

        if(!query.contains("users")){

            Map<String, String> map = queryToMap(query);

            String token = map.get(Constants.REQUEST_PARAM_TOKEN);
            int index = MessageHelper.parseToken(token);

            Portion portion = new Portion(index);
            String response = MessageHelper.buildServerResponseBody(messageStorage.getPortion(portion), messageStorage.size());

            resp.getOutputStream().write(response.getBytes());

        }else{

            String response = MessageHelper.buildServerResponseBodyUsers(UserStorage.getAllName());
            resp.getOutputStream().write(response.getBytes());

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());

            messageStorage.add(message);
        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String request = MessageHelper.inputStreamToString(req.getInputStream());

        try {
            JSONObject jsonObject = MessageHelper.stringToJsonObject(request);

            String messageId = jsonObject.get("id").toString();

            if(messageId != null && !messageId.isEmpty()){
                messageStorage.delete(messageId);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getQueryString();

        System.out.println(query);

        if(query == null || !query.equals("editname")){

            try {
                String message = MessageHelper.inputStreamToString(req.getInputStream());

                JSONObject jsonObject = MessageHelper.stringToJsonObject(message);

                Message mess = new Message();
                mess.setId(jsonObject.get(Constants.Message.FIELD_ID).toString());
                mess.setAuthor((String) jsonObject.get(Constants.Message.FIELD_AUTHOR));
                mess.setMessage((String) jsonObject.get(Constants.Message.FIELD_TEXT));
                mess.setTimestamp((long) jsonObject.get(Constants.Message.FIELD_TIMESTAMP));

                messageStorage.update(mess);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            try {
                String names = MessageHelper.inputStreamToString(req.getInputStream());
                JSONObject jsonObject = MessageHelper.stringToJsonObject(names);

                UserStorage.updateUser((String)jsonObject.get("oldName"), (String)jsonObject.get("newName"));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();

        for (String queryParam : query.split(Constants.REQUEST_PARAMS_DELIMITER)) {
            String paramKeyValuePair[] = queryParam.split("=");
            if (paramKeyValuePair.length > 1) {
                result.put(paramKeyValuePair[0], paramKeyValuePair[1]);
            } else {
                result.put(paramKeyValuePair[0], "");
            }
        }
        return result;
    }
}
