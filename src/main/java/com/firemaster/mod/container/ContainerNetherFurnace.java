package com.firemaster.mod.container;

import com.firemaster.mod.tileentity.TileEntityNetherFurnace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerNetherFurnace extends Container {
	private TileEntityNetherFurnace netherFurnace;
	
	public int lastBurnTime;
	public int lastCurrentItemBurnTime;
	public int lastCookTime;
	
	public ContainerNetherFurnace(InventoryPlayer inventory, TileEntityNetherFurnace entity) {
		this.netherFurnace = entity;
		
		this.addSlotToContainer(new Slot(entity, 0, 56, 35));
		this.addSlotToContainer(new Slot(entity, 1, 8, 62));
		this.addSlotToContainer(new SlotFurnace(inventory.player, entity, 2, 116, 35));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inventory, j + (i * 9) + 9, (j * 18) + 8, (i * 18) + 84));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inventory, i, (i * 18) + 8, 142));
		}
	}
	
	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
		
		crafting.sendProgressBarUpdate(this, 0, this.netherFurnace.cookTime);
		crafting.sendProgressBarUpdate(this, 0, this.netherFurnace.burnTime);
		crafting.sendProgressBarUpdate(this, 0, this.netherFurnace.currentItemBurnTime);
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting crafting = (ICrafting)this.crafters.get(i);
			
			if (this.lastCookTime != this.netherFurnace.cookTime) {
				crafting.sendProgressBarUpdate(this, 0, this.netherFurnace.cookTime);
			}
			
			if (this.lastBurnTime != this.netherFurnace.burnTime) {
				crafting.sendProgressBarUpdate(this, 1, this.netherFurnace.burnTime);
			}
			
			if (this.lastCurrentItemBurnTime != this.netherFurnace.currentItemBurnTime) {
				crafting.sendProgressBarUpdate(this, 2, this.netherFurnace.currentItemBurnTime);
			}
			
			this.lastCookTime = this.netherFurnace.cookTime;
			this.lastBurnTime = this.netherFurnace.burnTime;
			this.lastCurrentItemBurnTime = this.netherFurnace.currentItemBurnTime;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int newValue) {
		if (slot == 0) {
			this.netherFurnace.cookTime = newValue;
		} else if (slot == 1) {
			this.netherFurnace.burnTime = newValue;
		} else if (slot == 2) {
			this.netherFurnace.currentItemBurnTime = newValue;
		}
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNum == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (slotNum != 1 && slotNum != 0) {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                }
                else if (TileEntityNetherFurnace.isItemFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                }
                else if (slotNum >= 3 && slotNum < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return null;
                    }
                }
                else if (slotNum >= 30 && slotNum < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
