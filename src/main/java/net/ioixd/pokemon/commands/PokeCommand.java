package net.ioixd.pokemon.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.MessageType;
import co.aikar.commands.annotation.*;
import com.github.oscar0812.pokeapi.models.games.Pokedex;
import com.github.oscar0812.pokeapi.models.pokemon.Pokemon;
import com.github.oscar0812.pokeapi.models.pokemon.PokemonColor;
import com.github.oscar0812.pokeapi.models.utility.FlavorText;
import com.github.oscar0812.pokeapi.models.utility.Language;
import net.ioixd.pokemon.General;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.oscar0812.pokeapi.utils.Client;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PokeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(args.length <= 0) {
            player.sendMessage(Color.RED+"Must provide at least one subcommand; give or rand.");
        };
        switch(args[0]) {
            case "give":
                if(args.length <= 1) {
                    sender.sendMessage(ChatColor.RED+"Must provide ID for pokemon.");
                }
                String number = args[1];
                int num = 0;
                try {
                    num = Integer.parseInt(number);
                } catch (Exception ex) {
                    player.sendMessage(ChatColor.RED+"Invalid number given.");
                    return false;
                }
                return General.pokeGive(player, num, 0);
            case "rand":
                int rand = (int) (Math.random() * 1000);
                return General.pokeGive(player, rand, 0);
            default:
                sender.sendMessage(ChatColor.RED+"Invalid subcommand. Must be give or rand");
                break;
        }
        return true;
    }
}
