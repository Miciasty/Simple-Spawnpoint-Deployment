package nsk.nu.S.SD.Repository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nsk.nu.S.SD.EnhancedLogger;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerRepository {

    private static final List<Player> players = new ArrayList<Player>();
    private static final HashMap<Player, Integer> designated = new HashMap<>();

    private static Component msg(String m) {
        return MiniMessage.miniMessage().deserialize(m);
    }
    private static final EnhancedLogger logger = EnhancedLogger.get();

    public static void addPlayer(Player sender, Player player) {
        if (players.contains(player)) {
            logger.warning(player.displayName() + " is already in the list.");
            return;
        }

        if (designated.containsKey(player)) {
            designated.remove(player);
        }

        players.add(player);
        sender.sendMessage(msg( "<yellow>" + player.getName() + "</yellow><green> has been added to list.</green>"));
    }

    public static void addPlayer(Player sender, Player player, int spot) {
        if (players.contains(player)) {
            players.remove(player);
        }

        if (designated.containsKey(player)) {
            designated.remove(player);
        }

        designated.put(player, spot);
        sender.sendMessage(msg( "<yellow>" + player.getName() + "</yellow><green> has been added to list with spot <yellow>" + spot + "</yellow>.</green>"));
    }

    public static void removePlayer(Player player) {
        if (!players.contains(player)) {
            logger.warning(player.displayName() + " is not on the list.");
        }
        players.remove(player);

    }

    public static void removePlayer(Player player, int spot) {
        if (!designated.containsKey(player)) {
            logger.warning(player.displayName() + " is not on the list.");
        }
        designated.remove(player);

    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static HashMap<Player, Integer> getDesignated() { return designated; }

    public static void reset() {
        players.clear();
        designated.clear();
    }

}
