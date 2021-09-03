package eu.asangarin.monhun.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

public class MHJokeWeaponItem extends MHBaseItem {
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public MHJokeWeaponItem() {
		this(MHItemTexture.QUESTION, false);
	}

	public MHJokeWeaponItem(MHItemTexture texture) {
		this(texture, true);
	}

	public MHJokeWeaponItem(MHItemTexture texture, boolean dynamicTexture) {
		super(texture, 0xFFFFFF, MHRarity.RARE_1, 1, MonHun.EXTRA_GROUP, dynamicTexture);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 3, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}
}
