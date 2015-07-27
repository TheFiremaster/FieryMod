package com.firemaster.mod;

import com.firemaster.mod.blocks.*;
import com.firemaster.mod.handler.GuiHandler;
import com.firemaster.mod.items.*;
import com.firemaster.mod.tileentity.TileEntityNetherFurnace;
import com.firemaster.mod.worldgen.FieryWorldGen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

@Mod(modid = FieryMod.ModId, name = FieryMod.ModName, version = FieryMod.ModVersion)
public class FieryMod {
	public static final String ModId 		= "fierymod";
	public static final String ModName 		= "Firemaster's Fiery Mod";
	public static final String ModVersion 	= "0.0.0";
	
	@Instance(ModId)
	public static FieryMod instance;
	
	public static ToolMaterial emberMaterial = EnumHelper.addToolMaterial("emberMaterial", 2, 650, 6.5f, 2.5f, 12);
	public static ToolMaterial ignisMaterial = EnumHelper.addToolMaterial("ignisMaterial", 3, 2250, 9.0f, 3.5f, 18);
	
	public static CreativeTabs fierymodTab;
	
	public static Item itemEmberIngot;
	public static Item itemIgnisIngot;
	public static Item itemEmberSword;
	public static Item itemEmberPickaxe;
	public static Item itemEmberAxe;
	public static Item itemEmberShovel;
	public static Item itemEmberHoe;
	public static Item itemIgnisSword;
	public static Item itemIgnisPickaxe;
	public static Item itemIgnisAxe;
	public static Item itemIgnisShovel;
	public static Item itemIgnisHoe;
	
	public static Block oreEmberOre;
	public static Block oreNetherEmberOre;
	public static Block oreNetherIgnisOre;
	public static Block blockEmberBlock;
	public static Block blockIgnisBlock;
	
	public static final int guiIDNetherBrickFurnace = 0;
	public static Block blockNetherBrickFurnaceIdle;
	public static Block blockNetherBrickFurnaceActive;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event) {
		// Item and block initialization and registry; Config file handling.
		fierymodTab			= new CreativeTabs("fierymod") {
			public Item getTabIconItem() {
				return itemIgnisIngot;
			}
		};
		
		itemEmberIngot 		= new MaterialItem("EmberIngot");
		itemIgnisIngot 		= new MaterialItem("IgnisIngot");
		itemEmberSword		= EmberEquipment.makeEmberSword();
		itemEmberPickaxe	= EmberEquipment.makeEmberPickaxe();
		itemEmberAxe		= EmberEquipment.makeEmberAxe();
		itemEmberShovel		= EmberEquipment.makeEmberShovel();
		itemEmberHoe		= EmberEquipment.makeEmberHoe();
		itemIgnisSword		= new IgnisSword();
		itemIgnisPickaxe	= new IgnisPickaxe();
		itemIgnisAxe		= new IgnisAxe();
		itemIgnisShovel		= new IgnisShovel();
		itemIgnisHoe		= new IgnisHoe();
		
		oreEmberOre			= new OreBlock("EmberOre", 2);
		oreNetherEmberOre	= new OreBlock("NetherEmberOre", 2);
		oreNetherIgnisOre	= new OreBlock("NetherIgnisOre", 3);
		blockEmberBlock		= new EmberBlock();
		blockIgnisBlock		= new IgnisBlock();
		
		blockNetherBrickFurnaceIdle 	= new NetherBrickFurnace(false);
		blockNetherBrickFurnaceActive	= new NetherBrickFurnace(true);
		
		
		GameRegistry.registerWorldGenerator(new FieryWorldGen(), 0);
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event) {
		// Primarily about registering recipes, smelting, entities, network, etc.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		GameRegistry.registerTileEntity(TileEntityNetherFurnace.class, "NetherBrickFurnace");
		
		// Crafting Recipes
		GameRegistry.addRecipe(new ItemStack(blockEmberBlock), new Object[] {"EEE", "EEE", "EEE", 'E', itemEmberIngot});
		GameRegistry.addRecipe(new ItemStack(blockIgnisBlock), new Object[] {"III", "III", "III", 'I', itemIgnisIngot});
		GameRegistry.addRecipe(new ItemStack(itemEmberSword), new Object[] {"E", "E", "S", 'E', itemEmberIngot, 'S', Items.stick});
		GameRegistry.addRecipe(new ItemStack(itemEmberPickaxe), new Object[] {"EEE", " S ", " S ", 'E', itemEmberIngot, 'S', Items.stick});
		GameRegistry.addRecipe(new ItemStack(itemEmberAxe), new Object[] {"EE", "SE", "S ", 'E', itemEmberIngot, 'S', Items.stick});
		GameRegistry.addRecipe(new ItemStack(itemEmberAxe), new Object[] {"EE", "ES", " S", 'E', itemEmberIngot, 'S', Items.stick});
		GameRegistry.addRecipe(new ItemStack(itemEmberShovel), new Object[] {"E", "S", "S", 'E', itemEmberIngot, 'S', Items.stick});
		GameRegistry.addRecipe(new ItemStack(itemEmberHoe), new Object[] {"EE", "S ", "S ", 'E', itemEmberIngot, 'S', Items.stick});
		GameRegistry.addRecipe(new ItemStack(itemEmberHoe), new Object[] {"EE", " S", " S", 'E', itemEmberIngot, 'S', Items.stick});
		GameRegistry.addRecipe(new ItemStack(itemIgnisSword), new Object[] {"I", "I", "B", 'I', itemIgnisIngot, 'B', Items.blaze_rod});
		GameRegistry.addRecipe(new ItemStack(itemIgnisPickaxe), new Object[] {"III", " B ", " B ", 'I', itemIgnisIngot, 'B', Items.blaze_rod});
		GameRegistry.addRecipe(new ItemStack(itemIgnisAxe), new Object[] {"II", "BI", "B ", 'I', itemIgnisIngot, 'B', Items.blaze_rod});
		GameRegistry.addRecipe(new ItemStack(itemIgnisAxe), new Object[] {"II", "IB", " B", 'I', itemIgnisIngot, 'B', Items.blaze_rod});
		GameRegistry.addRecipe(new ItemStack(itemIgnisShovel), new Object[] {"I", "B", "B", 'I', itemIgnisIngot, 'B', Items.blaze_rod});
		GameRegistry.addRecipe(new ItemStack(itemIgnisHoe), new Object[] {"II", "B ", "B ", 'I', itemIgnisIngot, 'B', Items.blaze_rod});
		GameRegistry.addRecipe(new ItemStack(itemIgnisHoe), new Object[] {"II", " B", " B", 'I', itemIgnisIngot, 'B', Items.blaze_rod});
		
		// Shapeless Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(itemIgnisIngot), new Object[] {itemEmberIngot, Items.blaze_powder, Items.redstone});
		GameRegistry.addShapelessRecipe(new ItemStack(itemEmberIngot, 9), new Object[] {blockEmberBlock});
		GameRegistry.addShapelessRecipe(new ItemStack(itemIgnisIngot, 9), new Object[] {blockIgnisBlock});
		
		
		// Smelting Recipes
		GameRegistry.addSmelting(oreEmberOre, new ItemStack(itemEmberIngot), 0);
		GameRegistry.addSmelting(oreNetherEmberOre, new ItemStack(itemEmberIngot, 2), 0);
		GameRegistry.addSmelting(oreNetherIgnisOre, new ItemStack(itemIgnisIngot), 0);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event) {
		// Mod compatibility handling, basically.
		// Ex. checking if there is items from mod A so it enables something.
	}
}
