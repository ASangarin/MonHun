package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.ItemColors;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Getter;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MHAccountItem extends MHBaseItem {
	@Getter
	private final int value;

	public MHAccountItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, int value) {
		super(texture, color, rarity, maxCount, MonHun.ACCOUNT_GROUP);
		this.value = value;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.monhun.account.desc").formatted(Formatting.YELLOW));
	}
}
