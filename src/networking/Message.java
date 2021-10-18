package networking;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
    String text;
    Date time;
    String sender;
    String receiver;
    public  int method_invoke = 0;
    public final int server_client = 0;
    public final int client_client = 1;
    public final int exit = 2;
    public final int nothing = 0;
    public final int invoke_recive =1;

    int message_type;

    public Message(String text,String sender){
        this.text = text;
        time = new Date();
        this.sender = sender;
        this.receiver = receiver;
        message_type = 0;
    }

    public Message(String text,String sender,String receiver,int type){
        this.text = text;
        time = new Date();
        this.sender = sender;
        this.receiver = receiver;
        message_type = type;
    }

    public void setMethod_invoke(int method_invoke) {
        this.method_invoke = method_invoke;
    }

    public int getMethod_invoke() {
        return method_invoke;
    }

    public void setMessage_type(int type){
        this.message_type = type;
    }

    public int getMessage_type() {
        return message_type;
    }

    public String getText() {
        return text;
    }

    public Date getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}
