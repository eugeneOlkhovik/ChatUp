package by.olkhovik.chat.storage;

import by.olkhovik.chat.models.User;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 27.05.2016.
 */
public class UserStorage {

    private static List<User> users;
    private static FileStorage fileStorage;
    static{
        users = new ArrayList<>();
        fileStorage = new FileStorage();
        loadUsers();
    }

    public static boolean addUser(String name){

        if(getId(name) == null){

            String id = "token-u$#"+System.nanoTime();

            User user = new User(id, name);

            users.add(user);
            fileStorage.addUser(user);
            return true;
        }

        return false;
    }

    public static String getId(String username){
        for(User user : users){
            if(user.getName().equals(username)){
                return user.getId();
            }
        }

        return null;
    }

    public static String getName(String id){
        for(User user : users){
            if(user.getId().equals(id)){
                return user.getName();
            }
        }
        return null;
    }

    public static void loadUsers(){
        try {
            users.addAll(fileStorage.loadUsers());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteUser(String username){

        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()){
            User us = iterator.next();
            if(us.getName().equals(username)){
                try {
                    if(fileStorage.deleteUser(username)){
                        iterator.remove();
                        return true;
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static boolean updateUser(String oldName, String newName){

        for(int i = 0; i < users.size(); i++){
            if(oldName.equals(users.get(i).getName())){
                try {
                    if(fileStorage.updateUser(oldName, newName)){
                        users.get(i).setName(newName);
                        return true;
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static List<String> getAllName(){
        List<String> names = new ArrayList<>();
        for(User user : users){
            names.add(user.getName());
        }

        return names;
    }
}
