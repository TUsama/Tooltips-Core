//? if forge {
/*package me.clefal.tooltips_core.loaders.forge;

import com.mojang.logging.LogUtils;
import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsListener;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsRecorder;
import me.clefal.tooltips_core.enlighten.handlers.VanillaTooltipsHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(TooltipsCore.MOD_ID)
public class ForgeEntrypoint {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ForgeEntrypoint() {
        TooltipsCore.clientBus.register(new TooltipsListener());
        MinecraftForge.EVENT_BUS.register(new VanillaTooltipsHandler());
    }
}
*///?}
