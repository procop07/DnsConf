package com.novibe.common.util;

public class SecretMasker {

    public static String maskIp(String ip) {
        if (ip == null || ip.isBlank()) return "[NOT SET]";
        String[] parts = ip.split("\\.");
        if (parts.length == 4) return parts[0] + "." + parts[1] + ".**.**";
        return "[REDACTED]";
    }

    public static String maskToken(String token) {
        if (token == null || token.length() < 8) return "[REDACTED]";
        return token.substring(0, 4) + "****";
    }

}
