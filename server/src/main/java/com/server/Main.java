package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static boolean status = true;
    private static int inc = 0;
    static int[][] board1 = new int[5][5];
    static int[][] board2 = new int[5][5];

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(3000);
        System.out.println("Server listening on port: 3000");

        // first player connection
        Socket s1 = ss.accept();
        System.out.println("Player 1 connected from: " + s1.getInetAddress());
        BufferedReader in1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
        PrintWriter out1 = new PrintWriter(s1.getOutputStream(), true);
        out1.println("WAIT");

        // second player connection
        Socket s2 = ss.accept();
        System.out.println("Player 2 connected from: " + s2.getInetAddress());
        BufferedReader in2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
        PrintWriter out2 = new PrintWriter(s2.getOutputStream(), true);

        out1.println("READY");
        out2.println("READY");

        Socket setBattlePos = s1;
        BufferedReader setIn = in1;
        PrintWriter setOut = out1;
        int[][] currentBoard = board1;

        while (inc < 2) {
            for (int i = 0; i < 3; i++) {
                int inputX = -1;
                int inputY = -1;

                do {
                    try {
                        String sx = setIn.readLine();
                        String sy = setIn.readLine();
                        inputX = Integer.parseInt(sx);
                        inputY = Integer.parseInt(sy);
                    } catch (NumberFormatException | NullPointerException e) {
                        inputX = -1;
                        inputY = -1;
                    }
                } while (!checkIfExist(inputX) || !checkIfExist(inputY) || currentBoard[inputX][inputY] == 1);

                currentBoard[inputX][inputY] = 1;
            }

            setOut.println("PLACED");

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

        while (true) {
            int[][] attackedBoard = (currentBoard == board1) ? board2 : board1;

            int newX = -1;
            int newY = -1;

            do {
                try {
                    String sx = setIn.readLine();
                    String sy = setIn.readLine();
                    newX = Integer.parseInt(sx);
                    newY = Integer.parseInt(sy);
                } catch (NumberFormatException | NullPointerException e) {
                    newX = -1;
                    newY = -1;
                }

                if (!checkIfExist(newX) || !checkIfExist(newY) || checkIfItAlreadyHitted(newX, newY, attackedBoard)) {
                    setOut.println("INVALID");
                }
            } while (!checkIfExist(newX) || !checkIfExist(newY) || checkIfItAlreadyHitted(newX, newY, attackedBoard));

            if (attackedBoard[newX][newY] == 1) {
                attackedBoard[newX][newY] = 2;
                setOut.println("HIT");
            } else {
                attackedBoard[newX][newY] = 3;
                setOut.println("MISS");
            }

            if (checkWin(attackedBoard)) {
                setOut.println("WIN");
                break;
            }

            // clint - setOut.println("ENEMY_BOARD");
            setOut.println(printBoard(attackedBoard));
            // clint - setOut.println("YOUR_BOARD");
            setOut.println(printBoard(currentBoard));

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
        }

        s1.close();
        s2.close();
        ss.close();
    }

    public static boolean checkIfExist(int v) {
        return v >= 0 && v <= 4;
    }

    public static boolean checkIfItAlreadyHitted(int x, int y, int[][] attackedBoard) {
        int val = attackedBoard[x][y];
        return val == 2 || val == 3;
    }

    public static String printBoard(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append(board[i][j]);
                if (j < 4) sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static boolean checkWin(int[][] attackedBoard) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (attackedBoard[i][j] == 1) {
                    return false; 
                }
            }
        }
        return true; 
    }
}