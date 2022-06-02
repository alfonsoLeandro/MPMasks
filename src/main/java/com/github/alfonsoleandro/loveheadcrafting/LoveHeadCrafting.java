package com.github.alfonsoleandro.loveheadcrafting;

import com.codingforcookies.armorequip.ArmorListener;
import com.codingforcookies.armorequip.DispenserArmorListener;
import com.github.alfonsoleandro.lovecraftmobexp.LoveExp;
import com.github.alfonsoleandro.lovecraftmobexp.utils.Heads;
import com.github.alfonsoleandro.loveheadcrafting.commands.MainCommand;
import com.github.alfonsoleandro.loveheadcrafting.commands.MainCommandTabCompleter;
import com.github.alfonsoleandro.loveheadcrafting.events.ApplyRemoveEffects;
import com.github.alfonsoleandro.loveheadcrafting.events.CheckCraftingAmountsEvent;
import com.github.alfonsoleandro.loveheadcrafting.events.PreventMaskPlacingEvent;
import com.github.alfonsoleandro.loveheadcrafting.managers.CustomMasks;
import com.github.alfonsoleandro.loveheadcrafting.managers.EffectsManager;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.chicken.*;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.cow.*;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.horse.*;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.mooshroom.*;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.ocelot.*;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.pig.*;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.rabbit.*;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.sheep.*;
import com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.wolf.*;
import com.github.alfonsoleandro.loveheadcrafting.utils.Logger;
import com.github.alfonsoleandro.loveheadcrafting.utils.ReloaderPlugin;
import com.github.alfonsoleandro.loveheadcrafting.utils.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class LoveHeadCrafting extends ReloaderPlugin {

    private final Set<NamespacedKey> recipeKeys = new HashSet<>();
    final private String version = getDescription().getVersion();
    final private char color = 'e';
    private Logger logger;
    private CustomMasks masksManager;
    private EffectsManager effectsManager;
    private YamlFile configYaml;
    private YamlFile messagesYaml;
    private YamlFile itemsYaml;



    /**
     * Plugin enable logic.
     */
    @Override
    public void onEnable() {
        registerFiles();
        this.logger = new Logger(this);
        this.logger.send("&aEnabled&f. Version: &e" + version);
        this.logger.send("&fThank you for using my plugin! &" + color + getDescription().getName() + "&f By " + getDescription().getAuthors().get(0));
        this.logger.send("&fJoin my discord server at &chttps://discordapp.com/invite/ZznhQud");
        this.logger.send("Please consider subscribing to my yt channel: &c" + getDescription().getWebsite());
        this.masksManager = new CustomMasks();
        this.effectsManager = new EffectsManager(this);
        registerDefaultMasks();
        registerEvents();
        registerCommands();
        registerRecipes();
    }

    /**
     * Plugin disable logic.
     */
    @Override
    public void onDisable() {
        this.logger.send("&cDisabled&f. Version: &e" + version);
        this.logger.send("&fThank you for using my plugin! &" + color + getDescription().getName() + "&f By " + getDescription().getAuthors().get(0));
        this.logger.send("&fJoin my discord server at &chttps://discordapp.com/invite/ZznhQud");
        this.logger.send("Please consider subscribing to my yt channel: &c" + getDescription().getWebsite());
        unRegisterRecipes();
        for(Player player : Bukkit.getOnlinePlayers()) {
            effectsManager.removeAll(player);
        }
    }



    /**
     * Gets the plugins current version.
     * @return The version string.
     */
    public String getVersion() {
        return this.version;
    }





    /**
     * Registers plugin files.
     */
    private void registerFiles() {
        Map<String, Object> defaultItems = new HashMap<>();
        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) Objects.requireNonNull(enchantedBook.getItemMeta());
        bookMeta.addStoredEnchant(Enchantment.DIG_SPEED, 1, false);
        enchantedBook.setItemMeta(bookMeta);
        defaultItems.put("gApple", new ItemStack(Material.GOLDEN_APPLE));
        defaultItems.put("book", enchantedBook);
        defaultItems.put("leather boots", new ItemStack(Material.LEATHER_BOOTS));


        configYaml = new YamlFile(this, "config.yml");
        messagesYaml = new YamlFile(this, "messages.yml");
        itemsYaml = new YamlFile(this, "items.yml", defaultItems);
    }


    /**
     * Reloads plugin files.
     * {@link #reload()} must be used for reloading the plugin.
     */
    public void reloadFiles() {
        configYaml = new YamlFile(this, "config.yml");
        messagesYaml = new YamlFile(this, "messages.yml");
        itemsYaml = new YamlFile(this, "items.yml");
    }


    /**
     * Reloads the entire plugin.
     */
    public void reload(){
        reloadFiles();
        super.reload();
        unRegisterRecipes();
        registerRecipes();
    }

    /**
     * Registers the event listeners.
     */
    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new CheckCraftingAmountsEvent(this), this);
        pm.registerEvents(new ApplyRemoveEffects(this), this);
        pm.registerEvents(new PreventMaskPlacingEvent(this), this);
        //ArmorEquipEvent events
        pm.registerEvents(new ArmorListener(), this);
        pm.registerEvents(new DispenserArmorListener(), this);

    }


    /**
     * Registers commands and command classes.
     */
    private void registerCommands() {
        PluginCommand mainCommand = getCommand("loveHeadCrafting");

        if(mainCommand == null){
            this.logger.send("&cCommands were not registered properly. Please check your plugin.yml is intact");
            this.logger.send("&cDisabling LoveHeadCrafting");
            this.setEnabled(false);
            return;
        }

        mainCommand.setExecutor(new MainCommand(this));
        mainCommand.setTabCompleter(new MainCommandTabCompleter(this));
    }



    private void registerRecipes(){
        Heads heads = JavaPlugin.getPlugin(LoveExp.class).getHeads();
        ConfigurationSection section = configYaml.getAccess().getConfigurationSection("masks");
        if(section == null) return;

        char[] letters = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};

        outer: for(String maskName : section.getKeys(false)) {
            if(masksManager.getMask(maskName) == null) continue;
            ItemStack result = masksManager.getMask(maskName).getMaskItem();

            NamespacedKey key = new NamespacedKey(this, maskName);
            ShapedRecipe recipe = new ShapedRecipe(key, result);
            recipeKeys.add(key);

            recipe.shape("ABC", "DEF", "GHI");

            for(char letter : letters){
                if(!section.contains(maskName+".recipe."+letter)) continue;
                ItemStack ingredient;
                String headType = section.getString(maskName+".recipe."+letter).split(",")[0];
                try{
                    ingredient = heads.getMobHead(EntityType.valueOf(headType));
                }catch (IllegalArgumentException ignored){
                    if(masksManager.getMask(headType) == null){
                        logger.send("&cFailed to register recipe for "+headType);
                        logger.send("&cPlease check every "+headType+" mask ingredient is correct");
                        continue outer;
                    }
                    ingredient = masksManager.getMask(headType).getMaskItem();
                }
                recipe.setIngredient(letter, new RecipeChoice.ExactChoice(ingredient));
            }


            this.logger.send("&aSuccessfully added recipe for &c"+maskName);
            Bukkit.addRecipe(recipe);
        }
    }



    private void unRegisterRecipes(){
        for(NamespacedKey key : recipeKeys) Bukkit.removeRecipe(key);
    }



    private void registerDefaultMasks(){
        ConfigurationSection masksSection = configYaml.getAccess().getConfigurationSection("masks");
        if(masksSection == null) return;

        if(masksSection.contains("PIG1") && this.masksManager.registerMask("PIG1", new PIG1(
                masksSection.getString("PIG1.url"),
                masksSection.getString("PIG1.name"),
                masksSection.getStringList("PIG1.lore")))){
            this.logger.send("&aMask PIG1 successfully registered.");
        }
        if(masksSection.contains("PIG2") && this.masksManager.registerMask("PIG2", new PIG2(
                masksSection.getString("PIG2.url"),
                masksSection.getString("PIG2.name"),
                masksSection.getStringList("PIG2.lore")))){
            this.logger.send("&aMask PIG2 successfully registered.");
        }
        if(masksSection.contains("PIG3") && this.masksManager.registerMask("PIG3", new PIG3(
                masksSection.getString("PIG3.url"),
                masksSection.getString("PIG3.name"),
                masksSection.getStringList("PIG3.lore")))){
            this.logger.send("&aMask PIG3 successfully registered.");
        }
        if(masksSection.contains("PIG4") && this.masksManager.registerMask("PIG4", new PIG4(
                masksSection.getString("PIG4.url"),
                masksSection.getString("PIG4.name"),
                masksSection.getStringList("PIG4.lore")))){
            this.logger.send("&aMask PIG4 successfully registered.");
        }
        if(masksSection.contains("PIG5") && this.masksManager.registerMask("PIG5", new PIG5(
                masksSection.getString("PIG5.url"),
                masksSection.getString("PIG5.name"),
                masksSection.getStringList("PIG5.lore")))){
            this.logger.send("&aMask PIG5 successfully registered.");
        }

        if(masksSection.contains("WOLF1") && this.masksManager.registerMask("WOLF1", new WOLF1(
                masksSection.getString("WOLF1.url"),
                masksSection.getString("WOLF1.name"),
                masksSection.getStringList("WOLF1.lore")))){
            this.logger.send("&aMask WOLF1 successfully registered.");
        }
        if(masksSection.contains("WOLF2") && this.masksManager.registerMask("WOLF2", new WOLF2(
                masksSection.getString("WOLF2.url"),
                masksSection.getString("WOLF2.name"),
                masksSection.getStringList("WOLF2.lore")))){
            this.logger.send("&aMask WOLF2 successfully registered.");
        }
        if(masksSection.contains("WOLF3") && this.masksManager.registerMask("WOLF3", new WOLF3(
                masksSection.getString("WOLF3.url"),
                masksSection.getString("WOLF3.name"),
                masksSection.getStringList("WOLF3.lore")))){
            this.logger.send("&aMask WOLF3 successfully registered.");
        }
        if(masksSection.contains("WOLF4") && this.masksManager.registerMask("WOLF4", new WOLF4(
                masksSection.getString("WOLF4.url"),
                masksSection.getString("WOLF4.name"),
                masksSection.getStringList("WOLF4.lore")))){
            this.logger.send("&aMask WOLF4 successfully registered.");
        }
        if(masksSection.contains("WOLF5") && this.masksManager.registerMask("WOLF5", new WOLF5(
                masksSection.getString("WOLF5.url"),
                masksSection.getString("WOLF5.name"),
                masksSection.getStringList("WOLF5.lore")))){
            this.logger.send("&aMask WOLF5 successfully registered.");
        }


        if(masksSection.contains("OCELOT1") && this.masksManager.registerMask("OCELOT1", new OCELOT1(
                masksSection.getString("OCELOT1.url"),
                masksSection.getString("OCELOT1.name"),
                masksSection.getStringList("OCELOT1.lore")))){
            this.logger.send("&aMask OCELOT1 successfully registered.");
        }
        if(masksSection.contains("OCELOT2") && this.masksManager.registerMask("OCELOT2", new OCELOT2(
                masksSection.getString("OCELOT2.url"),
                masksSection.getString("OCELOT2.name"),
                masksSection.getStringList("OCELOT2.lore")))){
            this.logger.send("&aMask OCELOT2 successfully registered.");
        }
        if(masksSection.contains("OCELOT3") && this.masksManager.registerMask("OCELOT3", new OCELOT3(
                masksSection.getString("OCELOT3.url"),
                masksSection.getString("OCELOT3.name"),
                masksSection.getStringList("OCELOT3.lore")))){
            this.logger.send("&aMask OCELOT3 successfully registered.");
        }
        if(masksSection.contains("OCELOT4") && this.masksManager.registerMask("OCELOT4", new OCELOT4(
                masksSection.getString("OCELOT4.url"),
                masksSection.getString("OCELOT4.name"),
                masksSection.getStringList("OCELOT4.lore")))){
            this.logger.send("&aMask OCELOT4 successfully registered.");
        }
        if(masksSection.contains("OCELOT5") && this.masksManager.registerMask("OCELOT5", new OCELOT5(
                masksSection.getString("OCELOT5.url"),
                masksSection.getString("OCELOT5.name"),
                masksSection.getStringList("OCELOT5.lore")))){
            this.logger.send("&aMask OCELOT5 successfully registered.");
        }


        if(masksSection.contains("RABBIT1") && this.masksManager.registerMask("RABBIT1", new RABBIT1(
                masksSection.getString("RABBIT1.url"),
                masksSection.getString("RABBIT1.name"),
                masksSection.getStringList("RABBIT1.lore")))){
            this.logger.send("&aMask RABBIT1 successfully registered.");
        }
        if(masksSection.contains("RABBIT2") && this.masksManager.registerMask("RABBIT2", new RABBIT2(
                masksSection.getString("RABBIT2.url"),
                masksSection.getString("RABBIT2.name"),
                masksSection.getStringList("RABBIT2.lore")))){
            this.logger.send("&aMask RABBIT2 successfully registered.");
        }
        if(masksSection.contains("RABBIT3") && this.masksManager.registerMask("RABBIT3", new RABBIT3(
                masksSection.getString("RABBIT3.url"),
                masksSection.getString("RABBIT3.name"),
                masksSection.getStringList("RABBIT3.lore")))){
            this.logger.send("&aMask RABBIT3 successfully registered.");
        }
        if(masksSection.contains("RABBIT4") && this.masksManager.registerMask("RABBIT4", new RABBIT4(
                masksSection.getString("RABBIT4.url"),
                masksSection.getString("RABBIT4.name"),
                masksSection.getStringList("RABBIT4.lore")))){
            this.logger.send("&aMask RABBIT4 successfully registered.");
        }
        if(masksSection.contains("RABBIT5") && this.masksManager.registerMask("RABBIT5", new RABBIT5(
                masksSection.getString("RABBIT5.url"),
                masksSection.getString("RABBIT5.name"),
                masksSection.getStringList("RABBIT5.lore")))){
            this.logger.send("&aMask RABBIT5 successfully registered.");
        }


        if(masksSection.contains("SHEEP1") && this.masksManager.registerMask("SHEEP1", new SHEEP1(
                masksSection.getString("SHEEP1.url"),
                masksSection.getString("SHEEP1.name"),
                masksSection.getStringList("SHEEP1.lore")))){
            this.logger.send("&aMask SHEEP1 successfully registered.");
        }
        if(masksSection.contains("SHEEP2") && this.masksManager.registerMask("SHEEP2", new SHEEP2(
                masksSection.getString("SHEEP2.url"),
                masksSection.getString("SHEEP2.name"),
                masksSection.getStringList("SHEEP2.lore")))){
            this.logger.send("&aMask SHEEP2 successfully registered.");
        }
        if(masksSection.contains("SHEEP3") && this.masksManager.registerMask("SHEEP3", new SHEEP3(
                masksSection.getString("SHEEP3.url"),
                masksSection.getString("SHEEP3.name"),
                masksSection.getStringList("SHEEP3.lore")))){
            this.logger.send("&aMask SHEEP3 successfully registered.");
        }
        if(masksSection.contains("SHEEP4") && this.masksManager.registerMask("SHEEP4", new SHEEP4(
                masksSection.getString("SHEEP4.url"),
                masksSection.getString("SHEEP4.name"),
                masksSection.getStringList("SHEEP4.lore")))){
            this.logger.send("&aMask SHEEP4 successfully registered.");
        }
        if(masksSection.contains("SHEEP5") && this.masksManager.registerMask("SHEEP5", new SHEEP5(
                masksSection.getString("SHEEP5.url"),
                masksSection.getString("SHEEP5.name"),
                masksSection.getStringList("SHEEP5.lore")))){
            this.logger.send("&aMask SHEEP5 successfully registered.");
        }


        if(masksSection.contains("COW1") && this.masksManager.registerMask("COW1", new COW1(
                masksSection.getString("COW1.url"),
                masksSection.getString("COW1.name"),
                masksSection.getStringList("COW1.lore")))){
            this.logger.send("&aMask COW1 successfully registered.");
        }
        if(masksSection.contains("COW2") && this.masksManager.registerMask("COW2", new COW2(
                masksSection.getString("COW2.url"),
                masksSection.getString("COW2.name"),
                masksSection.getStringList("COW2.lore")))){
            this.logger.send("&aMask COW2 successfully registered.");
        }
        if(masksSection.contains("COW3") && this.masksManager.registerMask("COW3", new COW3(
                masksSection.getString("COW3.url"),
                masksSection.getString("COW3.name"),
                masksSection.getStringList("COW3.lore")))){
            this.logger.send("&aMask COW3 successfully registered.");
        }
        if(masksSection.contains("COW4") && this.masksManager.registerMask("COW4", new COW4(
                masksSection.getString("COW4.url"),
                masksSection.getString("COW4.name"),
                masksSection.getStringList("COW4.lore")))){
            this.logger.send("&aMask COW4 successfully registered.");
        }
        if(masksSection.contains("COW5") && this.masksManager.registerMask("COW5", new COW5(
                masksSection.getString("COW5.url"),
                masksSection.getString("COW5.name"),
                masksSection.getStringList("COW5.lore")))){
            this.logger.send("&aMask COW5 successfully registered.");
        }


        if(masksSection.contains("MOOSHROOM1") && this.masksManager.registerMask("MOOSHROOM1", new MOOSHROOM1(
                masksSection.getString("MOOSHROOM1.url"),
                masksSection.getString("MOOSHROOM1.name"),
                masksSection.getStringList("MOOSHROOM1.lore")))){
            this.logger.send("&aMask MOOSHROOM1 successfully registered.");
        }
        if(masksSection.contains("MOOSHROOM2") && this.masksManager.registerMask("MOOSHROOM2", new MOOSHROOM2(
                masksSection.getString("MOOSHROOM2.url"),
                masksSection.getString("MOOSHROOM2.name"),
                masksSection.getStringList("MOOSHROOM2.lore")))){
            this.logger.send("&aMask MOOSHROOM2 successfully registered.");
        }
        if(masksSection.contains("MOOSHROOM3") && this.masksManager.registerMask("MOOSHROOM3", new MOOSHROOM3(
                masksSection.getString("MOOSHROOM3.url"),
                masksSection.getString("MOOSHROOM3.name"),
                masksSection.getStringList("MOOSHROOM3.lore")))){
            this.logger.send("&aMask MOOSHROOM3 successfully registered.");
        }
        if(masksSection.contains("MOOSHROOM4") && this.masksManager.registerMask("MOOSHROOM4", new MOOSHROOM4(
                masksSection.getString("MOOSHROOM4.url"),
                masksSection.getString("MOOSHROOM4.name"),
                masksSection.getStringList("MOOSHROOM4.lore")))){
            this.logger.send("&aMask MOOSHROOM4 successfully registered.");
        }
        if(masksSection.contains("MOOSHROOM5") && this.masksManager.registerMask("MOOSHROOM5", new MOOSHROOM5(
                masksSection.getString("MOOSHROOM5.url"),
                masksSection.getString("MOOSHROOM5.name"),
                masksSection.getStringList("MOOSHROOM5.lore")))){
            this.logger.send("&aMask MOOSHROOM5 successfully registered.");
        }


        if(masksSection.contains("HORSE1") && this.masksManager.registerMask("HORSE1", new HORSE1(
                masksSection.getString("HORSE1.url"),
                masksSection.getString("HORSE1.name"),
                masksSection.getStringList("HORSE1.lore")))){
            this.logger.send("&aMask HORSE1 successfully registered.");
        }
        if(masksSection.contains("HORSE2") && this.masksManager.registerMask("HORSE2", new HORSE2(
                masksSection.getString("HORSE2.url"),
                masksSection.getString("HORSE2.name"),
                masksSection.getStringList("HORSE2.lore")))){
            this.logger.send("&aMask HORSE2 successfully registered.");
        }
        if(masksSection.contains("HORSE3") && this.masksManager.registerMask("HORSE3", new HORSE3(
                masksSection.getString("HORSE3.url"),
                masksSection.getString("HORSE3.name"),
                masksSection.getStringList("HORSE3.lore")))){
            this.logger.send("&aMask HORSE3 successfully registered.");
        }
        if(masksSection.contains("HORSE4") && this.masksManager.registerMask("HORSE4", new HORSE4(
                masksSection.getString("HORSE4.url"),
                masksSection.getString("HORSE4.name"),
                masksSection.getStringList("HORSE4.lore")))){
            this.logger.send("&aMask HORSE4 successfully registered.");
        }
        if(masksSection.contains("HORSE5") && this.masksManager.registerMask("HORSE5", new HORSE5(
                masksSection.getString("HORSE5.url"),
                masksSection.getString("HORSE5.name"),
                masksSection.getStringList("HORSE5.lore")))){
            this.logger.send("&aMask HORSE5 successfully registered.");
        }


        if(masksSection.contains("CHICKEN1") && this.masksManager.registerMask("CHICKEN1", new CHICKEN1(
                masksSection.getString("CHICKEN1.url"),
                masksSection.getString("CHICKEN1.name"),
                masksSection.getStringList("CHICKEN1.lore")))){
            this.logger.send("&aMask CHICKEN1 successfully registered.");
        }
        if(masksSection.contains("CHICKEN2") && this.masksManager.registerMask("CHICKEN2", new CHICKEN2(
                masksSection.getString("CHICKEN2.url"),
                masksSection.getString("CHICKEN2.name"),
                masksSection.getStringList("CHICKEN2.lore")))){
            this.logger.send("&aMask CHICKEN2 successfully registered.");
        }
        if(masksSection.contains("CHICKEN3") && this.masksManager.registerMask("CHICKEN3", new CHICKEN3(
                masksSection.getString("CHICKEN3.url"),
                masksSection.getString("CHICKEN3.name"),
                masksSection.getStringList("CHICKEN3.lore")))){
            this.logger.send("&aMask CHICKEN3 successfully registered.");
        }
        if(masksSection.contains("CHICKEN4") && this.masksManager.registerMask("CHICKEN4", new CHICKEN4(
                masksSection.getString("CHICKEN4.url"),
                masksSection.getString("CHICKEN4.name"),
                masksSection.getStringList("CHICKEN4.lore")))){
            this.logger.send("&aMask CHICKEN4 successfully registered.");
        }
        if(masksSection.contains("CHICKEN5") && this.masksManager.registerMask("CHICKEN5", new CHICKEN5(
                masksSection.getString("CHICKEN5.url"),
                masksSection.getString("CHICKEN5.name"),
                masksSection.getStringList("CHICKEN5.lore")))){
            this.logger.send("&aMask CHICKEN5 successfully registered.");
        }
    }


    public Logger getConsoleLogger(){
        return this.logger;
    }

    public CustomMasks getMasksManager(){
        return this.masksManager;
    }

    public EffectsManager getEffectsManager(){
        return this.effectsManager;
    }


    /**
     * Get the config YamlFile.
     * @return The YamlFile containing the config file.
     */
    public YamlFile getConfigYaml(){
        return this.configYaml;
    }

    /**
     * Get the messages YamlFile.
     * @return The YamlFile containing the messages file.
     */
    public YamlFile getMessagesYaml(){
        return this.messagesYaml;
    }

    /**
     * Get the items YamlFile.
     * @return The YamlFile containing the items file.
     */
    public YamlFile getItemsYaml(){
        return this.itemsYaml;
    }



}
