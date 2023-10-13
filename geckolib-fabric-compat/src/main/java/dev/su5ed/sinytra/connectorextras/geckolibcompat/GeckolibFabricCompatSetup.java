package dev.su5ed.sinytra.connectorextras.geckolibcompat;

import net.minecraft.Util;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.RenderProvider;

import java.util.function.Function;

@SuppressWarnings("unused")
public final class GeckolibFabricCompatSetup {
    private static final Function<ItemLike, RenderProvider> RENDERERS = Util.memoize(item -> {
        IClientItemExtensions extensions = IClientItemExtensions.of(item.asItem());
        return extensions != IClientItemExtensions.DEFAULT ? new ForgeRenderProvider(extensions) : null;
    });

    @Nullable
    public static RenderProvider get(ItemLike item) {
        return RENDERERS.apply(item);
    }

    private GeckolibFabricCompatSetup() {}
}
