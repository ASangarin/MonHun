package eu.asangarin.monhun.block.entity.gather;

import eu.asangarin.monhun.managers.MHBlocks;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Objects;
import java.util.Random;

public class MHBugBlockEntity extends MHAbstractGatheringBlockEntity implements IAnimatable {
	private static final AnimationBuilder FLY_ANIMATION = new AnimationBuilder().addAnimation("animation.flying_bug.fly", true);
	private static final AnimationBuilder PATH_ANIMATION = new AnimationBuilder().addAnimation("animation.flying_bug.path", true);
	private static final Random random = new Random();
	private final AnimationFactory factory = new AnimationFactory(this);
	private int animationStart;
	@Getter
	private int hurtTime, age = 0;
	private int health = 4;

	public MHBugBlockEntity(BlockPos pos, BlockState state) {
		super(MHBlocks.BUG_BLOCK_ENTITY, pos, state);
		animationStart = random.nextInt(60);
	}

	public static void tick(MHBugBlockEntity blockEntity) {
		blockEntity.age++;
		if (blockEntity.hurtTime > 0) blockEntity.hurtTime--;
	}

	protected double getShinyY() {
		return Objects.requireNonNull(world).random.nextDouble() / 2;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		health = nbt.getInt("health");
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("health", health);
		return nbt;
	}

	public boolean damage() {
		health--;
		markDirty();
		return health <= 0;
	}

	public void setHurt() {
		hurtTime = 10;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "fly-controller", 0, this::flyAnimationPredicate));
		data.addAnimationController(new AnimationController<>(this, "path-controller", 0, this::pathAnimationPredicate));
	}

	private <E extends IAnimatable> PlayState flyAnimationPredicate(AnimationEvent<E> event) {
		if (event.animationTick < animationStart) return PlayState.STOP;
		event.getController().setAnimation(FLY_ANIMATION);
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState pathAnimationPredicate(AnimationEvent<E> event) {
		if (event.animationTick < animationStart) return PlayState.STOP;
		event.getController().setAnimation(PATH_ANIMATION);
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public void cleanAnimation() {
		animationStart = 0;
	}
}
