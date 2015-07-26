package com.firemaster.mod.blocks;

import java.util.Random;

import com.firemaster.mod.FieryMod;
import com.firemaster.mod.tileentity.TileEntityNetherFurnace;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class NetherBrickFurnace extends BlockContainer {
	public final boolean isActive;

	@SideOnly(Side.CLIENT)
	private IIcon frontIcon;
	@SideOnly(Side.CLIENT)
	private IIcon topIcon;
	
	private static boolean keepInventory;
	private Random rand = new Random();
	
	public NetherBrickFurnace(boolean active) {
		super(Material.rock);
		
		this.setHardness(3.5f);
		
		this.isActive = active;
		if (active) {
			this.setBlockName("NetherBrickFurnaceActive");
			this.setLightLevel(0.625f);
		} else {
			this.setBlockName("NetherBrickFurnaceInactive");
			this.setCreativeTab(FieryMod.fierymodTab);
		}
		
		GameRegistry.registerBlock(this, getUnlocalizedName().substring(5));
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon	= register.registerIcon(FieryMod.ModId + ":NetherBrickFurnaceSide");
		this.frontIcon 	= register.registerIcon(FieryMod.ModId + ":NetherBrickFurnaceFront" + (isActive ? "Active" : "Idle"));
		this.topIcon	= register.registerIcon(FieryMod.ModId + ":NetherBrickFurnaceTop");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		// For in inventory.
		if (metadata == 0 && side == 3) {
			return this.frontIcon;
		}
		
		return side == 1 ? this.topIcon : (side == 0 ? this.topIcon : (side != metadata ? this.blockIcon : this.frontIcon));
	}
	
	public Item getItemDropped(int i, Random random, int j) {
		return Item.getItemFromBlock(FieryMod.blockNetherBrickFurnaceIdle);
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}

	private void setDefaultDirection(World world, int x, int y, int z) {
		if (!world.isRemote) {
			Block b1 = world.getBlock(x, y, z - 1);
			Block b2 = world.getBlock(x, y, z + 1);
			Block b3 = world.getBlock(x - 1, y, z);
			Block b4 = world.getBlock(x + 1, y, z);
			
			byte b0 = 3;
			if (b1.func_149730_j() && !b2.func_149730_j())
            {
                b0 = 3;
            }

            if (b2.func_149730_j() && !b3.func_149730_j())
            {
                b0 = 2;
            }

            if (b3.func_149730_j() && !b4.func_149730_j())
            {
                b0 = 5;
            }

            if (b4.func_149730_j() && !b3.func_149730_j())
            {
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			FMLNetworkHandler.openGui(player, FieryMod.instance, FieryMod.guiIDNetherBrickFurnace, world, x, y, z);
		}
		
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityNetherFurnace();
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (this.isActive) {
			int direction = world.getBlockMetadata(x, y, z);
			
			float x1 = (float)x + 0.5f;
			float y1 = (float)y + random.nextFloat();
			float z1 = (float)z + 0.5f;
			
			float f = 0.52f;
			float f1 = random.nextFloat() * 0.6f - 0.3f;
			
			if (direction == 4) {
				world.spawnParticle("smoke", (double)(x1 - f), (double)y1, (double)(z1 + f1), 0, 0, 0);
				world.spawnParticle("flame", (double)(x1 - f), (double)y1, (double)(z1 + f1), 0, 0, 0);
			} else if (direction == 5) {
				world.spawnParticle("smoke", (double)(x1 + f), (double)y1, (double)(z1 + f1), 0, 0, 0);
				world.spawnParticle("flame", (double)(x1 + f), (double)y1, (double)(z1 + f1), 0, 0, 0);
			} else if (direction == 2) {
				world.spawnParticle("smoke", (double)(x1 + f1), (double)y1, (double)(z1 - f), 0, 0, 0);
				world.spawnParticle("flame", (double)(x1 + f1), (double)y1, (double)(z1 - f), 0, 0, 0);
			} else if (direction == 3) {
				world.spawnParticle("smoke", (double)(x1 + f1), (double)y1, (double)(z1 + f), 0, 0, 0);
				world.spawnParticle("flame", (double)(x1 + f1), (double)y1, (double)(z1 + f), 0, 0, 0);
			}
		}
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityPlayer, ItemStack itemStack) {
		int l = MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		} else if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		} else if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		} else if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		
		if (itemStack.hasDisplayName()) {
			((TileEntityNetherFurnace)world.getTileEntity(x, y, z)).setGuiDisplayName(itemStack.getDisplayName());
		}
	}

	public static void updateNetherBrickFurnaceBlockState(boolean active, World worldObj, int xCoord, int yCoord, int zCoord) {
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		TileEntity entity = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		keepInventory = true;
		
		if (active) {
			worldObj.setBlock(xCoord, yCoord, zCoord, FieryMod.blockNetherBrickFurnaceActive);
		} else {
			worldObj.setBlock(xCoord, yCoord, zCoord, FieryMod.blockNetherBrickFurnaceIdle);
		}
		
		keepInventory = false;
		
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		
		if (entity != null) {
			entity.validate();
			worldObj.setTileEntity(xCoord, yCoord, zCoord, entity);
		}
	}
	
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int metadata) {
		if (!keepInventory) {
			TileEntityNetherFurnace entity = (TileEntityNetherFurnace)world.getTileEntity(x, y, z);
			
			if (entity != null) {
				for (int i = 0; i < entity.getSizeInventory(); i++) {
					ItemStack stack = entity.getStackInSlot(i);
					
					if (stack != null) {
						float f = this.rand.nextFloat() * 0.8f + 0.1f;
						float f1 = this.rand.nextFloat() * 0.8f + 0.1f;
						float f2 = this.rand.nextFloat() * 0.8f + 0.1f;

						
						while (stack.stackSize > 0) {
							int j = this.rand.nextInt(21) + 10;
							
							if (j > stack.stackSize) {
								j = stack.stackSize;
							}
							
							stack.stackSize -= j;
							
							EntityItem item = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(stack.getItem(), j, stack.getItemDamage()));
							
							if (stack.hasTagCompound()) {
								item.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
							}
							
							world.spawnEntityInWorld(item);
						}
					}
				}
				
				world.func_147453_f(x, y, z, oldBlock);
			}
		}
		
		super.breakBlock(world, x, y, z, oldBlock, metadata);
	}
}
