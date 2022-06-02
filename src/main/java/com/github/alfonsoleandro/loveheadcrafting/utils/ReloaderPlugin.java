package com.github.alfonsoleandro.loveheadcrafting.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public abstract class ReloaderPlugin extends JavaPlugin {

    private final Set<Reloadable> reloadables = new HashSet<>();

    public void reload(){
        for(Reloadable reloadable : reloadables){
            reloadable.reload();
        }
    }

    public void registerReloadable(Reloadable reloadable){
        reloadables.add(reloadable);
    }
}
