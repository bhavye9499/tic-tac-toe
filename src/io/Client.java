package io;

import constants.Constant;
import utils.Util;

import java.net.*;
import java.io.*;

public class Client {

    private Socket socket;
    private ObjectOutputStream serverOut;
    private ObjectInputStream serverIn;
    private BufferedReader kbIn;

    public Client (String address, int port) {
        try {
            this.socket = new Socket(address, port);
            System.out.println(Constant.CLEAR_SCREEN + "Connection to Server successful...");
            this.serverOut = new ObjectOutputStream(this.socket.getOutputStream());
            this.serverIn = new ObjectInputStream(this.socket.getInputStream());
            this.kbIn = new BufferedReader(new InputStreamReader(System.in));
            Util.addShutdownHook(new Thread(this::closeClient));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void closeClient() {
        try {
            this.serverOut.close();
            this.serverIn.close();
            this.kbIn.close();
            this.socket.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void startInteraction() {
        Response response;
        String clientKbIn;
        try {
            while (true) {
                response = (Response) serverIn.readObject();
                if (response.getMessage().equals("over")) {
                    break;
                }
                System.out.print(response.getMessage());
                if (response.isInput()) {
                    clientKbIn = kbIn.readLine();
                    serverOut.writeObject(new Response(clientKbIn, false));
                }
            }
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        finally {
            this.closeClient();
        }
    }

    public static void main(String[] args) {
        String address = "127.0.0.1";  // args[0];
        int port = 5000;  // Integer.parseInt(args[1]);
        Client client = new Client(address, port);
        client.startInteraction();
    }

}
