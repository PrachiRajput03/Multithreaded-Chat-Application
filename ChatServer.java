import java.io.*;
import java.net.*;
import java.util.*;
public class ChatServer {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static final int PORT = 12345;
    public static void main(String args[]){
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running...");

            while(true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void broadcastMessage(String message){
        for(ClientHandler client : clientHandlers) {
            client.sendMessage(message);
            }
    }


    static class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        String message;
        try{
            while((message = in.readLine()) != null){
                System.out.println("Received: "+message);

                ChatServer.broadcastMessage(message);
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            try{
                socket.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(String message){
        out.println(message);
        
    }
    }
}