package nsk.nu.S.SD;

import nsk.nu.S.SD.Commands.SDACommand;
import nsk.nu.S.SD.Events.PlayerMoveManager;
import nsk.nu.S.SD.Events.onRightClickBlock;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleSpawnpointDeployment extends JavaPlugin {

    @Override
    public void onEnable() {

        PluginInstance.setInstance(this);
        EnhancedLogger.set(this);

        this.registerCommands();

        getServer().getPluginManager().registerEvents(new onRightClickBlock(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveManager(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /* --- --- --- --- --- --- --- --- --- --- */

    private void registerCommands() {
        EnhancedLogger logger = EnhancedLogger.get();

        PluginCommand sda = this.getCommand("sda");
        if (sda != null) {
            SDACommand sdaCommand = new SDACommand();
            sda.setExecutor(sdaCommand);
            sda.setTabCompleter(sdaCommand);
            logger.fine("Command 'sda' registered.");
        } else {
            logger.severe("Command 'sda' not registered.");
        }
    }
}
