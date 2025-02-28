package nsk.nu.S.SD.Events;

import net.kyori.adventure.text.minimessage.MiniMessage;
import nsk.nu.S.SD.EnhancedLogger;
import nsk.nu.S.SD.Entity.SpawnPoint;
import nsk.nu.S.SD.Repository.SpawnPointRepository;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class onRightClickBlock implements Listener {

    private static final EnhancedLogger logger = EnhancedLogger.get();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        try {

            Material item = event.getItem().getType();

            if (!item.equals(Material.BLAZE_ROD)) {
                return;
            }

            if (!event.getItem().getItemMeta().displayName().equals(MiniMessage.miniMessage().deserialize("<gradient:#03f4fc:#03fc6f>SDA Selector</gradient>"))) {
                return;
            }

            int x = event.getClickedBlock().getLocation().getBlockX();
            int y = event.getClickedBlock().getLocation().getBlockY();
            int z = event.getClickedBlock().getLocation().getBlockZ();

            SpawnPoint spawnPoint = new SpawnPoint(x,y,z);

            SpawnPointRepository.setSpawnPoint(spawnPoint, player);


        } catch (NullPointerException ignored) {}
    }
}
