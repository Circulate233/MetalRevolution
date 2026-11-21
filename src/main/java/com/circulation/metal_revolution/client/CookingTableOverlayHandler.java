package com.circulation.metal_revolution.client;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.IRecipeHandler;

public final class CookingTableOverlayHandler extends DefaultOverlayHandler {

    public CookingTableOverlayHandler() {
        super(0, 0);
    }

    @Override
    public int transferRecipe(GuiContainer gui, IRecipeHandler handler, int recipeIndex, int multiplier) {
        var item = clearIngredients(gui, (ItemStack) null);
        try {
            return super.transferRecipe(gui, handler, recipeIndex, multiplier);
        } finally {
            clearIngredients(gui, item);
        }
    }

    private ItemStack clearIngredients(GuiContainer gui, ItemStack s) {
        for (Slot slot : gui.inventorySlots.inventorySlots) {
            if (slot.xDisplayPosition == 37 && slot.yDisplayPosition == 36) {
                var old = slot.getStack();
                slot.inventory.setInventorySlotContents(slot.getSlotIndex(), s);
                return old;
            }
        }

        return null;
    }

    @Override
    public Slot[][] mapIngredSlots(GuiContainer gui, List<PositionedStack> ingredients) {
        Slot[][] map = new Slot[6][];
        for (var slot : gui.inventorySlots.inventorySlots) {
            int offset = slot.xDisplayPosition - 19;
            if (slot.yDisplayPosition == 13 && offset >= 0 && offset < 5 * 18 && offset % 18 == 0) {
                map[offset / 18 + 1] = new Slot[] { slot };
            }
            if (slot.yDisplayPosition == 36 && slot.xDisplayPosition == 19) {
                map[0] = new Slot[] { slot };
            }
        }
        return map;
    }
}
