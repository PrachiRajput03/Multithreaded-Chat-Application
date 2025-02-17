// Importing all the necessary libraries required for the project
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Defining class
public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;


    // A constructor that conects to the server and generates GUI.
    public ChatClient(){
        buildGUI();
        connectToServer();
    }


    // Building GUI for client.
    private void buildGUI(){
        frame = new JFrame("Chat Applicaion");
        textArea = new JTextArea();
        textArea.setEditable(false);
        textField = new JTextField();

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(textField, 
        BorderLayout.SOUTH);

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Adding Event listener to send message
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                sendMessage(textField.getText());
                textField.setText("");
            }
        });
    }


    //Following code connects the client to the chat server.
    private void connectToServer(){
        try{
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Starting thread 
            new Thread(new IncomingReader()).start();

        } catch(IOException e){
            e.printStackTrace();
        }
    }
    private void sendMessage(String message){
        out.println(message);
    }
    private class IncomingReader implements Runnable{
        public void run(){
            String message;
            try{
                while((message = in.readLine()) != null){
                    textArea.append(message + "\n");
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        new ChatClient();
    }
}
