package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException  {
        
        ServerSocket ss = new ServerSocket(3000);
        System.out.println("Server listening on port: 3000");


        //first player connection's
        Socket s1 = ss.accept();
        System.out.println("Player 1 connected from: " + s1.getInetAddress());
        BufferedReader in1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
        PrintWriter out1 = new PrintWriter(s1.getOutputStream(), true);
        out1.println("WAIT");

        //second player connection's
        Socket s2 = ss.accept();
        System.out.println("Player 2 connected from: " + s2.getInetAddress());
        BufferedReader in2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
        PrintWriter out2 = new PrintWriter(s2.getOutputStream(), true);
        out2.println("WAIT");
        out1.println("READY");
        out2.println("READY");

    }

    
}