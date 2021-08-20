package eu.asangarin.monhun.item;

import eu.asangarin.monhun.client.dynamic.MHCachedItemDisplay;
import eu.asangarin.monhun.client.dynamic.MHItemDisplayManager;
import eu.asangarin.monhun.dynamic.MHCachedItemData;
import eu.asangarin.monhun.dynamic.MHItemDataManager;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MHDynamicItem extends MHBaseItem {
	public MHDynamicItem() {
		super(MHItemTexture.QUESTION);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return getDisplay(stack).getTranslationKey();
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		for (Map.Entry<String, MHCachedItemDisplay> entry : MHItemDisplayManager.getAll())
			if (group == ItemGroup.SEARCH || group == entry.getValue().getGroup().getItemGroup()) stacks.add(withNBT(entry.getKey()));
	}

	public static MHCachedItemDisplay getDisplay(ItemStack stack) {
		return MHItemDisplayManager.getDisplay(getDynamicID(stack));
	}

	private MHCachedItemData getData(ItemStack stack) {
		return MHItemDataManager.getData(getDynamicID(stack));
	}

	@SuppressWarnings("ConstantConditions")
	private static String getDynamicID(ItemStack stack) {
		if (stack.hasNbt() && stack.getNbt().contains("dynamic_id")) return stack.getNbt().getString("dynamic_id");
		return "invalid_display";
	}

	@Override
	protected MHRarity getMHRarity(ItemStack stack) {
		return getDisplay(stack).getRarity();
	}

	@SuppressWarnings("ConstantConditions")
	public static ItemStack withNBT(String key) {
		ItemStack stack = new ItemStack(MHItems.DYNAMIC_ITEM);
		NbtCompound nbt = stack.hasNbt() ? stack.getNbt() : new NbtCompound();
		nbt.putString("dynamic_id", key);
		stack.setNbt(nbt);
		return stack;
	}

	@Override
	public int getColor(ItemStack stack) {
		return getDisplay(stack).getColor();
	}

	@Override
	public boolean shouldRegisterModel() {
		return false;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		if (isAccount(stack)) tooltip.add(new TranslatableText("item.monhun.account.desc").formatted(Formatting.YELLOW));
	}

	public boolean isAccount(ItemStack stack) {
		return getData(stack).isAccount();
	}

	public int getPointsValue(ItemStack stack) {
		return getData(stack).getSellValue();
	}

	public int getStackSize(ItemStack stack) {
		return getData(stack).getStackSize();
	}
}
