package com.company;

import java.net.Socket;

public class person {
    private String login;
    private String password;
    private String key;
    private Socket socket;

    person(){};

    person(String login_n, String password_n){

        this.login = login_n;
        this.password = password_n;
    }
    public void setSocket(Socket socket1){

        this.socket = socket1;
    }

    public void setLogin(String login1){

        this.login = login1;
    }

    public void setPassword(String password1){

        this.password = password1;
    }

    public Socket getSocket() {

        return socket;
    }

    public String getLogin(){

        return login;
    }

    public String getPassword(){

        return password;
    }
}
