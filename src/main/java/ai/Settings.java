package ai;

class Settings {

    private static String[] WHITELISTED_SERVERS = new String[]{
            "", ""
    };

    public static boolean isWhitelisted(String serverID) {

        for (String s : WHITELISTED_SERVERS) {
            if (s.contains(serverID)) {
                return true;
            }
        }

        return false;

    }

}
