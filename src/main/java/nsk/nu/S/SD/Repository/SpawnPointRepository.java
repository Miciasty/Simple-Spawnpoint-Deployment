package nsk.nu.S.SD.Repository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import nsk.nu.S.SD.EnhancedLogger;
import nsk.nu.S.SD.Entity.SpawnPoint;
import nsk.nu.S.System.Tags;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnPointRepository {

    private static final List<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
    private static final EnhancedLogger logger = EnhancedLogger.get();

    private static Component msg(String m) {
        return MiniMessage.miniMessage().deserialize(m);
    }
    private static Component msg(String m, TagResolver tr) {
        return MiniMessage.miniMessage().deserialize(m, tr);
    }

    private static int nextId = 0;

    public static void setSpawnPoint(SpawnPoint sp, Player p) {

        TagResolver resolver = TagResolver.resolver(
                Placeholder.parsed("x", "<red>" + sp.getX() + "</red>"),
                Placeholder.parsed("y", "<red>" + (sp.getY()+1) + "</red>"),
                Placeholder.parsed("z", "<red>" + sp.getZ() + "</red>"),
                Tags.getStandard(),
                Tags.getGradient()
        );

        for (int i = 0; i < spawnPoints.size(); i++) {
            if (spawnPoints.get(i).verify(sp)) {
                int removedId = spawnPoints.get(i).getID();
                spawnPoints.remove(i);

                String rawMessage =
                        "<secondary>[SSD]<-secondary> " +
                        "<gradient:<gprimary>:<gsecondary>>" +
                        "New spawn point with ID: " +
                        "<hover:show_text:'X=<x>, Y=<y>, Z=<z>'><ascent>" + removedId + "<-ascent></hover> " +
                        "has been removed." +
                        "</gradient>";

                p.sendMessage(msg(rawMessage, resolver));
                return;
            }
        }

        sp.setID(nextId++);
        spawnPoints.add(sp);

        String rawMessage =
                "<secondary>[SSD]<-secondary> " +
                "<gradient:<gprimary>:<gsecondary>>" +
                "New spawn point with ID: " +
                "<hover:show_text:'X=<x>, Y=<y>, Z=<z>'><ascent>" + sp.getID() + "<-ascent></hover> " +
                "has been set." +
                "</gradient>";

        p.sendMessage(msg(rawMessage, resolver));

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
