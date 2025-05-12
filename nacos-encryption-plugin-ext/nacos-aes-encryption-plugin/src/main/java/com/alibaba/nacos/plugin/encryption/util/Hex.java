package com.alibaba.nacos.plugin.encryption.util;

import java.util.HexFormat;

/**
 * @author van
 * @date 2025/5/12 10:02
 * @description TODO
 */
public class Hex {

    public static byte[] decodeHex(String s) {
        return HexFormat.of().parseHex(s);
    }

    public static String encodeHexString(byte[] s) {
        return HexFormat.of().formatHex(s);
    }
}
