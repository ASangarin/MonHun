package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.client.item.MHCachedResource;
import eu.asangarin.monhun.client.item.MHItemManager;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class MHResourceItem extends MHBaseItem {
	public MHResourceItem() {
		super(MHItemTexture.QUESTION, 0, MHRarity.RARE_1, 64, MonHun.RESOURCE_GROUP);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return getResource(stack).getTranslationKey();
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (!this.isIn(group)) return;
		for (String key : MHItemManager.getResourceKeys())
			stacks.add(withNBT(key));
	}

	@Override
	protected MHRarity getMHRarity(ItemStack stack) {
		return getResource(stack).getRarity();
	}

	@SuppressWarnings("ConstantConditions")
	public static ItemStack withNBT(String key) {
		ItemStack stack = new ItemStack(MHItems.RESOURCE_ITEM);
		NbtCompound nbt = stack.hasNbt() ? stack.getNbt() : new NbtCompound();
		nbt.putString("mh_resource", key);
		stack.setNbt(nbt);
		return stack;
	}

	@Override
	public int getColor(ItemStack stack) {
		return getResource(stack).getColor();
	}

	@Override
	public boolean shouldRegisterModel() {
		return false;
	}

	public static MHCachedResource getResource(ItemStack stack) {
		return MHItemManager.getResource(getResourceString(stack));
	}

	@SuppressWarnings("ConstantConditions")
	private static String getResourceString(ItemStack stack) {
		if (stack.hasNbt() && stack.getNbt().contains("mh_resource")) return stack.getNbt().getString("mh_resource");
		return "invalid_resource";
	}
}
