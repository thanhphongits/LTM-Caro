/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Common.KMessage;
import Common.Users;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import Cryptography.PublicCryptography;
import Cryptography.SymmetricEncryption;
import java.security.PublicKey;
import javax.crypto.SealedObject;

/**
 *
 * @author Nguyen Cao Ky
 */
class ListenServer extends Thread {
        Socket socket;
        OutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        InputStream inputStream;
        ObjectInputStream objectInputStream;

        public Users user;
        
        public inReceiveMessage receive;
        
        public PublicKey svPubKey;
        
        public SymmetricEncryption se;
        
        ListenServer(Socket socket) throws IOException {
            this.socket = socket;
            se = new SymmetricEncryption();
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            //nhận public key từ server qua client
            receivePubKey();
            //gửi public key từ client qua server
            SendKey();
        }
        //hàm gửi public key của client cho server
        void SendKey() throws IOException
        {
            String clientKey = se.toString();
            String msg = PublicCryptography.Encrypt(clientKey, svPubKey);
            System.out.println(se.getIV());
            objectOutputStream.reset();
            objectOutputStream.writeUTF(msg);
            objectOutputStream.reset();
            objectOutputStream.write(se.getIV());
        }
        
        void receivePubKey(){
            try{
                svPubKey = (PublicKey)objectInputStream.readObject();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            do {
                try {
                    SealedObject o = (SealedObject) objectInputStream.readObject();
                    KMessage msg = (KMessage)se.Decrypt(o);
                    if (msg != null && receive!=null) {
                        receive.ReceiveMessage(msg);
                    }

                } catch (IOException e) {
                    //System.out.println("There're some error");
                } catch (ClassNotFoundException ex) {
                    
                }
            }while (true);
        }

        public void SendMessage(int ty, Object obj) throws IOException {
            KMessage temp = new KMessage(ty, obj);
            SendMessage(temp);
        }
        
        public void SendMessage(KMessage msg) throws IOException {
            SealedObject sealedmsg = se.Encrypt(msg);
            objectOutputStream.reset();
            objectOutputStream.writeObject(sealedmsg);
        }
    }
    