package com.firemaster.mod.items;

import com.firemaster.mod.FieryMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;

// This file will contain all the ember tool and armour classes since they will be very simple with no functionality beyond basic tools or armour. And then a class with static methods to create these tools.
// If I end up having more than one class of basic tools/armour, I'll refactor this to be more generic.
public class EmberEquipment {
	public static ItemSword makeEmberSword() {
		return new EmberSword();
	}
	
	public static ItemPickaxe makeEmberPickaxe() {
		return new EmberPickaxe();
	}
	
	public static ItemAxe makeEmberAxe() {
		return new EmberAxe();
	}
	
	public static ItemSpade makeEmberShovel() {
		return new EmberShovel();
	}
	
	public static ItemHoe makeEmberHoe() {
		return new EmberHoe();
	}
	
	// Will actually prefer a method like this in a more centralized static class, since everything uses it, but for now this will do.
	public static String makeTextureName(String unlocName) {
		return FieryMod.ModId + ":" + unlocName.substring(5);
	}
}

class EmberSword extends ItemSword {
	public EmberSword() {
		super(FieryMod.emberMaterial);
		
		this.setUnlocalizedName("EmberSword");
		this.setTextureName(EmberEquipment.makeTextureName(getUnlocalizedName()));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
}

class EmberPickaxe extends ItemPickaxe {
	public EmberPickaxe() {
		super(FieryMod.emberMaterial);
		
		this.setUnlocalizedName("EmberPickaxe");
		this.setTextureName(EmberEquipment.makeTextureName(getUnlocalizedName()));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
}

class EmberAxe extends ItemAxe {
	public EmberAxe() {
		super(FieryMod.emberMaterial);
		
		this.setUnlocalizedName("EmberAxe");
		this.setTextureName(EmberEquipment.makeTextureName(getUnlocalizedName()));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
}

class EmberShovel extends ItemSpade {
	public EmberShovel() {
		super(FieryMod.emberMaterial);
		
		this.setUnlocalizedName("EmberShovel");
		this.setTextureName(EmberEquipment.makeTextureName(getUnlocalizedName()));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
}

class EmberHoe extends ItemHoe {
	public EmberHoe() {
		super(FieryMod.emberMaterial);
		
		this.setUnlocalizedName("EmberHoe");
		this.setTextureName(EmberEquipment.makeTextureName(getUnlocalizedName()));
		this.setCreativeTab(FieryMod.fierymodTab);
		
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}
}
