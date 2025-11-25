//? if neoforge {
package me.clefal.tooltips_core.loaders.neoforge;

import com.mojang.logging.LogUtils;
import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsBlocker;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsListener;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsMovementHandler;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsRecorder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(TooltipsCore.MOD_ID)
public class NeoforgeEntrypoint {
    private static final Logger LOGGER = LogUtils.getLogger();

    public NeoforgeEntrypoint(IEventBus bus) {
        TooltipsCore.clientBus.register(new TooltipsListener());
        NeoForge.EVENT_BUS.addListener(TooltipsRecorder::OnScreenPre);
        NeoForge.EVENT_BUS.register(new TooltipsMovementHandler());
        NeoForge.EVENT_BUS.register(new TooltipsBlocker());
    }
}
//?}
