package com.github.alfonsoleandro.loveheadcrafting.utils;

/**
 * Simple interface implemented on classes that should reload in any way when
 * this plugin reloads.
 */
public abstract class Reloadable {

    public Reloadable(ReloaderPlugin plugin){
        plugin.registerReloadable(this);
    }

    /**
     * Reloads this class.
     */
     public abstract void reload();
}
