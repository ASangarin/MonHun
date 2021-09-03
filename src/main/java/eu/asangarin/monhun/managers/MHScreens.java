package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.gui.ItemBoxScreenHandler;
import eu.asangarin.monhun.gui.MHButtonListScreenHandler;
import eu.asangarin.monhun.gui.SupplyBoxScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class MHScreens {
	public static final ScreenHandlerType<SupplyBoxScreenHandler> SUPPLY_BOX_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(
			MonHun.i("supply_box_screenhandler"), SupplyBoxScreenHandler::new);
	public static final ScreenHandlerType<ItemBoxScreenHandler> ITEM_BOX_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(
			MonHun.i("item_box_screenhandler"), ItemBoxScreenHandler::new);
	public static final ScreenHandlerType<MHButtonListScreenHandler> BUTTON_LIST_SCREEN = ScreenHandlerRegistry.registerSimple(
			MonHun.i("button_list_screenhandler"), (id, inv) -> new MHButtonListScreenHandler(id, MHButtonListScreenHandler.TitleBoxType.SMITHY));
}
