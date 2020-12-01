/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryptography;

import java.io.Serializable;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import Common.KMessage;
import java.util.Base64;

/**
 *
 * @author ASUS
 */
public class SymmetricEncryption{
    private byte[] iv;
    private SecretKey key;
    public SymmetricEncryption(){
        try{
            // generate symmetric key
            KeyGenerator generator = KeyGenerator.getInstance( "AES" );
            key = generator.generateKey();
            
            // generate IV
            iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes( iv );
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SymmetricEncryption(SecretKey key, byte[] iv){
        this.key = key;
        this.iv = iv;
    }
    
    public SealedObject Encrypt(KMessage u){
        // create cipher
        SealedObject sealedKmessage = null;
        try{
            Cipher c = Cipher.getInstance( "AES" + "/CBC/PKCS5Padding" );
            c.init( Cipher.ENCRYPT_MODE, key, new IvParameterSpec( iv ) );
            sealedKmessage = new SealedObject(u, c);
        }catch(Exception e){
            e.printStackTrace();
        }
        return sealedKmessage;
    }
    
    public KMessage Decrypt(SealedObject sealedKmessage){
        KMessage u = null;
        try{
             Cipher c = Cipher.getInstance( "AES" + "/CBC/PKCS5Padding" );
             c.init( Cipher.DECRYPT_MODE, key, new IvParameterSpec( iv ) );
             u = (KMessage) sealedKmessage.getObject(c);
        }catch(Exception e){
            e.printStackTrace();
        }
        return u;
    }
    
    public boolean canDecrypt(SealedObject sealedmsg){
        try{
            Cipher c = Cipher.getInstance( "AES" + "/CBC/PKCS5Padding" );
             c.init( Cipher.DECRYPT_MODE, key, new IvParameterSpec( iv ) );
            KMessage u = (KMessage) sealedmsg.getObject(c);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public SecretKey getKey(){
        return key;
    }
    
    public byte[] getIV(){
        return iv;
    }
    
    @Override
    public String toString(){
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        String str = encodedKey;
        return str;
    }
}
