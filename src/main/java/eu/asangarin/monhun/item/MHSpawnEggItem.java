package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.enums.MHMonsterClass;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Getter;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MHSpawnEggItem extends SpawnEggItem {
	@Getter
	private final MHMonsterClass monsterClass;
	private final String monsterTranslationKey;

	public MHSpawnEggItem(EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor, MHMonsterClass monsterClass) {
		super(type, primaryColor, secondaryColor, new FabricItemSettings().group(MonHun.SPAWN_EGG_GROUP));
		this.monsterClass = monsterClass;
		this.monsterTranslationKey = type.getTranslationKey();
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(MHRarity.RARE_7.asText().formatted(Formatting.BOLD));
	}

	@Override
	public Text getName(ItemStack stack) {
		return new TranslatableText(this.getTranslationKey(stack), new TranslatableText(monsterTranslationKey));
	}

	@Override
	public Text getName() {
		return new TranslatableText(this.getTranslationKey(), new TranslatableText(monsterTranslationKey));
	}

	@Override
	public String getTranslationKey() {
		return "item.monhun.spawn_egg";
	}
}
