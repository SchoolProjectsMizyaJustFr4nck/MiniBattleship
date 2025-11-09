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

        while ((msg = in.readLine()) != null) {

            switch (msg) {
                case "WAIT":
                    System.out.println(msg);
                    System.out.println("You are the player 1, attend for the player 2");
                    break;

                case "READY":

                    System.out.println(msg);
                    System.out.println("Both players connected! Let's start!");

                    System.out.println("Insert ships");
                    for (int i = 0; i < 3; i++) {
                        System.out.println("Insert x(row) position for ship n: " + i + 1 + "\n");
                        out.println(input.nextLine());
                        System.out.println("Insert y(column) position for ship n: " + i + 1 + "\n");
                        out.println(input.nextLine());
                    }

                    break;

                case "PLACED":
                    System.out.println("Your ships are placed!");
                    break;

                case "INVALID":
                    System.out.println("Invalid move. Try again!");
                    System.out.println("Insert x(row) position: ");
                    out.println(input.nextLine());
                    System.out.println("Insert y(column) position");
                    out.println(input.nextLine());
                    break;

                default:
                    break;
            }
        }

    }
}

// false = player 2
// true = player 1

// 192.168.1.148