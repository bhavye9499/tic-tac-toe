package io;

import utils.Util;

import java.io.*;
import java.net.*;

public class ClientHandler {

    private static int idCtr;

    private int id;
    private Socket socket;
    private ObjectOutputStream serverOut;
    private ObjectInputStream serverIn;

    static {
        ClientHandler.idCtr = 1;
    }

    public ClientHandler (Socket socket) {
        try {
            this.id = ClientHandler.idCtr++;
            this.socket = socket;
            System.out.println("A Client Connected...");
            this.serverOut = new ObjectOutputStream(this.socket.getOutputStream());
            this.serverIn = new ObjectInputStream(this.socket.getInputStream());
            Util.addShutdownHook(new Thread(this::closeClientHandler));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void closeClientHandler() {
        System.out.printf("Running client-handler[%d] cleanup...", this.id);
        try {
            this.serverOut.close();
            this.serverIn.close();
            this.socket.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Client-handler[%d] cleanup completed...", this.id);
    }

    public Response receiveResponseFromClient() {
        Response clientResponse;
        try {
            clientResponse = (Response) this.serverIn.readObject();
            return clientResponse;
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendResponseToClient(Response response) {
        try {
            this.serverOut.writeObject(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

}
