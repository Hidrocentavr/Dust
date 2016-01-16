package com.mowmaster.dust.item.Tools.Pickaxes;

import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeYellow extends ItemPickaxe {

    public CrystalPickaxeYellow(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeYellow(String crystalpickaxe_yellow, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_yellow);
    }
}