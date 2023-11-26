package dev.su5ed.sinytra.connectorextras.forgeconfigapiport;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigPaths;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ForgeConfigPathsImpl implements ForgeConfigPaths {
    @Override
    public Path getClientConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Path getCommonConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Path getServerConfigDirectory(MinecraftServer server) {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean forceGlobalServerConfigs() {
        return false;
    }

    @Override
    public Path getDefaultConfigsDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
