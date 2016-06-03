package by.olkhovik.chat.storage;

import by.olkhovik.chat.models.Message;
import by.olkhovik.chat.models.User;
import by.olkhovik.chat.utils.MessageHelper;
import org.json.simple.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FileStorage {

    private static final String FILE_NAME = "log.txt";
    private static final String FILE_NAME_USERS = "users.txt";

    public void addMessage(Message message){

        try(
                FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
                PrintStream ps = new PrintStream(fos);
        ) {

            ps.println(MessageHelper.messageToJSONObject(message));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addUser(User user){

        try(
                FileOutputStream fos = new FileOutputStream(FILE_NAME_USERS, true);
                PrintStream ps = new PrintStream(fos);
        ) {

            ps.println(MessageHelper.userToJSONObject(user));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void rewrite(List<Message> messages){
        try(
                FileOutputStream fos = new FileOutputStream(FILE_NAME);
                PrintStream ps = new PrintStream(fos);
        ) {

            for(Message message : messages){
                ps.println(MessageHelper.messageToJSONObject(message));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Message> loadMessages() throws FileNotFoundException {
        List<Message> list = new ArrayList<>();

        Scanner scanner = new Scanner(new FileReader(FILE_NAME));
        while(scanner.hasNextLine()){

            try {
                JSONObject jsonObject = MessageHelper.stringToJsonObject(scanner.nextLine());

                Message message = MessageHelper.jsonObjectToMessage(jsonObject);

                list.add(message);
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }

        }

        return list;
    }

    public List<User> loadUsers() throws FileNotFoundException{

        List<User> list = new ArrayList<>();

        Scanner scanner = new Scanner(new FileReader(FILE_NAME_USERS));

        while(scanner.hasNextLine()){
            try {
                JSONObject jsonObject = MessageHelper.stringToJsonObject(scanner.nextLine());
                User user = MessageHelper.jsonObjectToUser(jsonObject);

                list.add(user);
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public boolean deleteUser(String username) throws FileNotFoundException {
        List<User> users = loadUsers();
        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()){
            User us = iterator.next();
            if(us.getName().equals(username)){
                iterator.remove();
                rewriteUsers(users);
                return true;
            }
        }

        return false;
    }

    public boolean updateUser(String oldName, String newName) throws FileNotFoundException {
        List<User> users = loadUsers();
        for(int i = 0; i < users.size(); i++){
            if(oldName.equals(users.get(i).getName())){
                users.get(i).setName(newName);
                rewriteUsers(users);
                return true;
            }
        }

        return false;
    }

    public void rewriteUsers(List<User> users){
        try(
                FileOutputStream fos = new FileOutputStream(FILE_NAME_USERS);
                PrintStream ps = new PrintStream(fos);
        ) {

            for(User user : users){
                ps.println(MessageHelper.userToJSONObject(user));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean update(Message message) throws FileNotFoundException {

        List<Message> messages = new ArrayList<>();
        messages.addAll(loadMessages());

        for(int i = 0; i < messages.size(); i++){

            if(messages.get(i).getId().equals(message.getId())){
                messages.get(i).setMessage(message.getMessage());
                rewrite(messages);
                return true;
            }

        }
        return false;
    }

    public boolean deleteMessage(String messageId) throws FileNotFoundException {
        List<Message> list = loadMessages();

        Iterator<Message> iterator = list.iterator();

        while(iterator.hasNext()){
            Message message = iterator.next();

            if(message.getId().equals(messageId)){
                iterator.remove();
                rewrite(list);
                return true;
            }

        }

        return false;
    }

}
