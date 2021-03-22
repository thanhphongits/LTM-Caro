/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryptography;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 *
 * @author Phong
 */
public class PublicCryptography {
    public static String a = "1";
    public PublicKey publicKey;
    private static PrivateKey privateKey;
    public PublicCryptography(){
        try{
            //khởi tạo phương thức mã hóa
            SecureRandom sr = new SecureRandom();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024, sr);
            
            //khởi tạo cặp khóa 
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
            
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
    
    public static String Encrypt(String msg,PublicKey publicKey){
        try{
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, publicKey);
            byte encryptOut[] = c.doFinal(msg.getBytes());
            msg = Base64.getEncoder().encodeToString(encryptOut);
        }catch(Exception e){
            e.printStackTrace();
        }
        return msg;
    }
    
    public static String Decrypt(String msg){
        try{
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(msg));
            msg = new String(decryptOut);
        }catch(Exception e){
            e.printStackTrace();
        }
        return msg;
    }
    
    public PublicKey getPublicKey(){
        return publicKey;
    }
    
    public PrivateKey getPrivateKey(){
        System.out.println(privateKey);
        return privateKey;
    }
}
