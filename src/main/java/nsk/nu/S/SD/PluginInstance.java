package nsk.nu.S.SD;

public class PluginInstance {

    private static SimpleSpawnpointDeployment instance;
    public static SimpleSpawnpointDeployment getInstance() {
        return instance;
    }

    public static void setInstance(SimpleSpawnpointDeployment p) {
        instance = p;
    }

}
