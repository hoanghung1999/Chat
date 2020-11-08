/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
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
public class ServerStart {
    private int port=1234;
    private Hashtable<String,Socket> listUserConnect= new Hashtable<>();
    public  void StartServer(){
        try { 
            ServerSocket serverSocket=new ServerSocket(port);
            while (true) {                
                System.out.println("listen......");
                Socket client=serverSocket.accept();
                //
                DataInputStream in= new DataInputStream(client.getInputStream());
               String nickname=in.readUTF();
                
                // thông báo cho các user đã có biết client mới login vào 
                 Set<String> keySet = listUserConnect.keySet();
                 System.out.println(keySet);
                        for (String key : keySet) {
                            DataOutputStream out = new DataOutputStream(listUserConnect.get(key).getOutputStream());
                            System.out.println(key + " " + listUserConnect.get(key));
                            out.writeInt(2);
                            out.writeUTF(nickname);
                        }
                // thông báo cho client mới vào biết các client có trong hệ thống
                 System.out.println(keySet);
                        for (String key : keySet) {
                            DataOutputStream out = new DataOutputStream(client.getOutputStream());
                            System.out.println(key + " " + listUserConnect.get(key));
                            out.writeInt(2);
                            out.writeUTF(key);
                        }
                listUserConnect.put(nickname, client);
                System.out.println(client+" connected");
                Runnable threadClient=new ClientToConnect(client, listUserConnect);
                Thread startThreadClient=new Thread(threadClient);
                startThreadClient.start();
            }
           
        } catch (IOException ex) {
            Logger.getLogger(ServerStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    
    }
    public static void main(String[] args) {
        ServerStart t=new ServerStart();
        t.StartServer();
    }
}
