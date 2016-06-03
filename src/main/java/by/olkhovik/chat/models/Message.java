package by.olkhovik.chat.models;

/**
 * Created by User on 24.05.2016.
 */
public class Message {

    private String id;
    private String message;
    private String author;
    private long timestamp;

    public Message(){

    }

    public Message(String id, String message, String author, long timestamp) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public String getAuthor() {
        return this.author;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[id: "+id+", message: "+message+", author: "+author+", timestamp: "+timestamp+"]";
    }
}
