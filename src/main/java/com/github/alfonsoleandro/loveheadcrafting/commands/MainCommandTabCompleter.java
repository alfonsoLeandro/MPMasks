package com.github.alfonsoleandro.loveheadcrafting.commands;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCommandTabCompleter implements TabCompleter {

    private final LoveHeadCrafting plugin;

    public MainCommandTabCompleter(LoveHeadCrafting plugin){
        this.plugin = plugin;
    }



    /**
     * Checks if a string is equal (ignoring upper cases) to another string completed or uncompleted.
     * @param input The first string.
     * @param string The string to "cut into pieces" in order to compare every possibility to the first string.
     * @return True if the string is at least partially equal to the input.
     */
    public boolean equalsToStrings(String input, String string){
        for(int i = 0; i < string.length(); i++){
            if(input.equalsIgnoreCase(string.substring(0,i))) return true;
        }
        return input.equalsIgnoreCase(string);
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("")) {
                completions.add("help");
                completions.add("version");
                completions.add("reload");
                completions.add("giveMask");
                completions.add("items");
                completions.add("listItems");


            } else if(equalsToStrings(args[0], "help")) {
                completions.add("help");

            } else if(equalsToStrings(args[0], "version")) {
                completions.add("version");

            } else if(equalsToStrings(args[0], "reload")) {
                completions.add("reload");

            } else if(equalsToStrings(args[0], "giveMask")) {
                completions.add("giveMask");

            } else if(equalsToStrings(args[0], "items")) {
                completions.add("items");

            } else if(equalsToStrings(args[0], "listItems")) {
                completions.add("listItems");
            }

        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("giveMask")) {
                if(args[1].equalsIgnoreCase("")) {
                    completions.addAll(plugin.getMasksManager().getRegisteredMasks());
                } else {
                    for (String mask : plugin.getMasksManager().getRegisteredMasks()) {
                        if(equalsToStrings(args[1], mask)) completions.add(mask);
                    }
                }

            }else if(args[0].equalsIgnoreCase("items")) {
                completions.add("get");
                completions.add("set");
                completions.add("remove");
            }


        }else if(args.length == 3){
            if(args[0].equalsIgnoreCase("giveMask")) {
                completions.add("1");
                completions.add("2");
                completions.add("3");
                completions.add("10");
                completions.add("16");
                completions.add("32");
                completions.add("64");

            } else if(args[0].equalsIgnoreCase("items") && !args[1].equalsIgnoreCase("set")) {
                completions.addAll(plugin.getItemsYaml().getAccess().getKeys(false));
            }

        }else if(args.length == 4){
            if(args[0].equalsIgnoreCase("giveMask")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            }



        }
        return completions;
    }



}
