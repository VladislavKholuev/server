package com.company;

import com.sun.corba.se.spi.activation.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;



public class Main {

    public static final int PORT = 8080;
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>(); // список всех нитей - экземпляров
    // сервера, слушающих каждый своего клиента
    public static Story story; // история переписки
    public static Vector<person> spisok = new Vector<>();
    public static Vector<person> last_spisok = new Vector<>();
    public static Vector<room> spisok_rooms = new Vector<>();
    public static Logging log ;
    

    public static void update_File(Vector<person> update_vec) throws IOException {
        FileWriter user_file_update = new FileWriter("users.txt");
        for(person per: update_vec){
            user_file_update.write(per.getLogin()+"\n"+per.getPassword()+"\n");
        }
        user_file_update.close();
    }
    public static void main(String[] args) throws IOException {
	// write your code here
        FileReader fr = new FileReader("users.txt");
        Scanner scan = new Scanner(fr);

//        log.start();
        while(scan.hasNextLine()){
            String old_user_log = scan.nextLine();
            String old_user_pas = scan.nextLine();
            person old_user = new person(old_user_log,old_user_pas);
            last_spisok.add(old_user);
            System.out.println(old_user.getLogin()+" "+old_user.getPassword());
        }
        log = new Logging();
//        log.start();
        ServerSocket server = new ServerSocket(PORT);
        story = new Story();
        System.out.println("Server Started");
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomthing(socket)); // добавить новое соединенние в список
//                    person per = new person();

                    for(int i=0;i<serverList.size();++i){
                        System.out.println("new user :"+socket.getInputStream());

                    }

                } catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}
