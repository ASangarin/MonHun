package eu.asangarin.monhun.item;

import eu.asangarin.monhun.entity.MHThrownItemEntity;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import lombok.Getter;
import net.minecraft.entity.Entity;

public class MHThrownItem extends MHBaseItem {
	@Getter
	private final ThrowableAction action;

	public MHThrownItem(MHItemTexture texture, int color, ThrowableAction action) {
		super(texture, color);
		this.action = action;
	}

	public void action(MHThrownItemEntity thrown, Entity entity, Entity owner) {
		action.apply(thrown, entity, owner);
	}

	@FunctionalInterface
	public interface ThrowableAction {
		void apply(MHThrownItemEntity thrown, Entity entity, Entity owner);
	}
}
