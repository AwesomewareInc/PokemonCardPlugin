package net.ioixd.pokemon;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.ioixd.pokemon.commands.PokeCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class Pokemon extends JavaPlugin implements Listener {

    @Getter
    @Accessors(fluent = true)
    private static net.ioixd.pokemon.Pokemon instance;

    private PaperCommandManager commandManager;

    public Pokemon() {
        instance = this;
    }

    public Pokemon(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);

        this.getCommand("poke").setExecutor(new PokeCommand());
    }

    @EventHandler
    public void playerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Material material = item.getType();
        int modelData = item.getItemMeta().getCustomModelData();
        if(material == Material.SKULL_BANNER_PATTERN && modelData != 0) {
            event.setCancelled(true);
            item.setAmount(item.getAmount()-1);
            for(int i = 0; i < 10; i++) {
                General.pokeGive(player, -1, modelData);
            }

        }
    }
}
