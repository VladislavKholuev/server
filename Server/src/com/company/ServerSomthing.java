package com.company;


import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

class ServerSomthing extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом,
    // кроме него - клиент и сервер никак не связаны
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток завписи в сокет
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;
//    private person user_in_this_thread;
    /**
     * для общения с клиентом необходим сокет (адресные данные)
     * @param socket
     * @throws IOException
     */

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Main.story.printStory(out); // поток вывода передаётся для передачи истории последних 10
        // сооюбщений новому поключению
        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word;
        System.out.println("1");
        try {
            person user_in_this_thread = new person();
            time = new Date(); // текущая дата
            dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
            dtime = dt1.format(time); // время
            // первое сообщение отправленное сюда - это никнейм
            boolean avtoriz = false;
            String Nickname ="";
//            String per_password = "";

            System.out.println("2");

            while(!avtoriz)
            {
                System.out.println("3");

                String log = in.readLine();
                try {
                    out.write(log + "\n");
                    System.out.println(log);
                    out.flush(); // flush() нужен для выталкивания оставшихся данных
                    // если такие есть, и очистки потока для дьнейших нужд
                } catch (IOException ignored) { }

                boolean no_find_spisok = true;
                boolean proverka_password =false;

                for(person per : Main.last_spisok)
                {
                    System.out.println("4");
                    System.out.println(per.getLogin()+" "+log);
                    if(per.getLogin().equals(log))
                    {
                        System.out.println("5");

                        while (!proverka_password)
                        {
                            System.out.println("6");

                            send("enter password or enter new_login: ");
                            String pas = in.readLine();
                            //пароль
                            try
                            {
                                out.write(pas + "\n");
                                System.out.println(pas);
                                out.flush(); // flush() нужен для выталкивания оставшихся данных
                                // если такие есть, и очистки потока для дьнейших нужд
                            } catch (IOException ignored) { }

                            if(per.getPassword().equals(pas))
                            {
                                System.out.println("7");

                                person user = new person(log, pas);
                                user.setSocket(this.socket);
                                Main.spisok.add(user);
                                Nickname = log;
                                System.out.println("76");
                                user_in_this_thread.setLogin(log);
                                user_in_this_thread.setPassword(pas);
                                System.out.println("77");

                                no_find_spisok = false;
                                avtoriz = true;
                                proverka_password = true;
                            }
                            if (pas.equals("new_login")){
                                proverka_password = true;
                                no_find_spisok =false;
                                System.out.println("8");

                            }
                        }
                    }
                }
                System.out.println("9");

                if(no_find_spisok)
                {
                    System.out.println("10");

                    String pas = in.readLine();
                    //пароль
                    try {
                        out.write(pas + "\n");
                        System.out.println(pas);
                        out.flush(); // flush() нужен для выталкивания оставшихся данных
                        // если такие есть, и очистки потока для дьнейших нужд
                    } catch (IOException ignored) { }

                    person user = new person(log, pas);
                    user_in_this_thread.setLogin(log);
                    user_in_this_thread.setPassword(pas);
                    user.setSocket(this.socket);
                    Main.spisok.add(user);
                    Main.last_spisok.add(user);
                    Nickname = log;
                    avtoriz =true;
//
//                for(person per : Main.spisok){
////                    if(word.contains(per.getLogin()+",")){
////                }
//                }
                }
            }

            Main.log.log("вошел: " +user_in_this_thread.getLogin());
            Main.log.log(user_in_this_thread.getPassword());
            Main.update_File(Main.last_spisok);
//            person user = new person(log,pas);
//            user.setSocket(this.socket);
            user_in_this_thread.setSocket(socket);
//            Main.spisok.add(user);

            try {
                while (true)
                {
                    boolean where_say =true;
//                    boolean not_private_mess = true;
                    word = in.readLine();
                    Main.log.log(user_in_this_thread.getLogin()+ "ввел" + word);
                    if(word.equals("stop")) {
                        System.out.println("(" + dtime + ") " + Nickname + ": left from chat");
                        for (ServerSomthing vr : Main.serverList)
                        {
                            vr.send("(" + dtime + ") " + Nickname + ": left from chat");
                        }

                        for(room ro : Main.spisok_rooms)
                        {
//                            if(ro.getRoom_name().equals(room_name))
//                            {
//                                send("ex");
                                ro.left_from_room(user_in_this_thread);
//                                send("you exit from room "+room_name);
//                            }
                        }

                        this.downService(); // харакири
                        break; // если пришла пустая строка - выходим из цикла прослушки
                    }
                    if(word.equals("create room"))
                    {
                        where_say=false;
                        boolean prov = true;
                        send("name room: ");
                        String room_name =in.readLine();

                        Main.log.log(user_in_this_thread.getLogin()+ "ввел" + room_name);
                        for(room ro : Main.spisok_rooms)
                        {
                            if(ro.getRoom_name().equals(room_name)){
                                prov = false;
                            }
                        }
                        if(prov)
                        {
                            send("you created: " + room_name + " room");
                            room new_room = new room(room_name);
                            Main.spisok_rooms.add(new_room);
                        }
                        else
                            {
                            send("this room was created in last");
                            }
                    }
                    if(word.equals("go to room"))
                    {
                        where_say=false;
                        send("enter name room");
                        String room_name = in.readLine();
                        Main.log.log(user_in_this_thread.getLogin()+ "ввел" + room_name);
                        for(room ro : Main.spisok_rooms)
                        {
                          if(ro.getRoom_name().equals(room_name))
                          {
                              boolean prov = true;
                              Vector<person> prov_per = ro.get_Users_spisok_in_room();
//                              for(person per : ro.get_Users_spisok_in_room()){}
                              for(person pov: prov_per)
                              {
                                  if(pov.getLogin().equals(user_in_this_thread.getLogin()))
                                  {
                                      prov = false;
                                  }
                              }
                              if(prov)
                              {
                                  ro.SetUser(user_in_this_thread);
                                  send("you entered in room "+room_name);
                              }
                              else
                                  {
                                    send("you was inside in lat");
                                  }
                          }
                        }
                    }
                    if(word.equals("exit from room"))
                    {
                        where_say=false;
                        send("enter name room");
                        String room_name = in.readLine();
                        for(room ro : Main.spisok_rooms)
                        {
                            if(ro.getRoom_name().equals(room_name))
                            {
                                send("ex");
                                ro.left_from_room(user_in_this_thread);
                                send("you exit from room "+room_name);
                            }
                        }
                    }
                    if(word.equals("get rooms"))
                    {
                        where_say=false;
                        for(room ro: Main.spisok_rooms)
                        {
                            Vector<person> vec_room_users = ro.get_Users_spisok_in_room();
                            send(ro.getRoom_name()+"\n [");
                            for(person vec : vec_room_users)
                            {
                                send(vec.getLogin());
                            }
                            send("]");
                        }
                    }
                    if(word.equals("room"))
                    {
                        where_say=false;
                        System.out.println("a");
                        send("enter name room");
                        String room_name = in.readLine();
                        Main.log.log(user_in_this_thread.getLogin()+ "ввел" + room_name);
                        String in_room_say = "";
                        boolean user_in_room_prov = false;
                        for(room ro : Main.spisok_rooms) {
                            System.out.println("b");
                            if (ro.getRoom_name().equals(room_name)) {
                                System.out.println("c");
                                Vector<person> vec_room_users = ro.get_Users_spisok_in_room();
                                boolean user_in_room = false;
                                for(person vec : vec_room_users)
                                {
                                    System.out.println("d");
                                    if(vec.getLogin().equals(user_in_this_thread.getLogin()))
                                    {
                                        System.out.println("e");
                                        send("you can say in room " + room_name);
                                        in_room_say = in.readLine();
                                        Main.log.log(user_in_this_thread.getLogin()+ "ввел" + in_room_say);
                                        user_in_room = true;
                                    }
                                }
                                System.out.println("f");
                                if(user_in_room)
                                {
                                    System.out.println("g");
                                    for (ServerSomthing vr : Main.serverList)
                                    {
                                        System.out.println("k");
                                        System.out.println(in_room_say);
                                        for(person vv : vec_room_users)
                                        {
                                            System.out.println("l");
                                            System.out.println(vr.socket+"  " + vv.getSocket()+"  " + vv.getLogin());
                                            if(vr.socket == vv.getSocket())
                                            {
                                                System.out.println("m");
                                                vr.send(room_name +": "+"(" + dtime + ") " + Nickname + ": " + in_room_say);
                                            }
                                        }
                                    }
                                }else {send("you not in room");}
                            }
                        }
                    }
                    if(where_say) {
                        for (person per : Main.spisok) {
                            System.out.println(word + " find in spisok");
                            if (word.contains(per.getLogin() + ",")) {
                                System.out.println(word + " find in online");
                                for (ServerSomthing vr : Main.serverList) {
//                                vr.send(word); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                                    if (vr.socket == per.getSocket()) {
                                        vr.send("(" + dtime + ") " + Nickname + ":" + "private msg: " + word);
                                        System.out.println(word + " found and sent");
                                        where_say = false;
                                    }
                                }
                            }
                        }
                    }

                    if(where_say)
                    {
                        System.out.println("Echoing: " + word);
                        Main.story.addStoryEl(word);
                        for (ServerSomthing vr : Main.serverList)
                        {
                            vr.send("(" + dtime + ") " + Nickname + ": " +word); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                        }
                    }

                }
            } catch (NullPointerException ignored) {}


        } catch (IOException e) {
            this.downService();
        }
    }

    /**
     * отсылка одного сообщения клиенту по указанному потоку
     * @param msg
     */
    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }

    /**
     * закрытие сервера
     * прерывание себя как нити и удаление из списка нитей
     */
    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : Main.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Main.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}