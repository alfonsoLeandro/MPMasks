package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.utils.Reloadable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishRareLoot extends Reloadable implements Listener {

    private final LoveHeadCrafting plugin;
    private final Player player;
    private final int probability;
    private final String maskType;
    private List<ItemStack> items;
    private String fishedMessage;
    private final Random r = new Random();

    public FishRareLoot(LoveHeadCrafting plugin, Player player, int probability, String maskType){
        super(plugin);
        this.plugin = plugin;
        this.player = player;
        this.probability = probability;
        this.maskType = maskType;
        this.items = fillPossibleItems();
        this.fishedMessage = plugin.getMessagesYaml().getAccess().getString("special drop fished");
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerFish(PlayerFishEvent event){
        Player player = event.getPlayer();

        if(!player.equals(this.player)) return;
        if(event.isCancelled()) return;
        if(!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) return;

        tryAddItems(player, ((Item)event.getCaught()), maskType);

    }


    private void tryAddItems(Player player, Item caught, String maskType){
        int rand = r.nextInt(100);
        ItemStack item = items.get(r.nextInt(items.size()));

        if(this.probability > rand) {
            caught.setItemStack(item);
            sendFishedMessage(player, item.getType().toString(), maskType);
        }
    }

    private void sendFishedMessage(Player player, String item, String maskType){
        plugin.getConsoleLogger().send(player, fishedMessage.replace("%type%", item).replace("%mask%", maskType));
    }


    private List<ItemStack> fillPossibleItems(){
        List<ItemStack> items = new ArrayList<>();
        FileConfiguration config = plugin.getConfigYaml().getAccess();
        FileConfiguration itemsFile = plugin.getItemsYaml().getAccess();

        for(String item : config.getStringList("possible fishing drops")){
            items.add(itemsFile.getItemStack(item));
        }

        return items;
    }

    @Override
    public void reload(){
        this.items = fillPossibleItems();
        this.fishedMessage = plugin.getMessagesYaml().getAccess().getString("special drop fished");
    }


}
