/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.GPos;
import Common.KMessage;
import Common.Room;
import Common.Users;
import Cryptography.PublicCryptography;
import Database.DataFunc;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.PublicKey;
import Cryptography.SymmetricEncryption;
import java.util.Base64;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Nguyen Cao Ky
 */
public class ClientHandler extends Thread {
        public Room room = null;
    
        private Socket socket;
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;

        public Users user;
        
        Boolean execute = true;
        
        private SymmetricEncryption clientKey;
        
        ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            //gửi public key của server cho client
            SendPubKey();
            //nhận public key từ client cho server
            ReceiveClientKey();
            execute = true;
        }
        
        //hàm gửi public key của server cho client
        void SendPubKey() throws IOException
        {
            outputStream.reset();
            outputStream.writeObject(Main.pct.getPublicKey());
        }
        //nhận public key từ client gửi cho server
        void ReceiveClientKey() {
            try{
                String CliKey = inputStream.readUTF();
                CliKey = PublicCryptography.Decrypt(CliKey);
                byte[] iv = inputStream.readAllBytes();
                byte[] decodedKey = Base64.getDecoder().decode(CliKey);
                SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
                
                System.out.println(iv);
                clientKey = new SymmetricEncryption(originalKey,iv);
                //System.out.println(clientKey.getKey().toString());
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        void ReceiveMessage(KMessage msg) throws IOException {
            System.out.println(msg.getType());
            switch (msg.getType()) {
                case 0: {
                    Users temp = (Users)msg.getObject();
                    DataFunc df = new DataFunc();
                    user = df.checkLogin(temp.getUsername(), temp.getPassword());
                    if(user != null)
                    {
                        Boolean flag = true;
                        // Kiem tra coi da co ai dang nhap hay chua
                        for (ClientHandler cli : Main.lstClient) {
                            if (cli!=this && cli.user!=null && cli.user.getUsername().equalsIgnoreCase(user.getUsername()))
                            {
                                user = null;
                                break;
                            }
                        }
                        if (user!=null)
                            System.out.println("Server: Xin chao " + user.getUsername());
                    }
                    SendMessage(0, user);
                    break;
                }
                case 1: {
                    Users temp = (Users)msg.getObject();
                    DataFunc df = new DataFunc();
                    boolean succ;
                    succ = df.checkAva(df.getId(temp.getUsername()));
                    if (succ == true) {
                        SendMessage(1, temp.getUsername() + " is Available");
                        return;
                    }

                    succ = df.register(temp.getUsername(), temp.getPassword());
                    if (succ == true) {
                        SendMessage(1, "Register succesfull");
                    }
                    
                    break;
                }
                case 2: {
                    DataFunc df = new DataFunc();
                    SendMessage(2, df.getTop10User());
                    break;
                }
                case 10: //chuyen de chat chit
                {
                    System.out.println(msg.getObject().toString());
                    break;
                }
                //Room
                case 20: // Join room
                {
                    room = Main.lstRoom.get(Integer.parseInt(msg.getObject().toString()));
                    if (room.add(this)==false) //full
                    {
                        int[] arrRoom = new int[Main.lstRoom.size()];
                        for (int i=0; i<Main.lstRoom.size(); i++)
                        {
                            arrRoom[i] = Main.lstRoom.get(i).countAvailable();
                        }
                        SendMessage(22, arrRoom);
                    }
                    else
                        SendMessage(20, null);
                    
                    break;
                }
                case 21: //Get all room
                {
                    int[] arrRoom = new int[Main.lstRoom.size()];
                    for (int i=0; i<Main.lstRoom.size(); i++)
                    {
                        arrRoom[i] = Main.lstRoom.get(i).countAvailable();
                    }
                    SendMessage(21, arrRoom);
                    break;
                }
                case 28:
                {
                    if (room.client1!=null && room.client2!=null)
                    {
                        Users[] arrUser = new Users[2];
                        arrUser[0] = room.client1.user;
                        arrUser[1] = room.client2.user;
                        room.client1.SendMessage(34, arrUser);
                        room.client2.SendMessage(34, arrUser);
                        room.client2.SendMessage(36, null);
                    }
                    break;
                }
                case 30: // Lay ban co
                {
                    GPos gPos = (GPos)msg.getObject();
                    if (gPos!=null)
                        room.put(this, gPos);
                    
                    if (room != null) {
                        for (ClientHandler cli : room.lstClientView) {
                                cli.SendMessage(30, room.pieceses);
                        }
                    }
        
                    break;
                }
                case 39: //Exit room
                {
                    if (room!=null)
                    {
                        room.clientExit(this);
                    }
                    break;
                }
                case 40: //Chat
                {
                    if (room!=null)
                    {
                        // Gui cho 2 client
                        if (room.client1!=this)
                            room.client1.SendMessage(msg);
                        if (room.client2!=this)
                            room.client2.SendMessage(msg);

                        for (ClientHandler cli : room.lstClientView) {
                            if (cli!=this)
                            {
                                cli.SendMessage(msg);
                            }
                        }
                    }
                    break;
                }
                case 41: //View
                {
                    room = Main.lstRoom.get(Integer.parseInt(msg.getObject().toString()));
                    room.lstClientView.add(this);
                    SendMessage(20, null);
                    break;
                }
                //gửi và nhận publickey từ client
//                case 101:
//                {
//                    SendMessage(101, Main.serverKey.getPublicKey());
//                    this.cliPubKey = (PublicKey)msg.getObject();
//                    System.out.println(this.cliPubKey);
//                    break;
//                }
            }
        }

        public void SendMessage(int ty, Object obj) throws IOException {
            KMessage temp = new KMessage(ty, obj);
            SendMessage(temp);
        }
                
        public void SendMessage(KMessage msg) throws IOException {
            SealedObject sealedmsg = clientKey.Encrypt(msg);
            outputStream.reset();
            outputStream.writeObject(sealedmsg);
        }
        
        public Boolean closeClient() throws Throwable
        {
            
            
            if (room!=null) // Thong bao thoat room
            {
                try {
                    room.lstClientView.remove(this);
                } catch (Exception e) {
                    
                }
                
                room.clientExit(this);
            }
            
            Main.lstClient.remove(this);
            try {
                this.socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Client Exit");
            execute = false;
            
            
            return true;
        }
        
        @Override
        public void run() {
            
            while (execute) {
                
                try {
                    SealedObject o = (SealedObject) inputStream.readObject();
                    KMessage msg = null;
                    //for(ClientHandler cli : Main.lstClient){
                      //  System.out.println(cli.clientKey.getIV());
                        //if(cli.clientKey.canDecrypt(o))
                        msg = clientKey.Decrypt(o);
                    //}
                    if (msg != null) {
                        ReceiveMessage(msg);
                    }
                    //Guilai();
                } catch (IOException e) {
                    try {
                        closeClient();
                    } catch (Throwable ex) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }


    }