package com.dx.netty.tool;



import io.netty.util.internal.StringUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 各种进制转换类
 */
public class ToolConvert {

    /* - - - - - - - -  base - - - - - - - - - - - - - - - - - - - */

    public static byte[] bytes2(byte[] bytes) {
        return bytes;
    }

    /**
     * byte数组中取int数值，本方法适用于(高位在后)的顺序。
     *
     * @param srcIn byte数组
     * @return int数值
     */
    public static int bytesToInt(byte[] srcIn) {
        byte[] src = new byte[srcIn.length];
        int n = srcIn.length - 1;
        for (int j = 0; j < srcIn.length; j++) {
            src[j] = srcIn[n];
            n--;
        }
        int iOutcome = 0;
        byte bLoop;
        for (int i = 0; i < src.length; i++) {
            bLoop = src[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }


    /**
     * bytes 转long
     */
    public static double bytesToLong(byte[] bytes) {
        return ((((long) bytes[0] & 0xff) << 24)
                | (((long) bytes[1] & 0xff) << 16)
                | (((long) bytes[2] & 0xff) << 8)
                | (((long) bytes[3] & 0xff)));
    }

    /**
     * byte数组中取int数值，本方法适用于(高位在后)的顺序。
     *
     * @param srcIn byte数组
     * @return 字符串
     */
    public static String bytesToGB2312(byte[] srcIn) {

        if (srcIn == null) {
            return null;
        }

        String res = null;
        try {
            res = new String(srcIn, "GB2312");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    public static String bytesToStr(byte[] srcIn) {
        String res = null;
        try {
            int pos = 0;
            for (int i = 0; i < srcIn.length; i++) {
                if (srcIn[i] != 0) {
                    break;
                }
                pos++;
            }
            res = new String(srcIn, "GB2312").substring(pos);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }


    private static String[] binaryArray = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};

    /**
     * @param bArray
     * @return 转换为二进制字符串
     */
    public static String bytesToBin(byte[] bArray) {
        String outStr = "";
        int pos = 0;
        for (byte b : bArray) {
            pos = (b & 0xF0) >> 4;
            outStr += binaryArray[pos];
            pos = b & 0x0F;
            outStr += binaryArray[pos];
        }
        return outStr;
    }

    /**
     * @param hexString
     * @return 将十六进制转换为字节数组
     */
    public static byte[] hexStrToBin(String hexString) {
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位

        for (int i = 0; i < len; i++) {//右移四位得到高位
            high = (byte) ((hexString.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexString.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);//高地位做或运算
        }
        return bytes;
    }

    /**
     * @param hexString
     * @return 将十六进制转换为字节数组
     */
    public static int hexStrToInt(String hexString) {
        return Integer.parseInt(hexString, 16);
    }


    /**
     * @param binStr
     * @return 将2进制转换为10进制
     */
    public static int binToInt(String binStr) {
        BigInteger bi = new BigInteger(binStr, 2);
        return bi.intValue();
    }


    public static String binToHex(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0) {
            return null;
        }
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }


    /**
     * 返回prop中的长度.
     *
     * @param str body len
     * @return 10个长度的二进制数据（例如：00000000001）
     */
    public static String intToBin(int str) {
        String binStr = Integer.toBinaryString(str);
        if (binStr.length() < 10) {
            int s = 10 - binStr.length();
            for (int i = 0; i < s; i++) {
                binStr = "0" + binStr;
            }
        }
        return binStr;
    }


    /**
     * 16进制数组字符串得到byte数组.
     */
    public static byte[] getByteString(String s) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < s.length() / 2; i++) {
            String b = s.substring(2 * i, 2 * (i + 1));
            bytes[i] = Byte.parseByte(b);
        }
        return bytes;
    }


    /**
     * 10转16进制字符串.格式化
     *
     * @param s 需要转换的数字
     * @return 1616进制字符串
     * @length 字节数
     */
    public static String intToHexStr(int s) {
        String hex = Integer.toHexString(s);
        if (hex.length() % 2 == 1) {
            hex = "0" + hex;
        }
        return hex;
    }

    public static String doubleToHexStr(double s) {
        String hex = Double.toHexString(s);
        if (hex.length() % 2 == 1) {
            hex = "0" + hex;
        }
        return hex;
    }


    /**
     * 10转16进制字符串.格式化
     *
     * @param s 需要转换的数字
     * @return 1616进制字符串
     * @length 字节数
     */
    public static String intToHexStr(int s, int length) {
        String hex = Integer.toHexString(s);
        if (hex.length() % 2 == 1) {
            hex = "0" + hex;
        }
        int k = length - hex.length() / 2;
        for (int i = 0; i < k; i++) {
            hex = "00" + hex;
        }
        return hex;
    }


    public static byte[] hexStrToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * bytes转换为GPS坐标
     */
    public static double bytesToGPS(byte[] bytes) {
        BigInteger bi = new BigInteger(bytes);
        return bi.doubleValue() / 1000000;
    }

    /**
     * bytes转换为GPS坐标
     */
    public static double bytesToDouble(byte[] bytes) {
        BigInteger bi = new BigInteger(bytes);
        return bi.doubleValue();
    }


    /**
     * IP 转 hex
     */
    public static String ipToHex(String ip) {
        String result = "";
        String[] ips = ip.trim().split("\\.");
        for (String s : ips) {
            String i = Integer.toHexString(Integer.parseInt(s));
            if (i.length() == 1) {
                i = "0" + i;
            }
            result = result + i;
        }
        return result;
    }


    /**
     * 字符串补齐，
     *
     * @hex 参数
     * @len 字节
     * @sort 顺序，0表示末尾补齐，1 表示前面补齐
     */
    public static String fullHex(String hex, int len, int sort) {
        int i = 2 * len - hex.length();
        String s = "";
        for (int j = 0; j < i; j++) {
            s = s + "0";
        }
        if (sort == 0) {
            return hex + s;
        } else {
            return s + hex;
        }
    }

    public static String fullHex(String hex, int len) {
        return fullHex(hex, len, 0);
    }


    public static String getCurrentTime() {
        SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
        String currentTime = time.format(new Date());
        String result = "";
        for (int i = 0; i < currentTime.length() / 2; i++) {
            String c = currentTime.substring(2 * i, 2 * i + 2);
            result = result + c;
        }
        return result;
    }


    public static String toMD5(String key) {
        MessageDigest code;
        StringBuffer sb = new StringBuffer();
        try {
            code = MessageDigest.getInstance("MD5");
            code.update((key).getBytes());
            byte[] bs = code.digest();
            for (int i = 0; i < bs.length; i++) {
                int v = bs[i] & 0xFF;
                if (v < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(v));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 数据复制.
     */
    public static byte[] getSource(byte[] src, int start, int len) {
        byte[] to = new byte[len];
        System.arraycopy(src, start, to, 0, len);
        return to;
    }

    /**
     * 数据连接.
     */
    public static byte[] connection(byte[] a, byte[] b) {
        byte[] r = new byte[a.length + b.length];
        for (int i = 0; i < (a.length); i++) {
            r[i] = a[i];
        }
        for (int j = a.length; j < (a.length + b.length); j++) {
            r[j] = b[j - a.length];
        }
        return r;
    }

    /**
     * 获取填充内容.
     */
    public static String getPad(int size) {
        switch (size) {
            case 1:
                return "01";
            case 2:
                return "0101";
            case 3:
                return "010101";
            case 4:
                return "01010101";
            case 5:
                return "0101010101";
            case 6:
                return "010101010101";
            case 7:
                return "01010101010101";
            default:
                return "0101010101010101";
        }
    }


    public static long getDate(String dataStr) {
        if (dataStr == null || dataStr.length() != 12) {
            return 0;
        }
        dataStr = "20" + dataStr;
        Date date = parseDate(dataStr, "yyyyMMddHHmmss");
        return date.getTime();
    }

    public static Date parseDate(String dateStr, String datePattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Could not parse date: " + dateStr, e);
        }
    }


    public static String convertMsgReq(String srcHex) {
        StringBuilder sb = new StringBuilder("7e");
        for (int i = 0; i < srcHex.length() / 2; i++) {
            String si = srcHex.substring(2 * i, 2 * (i + 1));
            si = si.replace("7d", "7d01");
            si = si.replace("7e", "7d02");
            sb.append(si);
        }
        return sb.toString() + "7e";
    }


    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(Long.valueOf(1560848427 + "000"))));
    }

    public static String BCDtoString(byte bcd) {
        StringBuffer sb = new StringBuffer();

        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        byte low = (byte) (bcd & 0x0f);

        sb.append(high);
        sb.append(low);

        return sb.toString();
    }

    /**
     * BCD字节数组===>String
     *
     * @param bcd
     * @return 十进制字符串
     */
    public static String BCDtoString(byte[] bcd) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bcd.length; i++) {
            sb.append(BCDtoString(bcd[i]));
        }

