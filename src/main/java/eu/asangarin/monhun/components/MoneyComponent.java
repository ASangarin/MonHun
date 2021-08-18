package eu.asangarin.monhun.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class MoneyComponent implements Component, AutoSyncedComponent {
	@Getter
	private int zenny, points;

	@Override
	public void readFromNbt(NbtCompound tag) {
		zenny = tag.getInt("zenny");
		points = tag.getInt("points");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("zenny", zenny);
		tag.putInt("points", points);
	}

	private int calculate(int value) {
		return Math.min(9999999, Math.max(0, value));
	}

	public void setZenny(int value, PlayerEntity player) {
		zenny = calculate(value);
		MHComponents.MONEY.sync(player);
	}

	public void setPoints(int value, PlayerEntity player) {
		points = calculate(value);
		MHComponents.MONEY.sync(player);
	}

	public void addZenny(int value, PlayerEntity player) {
		setZenny(getZenny() + value, player);
	}

	public void addPoints(int value, PlayerEntity player) {
		setPoints(getPoints() + value, player);
	}

	public boolean hasZenny(int value) {
		return points >= value;
	}

	public boolean hasPoints(int value) {
		return points >= value;
	}
}