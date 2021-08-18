package eu.asangarin.monhun.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.enums.MHWeaponType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class MHBlademasterItem extends MHBaseModelItem {
	protected static final UUID MOVE_SPEED_MODIFIER_ID = UUID.fromString("88B01EE1-40B5-4496-88EC-3477DC9AEF5A");
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	private final String model;

	public MHBlademasterItem(String model, MHWeaponType type, MHRarity rarity, double damage) {
		super(type, rarity, 1, MonHun.WEAPON_GROUP);
		this.model = model;
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", damage, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", type.getAttackSpeed(),
						EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED,
				new EntityAttributeModifier(MOVE_SPEED_MODIFIER_ID, "Weapon modifier", type.getMoveSpeed(),
						EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	@Override
	public String getResourcePath() {
		return "weapon/" + type.getName() + "/" + type.getId() + "_" + model;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(LiteralText.EMPTY);
		tooltip.add(new LiteralText("Work in Progress!").formatted(Formatting.BOLD, Formatting.RED));
	}
}