        return sb.toString();
    }

    public static String toBcdDateString(byte[] bs) {
        if (bs.length != 3 && bs.length != 4) {
            return "0000-00-00";
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        if (bs.length == 3) {
            sb.append("20");
        } else {
            sb.append(BCDtoString(bs[i++]));
        }
        sb.append(BCDtoString(bs[i++]));
        sb.append("-").append(BCDtoString(bs[i++]));
        sb.append("-").append(BCDtoString(bs[i++]));
        return sb.toString();
    }

    public static String convertMsgRsq(String srcHex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (srcHex.length() / 2 - 1); i++) {
            String si = srcHex.substring(2 * i, 2 * (i + 2));
            String s2 = srcHex.substring(2 * i, 2 * (i + 1));
            if (si.indexOf("7d02") != -1) {
                sb.append("7e");
                i++;
            } else if (si.indexOf("7d01") != -1) {
                sb.append("7d");
                i++;
            } else {
                if (i == srcHex.length() / 2 - 2) {
                    sb.append(si);
                } else {
                    sb.append(s2);
                }
            }
        }
        return sb.toString();
    }

    public static String convertMsgRsq(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        byte[] c1 = new byte[]{0x7d, 0x02};
        byte[] c2 = new byte[]{0x7d, 0x01};
        for (int i = 0; i < (bs.length - 1); i++) {
            byte[] b1 = new byte[]{bs[i], bs[i + 1]};
            byte[] b2 = new byte[]{bs[i]};
            if (Arrays.equals(b1, c1)) {
                sb.append("7e");
                i++;
            } else if (Arrays.equals(b1, c2)) {
                sb.append("7d");
                i++;
            } else {
                if (i == bs.length - 2) {
                    String a = Integer.toHexString(b1[0] & 0xFF);
                    sb.append(a.length() > 1 ? a : "0" + a);
                    String b = Integer.toHexString(b1[1] & 0xFF);
                    sb.append(b.length() > 1 ? b : "0" + b);
                } else {
                    String a = Integer.toHexString(b2[0] & 0xFF);
                    sb.append(a.length() > 1 ? a : "0" + a);
                }
            }
        }
        return sb.toString();
    }


    public static String hexStrToString(String parm) {
        String result = "";
        if (StringUtil.isNullOrEmpty(parm)) {
            return null;
        }
        for (int i = 0; i < parm.length() / 2; i++) {
            String s = parm.substring(i * 2, (i + 1) * 2);
            int p = Integer.valueOf(s, 16);
            if (p < 10) {
                result += "0";
            }
            result += p;
        }
        return result;
    }

    /* - - - - - - - -  encode - - - - - - - - - - - - - - - - - - - */

    /**
     * @param sort   补齐方式.
     * @param len    期望长度.
     * @param hexStr 需要转换的内容.
     */
    public static String hexStr2(Integer sort, Integer len, String hexStr) {
        if (len == -1) {
            len = hexStr.length() / 2;
        }
        return fullHex(hexStr, len, sort);
    }

    public static String strToHexStr(Integer sort, Integer len, String str) {
        String hexStr = bytesToHexStr(str.getBytes());
        return fullHex(hexStr, len, sort);
    }

    public static String strToGBKHexStr(Integer sort, Integer len, String str) {
        try {
            String hexStr = bytesToHexStr(str.getBytes("GBK"));
            return fullHex(hexStr, len, sort);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bytesToGBKStr(byte[] bytes) {
        try {
            return new String(bytes, "GBK").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String intToHexStr(Integer sort, Integer len, int str) {
        String hexStr = intToHexStr(str);
        return fullHex(hexStr, len, sort);
    }

    public static String intToHexStr2(Integer sort, Integer len, int str) {
        String hexStr = intToHexStr(str);
        String hexStr2 = fullHex(hexStr, len, sort);
        if (str < 0 && len == 2 && hexStr2.length() == 8) {
            hexStr2 = hexStr2.substring(4, 8);
        }
        return hexStr2;
    }

    public static String binToHexStr(Integer sort, Integer len, String bin) {
        String hexStr = binToHex(bin);
        return fullHex(hexStr, len, sort);
    }

    public static String gpsToHexStr(Integer sort, Integer len, double gps) {
        String hexStr = intToHexStr((int) (gps * 1000000));
        return fullHex(hexStr, len, sort);
    }

    public static String doubleToHexStr(Integer sort, Integer len, double dou) {
        String hexStr = intToHexStr((int) (dou * 10));
        return fullHex(hexStr, len, sort);
    }

    /**
     * @param hexString
     * @return 将十六进制转换为字节数组
     */
    public static int hexStringToInt(String hexString) {
        BigInteger bi = new BigInteger(hexString, 16);
        return bi.intValue();
    }

    /* - - - - - - - -  decode - - - - - - - - - - - - - - - - - - - */

    public static String bytesToHexStr(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /* - - - - - - - -  协议属性组的编码方法 - - - - - - - - - - - - - - - - - - - */


    /**
     * 数据加解密算法
     *
     * @param key
     */
    public static String encryptData(String data, String key) {
        return null;
    }


    /**
     * 数据加解密算法
     *
     * @param key
     */
    public static byte[] decryptData(byte[] data, String key) {
        return null;
    }


    /**
     * 异或校验.
     */
    public static String respXOR(String str) {
        byte[] bytes = ToolConvert.hexStrToBytes(str);
        byte target = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            target = (byte) (target ^ bytes[i]);
        }
        int v = target & 0xFF;
        String hex = Integer.toHexString(v);
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        return hex;
    }

    public static String strToHexString(String str) {
        try {
            return bytesToHexStr(str.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long hexStringToLong(String parm) {
        if (StringUtil.isNullOrEmpty(parm)) {
            return null;
        }
        return Long.parseLong(parm, 16);
    }
}
