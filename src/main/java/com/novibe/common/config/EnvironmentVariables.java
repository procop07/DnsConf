package com.novibe.common.config;

import com.novibe.common.util.Log;
import com.novibe.common.util.SecretMasker;

import java.net.Inet4Address;
import java.net.InetAddress;

import static java.util.Objects.isNull;

public class EnvironmentVariables {

    public static final String DNS = extractMandatoryVariable("DNS");

    public static final String CLIENT_ID = extractMandatoryVariable("CLIENT_ID");

    public static final String AUTH_SECRET = extractMandatoryVariable("AUTH_SECRET");

    public static final String BLOCK = System.getenv("BLOCK");

    public static final String REDIRECT = System.getenv("REDIRECT");

    public static final String EXCLUDE_REDIRECT = System.getenv("EXCLUDE_REDIRECT");

    public static final String BYPASS_PROXY_IP = extractOptionalIp("BYPASS_PROXY_IP");

    private static String extractMandatoryVariable(String key) {
        String env = System.getenv(key);
        if (isNull(env) || env.isBlank()) {
            Log.fail("Mandatory environment variable is not provided: " + key);
            System.exit(1);
        }
        return env;
    }

    private static String extractOptionalIp(String key) {
        String env = System.getenv(key);
        if (isNull(env) || env.isBlank()) {
            Log.fail("WARNING: " + key + " is not set. Custom routing will be skipped.");
            return null;
        }
        try {
            InetAddress addr = InetAddress.ofLiteral(env);
            if (addr instanceof Inet4Address) {
                Log.common("Loaded " + key + ": " + SecretMasker.maskIp(env));
                return env;
            }
            Log.fail("WARNING: " + key + " is not a valid IPv4 address: " + SecretMasker.maskIp(env) + ". Custom routing will be skipped.");
            return null;
        } catch (Exception e) {
            Log.fail("WARNING: " + key + " has invalid format. Custom routing will be skipped.");
            return null;
        }
    }

}

