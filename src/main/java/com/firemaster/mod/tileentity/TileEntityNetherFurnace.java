package com.firemaster.mod.tileentity;

import com.firemaster.mod.blocks.NetherBrickFurnace;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityNetherFurnace extends TileEntity implements ISidedInventory {
	private String localizedName;
	
	private ItemStack[] slots = new ItemStack[3];
	
	private static final int[] slots_top = new int[]{0};
	private static final int[] slots_side = new int[]{1};
	private static final int[] slots_bottom = new int[]{2, 1};
	
	public int furnaceSpeed = 150;
	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;
	
	public void setGuiDisplayName(String displayName) {
		this.localizedName = displayName;
	}

	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.localizedName : "container.NetherBrickFurnace";
	}

	public boolean hasCustomInventoryName() {
		return this.localizedName != null && this.localizedName.length() > 0;
	}
	
	public int getSizeInventory() {
		return this.slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return false;
	}

	public void openInventory() {}
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 2 ? false : (slot == 1 ? isItemFuel(itemStack) : true);
	}
	
	private boolean isItemFuel(ItemStack itemStack) {
		return getItemBurnTime(itemStack) > 0;
	}

	private int getItemBurnTime(ItemStack itemStack) {
		if (itemStack == null) {
			return 0;
		} else {
			Item item = itemStack.getItem();
			
			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);
				
				if (block == Blocks.log) return 300;
				if (block == Blocks.planks) return 300;
				if (block == Blocks.sapling) return 100;
			} else {
				if (item == Items.coal) return 1600;
				if (item == Items.stick) return 100;
				if (item == Items.blaze_rod) return 2400;
				if (item == Items.lava_bucket) return 20000;
			}
		}

		return 0;
	}
	
	public void updateEntity() {
		boolean flag = this.burnTime > 0;
		boolean flag1 = false;
		
		if (this.isBurning()) {
			burnTime--;
		}
		
		if (!this.worldObj.isRemote) {
			if (this.burnTime == 0 && this.canSmelt()) {
				this.currentItemBurnTime = this.burnTime = getItemBurnTime(this.slots[1]);
				
				if (this.isBurning()) {
					flag1 = true;
					
					if (this.slots[1] != null) {
						this.slots[1].stackSize--;
						
						if (this.slots[1].stackSize == 0) {
							this.slots[1] = this.slots[1].getItem().getContainerItem(this.slots[1]);
						}
					}
				}
			}
			
			if (this.isBurning() && this.canSmelt()) {
				this.cookTime++;
				
				if (this.cookTime == this.furnaceSpeed) {
					this.cookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
			} else {
				this.cookTime = 0;
			}
			
			if (flag != this.isBurning()) {
				flag1 = true;
				NetherBrickFurnace.updateNetherBrickFurnaceBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}
		
		if (flag1) {
			this.markDirty();
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if (side == 0) {
			return this.slots_bottom;
		} else if (side == 1) {
			return this.slots_top;
		} else {
			return this.slots_side;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return slot != 0 || slot != 1 || itemStack.getItem() == Items.bucket;
	}
}