package hainb21127.poly.appfastfood.model;

public class Messager {
    String id;
    String content;
    User id_sender;
    String id_receiver;
    String time;

    public Messager() {
    }

    public Messager(String id, String content, User id_sender, String id_receiver, String time) {
        this.id = id;
        this.content = content;
        this.id_sender = id_sender;
        this.id_receiver = id_receiver;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getId_sender() {
        return id_sender;
    }

    public void setId_sender(User id_sender) {
        this.id_sender = id_sender;
    }

    public String getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(String id_receiver) {
        this.id_receiver = id_receiver;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
