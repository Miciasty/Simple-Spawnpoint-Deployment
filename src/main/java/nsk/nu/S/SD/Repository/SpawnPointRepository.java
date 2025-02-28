package nsk.nu.S.SD.Repository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nsk.nu.S.SD.EnhancedLogger;
import nsk.nu.S.SD.Entity.SpawnPoint;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnPointRepository {

    private static final List<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
    private static final EnhancedLogger logger = EnhancedLogger.get();

    private static Component msg(String m) {
        return MiniMessage.miniMessage().deserialize(m);
    }

    private static int nextId = 0;

    public static void setSpawnPoint(SpawnPoint sp, Player p) {

        for (int i = 0; i < spawnPoints.size(); i++) {
            if (spawnPoints.get(i).verify(sp)) {
                int removedId = spawnPoints.get(i).getID();

                spawnPoints.remove(i);
                p.sendMessage(msg("SpawnPoint <green>" + removedId  + "</green> on coordinates: " + sp.toString() + " has been removed."));
                return;
            }
        }

        sp.setID(nextId++);
        spawnPoints.add(sp);
        p.sendMessage(msg("SpawnPoint <green>" + sp.getID() + "</green>  on coordinates: " + sp.toString() + " has been created."));

        logger.security(spawnPoints.toString());
    }

    public static List<SpawnPoint> getSpawnPoints() {
        return spawnPoints;
    }

    public static void reset() {
        spawnPoints.clear();
        nextId = 0;
    }

}
