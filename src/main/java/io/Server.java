package io;

import game_objects.Driver;
import game_objects.Game;
import globals.Global;
import utils.Util;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private ServerSocket serverSocket;
    private ClientHandler waitingClient;
    private Driver driver;

    public Server (int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.driver = new Driver();
            Util.addShutdownHook(new Thread(this::closeServer));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAndResetWaitingClient(ClientHandler clientHandler) {
        if (this.waitingClient.equals(clientHandler)) {
            this.resetWaitingClient();
        }
    }

    public void closeServer() {
        try {
            this.serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runServer() {
        System.out.println("Server started...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                if (Objects.isNull(this.waitingClient)) {
                    this.waitingClient = new ClientHandler(clientSocket);
                    Response response = new Response("Waiting for an opponent... \n", false);
                    this.waitingClient.sendResponseToClient(response);
                }
                else {
                    ClientHandler clientHandler1 = this.waitingClient;
                    ClientHandler clientHandler2 = new ClientHandler(clientSocket);
                    Game game = new Game(new ArrayList<>(Arrays.asList(clientHandler1, clientHandler2)));
                    this.driver.addGame(game);
                    this.resetWaitingClient();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetWaitingClient() {
        this.waitingClient = null;
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        Global.server = new Server(port);
        Global.server.runServer();
    }

}
