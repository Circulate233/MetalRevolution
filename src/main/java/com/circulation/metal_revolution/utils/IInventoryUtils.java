package com.circulation.metal_revolution.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class IInventoryUtils {

    public static boolean compareItems(ItemStack a, ItemStack b) {
        if (a != null && b != null && a.isItemEqual(b)) {
            return Objects.equals(a.getTagCompound(), b.getTagCompound());
        } else {
            return false;
        }
    }

    public static ItemStack insertItem(IInventory inv, int slot, ItemStack stack, boolean simulate) {
        if (stack == null) return null;

        ItemStack stackInSlot = inv.getStackInSlot(slot);
        int limit = inv.getInventoryStackLimit();

        int m;
        if (stackInSlot != null) {
            if (stackInSlot.stackSize >= Math.min(stackInSlot.getMaxStackSize(), limit)) return stack;

            if (!compareItems(stack, stackInSlot)) return stack;

            if (!inv.isItemValidForSlot(slot, stack)) return stack;

            m = Math.min(stack.getMaxStackSize(), limit) - stackInSlot.stackSize;

            if (stack.stackSize <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.stackSize += stackInSlot.stackSize;
                    inv.setInventorySlotContents(slot, copy);
                    inv.markDirty();
                }

                return null;
            } else {
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.stackSize += stackInSlot.stackSize;
                    inv.setInventorySlotContents(slot, copy);
                    inv.markDirty();
                } else {
                    stack.stackSize -= m;
                }
                return stack;
            }
        } else {
            if (!inv.isItemValidForSlot(slot, stack)) return stack;

            m = Math.min(stack.getMaxStackSize(), limit);
            if (m < stack.stackSize) {
                stack = stack.copy();
                if (!simulate) {
                    inv.setInventorySlotContents(slot, stack.splitStack(m));
                    inv.markDirty();
                } else {
                    stack.stackSize -= m;
                }
                return stack;
            } else {
                if (!simulate) {
                    inv.setInventorySlotContents(slot, stack);
                    inv.markDirty();
                }
                return null;
            }
        }
    }

    public static ItemStack extractItem(IInventory inv, int slot, int amount, boolean simulate) {
        if (amount == 0) return null;

        ItemStack stackInSlot = inv.getStackInSlot(slot);

        if (stackInSlot == null) return null;

        if (simulate) {
            if (stackInSlot.stackSize < amount) {
                return stackInSlot.copy();
            } else {
                ItemStack copy = stackInSlot.copy();
                copy.stackSize = amount;
                return copy;
            }
        } else {
            int m = Math.min(stackInSlot.stackSize, amount);

            ItemStack decrStackSize = inv.decrStackSize(slot, m);
            inv.markDirty();
            return decrStackSize;
        }
    }
}
