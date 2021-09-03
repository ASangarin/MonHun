package eu.asangarin.monhun.item;

import eu.asangarin.monhun.util.ItemColors;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Getter;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MHBaseItem extends Item {
	protected final MHRarity rarity;
	private final int color;
	@Getter
	private final MHItemTexture texture;
	@Getter
	private final boolean dynamicTexture;


	public MHBaseItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, ItemGroup group, boolean dynamicTexture) {
		super(new FabricItemSettings().maxCount(maxCount).group(group));
		this.rarity = rarity;
		this.color = color;
		this.texture = texture;
		this.dynamicTexture = dynamicTexture;
	}

	public MHBaseItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, ItemGroup group) {
		this(texture, color, rarity, maxCount, group, true);
	}

	public MHBaseItem(MHItemTexture texture, int color, boolean dynamicTexture) {
		super(new FabricItemSettings().maxCount(1));
		this.rarity = MHRarity.RARE_0;
		this.color = color;
		this.texture = texture;
		this.dynamicTexture = dynamicTexture;
	}

	public MHBaseItem(MHItemTexture texture, int color) {
		this(texture, color, true);
	}

	public MHBaseItem(MHItemTexture texture) {
		this(texture, ItemColors.WHITE, true);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(getMHRarity(stack).asText().formatted(Formatting.BOLD));
		String description = new TranslatableText(getTranslationKey(stack) + ".desc").getString();
		for (String s : description.split("\\n"))
			tooltip.add(new LiteralText(s).formatted(Formatting.GRAY));
		if (this instanceof ISupplyItem) {
			tooltip.add(new TranslatableText("item.monhun.supply.desc").formatted(Formatting.YELLOW));
			tooltip.add(LiteralText.EMPTY);
			tooltip.add(new LiteralText("Work in Progress!").formatted(Formatting.BOLD, Formatting.RED));
		}
	}

	protected MHRarity getMHRarity(ItemStack stack) {
		return rarity;
	}

	public int getColor(ItemStack stack) {
		return color;
	}

	public boolean shouldRegisterModel() {
		return true;
	}

	public interface ISupplyItem {
	}
}
