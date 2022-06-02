package com.github.alfonsoleandro.loveheadcrafting.utils;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Logger extends Reloadable {

    private final LoveHeadCrafting plugin;
    private String prefix;

    public Logger(LoveHeadCrafting plugin){
        super(plugin);
        this.plugin = plugin;
        this.prefix = plugin.getConfigYaml().getAccess().getString("prefix");
    }


    public void send(CommandSender sender, String msg){
        sender.sendMessage(StringUtils.colorizeString(prefix+" "+msg));
    }


    public void send(String msg){
        send(Bukkit.getConsoleSender(), msg);
    }


    public void reload(){
        this.prefix = plugin.getConfigYaml().getAccess().getString("prefix");
    }


}