package com.github.alfonsoleandro.loveheadcrafting.events;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CheckCraftingAmountsEvent implements Listener {

    private final LoveHeadCrafting plugin;

    public CheckCraftingAmountsEvent(LoveHeadCrafting plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void checkAmounts(PrepareItemCraftEvent event){
        if(!event.getView().getType().equals(InventoryType.WORKBENCH)) return;
        if(event.getRecipe() == null) return;
        ItemStack result = event.getRecipe().getResult();
        if(!result.hasItemMeta()) return;
        ItemMeta meta = result.getItemMeta();
        assert meta != null;
        PersistentDataContainer data = meta.getPersistentDataContainer();
        String maskType = null;
        for(NamespacedKey key : data.getKeys()){
            if(key.getKey().equalsIgnoreCase("MPMask")) {
                maskType = data.get(key, PersistentDataType.STRING);
            }
        }
        if(maskType == null) return;

        //Check amounts
        ConfigurationSection recipeAmounts = plugin.getConfigYaml().getAccess().getConfigurationSection("masks."+maskType+".recipe");
        if(recipeAmounts == null) return;

        int[] amounts = new int[9];

        short i = 0;

        for(char letter : new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'}){
            if(recipeAmounts.contains(String.valueOf(letter))) {
                amounts[i] = Integer.parseInt(recipeAmounts.getString(String.valueOf(letter), ",0").split(",")[1]);
            }
            i++;
        }

        ItemStack[] itemStacks = event.getInventory().getMatrix();

        for(int j = 0; j < 9; j++){
            if(amounts[j] == 0) continue;
            if(itemStacks[j].getAmount() < amounts[j]){
                event.getInventory().setResult(null);
                return;
            }
        }

    }


    @EventHandler
    public void removeAmounts(CraftItemEvent event) {
        if(!event.getView().getType().equals(InventoryType.WORKBENCH)) return;
        ItemStack result = event.getRecipe().getResult();
        if(!result.hasItemMeta()) return;
        ItemMeta meta = result.getItemMeta();
        assert meta != null;
        PersistentDataContainer data = meta.getPersistentDataContainer();
        String maskType = null;

        for (NamespacedKey key : data.getKeys()) {
            if(key.getKey().equalsIgnoreCase("MPMask")) {
                maskType = data.get(key, PersistentDataType.STRING);
            }
        }
        if(maskType == null) return;

        //Check amounts
        ConfigurationSection recipeAmounts = plugin.getConfigYaml().getAccess().getConfigurationSection("masks." + maskType + ".recipe");
        if(recipeAmounts == null) return;

        if(event.isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        int[] amounts = new int[9];
        short i = 0;

        //Get correct amounts from config
        for (char letter : new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'}) {
            if(recipeAmounts.contains(String.valueOf(letter))) {
                amounts[i] = Integer.parseInt(recipeAmounts.getString(String.valueOf(letter)).split(",")[1]);
            }
            i++;
        }


        //Correct amounts
        ItemStack[] itemStacks = event.getInventory().getMatrix();
        for (int j = 0; j < 9; j++) {
            if(amounts[j] == 0) continue;
            int resultingAmount = (itemStacks[j].getAmount() - amounts[j]) + 1;
            itemStacks[j].setAmount(resultingAmount);
        }


    }

}
