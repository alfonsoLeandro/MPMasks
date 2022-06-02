package com.github.alfonsoleandro.loveheadcrafting.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static String colorizeString(String string){
        if(string == null){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[PLUGIN] There was an error while colorizing a string, check your config");
            return "";
        }
        final char[] chars = string.toCharArray();
        final List<String> hexColors = new ArrayList<>();

        //Check every character on the string looking for the "&#" combination
        //length-1 in order to ensure there are 2 characters to look for
        outer: for (int i = 0; i < chars.length-1; i++) {
            if(chars[i] == '&' && chars[i+1] == '#'){
                final StringBuilder builder = new StringBuilder();

                //find the color combination #RRGGBB
                for (int j = i+1; j < i+8; j++) {
                    if(j < chars.length) {
                        builder.append(chars[j]);
                    }else{
                        break outer;
                    }
                }
                //Try adding the hex colors, if version < 1.16.1 it will still apply the usual colors
                // (translateAlternateColorCodes)
                try{
                    net.md_5.bungee.api.ChatColor.of(builder.toString());
                    hexColors.add(builder.toString());
                }catch (Error | Exception e){
                    if(e instanceof NoSuchMethodError){
                        return ChatColor.translateAlternateColorCodes('&', string);
                    }
                }
            }

        }
        for(String hex : hexColors){
            string = string.replace("&"+hex, String.valueOf(net.md_5.bungee.api.ChatColor.of(hex)));
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
