package hi.im.jenga.member.util.cipher;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Singleton;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@SuppressWarnings("restriction")
@Component
@Singleton
public class AES256Cipher {
    
    //private static volatile AES256Cipher INSTANCE;
    
    @Value("#{data['aes.secretkey']}")
     private String secretKey; //32bit
     private String IV                = ""; //16bit
    
    
    /*public void setValue(String key){
        secretKey = key;
    }
    
    public String getValue() {
        return secretKey;
    }*/
    
 /*   public static AES256Cipher getInstance(){
        if(INSTANCE==null){
            synchronized(AES256Cipher.class){
                if(INSTANCE==null)
                    INSTANCE=new AES256Cipher();
            }
        }
        return INSTANCE;
    } 
    */
     
     //디폴트생성자
    public AES256Cipher() {
    }
    
    @PostConstruct
    public void setIV() {
        IV = secretKey.substring(0,16);
    }
    
    //암호화
    public  String AES_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        byte[] keyData = secretKey.getBytes();
    
    SecretKey secureKey = new SecretKeySpec(keyData, "AES");
    
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
    
    byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
    String enStr = new String(Base64.encodeBase64(encrypted));
    
    return enStr;
    }
    
    //복호화
    public  String AES_Decode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
     byte[] keyData = secretKey.getBytes();
     SecretKey secureKey = new SecretKeySpec(keyData, "AES");
     Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
     c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
    
     byte[] byteStr = Base64.decodeBase64(str.getBytes());
    
     return new String(c.doFinal(byteStr),"UTF-8");
    }
    
}
