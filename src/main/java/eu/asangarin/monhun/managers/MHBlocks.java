package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.MHBlockItem;
import eu.asangarin.monhun.block.MHBoxBlock;
import eu.asangarin.monhun.block.MHInventoryBoxBlock;
import eu.asangarin.monhun.block.entity.MHItemBoxBlockEntity;
import eu.asangarin.monhun.block.entity.MHSupplyBoxBlockEntity;
import eu.asangarin.monhun.block.entity.gather.MHBugBlockEntity;
import eu.asangarin.monhun.block.entity.gather.MHGatheringBlockEntity;
import eu.asangarin.monhun.block.gather.MHBottomPlantBlock;
import eu.asangarin.monhun.block.gather.MHBugBlock;
import eu.asangarin.monhun.block.gather.MHHoneyBlock;
import eu.asangarin.monhun.block.gather.MHMushroomBlock;
import eu.asangarin.monhun.block.gather.MHOreBlock;
import eu.asangarin.monhun.block.gather.MHSpiderWebBlock;
import eu.asangarin.monhun.block.gather.MHTopPlantBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
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
	public static final Block MUSHROOM_BLOCK = new MHMushroomBlock();
	public static final Block HONEY_BLOCK = new MHHoneyBlock();
	public static final Block TOP_PLANT_BLOCK = new MHTopPlantBlock(), BOTTOM_PLANT_BLOCK = new MHBottomPlantBlock();
	public static final Block SPIDER_WEB_BLOCK = new MHSpiderWebBlock();
	public static final MHBlockItem ORE_BLOCK_ITEM = new MHBlockItem(ORE_BLOCK);
	public static final MHBlockItem BUG_BLOCK_ITEM = new MHBlockItem(BUG_BLOCK);
	public static final MHBlockItem MUSHROOM_BLOCK_ITEM = new MHBlockItem(MUSHROOM_BLOCK);
	public static final MHBlockItem HONEY_BLOCK_ITEM = new MHBlockItem(HONEY_BLOCK);
	public static final MHBlockItem PLANT_BLOCK_ITEM = new MHBlockItem(BOTTOM_PLANT_BLOCK);
	public static final MHBlockItem SPIDER_WEB_BLOCK_ITEM = new MHBlockItem(SPIDER_WEB_BLOCK);
	public static BlockEntityType<MHBugBlockEntity> BUG_BLOCK_ENTITY;

	public static BlockEntityType<MHGatheringBlockEntity> GATHERING_BLOCK_ENTITY;

	public static void registerBlockEntities() {
		SUPPLY_BOX_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MonHun.i("supply_box_entity"),
				FabricBlockEntityTypeBuilder.create(MHSupplyBoxBlockEntity::new, SUPPLY_BOX).build(null));
		ITEM_BOX_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MonHun.i("item_box_entity"),
				FabricBlockEntityTypeBuilder.create(MHItemBoxBlockEntity::new, ITEM_BOX).build(null));
		BUG_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MonHun.i("bug_block_entity"),
				FabricBlockEntityTypeBuilder.create(MHBugBlockEntity::new, BUG_BLOCK).build(null));
		GATHERING_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MonHun.i("ore_block_entity"),
				FabricBlockEntityTypeBuilder.create(MHGatheringBlockEntity::new, ORE_BLOCK, MUSHROOM_BLOCK, HONEY_BLOCK, TOP_PLANT_BLOCK,
						SPIDER_WEB_BLOCK).build(null));
	}
}
