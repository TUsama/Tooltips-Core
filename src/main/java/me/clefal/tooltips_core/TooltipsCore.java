package me.clefal.tooltips_core;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.BusBuilder;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.IEventBus;
import com.clefal.nirvana_lib.utils.ResourceLocationUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class TooltipsCore {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "tooltips_core";
    public static final IEventBus clientBus = BusBuilder.builder().setExceptionHandler((iEventBus, event, eventListeners, i, throwable) -> {
        try {
            throw throwable;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }).build();

    public static void initialize() {
    }

    public static ResourceLocation gui(String path){
        return ResourceLocationUtils.make(MOD_ID, "textures/gui/" + path + ".png");
    }
}
