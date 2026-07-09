package org.sinytra.connectorextras.terrablenderbridge;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforgespi.locating.IModFile;
import org.sinytra.connectorextras.util.HackyModuleInjector;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(TerraBlenderBridge.MODID)
public class TerraBlenderBridge {
    public static final String MODID = "connectorextras_terrablender_bridge";
    private static final String TERRABLENDER_MODID = "terrablender";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TerraBlenderBridge(IEventBus bus) {
        LOGGER.info("Injecting TerraBlender API classes");
        try {
            if (injectTerrablender()) {
                bus.addListener(TerraBlenderBridge::onCommonSetup);   
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(TerraBlenderBridgeSetup::init);
    }

    private static boolean injectTerrablender() throws Throwable {
        LoadingModList list = FMLLoader.getCurrent().getLoadingModList();
        IModFile ownFile = list.getModFileById(MODID).getFile();
        Path relocated = getRelocatedPath(ownFile.getContents().getPrimaryPath());
        return HackyModuleInjector.injectModuleSource(TERRABLENDER_MODID, TERRABLENDER_MODID, relocated);
    }

    private static Path getRelocatedPath(Path root) throws IOException {
        if (Files.isDirectory(root)) {
            return root.resolve("relocated");
        }
        FileSystem fs = FileSystems.newFileSystem(root);
        return fs.getPath("relocated");
    }
}
