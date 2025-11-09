package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws UnknownHostException, IOException {

        Scanner input = new Scanner(System.in);
        System.out.println("___!!!Welcome to the crazy Battleship!!!___");

        System.out.println("Insert Ip address: ");
        String ip = input.nextLine().trim();

        System.out.println("Insert port: ");
        Integer port = Integer.parseInt(input.nextLine());

        Socket s = new Socket(ip, port);

        System.out.println("Connection successfull");

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        String msg;
        boolean gamePhase = false;
        boolean myTurn = false;

        while ((msg = in.readLine()) != null) {
            System.out.println(msg);
            switch (msg) {
                case "WAIT":
                    System.out.println("Waiting for other player");
                    myTurn = false;
                    break;

                case "READY":
                    if (!gamePhase) {
                        System.out.println("It's your turn to place ships. Place your 3 ships.");
                        myTurn = true;
                        placeShips(in, out, input);
                    } else {
                        System.out.println("--- YOUR TURN TO ATTACK ---");
                        myTurn = true;
                        playTurn(in, out, input);
                    }
                    break;

                case "PLACED":
                    System.out.println("Your ships are placed");
                    gamePhase = true;
                    myTurn = false;
                    break;

                case "INVALID":
                    System.out.println(" Invalid coordinates, try again");
                    if (myTurn) {
                        if (gamePhase) {
                            playTurn(in, out, input);
                        } else {
                            placeShips(in, out, input);
                        }
                    }
                    break;
                case "HIT":
                    System.out.println("*** HIT! ***");
                    myTurn = false;
                    break;

                case "MISS":
                    System.out.println("*** MISS! ***");
                    myTurn = false;
                    break;

                case "WIN":
                    System.out.println("*** YOU WIN! ***");
                    displayBoard(in);
                    return;

                case "LOSE":
                    System.out.println("*** YOU LOSE! ***");
                    displayBoard(in);
                    return;
                default:
                    if (msg.startsWith("ENEMY_BOARD:")) {
                        String boardStr = msg.substring("ENEMY_BOARD:".length());
                        displayReceivedBoard(boardStr, "Enemy Board");
                    } else if (msg.startsWith("YOUR_BOARD:")) {
                        String boardStr = msg.substring("YOUR_BOARD:".length());
                        displayReceivedBoard(boardStr, "Your Board");
                    }

                    break;
            }
        }

    }

    private static int controlPosition(Scanner input, String pos) {
        int val = -1;
        boolean validInput = false;

        do {
            System.out.print("Insert " + pos + " (0-4): ");
            if (input.hasNextInt()) {
                val = input.nextInt();
                if (val >= 0 && val <= 4) {
                    validInput = true;
                } else {
                    System.out.println(pos + " need to be a number between 0 and 4");
                    input.nextLine();
                }
            } else {
                System.out.println("Insert a valid number (0-4)");
                input.next();
            }
        } while (!validInput);

        return val;
    }

    private static void placeShips(BufferedReader in, PrintWriter out, Scanner input) {
        int shipsPlaced = 0;

        while (shipsPlaced < 3) {

            System.out.println("Placing ship " + (shipsPlaced + 1) + ":");

            int x = controlPosition(input, "x");
            int y = controlPosition(input, "y");

            out.println(x);
            out.println(y);
            System.out.println("Sent coordinates to server: " + x + ", " + y);

            try {
                String res = in.readLine();
                if (res != null && res.equals("INVALID")) {
                    System.out.println("Coordinates already occupied, please try again");
                } else {
                    shipsPlaced++;
                    System.out.println("Ship " + shipsPlaced + " positioned correctly");
                }
            } catch (IOException e) {
                System.out.println("Error reading server response: " + e.getMessage());
                break;
            }
        }

        System.out.println("All " + shipsPlaced + " ships placed successfully");

    }

    private static void playTurn(BufferedReader in, PrintWriter out, Scanner input) {
        System.out.println("Enter attack coordinates:");

        int x = controlPosition(input, "x");
        int y = controlPosition(input, "y");

        out.println(x);
        out.println(y);
        System.out.println("Attacking coordinates: " + x + ", " + y);

        try {
            String res;
            boolean getHitMiss = false;
            boolean getEnemyBoard = false;
            boolean getYourBoard = false;

            while ((res = in.readLine()) != null) {
                System.out.println(res);
                if (res.equals("HIT") || res.equals("MISS")) {
                    getHitMiss = true;
                } else if (res.startsWith("ENEMY_BOARD:")) {
                    String boardStr = res.substring("ENEMY_BOARD:".length());
                    displayReceivedBoard(boardStr, "Enemy Board");
                    getEnemyBoard = true;
                } else if (res.startsWith("YOUR_BOARD:")) {
                    String boardStr = res.substring("YOUR_BOARD:".length());
                    displayReceivedBoard(boardStr, "Your Board");
                    getYourBoard = true;
                } else if (res.equals("WAIT")) {
                    System.out.println("Waiting for other player");
                    break;
                }

                if (getHitMiss && getEnemyBoard && getYourBoard) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayBoard(BufferedReader in) {
        try {
            String board1 = in.readLine();
            String board2 = in.readLine();

            if (board1 != null) {
                if (board1.startsWith("YOUR_BOARD:")) {
                    displayReceivedBoard(board1.substring("YOUR_BOARD:".length()), "Your Final Board");
                } else {
                    displayReceivedBoard(board1, "Your Final Board");
                }
            }
            if (board2 != null) {
                if (board2.startsWith("ENEMY_BOARD:")) {
                    displayReceivedBoard(board2.substring("ENEMY_BOARD:".length()), "Enemy Final Board");
                } else {
                    displayReceivedBoard(board2, "Enemy Final Board");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading boards: " + e.getMessage());
        }
    }

    private static void displayReceivedBoard(String boardStr, String title) {
        System.out.println("\n" + title + ":");
        String[] rows = boardStr.split(";");
        for (String row : rows) {
            System.out.println(row);
        }
        System.out.println();
    }

}
// false = player 2
// true = player 1

// 192.168.1.148

// 192.168.56.1