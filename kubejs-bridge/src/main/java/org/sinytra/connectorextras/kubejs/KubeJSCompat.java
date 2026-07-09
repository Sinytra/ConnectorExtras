package org.sinytra.connectorextras.kubejs;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;

@Mod(KubeJSCompat.MODID)
public class KubeJSCompat {
    public static final String MODID = "connectorextras_kubejs_bridge";
    private static final String KUBEJS_MODID = "kubejs";
    private static final String CONNECTOR_MODID = "connector";

    public static boolean isEnabled() {
        LoadingModList list = FMLLoader.getCurrent().getLoadingModList();
        return list.getModFileById(KUBEJS_MODID) != null;
    }
}
