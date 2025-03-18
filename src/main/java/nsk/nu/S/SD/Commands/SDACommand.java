package nsk.nu.S.SD.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import nsk.nu.S.SD.EnhancedLogger;
import nsk.nu.S.SD.Entity.SpawnPoint;
import nsk.nu.S.SD.Events.PlayerMoveManager;
import nsk.nu.S.SD.PluginInstance;
import nsk.nu.S.SD.Repository.PlayerRepository;
import nsk.nu.S.SD.Repository.SpawnPointRepository;
import nsk.nu.S.SD.SimpleSpawnpointDeployment;
import nsk.nu.S.System.Tags;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SDACommand implements CommandExecutor, TabCompleter {

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

                case "get":
                    return this.handleGet(sender);

                case "list":
                    return this.handleList(sender);

                case "start":
                    return this.handleStart(sender);

                case "add":
                    if (args.length < 2) {
                        sender.sendMessage("Usage: /sda <argument> <player>");
                        return true;
                    }
                    return this.handleAdd(sender, args);

                case "reset":
                    PlayerRepository.reset();
                    SpawnPointRepository.reset();
                    break;

                default:
                    sender.sendMessage("Unknown argument: " + args[0]);
                    return true;
            }

        }

        return true;
    }

    private boolean handleGet(CommandSender sender) {

        if (sender instanceof Player player) {

            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta meta = item.getItemMeta();

            meta.displayName(MiniMessage.miniMessage().deserialize("<gradient:#03f4fc:#03fc6f>SDA Selector</gradient>"));

            item.setItemMeta(meta);

            player.getInventory().addItem(item);

            return true;

        }

        sender.sendMessage("You need to be player to use this command.");

        return true;

    }

    private boolean handleList(CommandSender sender) {

        List<SpawnPoint> sp_list = SpawnPointRepository.getSpawnPoints();

        /*
        *   List of all spawn points:
        *   1. ID: 0 - [⏩ Teleport]
        *   2. ID: 2 - [⏩ Teleport]
        *   3. ID: 8 - [⏩ Teleport]
        *   4. ID: 14 - [⏩ Teleport]
        *
        *   Teleport 1: #00ffff
        *   Teleport 2: #7fffd4
        */

        Component full_message = MiniMessage.miniMessage().deserialize("<#6d8cfd>[SSD]</#6d8cfd> <gradient:#00ffff:#6d8cfd>List of all spawn points:</gradient>").appendNewline();

        for (int i = 0; i < sp_list.size(); i++) {
            full_message = full_message.append(MiniMessage.miniMessage().deserialize("<#6d8cfd>" + (i + 1) + ". </#6d8cfd>"));
            full_message = full_message.append( this.createLine( sp_list.get(i) )).appendNewline();
        }

        sender.sendMessage(full_message);

        return true;

    }

    private boolean handleStart(CommandSender sender) {

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

        return true;

    }

    private boolean handleAdd(CommandSender sender, String[] args) {

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

        } catch (Exception e) {
            logger.warning(e.getMessage());
        }

        return true;
    }

    //

    private Component createLine(SpawnPoint sp) {



        TagResolver resolver = TagResolver.resolver(
                Placeholder.parsed("x", "<red>" + sp.getX() + "</red>"),
                Placeholder.parsed("y", "<red>" + (sp.getY()+1) + "</red>"),
                Placeholder.parsed("z", "<red>" + sp.getZ() + "</red>"),
                Tags.getStandard(),
                Tags.getGradient()
        );

        Component line = MiniMessage.miniMessage().deserialize("<gradient:<gprimary>:<gsecondary>>ID: <hover:show_text:'<gold>X: <x>, Y: <y>, Z: <z></gold>'><ascent>" + sp.getID() + "<-ascent></hover> - </gradient>", resolver);
        return line.append( this.teleportButton(sp) );
    }

    private Component teleportButton(SpawnPoint sp) {

        TagResolver resolver = TagResolver.resolver(
                Placeholder.parsed("x", String.valueOf(sp.getX())),
                Placeholder.parsed("y", String.valueOf(sp.getY()+1)),
                Placeholder.parsed("z", String.valueOf(sp.getZ())),
                Tags.getStandard(),
                Tags.getGradient()
        );

        return MiniMessage.miniMessage().deserialize("<click:run_command:/tp <x> <y> <z>><gradient:<gascent>:<gascent2>>[ ⏩ Teleport ]</gradient></click>", resolver);
    }

    //

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("add");
            completions.add("get");
            completions.add("list");
            completions.add("start");
            completions.add("reset");
        }

        if (args[0].equals("add") && args.length == 2) {
            completions.add("*");
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                completions.add(p.getName());
            }
        }

        return completions;
    }

}
