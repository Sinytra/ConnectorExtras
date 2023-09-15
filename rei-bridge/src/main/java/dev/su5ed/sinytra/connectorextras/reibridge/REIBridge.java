package dev.su5ed.sinytra.connectorextras.reibridge;

import com.google.common.base.Suppliers;
import me.shedaniel.rei.fabric.PluginDetectorImpl;
import me.shedaniel.rei.impl.init.PluginDetector;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod("reibridge")
public class REIBridge {
    public static final Supplier<PluginDetector> FABRIC_PLUGIN_DETECTOR = Suppliers.memoize(PluginDetectorImpl::new);
}
