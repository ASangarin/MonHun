package eu.asangarin.monhun.client.dynamic;

import eu.asangarin.monhun.MonHun;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemGroup;

@Getter
@RequiredArgsConstructor
public enum MHDisplayGroup {
	RESOURCE(MonHun.RESOURCE_GROUP), ACCOUNT(MonHun.ACCOUNT_GROUP);

	private final ItemGroup itemGroup;

	public static MHDisplayGroup getFromString(String value) {
		for (MHDisplayGroup group : MHDisplayGroup.values())
			if (group.name().equalsIgnoreCase(value)) return group;
		return MHDisplayGroup.RESOURCE;
	}
}
