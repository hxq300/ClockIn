package com.lsy.wisdom.clockin.request;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lsy on 2020/3/16
 * todo :
 */
public class MD5Utils {

    public static String MD5(String sourceStr, int weishu) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            if (weishu == 32) {
                result = buf.toString();
            } else {
                result = buf.toString().substring(8, 24);
            }

//            result = buf.toString().substring(8, 24);

//            System.out.println("MD5(" + sourceStr + ",32) = " + result);
//            System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));

        } catch (NoSuchAlgorithmException e) {

        }
        return result;
    }
}

