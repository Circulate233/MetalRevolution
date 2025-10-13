package com.circulation.metal_revolution.mixins.MMM.item;

import appeng.api.config.Actionable;
import appeng.api.networking.security.PlayerSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.data.IAEItemStack;
import appeng.tile.misc.TileInterface;
import appeng.util.item.AEItemStack;
import com.circulation.metal_revolution.utils.IInventoryUtils;
import com.circulation.metal_revolution.utils.TagUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import project.studio.manametalmod.items.ItemToolBackpackBase;

@Mixin(ItemToolBackpackBase.class)
public abstract class MixinItemToolBackpackBase extends Item {

    @Shadow(remap = false)
    public abstract int getSoltCount();

    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
    public void onItemRightClick(ItemStack item, World world, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        if (player.isSneaking()) {
            cir.setReturnValue(item);
        }
    }

    @Intrinsic
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
                             float hitX, float hitY, float hitZ) {
        if (!world.isRemote && player.isSneaking()) {
            return this.m$tryMoveItems(world, x, y, z, player.getHeldItem(), player);
        } else {
            return false;
        }
    }

    @Unique
    private boolean m$tryMoveItems(World world, int x, int y, int z, ItemStack stack, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof IInventory inv) {
            var bagInv = m$getInventory(stack);
            if (m$tryMoveItemsToAE(te, bagInv, player)) {
                this.m$saveInventory(stack, bagInv);
                return true;
            }

            for (int slot = 0; slot < bagInv.getSizeInventory(); slot++) {
                var item = bagInv.getStackInSlot(slot);
                if (item == null) continue;
                item = item.copy();
                for (int iSlot = 0; iSlot < inv.getSizeInventory(); iSlot++) {
                    item = IInventoryUtils.insertItem(inv, iSlot, item, false);
                    if (item == null) break;
                }
                if (item == null || bagInv.getStackInSlot(slot).stackSize != item.stackSize) {
                    bagInv.setInventorySlotContents(slot, item);
                }
            }
            this.m$saveInventory(stack, bagInv);
            return true;
        }
        return false;
    }

    @Unique
    private boolean m$tryMoveItemsToAE(TileEntity te, IInventory bagInv, EntityPlayer player) {
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

    @Unique
    protected IInventory m$getInventory(ItemStack stack) {
        InventoryBasic inv = new InventoryBasic(this.getUnlocalizedName(stack), false, this.getSoltCount());

        if (stack.hasTagCompound()) {
            NBTTagList list = stack.getTagCompound()
                .getTagList("Items", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound itemTag = list.getCompoundTagAt(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < inv.getSizeInventory()) {
                    inv.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(itemTag));
                }
            }
        }

        return inv;
    }

    @Unique
    protected void m$saveInventory(ItemStack stack, IInventory inv) {
        NBTTagList list = new NBTTagList();
        for (byte i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (s != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", i);
                s.writeToNBT(tag);
                list.appendTag(tag);
            }
        }
        TagUtils.initTag(stack).setTag("Items", list);
    }
}
