package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Locale;

public class Main {

    private final static String mHexStr = "0123456789ABCDEF";

    private static byte convertIntToByte(int i) {
        if(i<= 127)
            return (byte)i;
        else
            return (byte)(i-256);
    }

    public static byte[] convertHexStrToByteArray(String hexs){
        byte[] a = new byte[hexs.length()/2];
        int index = 0;
        for(int i=0; i<hexs.length(); i+=2){
            String e = hexs.substring(i, i+2);
            a[index++] = convertIntToByte(Integer.valueOf(e, 16));
        }
        return a;
    }

    public static String hexStr2Str(String hexStr){
        hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int iTmp = 0x00;;

        for (int i = 0; i < bytes.length; i++){
            iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;
            iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (iTmp & 0xFF);
        }
        return new String(bytes);
    }

    public static String toStringHex(String s)
    {
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++)
        {
            try
            {
                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return s;
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",3306);

        String login = "mysql -uroot";

        byte[] bytes = login.getBytes();
//        for(int i = 0 ; i< bytes.length;i++) {
//            System.out.print(bytes[i]);
//        }
//        System.out.println(new String(bytes,"utf-8"));
        OutputStream out = socket.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(out);
////建立连接报文信息 来自wireshark(捕捉终端执行mysql -u root -p -h 127.0.0.1时对应的login request信息)
//        String hexs = "bb00000185a67f0000000001210000000000000000000000000000000000000000000000726f6f740014c2ee436b504f4f78089396223ad76f21fd5aee566d7973716c5f6e61746976655f70617373776f7264006a035f6f730964656269616e362e300c5f636c69656e745f6e616d65086c69626d7973716c045f7069640531363638330f5f636c69656e745f76657273696f6e06352e362e3137095f706c6174666f726d067838365f36340c70726f6772616d5f6e616d65056d7973716c";
//        byte[] bytes = convertHexStrToByteArray(hexs); //将上述的16进制信息转为byte数组 如"bb"--> -69
//        bos.write(bytes);
//        bos.flush();
//
////执行查询命令 select 'hello' 来自wireshark
//        hexs = "0f0000000373656c656374202768656c6c6f27";
        bytes = "use play_test".getBytes();
        bos.write(bytes);
        bos.flush();

        bytes = "INSERT INTO `play_test`.`user` (`id`, `username`, `email`, `password`, `gmt_create`, `gmt_modified`) VALUES ('2', 'pgs', 'godpan.me@gmail.com', '1234567', '2017-04-26 00:00:00', '2017-04-26 00:00:00')".getBytes();
        bos.write(bytes);
        bos.flush();
//
////读取查询SQL的返回
        InputStream in = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(in);
        byte[] buf = new byte[1024];
        int len = bis.read(buf);
        System.out.println(new String(buf));

    }
}
