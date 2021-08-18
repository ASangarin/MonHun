package eu.asangarin.monhun.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import eu.asangarin.monhun.config.MHConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class MHModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(MHConfig.class, parent).get();
	}
}
