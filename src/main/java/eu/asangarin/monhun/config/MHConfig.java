package eu.asangarin.monhun.config;

import lombok.Getter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = "monhun")
@Config.Gui.Background("monhun:textures/background.png")
@Getter
public class MHConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("main")
	@ConfigEntry.Gui.TransitiveObject
	MainConfig main = new MainConfig();

	@ConfigEntry.Category("client")
	@ConfigEntry.Gui.TransitiveObject
	ClientConfig client = new ClientConfig();

	@Config(name = "main")
	@Getter
	public static class MainConfig implements ConfigData {
	}

	@Config(name = "client")
	@Getter
	public static class ClientConfig implements ConfigData {
		@ConfigEntry.Gui.CollapsibleObject
		Rendering rendering = new Rendering();

		@ConfigEntry.Gui.CollapsibleObject
		Items items = new Items();

		@Getter
		public static class Rendering {
			boolean useCustomSlotHighlight = true;
			ZennyRenderType renderZennyAndPoints = ZennyRenderType.ALWAYS;
		}

		@Getter
		public static class Items {
			@ConfigEntry.BoundedDiscrete(max = 200L)
			double binocularsMaxRange;
		}
	}
}
