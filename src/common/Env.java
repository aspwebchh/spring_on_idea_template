package common;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

public class Env{
    public static abstract class EnvBase{
        public abstract String basePath();

        public String withBasePath( String path ) {
            if( path != null && path.startsWith("/")) {
                return this.basePath() + path;
            } else {
                return this.basePath() + "/" + path;
            }
        }
    }

    public static class DevEnv extends EnvBase{
        public String basePath(){
            return "";
        }
    }

    public static class OnlineEnv extends EnvBase{
        public String basePath() {
            return "/mobile1";
        }
    }

    public static class TestEnv extends EnvBase{
        public String basePath() {
            return "";
        }
    }

    private enum EnvItem{
        Dev,
        Test,
        Online
    }

    private static EnvItem getEnv( String serverName ) {
        Map<String,EnvItem> data = new HashMap<>();
        data.put("192.168.110.227", EnvItem.Test);
        data.put("service.17m3.com", EnvItem.Online);
        data.put("120.26.160.106", EnvItem.Online);
        if( data.containsKey(serverName)) {
            return data.get(serverName);
        } else {
            return EnvItem.Dev;
        }
    }

    public static EnvBase getEnv(ServletRequest request) {
        String serverName = request.getServerName();
        EnvItem envItem = getEnv(serverName);
        switch (envItem) {
            case Online:
                return new OnlineEnv();
            case Test:
                return new TestEnv();
            default:
                return new DevEnv();
        }
    }
}

