package com.github.alfonsoleandro.loveheadcrafting.commands;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.utils.MPCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public final class MainCommand extends MPCommand implements CommandExecutor {

    //Translatable messages
    private String reloaded;
    private String cannotConsole;
    private String invalidMask;
    private String invalidPlayer;
    private String addedToYourInv;
    private String fullInv;
    private String addedToPlayerInv;
    private String maskAddedToYourInv;
    private String playerHasFullInv;
    private String mustHoldItem;
    private String itemSaved;
    private String itemNotExist;
    private String itemRemoved;
    private String itemAdded;

    /**
     * MainCommand class constructor.
     * @param plugin The main class instance.
     */
    public MainCommand(LoveHeadCrafting plugin){
        super(plugin);
        loadMessages();
    }

    /**
     * Pre loads every message used for this command.
     */
    protected void loadMessages(){
        super.loadMessages();
        FileConfiguration messages = plugin.getMessagesYaml().getAccess();

        reloaded = messages.getString("reloaded");
        cannotConsole = messages.getString("cannot send from console");
        invalidMask = messages.getString("invalid mask type");
        invalidPlayer = messages.getString("invalid player");
        addedToYourInv = messages.getString("added mask yourself");
        fullInv = messages.getString("full inventory");
        addedToPlayerInv = messages.getString("added mask to player");
        maskAddedToYourInv = messages.getString("received mask from player");
        playerHasFullInv = messages.getString("player has full inventory");
        mustHoldItem = messages.getString("must be holding item");
        itemSaved = messages.getString("item saved");
        itemNotExist = messages.getString("nonexistent item");
        itemRemoved = messages.getString("item removed");
        itemAdded = messages.getString("item added");
    }



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
            send(sender, "&6List of commands");
            send(sender, "&f/"+label+" help");
            send(sender, "&f/"+label+" version");
            send(sender, "&f/"+label+" reload");
            send(sender, "&f/"+label+" giveMask (mask) <amount> <player>");
            send(sender, "&f/"+label+" items (get/set/remove) (item name)");
            send(sender, "&f/"+label+" listItems");


        }else if(args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("loveHeadCrafting.reload")) {
                send(sender, noPerm);
                return true;
            }
            plugin.reload();
            send(sender, reloaded);


        }else if(args[0].equalsIgnoreCase("version")) {
            if(!sender.hasPermission("loveHeadCrafting.version")) {
                send(sender, noPerm);
                return true;
            }
            send(sender, "&fVersion: &e" + plugin.getVersion() + "&f. &aUp to date!");




        }else if(args[0].equalsIgnoreCase("giveMask")) {
            if(!sender.hasPermission("loveHeadCrafting.giveMask")) {
                send(sender, noPerm);
                return true;
            }
            if(args.length < 2){
                send(sender, "&cUse: &f/"+label+" giveMask (maskType) <amount> <player>");
                return true;
            }

            //Get type of mask
            String maskType = args[1].toUpperCase(Locale.ROOT);
            ItemStack mask;
            try {
                mask = plugin.getMasksManager().getMask(maskType).getMaskItem().clone();
            }catch (IllegalArgumentException | NullPointerException ignored){
                send(sender, invalidMask.replace("%type%", maskType));
                return true;
            }

            //set amount
            if(args.length > 2){
                try{
                    mask.setAmount(Integer.parseInt(args[2]));
                }catch (NumberFormatException ignored){
                    send(sender, "&cInvalid number \"&e"+args[2]+"&c\"");
                }
            }

            //Add to sender or a player
            if(args.length < 4){
                if(sender instanceof ConsoleCommandSender){
                    send(sender, cannotConsole);
                    return true;
                }
                if(addToInv((Player) sender, mask)){
                    send(sender, addedToYourInv.replace("%type%", maskType).replace("%amount%", String.valueOf(mask.getAmount())));
                }else{
                    send(sender, fullInv);
                }
            }else{
                Player toAdd = Bukkit.getPlayer(args[3]);

                if(toAdd == null){
                    send(sender, invalidPlayer.replace("%player%", args[3]));
                    return true;
                }

                if(addToInv(toAdd, mask)){
                    send(sender, addedToPlayerInv.replace("%player%", args[3]).replace("%type%", maskType).replace("%amount%", String.valueOf(mask.getAmount())));
                    send(toAdd, maskAddedToYourInv.replace("%player%", sender.getName()).replace("%type%", maskType).replace("%amount%", String.valueOf(mask.getAmount())));
                }else{
                    send(sender, playerHasFullInv.replace("%player%", args[3]));
                }
            }




        }else if(args[0].equalsIgnoreCase("modifyItems")) {
            if(sender instanceof ConsoleCommandSender) {
                send(sender, cannotConsole);
                return true;
            }
            if(!sender.hasPermission("loveHeadCrafting.items")) {
                send(sender, noPerm);
                return true;
            }
            if(args.length < 3
                    || (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("get") && !args[1].equalsIgnoreCase("remove"))) {
                send(sender, "&cUse: &f/" + label + " items (get/set/remove) (item name)");
                return true;
            }

            FileConfiguration items = plugin.getItemsYaml().getAccess();
            Player player = (Player) sender;
            ItemStack inHand = player.getInventory().getItemInMainHand();
            StringBuilder itemName = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                itemName.append(args[i]).append(" ");
            }
            String itemConfigName = itemName.toString().trim();

            if(args[1].equalsIgnoreCase("set")) {
                if(inHand.getType().isAir()) {
                    send(sender, mustHoldItem);
                    return true;
                }
                items.set(itemConfigName, inHand);
                send(sender, itemSaved.replace("%name%", itemConfigName));

            } else {
                if(!items.contains(itemConfigName)) {
                    send(sender, itemNotExist.replace("%name%", itemConfigName));
                    return true;
                }
                if(args[1].equalsIgnoreCase("remove")) {
                    items.set(itemConfigName, null);
                    plugin.getItemsYaml().save();
                    send(sender, itemRemoved);

                } else {
                    ItemStack item = items.getItemStack(itemConfigName);
                    if(addToInv(player, item)) {
                        send(sender, itemAdded);
                    } else {
                        send(sender, fullInv);
                    }
                }

            }


        }else if(args[0].equalsIgnoreCase("listItems")){
            if(!sender.hasPermission("loveHeadCrafting.items")) {
                send(sender, noPerm);
                return true;
            }
            send(sender, "&fAvailable items: ");
            StringBuilder sb = new StringBuilder();
            for(String key : plugin.getItemsYaml().getAccess().getKeys(false)){
                sb.append("&c").append(key).append(", ");
            }
            sb.append(";");
            send(sender, sb.toString().replace(", ;", ""));





            //unknown command
        }else {
            send(sender, unknown.replace("%command%", label));
        }


        return true;
    }


    private boolean addToInv(Player toAdd, ItemStack item){
        Inventory inv = toAdd.getInventory();
        if(inv.firstEmpty() != -1) {
            inv.addItem(item);
            return true;
        }else{
            for(int j = 0; j <= 35 ; j++) {
                ItemStack tempItem = inv.getItem(j);
                assert tempItem != null;
                if(tempItem.isSimilar(item) && tempItem.getAmount() < 64) {
                    inv.addItem(item);
                    return true;
                }
            }
        }

        return false;
    }



    @Override
    public void reload(){
        loadMessages();
    }

}
