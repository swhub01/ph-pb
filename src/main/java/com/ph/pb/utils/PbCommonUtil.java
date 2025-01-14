package com.ph.pb.utils;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;

import java.nio.charset.StandardCharsets;

/**
 * PbCommonUtil 工具类
 */
public class PbCommonUtil {

    // 将16进制字符串转换为JSON字符串
    public static String convertJsonStr(String hexPbStr) {
        try {
            return parseFromString(hexPbStr);
        } catch (Exception e) {
            return "";
        }
    }

    // 将16进制字符串转换为字节数组
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    // 将字节数组转换为十六进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String stringToHex(String input) {
        return bytesToHex(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String hexToString(String hex) {
        byte[] bytes = hexToBytes(hex);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

    // 将字符串解析为JSON字符串
    public static String parseFromString(String form) throws Exception {
        // 创建一个空的文件描述符
        DescriptorProtos.FileDescriptorProto fileDescriptorProto = DescriptorProtos.FileDescriptorProto.newBuilder()
                .setName("empty_message.proto")
                .addMessageType(DescriptorProtos.DescriptorProto.newBuilder().setName("EmptyMessage").build())
                .build();
        // 生成一个 Descriptors.FileDescriptor 对象
        Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(fileDescriptorProto, new Descriptors.FileDescriptor[]{});
        // 获取 EmptyMessage 类型
        Descriptors.Descriptor emptyMessage = fileDescriptor.findMessageTypeByName("EmptyMessage");
        // 将字节数组解析为 DynamicMessage 对象
        DynamicMessage dynamicMessage = DynamicMessage.parseFrom(emptyMessage, hexStringToByteArray(form));
        // 将 DynamicMessage 对象转换为字符串
        return TextFormat.printer().printToString(dynamicMessage);
    }


    public static void main(String[] args) {
        String s = PbCommonUtil.convertJsonStr("08B5A1EB82988080031082E6A0F6938080031083E6A0F69380800310ECEDA0F69380800310D9DCCFBC9580800310A2FDF6CF9D808003");
        System.out.println(s);
    }
}