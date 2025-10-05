package com.circulation.metal_revolution.client;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.IRecipeHandler;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CookingTableOverlayHandler extends DefaultOverlayHandler {

    public CookingTableOverlayHandler() {
        super(0, 0);
    }

    @Override
    public int transferRecipe(GuiContainer gui, IRecipeHandler handler, int recipeIndex, int multiplier) {
        var item = clearIngredients(gui, null);
        int out = super.transferRecipe(gui, handler, recipeIndex, multiplier);
        clearIngredients(gui, item);
        return out;
    }

    private ItemStack clearIngredients(GuiContainer gui, ItemStack s) {
        final EntityClientPlayerMP thePlayer = NEIClientUtils.mc().thePlayer;

        for (Slot slot : (Collection<Slot>) gui.inventorySlots.inventorySlots) {
            if (slot.xDisplayPosition == 37 && slot.yDisplayPosition == 36) {
                var old = slot.getStack();
                slot.inventory.setInventorySlotContents(slot.getSlotIndex(), s);
                return old;
            }
        }

        return null;
    }

    private List<DistributedIngred> getPermutationIngredients(List<PositionedStack> ingredients) {
        ArrayList<DistributedIngred> ingredStacks = new ArrayList<>();
        for (PositionedStack posstack : ingredients) {
            for (ItemStack pstack : posstack.items) {
                DistributedIngred istack = findIngred(ingredStacks, pstack);
                if (istack == null) ingredStacks.add(istack = new DistributedIngred(pstack));
                istack.recipeAmount += pstack.stackSize;
            }
        }
        return ingredStacks;
    }

    @Override
    public Slot[][] mapIngredSlots(GuiContainer gui, List<PositionedStack> ingredients) {
        Slot[][] map = new Slot[6][];
        for (var slot : (List<Slot>) gui.inventorySlots.inventorySlots) {
            for (int i = 0; i < 5; ++i) {
                if (slot.yDisplayPosition == 13 && slot.xDisplayPosition == 19 + i * 18) {
                    map[i + 1] = new Slot[]{slot};
                    break;
                }
            }
            if (slot.yDisplayPosition == 36 && slot.xDisplayPosition == 19) {
                map[0] = new Slot[]{slot};
            }
        }
        return map;
    }
}
