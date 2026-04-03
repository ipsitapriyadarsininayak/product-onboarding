package utils;

import java.util.Base64;

public class Base64Utils {

    public static String decode(String value) {
        return new String(Base64.getDecoder().decode(value));
    }
}