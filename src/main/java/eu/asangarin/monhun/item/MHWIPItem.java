package eu.asangarin.monhun.item;

import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MHWIPItem extends MHBaseItem {
	public MHWIPItem(MHItemTexture textures, int color, MHRarity rarity, int maxCount, ItemGroup group) {
		super(textures, color, rarity, maxCount, group);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(LiteralText.EMPTY);
		tooltip.add(new LiteralText("Work in Progress!").formatted(Formatting.BOLD, Formatting.RED));
	}
}
