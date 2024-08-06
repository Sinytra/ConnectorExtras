package me.shedaniel.rei.fabric;

import com.google.common.base.Suppliers;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.common.plugins.PluginManager;
import me.shedaniel.rei.api.common.plugins.PluginView;
import me.shedaniel.rei.impl.ClientInternals;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class ClientPluginDetector {

    public static void detectClientPlugins() {
        PluginDetectorImpl.loadPlugin(REIClientPlugin.class, ((PluginView<REIClientPlugin>) PluginManager.getClientInstance())::registerPlugin);
        Supplier<Method> method = Suppliers.memoize(() -> {
            String methodName = FabricLoader.getInstance().isDevelopmentEnvironment() ? FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_332", "method_51442", "(Ljava/util/List;Lnet/minecraft/class_5632;)V")
                : "lambda$renderTooltip$3";
            try {
                Method declaredMethod = GuiGraphics.class.getDeclaredMethod(methodName, List.class, TooltipComponent.class);
                if (declaredMethod != null) declaredMethod.setAccessible(true);
                return declaredMethod;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        ClientInternals.attachInstance((BiConsumer<List<ClientTooltipComponent>, TooltipComponent>) (lines, component) -> {
            try {
                method.get().invoke(null, lines, component);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }, "clientTooltipComponentProvider");
    }
}
