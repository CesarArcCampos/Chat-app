package sample;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Controller controller;
    private URL url;
    private VBox vBox;

    public Server() throws URISyntaxException, MalformedURLException {

    }

    public void connectionSocket(ServerSocket serverSocket, Controller controller) {
        try {
            this.controller = controller;
            url = controller.getUrl();
            vBox = controller.getVbox_messages();

            this.serverSocket = serverSocket;
            System.out.println("> Waiting for Client");
            this.socket = serverSocket.accept();
            System.out.println("> Client has entered");

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Failed to create Server.");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessageToClient (String messageToClient) {
        try{
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Failed to send message to Client");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessageFromClient(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String messageFromClient = bufferedReader.readLine();
                        if (messageFromClient.equals("Code:1234")) {
                            System.out.println("> Code:1234 - Client has disconnected.");
                            closeEverything(socket, bufferedReader, bufferedWriter);
                            break;
                        } else {

                            Controller.addLabel(messageFromClient, vBox);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("> Failed to receive message from Client");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }

                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                controller.setFlag(false);
                controller.initialize(url, null);
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket(ServerSocket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
