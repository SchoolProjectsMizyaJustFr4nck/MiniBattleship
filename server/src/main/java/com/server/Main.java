package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static Boolean status = true;
    private static int inc = 0;
    static int[][] board1 = new int[5][5];
    static int[][] board2 = new int[5][5];
    static int inputX = 0;
    static int inputY = 0;

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(3000);
        System.out.println("Server listening on port: 3000");

        // first player connection's
        Socket s1 = ss.accept();
        System.out.println("Player 1 connected from: " + s1.getInetAddress());
        BufferedReader in1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
        PrintWriter out1 = new PrintWriter(s1.getOutputStream(), true);
        out1.println("WAIT");

        // second player connection's
        Socket s2 = ss.accept();
        System.out.println("Player 2 connected from: " + s2.getInetAddress());
        BufferedReader in2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
        PrintWriter out2 = new PrintWriter(s2.getOutputStream(), true);
        out2.println("WAIT");
        out1.println("READY");
        out2.println("READY");

        Socket setBattlePos = s1;
        BufferedReader setIn = in1;
        PrintWriter setOut = out1;
        int [][] currentBoard = board1;


        while (inc < 2) {
            for (int i = 1; i < 4; i++) {

                do {
                    setOut.println("insert x(line) of " + i + " ship(0-4): ");
                    inputX = Integer.parseInt(setIn.readLine());
                    setOut.println("insert y(column) of " + i + " ship(0-4): ");
                    inputY = Integer.parseInt(setIn.readLine());

                } while (checkIfExist(inputX) || checkIfExist(inputY));

                currentBoard[inputX][inputY] = 1;
            }

            if (status) {
                status = false;
                setBattlePos = s2;
                setIn = in2;
                setOut = out2;
                currentBoard = board2;
            } else {
                status = true;
                setBattlePos = s1;
                setIn = in1;
                setOut = out1;
                currentBoard = board1;
            }

            inc++;
        }

        
    }

    public static Boolean checkIfExist(int v) {

        return v >= 0 && v <= 4;

    }

}