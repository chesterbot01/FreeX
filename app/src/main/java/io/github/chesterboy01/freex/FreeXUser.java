package io.github.chesterboy01.freex;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 10/8/2016.
 * 专门用来存用户的数据结构。
 */

public class FreeXUser {
    //除了id都要加密
    private String UserName;
    private String Email;
    private String EncryptedPassword;
    //private int number;
    //下面的静态变量cnt并不是每个对象都存，只占用一个地方。专门用于计数给用户标号。
    //static int cnt = 0;



    FreeXUser(String userName, String email, String password){
        //number = cnt++;
        UserName = Encryption(userName);
        Email = Encryption(email);
        EncryptedPassword = Encryption(password);
    }

    /*public String getUserName(FreeXUser freexuser){
        return freexuser.UserName;
    }
    public int getUserID(FreeXUser freexuser){
       return freexuser.number;
    }
    public String getEmail(FreeXUser freexuser){
        return freexuser.Email;
    }*/
    //我传给服务器还是byte[]，服务器那边从数据库里抽出加密的密码，两边同时解密完一起比对。
    public static String Encryption(String toBeEncrypted){
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(toBeEncrypted.getBytes(), 0,toBeEncrypted.length());
            BigInteger i = new BigInteger(1,m.digest());
            return String.format("%1$032x", i);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
