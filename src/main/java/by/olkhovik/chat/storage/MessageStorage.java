package by.olkhovik.chat.storage;

import by.olkhovik.chat.models.Message;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 24.05.2016.
 */
public class MessageStorage {

    private List<Message> messages;
    private FileStorage fileStorage;

    public MessageStorage(){
        messages = new ArrayList<>();
        fileStorage = new FileStorage();
        loadMessages();
    }

    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    public boolean add(Message message){

        for(Message item : messages){
            if(item.getId().equals(message.getId())){
                return false;
            }
        }

        messages.add(message);
        fileStorage.addMessage(message);

        return true;
    }

    public boolean delete(String messageId) throws FileNotFoundException {
        Iterator<Message> iterator = messages.iterator();

        while(iterator.hasNext()){
            Message message = iterator.next();
            if(message.getId().equals(messageId)){

                iterator.remove();

                fileStorage.deleteMessage(messageId);

                return true;
            }
        }

        return false;
    }

    public boolean update(Message message){
        for(int i = 0; i < messages.size(); i++){

            if(messages.get(i).getId().equals(message.getId())){
                try {
                    fileStorage.update(message);
                    messages.get(i).setMessage(message.getMessage());
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    public void loadMessages() {
        try {
            messages.addAll(fileStorage.loadMessages());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getSortChronologicalHistory(){
        List<Message> list = new ArrayList<>();
        list.addAll(messages);
        Collections.sort(messages, new SortComparator());
        return list;
    }

    public List<Message> getMessages(){
        return messages;
    }

    public Message searchAuthor(String author){
        for(Message item : messages){
            if(item.equals(author)){
                return item;
            }
        }

        return null;
    }

    public int size(){
        return messages.size();
    }

}
