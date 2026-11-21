package com.circulation.metal_revolution.utils;

import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import appeng.api.config.Actionable;
import appeng.api.networking.security.PlayerSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.data.IAEItemStack;
import appeng.tile.misc.TileInterface;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.common.Optional;

public class IInventoryUtils {

    public static boolean compareItems(ItemStack a, ItemStack b) {
        if (a != null && b != null && a.isItemEqual(b)) {
            return Objects.equals(a.getTagCompound(), b.getTagCompound());
        } else {
            return false;
        }
    }

    public static ItemStack insertItem(IInventory inv, int slot, ItemStack stack, boolean simulate) {
        if (stack == null || stack.stackSize <= 0) return null;

        ItemStack stackInSlot = inv.getStackInSlot(slot);
        int limit = inv.getInventoryStackLimit();

        if (stackInSlot != null) {
            if (stackInSlot.stackSize >= Math.min(stackInSlot.getMaxStackSize(), limit)) return stack;
            if (!compareItems(stack, stackInSlot)) return stack;
        }
        if (!inv.isItemValidForSlot(slot, stack)) return stack;

        int available = Math.min(stack.getMaxStackSize(), limit) - (stackInSlot == null ? 0 : stackInSlot.stackSize);
        if (available <= 0) return stack;

        int inserted = Math.min(stack.stackSize, available);
        ItemStack remainder = null;
        if (inserted < stack.stackSize) {
            remainder = stack.copy();
            remainder.stackSize -= inserted;
        }
        if (!simulate) {
            ItemStack result = stack;
            if (stackInSlot != null || remainder != null) {
                result = stack.copy();
                result.stackSize = inserted + (stackInSlot == null ? 0 : stackInSlot.stackSize);
            }
            inv.setInventorySlotContents(slot, result);
            inv.markDirty();
        }
        return remainder;
    }

    public static ItemStack extractItem(IInventory inv, int slot, int amount, boolean simulate) {
        if (amount <= 0) return null;

        ItemStack stackInSlot = inv.getStackInSlot(slot);

        if (stackInSlot == null) return null;

        if (simulate) {
            ItemStack copy = stackInSlot.copy();
            copy.stackSize = Math.min(stackInSlot.stackSize, amount);
            return copy;
        } else {
            int m = Math.min(stackInSlot.stackSize, amount);

            ItemStack decrStackSize = inv.decrStackSize(slot, m);
            inv.markDirty();
            return decrStackSize;
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    public static boolean tryMoveItemsToAE(TileEntity te, IInventory bagInv, EntityPlayer player) {
        if (!(te instanceof TileInterface ae)) return false;
        final var node = ae.getActionableNode();
        if (node == null) return false;
        final var grid = node.getGrid();
        if (grid == null) return false;
        IStorageGrid storageGrid = grid.getCache(IStorageGrid.class);
        if (storageGrid == null) return false;
        final var s = storageGrid.getItemInventory();
        final var source = new PlayerSource(player, ae);
        for (int slot = 0; slot < bagInv.getSizeInventory(); slot++) {
            final var item = bagInv.getStackInSlot(slot);
            if (item == null) continue;
            IAEItemStack aeItem = AEItemStack.create(item);
            aeItem = s.injectItems(aeItem, Actionable.MODULATE, source);
            if (aeItem == null) {
                bagInv.setInventorySlotContents(slot, null);
            } else if (bagInv.getStackInSlot(slot).stackSize != aeItem.getStackSize()) {
                bagInv.getStackInSlot(slot).stackSize = (int) aeItem.getStackSize();
            }
        }
        return true;
    }
}
