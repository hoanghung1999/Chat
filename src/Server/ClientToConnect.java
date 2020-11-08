/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import chatclient.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HoangHung
 */
public class ClientToConnect implements Runnable {

    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private Hashtable<String, Socket> listUserConnect = new Hashtable<>();

    public ClientToConnect(Socket client, Hashtable<String, Socket> listUser) {
        this.client = client;
        this.listUserConnect = listUser;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(client.getInputStream());
            while (true) {
                int event = in.readInt();
                System.out.println(event);
                        
                switch (event) {
                    case 1:
                    try {
                        
                        String messRecieve = in.readUTF();
                        Set<String> keySet = listUserConnect.keySet();
                        for (String key : keySet) {
                            out = new DataOutputStream(listUserConnect.get(key).getOutputStream());
                            System.out.println(key + " " + listUserConnect.get(key));
                            out.writeInt(event);
                            out.writeUTF(messRecieve);
                        }
                        
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }

        } catch (IOException ex) {
            //Nếu một ngoại lệ xảy ra, điều an toàn nhất là do máy khách đã ngắt kết nối nên chúng tôi xóa nó khỏi danh sách kết nối
            for (int i = 0; i < listUserConnect.size(); i++) {
                if (listUserConnect.get(i) == client) {
                    listUserConnect.remove(i);
                    break;
                }
            }
        }

    }

}
