package dev.su5ed.sinytra.connectorextras.reibridge;

import com.google.common.base.Suppliers;
import me.shedaniel.rei.fabric.PluginDetectorImpl;
import me.shedaniel.rei.impl.init.PluginDetector;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod(REIBridge.MODID)
public class REIBridge {
    public static final String MODID = "connectorextras_rei_bridge";
    public static final Supplier<PluginDetector> FABRIC_PLUGIN_DETECTOR = Suppliers.memoize(PluginDetectorImpl::new);
}
