package eu.asangarin.monhun.block.entity;

import eu.asangarin.monhun.gui.SupplyBoxScreenHandler;
import eu.asangarin.monhun.managers.MHBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MHSupplyBoxBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {
	private final DefaultedList<ItemStack> items = DefaultedList.ofSize(54, ItemStack.EMPTY);

	public MHSupplyBoxBlockEntity(BlockPos pos, BlockState state) {
		super(MHBlocks.SUPPLY_BOX_ENTITY, pos, state);
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < size(); i++) {
			ItemStack stack = getStack(i);
			if (!stack.isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return items.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int count) {
		ItemStack result = Inventories.splitStack(items, slot, count);
		if (!result.isEmpty()) markDirty();
		return result;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(items, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		items.set(slot, stack);
		if (stack.getCount() > getMaxCountPerStack()) stack.setCount(getMaxCountPerStack());
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, items);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		Inventories.writeNbt(nbt, items);
		return super.writeNbt(nbt);
	}

	@Override
	public Text getDisplayName() {
		return new TranslatableText(getCachedState().getBlock().getTranslationKey()).formatted(Formatting.WHITE);
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new SupplyBoxScreenHandler(syncId, inv, this);
	}
}
