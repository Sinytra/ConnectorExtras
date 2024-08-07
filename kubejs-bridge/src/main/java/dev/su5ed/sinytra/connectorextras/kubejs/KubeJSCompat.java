package dev.su5ed.sinytra.connectorextras.kubejs;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.LoadingModList;

@Mod(KubeJSCompat.MODID)
public class KubeJSCompat {
    public static final String MODID = "connectorextras_kubejs_bridge";
    private static final String KUBEJS_MODID = "kubejs";
    private static final String CONNECTOR_MODID = "connectormod";

    public static boolean isEnabled() {
        return LoadingModList.get().getModFileById(KUBEJS_MODID) != null && LoadingModList.get().getModFileById(CONNECTOR_MODID) != null;
    }
}
