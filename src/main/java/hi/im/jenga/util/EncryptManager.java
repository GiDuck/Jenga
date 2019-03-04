package hi.im.jenga.util;

import hi.im.jenga.member.util.cipher.AES256Cipher;

public class EncryptManager {

    public static String aesEnocde(AES256Cipher aes256Cipher, String str){
        try{
            return aes256Cipher.AES_Encode(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static String aesDecode(AES256Cipher aes256Cipher, String str){
        try{
            return aes256Cipher.AES_Decode(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



}
