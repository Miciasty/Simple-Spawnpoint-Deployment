package nsk.nu.S.SD.Commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import nsk.nu.S.SD.EnhancedLogger;
import nsk.nu.S.SD.Entity.SpawnPoint;
import nsk.nu.S.SD.Events.PlayerMoveManager;
import nsk.nu.S.SD.PluginInstance;
import nsk.nu.S.SD.Repository.PlayerRepository;
import nsk.nu.S.SD.Repository.SpawnPointRepository;
import nsk.nu.S.SD.SimpleSpawnpointDeployment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SDACommand implements CommandExecutor {

    private final SimpleSpawnpointDeployment plugin = PluginInstance.getInstance();

    private static final EnhancedLogger logger = EnhancedLogger.get();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("sda")) {

            if (sender instanceof Player player) {

                if (!player.isOp()) {
                    sender.sendMessage("You don't have permission to use this command!");
                    return true;
                }
            }

            if (args.length < 1) {
                sender.sendMessage("Usage: /sda <argument> <player>");
                return true;
            }

            switch (args[0].toLowerCase()) {

                case "add":

                    if (args.length < 2) {
                        sender.sendMessage("Usage: /sda <argument> <player>");
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("*")) {

                        for (Player player : plugin.getServer().getOnlinePlayers()) {
                            if (!player.isOp()) {
                                assert sender instanceof Player;
                                PlayerRepository.addPlayer((Player) sender,player);
                            }
                        }
                        return true;
                    }

                    try {
                        Player p = plugin.getServer().getPlayer(args[1]);
                        if (p == null) {
                            sender.sendMessage("Player " + args[1] + " not found.");
                            return true;
                        }

                        if (args.length >= 3) {
                            int d = Integer.parseInt(args[2]);
                            assert sender instanceof Player;
                            PlayerRepository.addPlayer((Player) sender, p, d);
                        } else {
                            assert sender instanceof Player;
                            PlayerRepository.addPlayer((Player) sender, p);
                        }

                        return true;

                    } catch (Exception e) {
                        logger.warning(e.getMessage());
                    }
                    break;

                case "get":

                    if (sender instanceof Player player) {

                        ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
                        ItemMeta meta = item.getItemMeta();

                        meta.displayName(MiniMessage.miniMessage().deserialize("<gradient:#03f4fc:#03fc6f>SDA Selector</gradient>"));

                        item.setItemMeta(meta);

                        player.getInventory().addItem(item);

                        return true;
                    }
                    break;

                case "start":

                    List<Player> players = PlayerRepository.getPlayers();
                    HashMap<Player, Integer> designated = PlayerRepository.getDesignated();
                    List<SpawnPoint> spawnPoints = SpawnPointRepository.getSpawnPoints();

                    int f = 0;

                    if (players.isEmpty()) {
                        sender.sendMessage("There are no players available in list.");
                        f++;
                    }

                    if (designated.isEmpty()) {
                        sender.sendMessage("There are no players available in list.");
                        f++;
                    }

                    if (f==2) return true;

                    if (spawnPoints.isEmpty()) {
                        sender.sendMessage("There are no spawn points available in list.");
                        return true;
                    }

                    List<SpawnPoint> availableSpawnPoints = new ArrayList<>(spawnPoints);

                    for (Player p : designated.keySet()) {
                        if (designated.containsKey(p)) {
                            int spawnId = designated.get(p);
                            SpawnPoint assignedSpawn = null;

                            for (SpawnPoint sp : availableSpawnPoints) {
                                if (sp.getID() == spawnId) {
                                    assignedSpawn = sp;
                                    break;
                                }
                            }
                            if (assignedSpawn != null) {
                                Location loc = new Location(p.getWorld(), assignedSpawn.getX() + 0.5, assignedSpawn.getY() + 1, assignedSpawn.getZ() + 0.5);
                                p.teleport(loc);
                                PlayerMoveManager.freezePlayer(p);
                                p.sendMessage("You are now in game, good luck :)");

                                availableSpawnPoints.remove(assignedSpawn);
                            } else {

                                if (!availableSpawnPoints.isEmpty()) {
                                    SpawnPoint sp = availableSpawnPoints.remove(0);
                                    Location loc = new Location(p.getWorld(), sp.getX() + 0.5, sp.getY() + 1, sp.getZ() + 0.5);
                                    p.teleport(loc);
                                    PlayerMoveManager.freezePlayer(p);
                                    p.sendMessage("You are now in game, good luck :)");
                                }
                            }
                        }
                    }

                    for (Player p : players) {
                        if (!designated.containsKey(p)) {
                            if (availableSpawnPoints.isEmpty()) {
                                break;
                            }
                            SpawnPoint sp = availableSpawnPoints.remove(0);
                            Location loc = new Location(p.getWorld(), sp.getX() + 0.5, sp.getY() + 1, sp.getZ() + 0.5);
                            p.teleport(loc);
                            PlayerMoveManager.freezePlayer(p);
                            p.sendMessage("You are now in game, good luck :)");
                        }
                    }

                    PlayerMoveManager.startTimer(10);
                    break;

                case "reset":

                    PlayerRepository.reset();
                    SpawnPointRepository.reset();
                    break;
            }

        }

        return true;
    }

}
