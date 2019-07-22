package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

        import java.io.FileWriter;
        import java.io.IOException;
        import java.time.LocalDateTime;
        import java.util.concurrent.locks.Lock;
        import java.util.concurrent.locks.ReentrantLock;

public class Logging extends Thread {
    private String msg;
    private boolean thrStop = false;
    private Lock lock;
    private FileWriter file;

    public Logging(){
        this.msg="*";
        thrStop = true;
        try {
            file = new FileWriter("log.txt", false);
            file.close();
        } catch (IOException e) {
            System.out.println("Can't open file");
        }
        start();
    }

    public void log(String message){
        this.msg = message;
        this.thrStop = false;
        run();
    }

    public void run() {
        if (!this.thrStop){
            try {
                file = new FileWriter("log.txt", true);
                file.append("[" + LocalDateTime.now() + "]:  " + msg+"\n");
                file.flush();
            } catch (IOException e){

            };
        }
        this.thrStop = true;
        stop();
    }
}
