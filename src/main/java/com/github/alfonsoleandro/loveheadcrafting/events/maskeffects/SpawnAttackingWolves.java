package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class SpawnAttackingWolves implements Listener {

    private final Player player;
    private final int wolfCount;
    private final int probability;
    private final String entityName;
    private final Random r = new Random();

    public SpawnAttackingWolves(Player player, int wolfCount, int probability, String entityName){
        this.player = player;
        this.wolfCount = wolfCount;
        this.probability = probability;
        this.entityName = entityName;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player)) return;
        Player dealer = (Player) event.getDamager();

        if(!dealer.equals(player))
        if(event.isCancelled()) return;
        if(!event.getEntity().isValid()) return;

        if(probability > r.nextInt(100)){
            spawnWolves(player, wolfCount, (LivingEntity) event.getEntity());
        }

    }

    private void spawnWolves(Player player, int amount, LivingEntity target){
        for (int i = 0; i < amount; i++) {
            Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation().add(r.nextInt(2),0, r.nextInt(2)), EntityType.WOLF);
            wolf.setAngry(true);
            wolf.setCustomName(ChatColor.translateAlternateColorCodes('&', entityName));
            wolf.setTarget(target);
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
            Bukkit.getPluginManager().registerEvents(new ScheduledEntityDeSpawn(wolf), JavaPlugin.getPlugin(LoveHeadCrafting.class));
        }
    }


    private static class ScheduledEntityDeSpawn implements Listener{

        private final LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
        private final Entity entity;
        private BukkitTask task;

        ScheduledEntityDeSpawn(Entity entity){
            this.entity = entity;
            startTask();
        }

        @EventHandler
        void onEntityDamage(EntityDamageByEntityEvent event){
            if(event.getDamager().equals(entity)){
                delete();
            }
        }

        @EventHandler
        void onChangeTarget(EntityTargetLivingEntityEvent event){
            if(event.getEntity().equals(entity)){
                event.setCancelled(true);
            }
        }


        void delete(){
            if(!task.isCancelled()) task.cancel();
            if(entity.isValid()) entity.remove();
            HandlerList.unregisterAll(this);
        }


        void startTask(){
            task = new BukkitRunnable(){
                public void run() {
                    delete();
                }
            }.runTaskLater(plugin, 100);
        }

    }



}
