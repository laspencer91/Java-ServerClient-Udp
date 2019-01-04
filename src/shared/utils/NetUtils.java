package shared.utils;

public final class NetUtils {
    public static String getConnectionKeyFromAddress(String ip, int port) {
        return ip + ":" + port;
    }
}
