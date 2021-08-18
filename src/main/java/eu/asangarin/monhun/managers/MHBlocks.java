package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.MHBlockItem;
import eu.asangarin.monhun.block.MHBoxBlock;
import eu.asangarin.monhun.block.MHBugBlock;
import eu.asangarin.monhun.block.MHInventoryBoxBlock;
import eu.asangarin.monhun.block.MHOreBlock;
import eu.asangarin.monhun.block.entity.MHBugBlockEntity;
import eu.asangarin.monhun.block.entity.MHItemBoxBlockEntity;
import eu.asangarin.monhun.block.entity.MHOreBlockEntity;
import eu.asangarin.monhun.block.entity.MHSupplyBoxBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

public class MHBlocks {
	public static final Material RESOURCE = new Material(MapColor.GOLD, false, false, true, false, false, false, PistonBehavior.BLOCK);

	public static final Block DELIVERY_BOX = new MHBoxBlock();
	public static final Block SUPPLY_BOX = new MHInventoryBoxBlock(false);
	public static final Block ITEM_BOX = new MHInventoryBoxBlock(true);
	public static final MHBlockItem DELIVERY_BOX_ITEM = new MHBlockItem(DELIVERY_BOX);
	public static final MHBlockItem SUPPLY_BOX_ITEM = new MHBlockItem(SUPPLY_BOX);
	public static final MHBlockItem ITEM_BOX_ITEM = new MHBlockItem(ITEM_BOX);
	public static BlockEntityType<MHSupplyBoxBlockEntity> SUPPLY_BOX_ENTITY;
	public static BlockEntityType<MHItemBoxBlockEntity> ITEM_BOX_ENTITY;

	public static final Block ORE_BLOCK = new MHOreBlock();
	public static final Block BUG_BLOCK = new MHBugBlock();
	public static final MHBlockItem ORE_BLOCK_ITEM = new MHBlockItem(ORE_BLOCK);
	public static final MHBlockItem BUG_BLOCK_ITEM = new MHBlockItem(BUG_BLOCK);
	public static BlockEntityType<MHBugBlockEntity> BUG_BLOCK_ENTITY;
	public static BlockEntityType<MHOreBlockEntity> ORE_BLOCK_ENTITY;

	public static void onInitialize() {
		for (Field f : MHBlocks.class.getDeclaredFields()) {
			try {
				Identifier id = MonHun.i(f.getName().toLowerCase());
				if (Block.class.isAssignableFrom(f.getType())) {
					Registry.register(Registry.BLOCK, id, (Block) f.get(null));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		Registry.register(Registry.ITEM, MonHun.i("delivery_box"), DELIVERY_BOX_ITEM);
		Registry.register(Registry.ITEM, MonHun.i("supply_box"), SUPPLY_BOX_ITEM);
		Registry.register(Registry.ITEM, MonHun.i("item_box"), ITEM_BOX_ITEM);
		Registry.register(Registry.ITEM, MonHun.i("ore_block"), ORE_BLOCK_ITEM);
		Registry.register(Registry.ITEM, MonHun.i("bug_block"), BUG_BLOCK_ITEM);

		SUPPLY_BOX_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MonHun.i("supply_box_entity"),
				FabricBlockEntityTypeBuilder.create(MHSupplyBoxBlockEntity::new, SUPPLY_BOX).build(null));
		ITEM_BOX_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MonHun.i("item_box_entity"),
				FabricBlockEntityTypeBuilder.create(MHItemBoxBlockEntity::new, ITEM_BOX).build(null));
		BUG_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MonHun.i("bug_block_entity"),
				FabricBlockEntityTypeBuilder.create(MHBugBlockEntity::new, BUG_BLOCK).build(null));
		ORE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MonHun.i("ore_block_entity"),
				FabricBlockEntityTypeBuilder.create(MHOreBlockEntity::new, ORE_BLOCK).build(null));
	}
}
