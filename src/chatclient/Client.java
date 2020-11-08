/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

/**
 *
 * @author HoangHung
 */
public class Client implements Runnable {

    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;
    private String host = "localhost";
    private int port = 1234;
    private String message = "";
    private JTextArea contentContainer;
    private JComboBox listUserOnlineContainer;
    private ArrayList<String> userOnline= new ArrayList<>();

    public Client(String nickname, JTextArea content, JComboBox listUserOnline) {

        this.contentContainer = content;
        this.listUserOnlineContainer=listUserOnline;
        try {
            client = new Socket(host, port);

            out = new DataOutputStream(client.getOutputStream());
            out.writeUTF(nickname);
            in = new DataInputStream(client.getInputStream());
            userOnline.add("ALL member");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        int event=0;
        while (true) {
            try {
                event=0;
                System.out.println(event);
                event = in.readInt();
                System.out.println(event);
                switch (event) {
                    case 1:
                    try {
                        message += in.readUTF();
                        contentContainer.setText(message);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                    case 2:
                    try {
                        String newuser = in.readUTF();
                            userOnline.add(newuser);
                            listUserOnlineContainer.setModel(new DefaultComboBoxModel(userOnline.toArray()));
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                    case 3:
                        break;

                }
               
                        

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void sentMessToAll(String messagesent) {
        try {
            out.writeInt(1);
            out.writeUTF(messagesent);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
