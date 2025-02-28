package nsk.nu.S.SD.Events;

import nsk.nu.S.SD.PluginInstance;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PlayerMoveManager implements Listener {

    private static final Set<OfflinePlayer> frozenPlayers = new HashSet<>();

    public static void startTimer(int timeInSeconds) {
        new BukkitRunnable() {
            int remaining = timeInSeconds;

            @Override
            public void run() {
                if (remaining <= 0) {

                    for (OfflinePlayer offlinePlayer : new ArrayList<>(frozenPlayers)) {
                        if (offlinePlayer.isOnline()) {
                            unfreezePlayer(offlinePlayer.getPlayer());
                        }
                    }
                    cancel();
                } else {

                    for (OfflinePlayer offlinePlayer : frozenPlayers) {
                        if (offlinePlayer.isOnline()) {
                            Player player = offlinePlayer.getPlayer();
                            player.sendTitle(String.valueOf(remaining), "", 0, 20, 0);
                        }
                    }
                    remaining--;
                }
            }
        }.runTaskTimer(PluginInstance.getInstance(), 0L, 20L);
    }

    public static void freezePlayer(Player player) {
        frozenPlayers.add(player);
    }

    public static void unfreezePlayer(Player player) {
        frozenPlayers.remove(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if (frozenPlayers.contains(p)) {
            if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {

                event.setTo(event.getFrom());
            }
        }
    }

}
