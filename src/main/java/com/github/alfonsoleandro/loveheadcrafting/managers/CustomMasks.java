package com.github.alfonsoleandro.loveheadcrafting.managers;

import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import java.util.*;

public class CustomMasks {

    private final Map<String, Mask> masks = new HashMap<>();

    public boolean registerMask(String maskName, Mask mask){
        if(masks.containsKey(maskName)) return false;
        masks.put(maskName, mask);
        return true;
    }


    public Mask getMask(String maskType){
        return masks.get(maskType);
    }

    public void unRegisterMask(String maskType){
        this.masks.remove(maskType);
    }

    public Set<String> getRegisteredMasks(){
        return this.masks.keySet();
    }
}
