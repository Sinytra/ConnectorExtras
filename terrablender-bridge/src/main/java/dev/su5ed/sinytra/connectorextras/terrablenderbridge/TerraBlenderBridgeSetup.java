package dev.su5ed.sinytra.connectorextras.terrablenderbridge;

import net.fabricmc.loader.api.FabricLoader;
import terrablender.api.TerraBlenderApi;

public final class TerraBlenderBridgeSetup {

    public static void init() {
        FabricLoader.getInstance().getEntrypointContainers("terrablender", TerraBlenderApi.class).forEach(entrypoint -> {
            TerraBlenderApi api = entrypoint.getEntrypoint();
            api.onTerraBlenderInitialized();
        });
    }

    private TerraBlenderBridgeSetup() {}
}
