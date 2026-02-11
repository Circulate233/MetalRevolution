package com.circulation.metal_revolution.utils;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TagUtils {

    public static NBTTagCompound initTag(@Nonnull ItemStack stack) {
        var tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        return tag;
    }
}
