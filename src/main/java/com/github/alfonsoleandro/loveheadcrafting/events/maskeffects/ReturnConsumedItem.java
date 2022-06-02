package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.Set;

public class ReturnConsumedItem implements Listener {

    private final Player player;
    private final Set<Material> possibleItems;
    private final int probability;
    private final Random r = new Random();

    public ReturnConsumedItem(Player player, Set<Material> possibleItems, int probability){
        this.player = player;
        this.possibleItems = possibleItems;
        this.probability = probability;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        ItemStack consumed = event.getItem();

        if(!player.equals(this.player)) return;
        if(event.isCancelled()) return;

        if(possibleItems.contains(consumed.getType())){
            if(probability > r.nextInt(100)){
                giveBackItem(player, consumed);
            }
        }
    }


    private void giveBackItem(Player player, ItemStack item){
        item = item.clone();
        item.setAmount(1);
        player.getInventory().addItem(item);

    }

}
