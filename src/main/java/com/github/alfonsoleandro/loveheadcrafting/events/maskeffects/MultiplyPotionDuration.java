package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class MultiplyPotionDuration implements Listener {

    private final LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
    private final Player player;
    private final double multiplier;

    public MultiplyPotionDuration(Player player, double multiplier){
       this.player = player;
       this.multiplier = multiplier;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerConsume(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        if(!this.player.equals(player)) return;
        if(event.isCancelled()) return;
        ItemStack consumed = event.getItem();


        if(consumed.getType().equals(Material.POTION)){
            new BukkitRunnable() {
                public void run() {
                    multiplyPotionDuration(player,
                            (PotionMeta) Objects.requireNonNull(consumed.getItemMeta()), multiplier);
                }
            }.runTaskLater(plugin, 1);
        }
    }


    private void multiplyPotionDuration(Player player, PotionMeta meta, double multiplier){
        PotionType pd = meta.getBasePotionData().getType();
        if(!pd.isInstant() && pd.getEffectType() != null) {
            PotionEffect effect = player.getPotionEffect(pd.getEffectType());

            player.removePotionEffect(effect.getType());
            double finalDuration = effect.getDuration()*multiplier;
            player.removePotionEffect(effect.getType());
            player.addPotionEffect(new PotionEffect(effect.getType(),
                    (int)finalDuration,
                    effect.getAmplifier(),
                    effect.isAmbient()));

            plugin.getConsoleLogger().send(player,
                    plugin.getMessagesYaml().getAccess().getString("potion extended by pig mask")
                            .replace("%multiplier%", String.valueOf(multiplier)));
        }



        for(PotionEffect effect : meta.getCustomEffects()){
            double finalDuration = effect.getDuration()*multiplier;
            player.removePotionEffect(effect.getType());
            player.addPotionEffect(new PotionEffect(effect.getType(),
                    (int)finalDuration,
                    effect.getAmplifier(),
                    effect.isAmbient()));

            plugin.getConsoleLogger().send(player,
                    plugin.getMessagesYaml().getAccess().getString("potion extended by pig mask")
                            .replace("%multiplier%", String.valueOf(multiplier)));
        }
    }


}
