package com.github.alfonsoleandro.loveheadcrafting.masks;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.masks.effects.Effect;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Mask {

    protected final Map<Player, Set<Effect>> playerEffects = new HashMap<>();
    protected final Map<Player, Set<Listener>> listeners = new HashMap<>();
    protected final Set<PotionEffect> potionEffects;
    protected final String maskType;
    protected ItemStack mask;

    public Mask(Set<PotionEffect> potionEffects, String maskType, String skinUrl, String itemName, List<String> itemLore) {
        this.potionEffects = potionEffects;
        this.maskType = maskType;
        generateMaskHead(skinUrl, itemName, itemLore);
    }

    /**
     * Sets the item that will be represented by this Mask object.
     * @param skinUrl This head's skin URL. When used here "http://textures.minecraft.net/texture/"+skinUrl
     *               it should show a valid skin.
     * @param itemName The name for the item itself.
     * @param itemLore The lore for the item.
     */
    protected void generateMaskHead(String skinUrl, String itemName, List<String> itemLore){
        String skullUrl = "http://textures.minecraft.net/texture/"+skinUrl;

        this.mask = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) this.mask.getItemMeta();
        assert skullMeta != null;

        //Set skin
        GameProfile profile = new GameProfile(UUID.fromString("8561b610-ad5c-390d-ac31-1a1d8ca69fd7"), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", skullUrl).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }

        //Set name and lore
        skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));

        List<String> lore = new ArrayList<>();
        for(String line : itemLore){
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        skullMeta.setLore(lore);


        //Set NBT tags
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(JavaPlugin.getPlugin(LoveHeadCrafting.class), "MPMask"),
                PersistentDataType.STRING,
                maskType);


        this.mask.setItemMeta(skullMeta);
    }



    public ItemStack getMaskItem(){
        return this.mask;
    }

    public Set<PotionEffect> getPotionEffects(){
        return this.potionEffects;
    }

    public String getMaskType(){
        return this.maskType;
    }


    /**
     * Code executed when a player equips this mask.
     * @param player The player to apply any effects to.
     * @see #remove(Player).
     */
    public void start(Player player){
        if(potionEffects != null) {
            playerEffects.put(player, new HashSet<Effect>(){{add(new Effect(player, maskType, potionEffects));}});
        }
    }


    /**
     * Code executed when a player de-equips this mask.
     * @param player The player to remove any effects from.
     * @see #start(Player).
     */
    public void remove(Player player){
        if(listeners.containsKey(player)) {
            listeners.get(player).forEach(HandlerList::unregisterAll);
            listeners.remove(player);
        }
        if(playerEffects.containsKey(player) && playerEffects.get(player) != null){
            playerEffects.get(player).forEach(Effect::remove);
            playerEffects.remove(player);
        }
    }


}
