package com.github.alfonsoleandro.loveheadcrafting.utils;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Simple abstract class meant to be extended by Command executor classes.
 */
public abstract class MPCommand extends Reloadable{

    final protected LoveHeadCrafting plugin;
    //Translatable messages
    protected String noPerm;
    protected String unknown;

    /**
     * Command class constructor.
     * @param plugin This plugin's main class instance.
     */
    public MPCommand(LoveHeadCrafting plugin){
        super(plugin);
        this.plugin = plugin;
        loadMessages();
    }

    /**
     * Pre loads every message used for this command.
     */
    protected void loadMessages(){
        FileConfiguration messages = plugin.getMessagesYaml().getAccess();

        noPerm = messages.getString("no permission");
        unknown = messages.getString("unknown command");
    }

    /**
     * Sends a message to the CommandSender.
     * @param msg The message to be sent.
     */
    protected void send(CommandSender sender, String msg){
        plugin.getConsoleLogger().send(sender, msg);
    }

}
