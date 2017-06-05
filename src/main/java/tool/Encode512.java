package tool;

public class Encode512 {
    public String encode(String s) {
        String result = "";
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.
                    getInstance("SHA-256");
            byte[] hash = digest.digest(s.getBytes("UTF-8"));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    result += '0';
                }
                result += hex;
            }
        } catch (Exception e) {
        }
        return result;
    }
}
