package me.clefal.tooltips_core.config;

import com.clefal.nirvana_lib.utils.ResourceLocationUtils;
import me.clefal.tooltips_core.TooltipsCore;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class TooltipsCoreConfig extends Config {
    public static TooltipsCoreConfig tooltipsCoreConfig = ConfigApiJava.registerAndLoadConfig(TooltipsCoreConfig::new, RegisterType.CLIENT);

    public boolean enable_reveal_on_default = true;

    public TooltipsCoreConfig() {
        super(ResourceLocationUtils.make(TooltipsCore.MOD_ID, "config"));
    }

    public static void init() {
        TooltipsCore.LOGGER.info("Initiating Tooltips Core Config!");
    }
}