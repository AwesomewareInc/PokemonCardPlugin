package net.ioixd.pokemon;

import com.github.oscar0812.pokeapi.models.games.Pokedex;
import com.github.oscar0812.pokeapi.models.moves.Move;
import com.github.oscar0812.pokeapi.models.moves.MoveFlavorText;
import com.github.oscar0812.pokeapi.models.pokemon.*;
import com.github.oscar0812.pokeapi.models.pokemon.Pokemon;
import com.github.oscar0812.pokeapi.models.utility.FlavorText;
import com.github.oscar0812.pokeapi.models.utility.Language;
import com.github.oscar0812.pokeapi.utils.Client;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class General {

    public static String RealReset = ChatColor.RESET+""+ChatColor.WHITE;

    public static boolean pokeGive(Player player, int num, int minCaptureRate) {
        Pokemon pokemon = new Pokemon();


        if(num == -1) {
            int captureRate = -1;
            while(minCaptureRate >= captureRate) {
                num = (int) (Math.random() * 1000);
                try {
                    pokemon = Client.getPokemonById(num);
                } catch (Exception ex) {
                    player.sendMessage(ChatColor.RED +ex.getMessage());
                    return false;
                }
                captureRate = pokemon.getSpecies().getCaptureRate();
            }
        } else {
            try {
                pokemon = Client.getPokemonById(num);
            } catch (Exception ex) {
                player.sendMessage(ChatColor.RED +ex.getMessage());
                return false;
            }
        }

        // name
        String speciesName = pokemon.getSpecies().getName();

        // pokedex entry
        String pokedexEntry = General.getFlavorTextInEnglish(pokemon.getSpecies().getFlavorTextEntries());

        // species genus
        String speciesDesc = General.getGenusInEnglish(pokemon.getSpecies().getGenera());

        // pokemon stats
        ArrayList<PokemonStat> stats = pokemon.getStats();
        String hp = "0";
        for (PokemonStat stat : stats) {
            switch(stat.getStat().getName()) {
                case "hp":
                    hp = String.valueOf(stat.getBaseStat());
                    break;
                default:
                    break;
            }
        }

        // typing
        ArrayList<PokemonType> types = pokemon.getTypes();

        // pokemon color
        String pokemonColor = pokemon.getSpecies().getColor().getName();
        ChatColor nameColor = General.ChatColorFromPokeColor(pokemonColor);

        // width/height
        int weight = pokemon.getWeight();
        double height = pokemon.getHeight();
        double heightInFeet = (height * 3.048);
        int heightInInches = ((int) Math.round((heightInFeet % 1) * 100));

        // make the item
        ItemStack card = new ItemStack(Material.PAPER);
        ItemMeta meta = card.getItemMeta();

        String nameFormated = speciesName.toUpperCase();
        String seperator = General.RealReset+nameFormated.replaceAll("(?s).","-");
        meta.setDisplayName(
                ChatColor.RESET+""+nameColor+""+ChatColor.BOLD+""+nameFormated
        );
        List<String> lore = new ArrayList<>();

        lore.add(seperator);
        lore.add(General.RealReset+"HP "+hp);
        lore.add(General.RealReset+"#"+num+"    "+speciesDesc+"    HT: "+(int) heightInFeet+"' "+heightInInches+"\"    WT: "+weight+"lbs.");
        lore.add(" ");

        // typing
        String typingString = "";
        for(PokemonType type : types) {
            typingString += General.ChatColorFromPokeType(type)+"["+type.getType().getName().toUpperCase()+"] ";
        }
        lore.add(ChatColor.RESET+typingString);

        // pokedex entry
        lore.add(" ");
        for(String entry : pokedexEntry.split("(\n|\f)")) {
            lore.add(ChatColor.RESET+""+ChatColor.GOLD+
                    entry
                            .replace("É","E")
                            .replace("é","e")
            );
        }

        // moves
        lore.add(seperator+seperator);
        ArrayList<PokemonMove> moves = pokemon.getMoves();
        int numOfMoves = moves.size();
        int moveNum = (int) (Math.random() * 2) + 1;
        for(int i = 0; i < moveNum; i++) {
            int moveId = (int) (Math.random() * numOfMoves);
            Move move = moves.get(moveId).getMove();

            String moveName = move.getName().replace("-", " ").toUpperCase();
            ChatColor FlavorType = General.ChatColorFromPokeType(move.getType());
            lore.add(
                    FlavorType+moveName+" "
                            +ChatColor.BOLD+"["+move.getType().getName().toUpperCase()+"]"+
                            ChatColor.RESET+FlavorType+": "+
                            General.RealReset);
            String desc = General.getMoveFlavorTextInEnglish(move.getFlavorTextEntries());
            for(String entry : desc.split("(\n|\f)")) {
                lore.add(FlavorType+
                        entry
                                .replace("É","E")
                                .replace("é","e")
                );
            }

        }
        meta.setLore(lore);

        card.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(net.ioixd.pokemon.Pokemon.instance(), "second-edition");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        player.getInventory().addItem(card);
        return true;
    }

    public static String getGenusInEnglish(ArrayList<Genus> genera) {
        for(Genus entry : genera) {
            if(entry.getLanguage().getName().equals("en")) {
                return entry.getGenus().split(" ")[0]+" Pokemon";
            }
        }
        return ChatColor.GRAY+""+ChatColor.ITALIC+"???";
    }
    public static String getFlavorTextInEnglish(ArrayList<FlavorText> entries) {
        for(FlavorText entry : entries) {
            if(entry.getLanguage().getName().equals("en")) {
                return entry.getFlavorText();
            }
        }
        return ChatColor.GRAY+""+ChatColor.ITALIC+"(no description)";
    }
    public static String getMoveFlavorTextInEnglish(ArrayList<MoveFlavorText> entries) {
        for(MoveFlavorText entry : entries) {
            if(entry.getLanguage().getName().equals("en")) {
                return entry.getFlavorText();
            }
        }
        return ChatColor.GRAY+""+ChatColor.ITALIC+"(no description)";
    }

    public static ChatColor ChatColorFromPokeColor(String pokemonColor) {
        switch(pokemonColor) {
            case "black", "gray": return ChatColor.GRAY;
            case "blue": return ChatColor.BLUE;
            case "brown", "red": return ChatColor.DARK_RED;
            case "green": return ChatColor.GREEN;
            case "pink": return ChatColor.RED;
            case "purple": return ChatColor.LIGHT_PURPLE;
            case "white": return ChatColor.WHITE;
            case "yellow": return ChatColor.GOLD;
            default: return ChatColor.WHITE;
        }
    }

    public static ChatColor ChatColorFromPokeType(PokemonType type) {
        return General.ChatColorFromPokeType(type.getType());
    }
    public static ChatColor ChatColorFromPokeType(Type type) {
        switch(type.getName()) {
            case "normal":      return ChatColor.WHITE;
            case "fire":        return ChatColor.RED;
            case "water":       return ChatColor.BLUE;
            case "grass":       return ChatColor.DARK_GREEN;
            case "electric":    return ChatColor.YELLOW;
            case "ice":         return ChatColor.AQUA;
            case "fighting":    return ChatColor.DARK_RED;
            case "poison":      return ChatColor.DARK_PURPLE;
            case "ground":      return ChatColor.DARK_RED;
            case "flying":      return ChatColor.WHITE;
            case "psychic":     return ChatColor.LIGHT_PURPLE;
            case "bug":         return ChatColor.GREEN;
            case "rock":        return ChatColor.DARK_RED;
            case "ghost":       return ChatColor.GRAY;
            case "dark":        return ChatColor.DARK_GRAY;
            case "dragon":      return ChatColor.DARK_BLUE;
            case "steel":       return ChatColor.GRAY;
            case "fairy":       return ChatColor.WHITE;
            default:            return ChatColor.WHITE;
        }
    }
}
