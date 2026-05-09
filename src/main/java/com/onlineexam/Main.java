package com.onlineexam;

import com.onlineexam.web.ExamServer;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        ExamServer server = new ExamServer(port);
        server.start();
    }
}
