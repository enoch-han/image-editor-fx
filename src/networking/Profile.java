package networking;

import java.util.ArrayList;

public class Profile extends Thread {
    Client client = new Client();
    private String fullName;
    private String userName;
    private String password;
    private boolean online = false;
    private ArrayList<String> friends;
    private ArrayList<Boolean> freinds_Status;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void setFreinds_Status(ArrayList<Boolean> freinds_Status) {
        this.freinds_Status = freinds_Status;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isOnline() {
        return online;
    }

    public void createAccount(String fullName,String userName,String password){
        setFullName(fullName);
        setUserName(userName);
        setPassword(password);
    }

    public void client_to_client(String text,String receiver){
        Message message = new Message(text,userName,receiver,1);
    }

    public void client_to_server(String text){
        Message message = new Message(text,userName);
    }

}
