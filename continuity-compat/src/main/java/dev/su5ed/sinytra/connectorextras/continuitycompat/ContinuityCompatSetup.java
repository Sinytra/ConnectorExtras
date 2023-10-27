package dev.su5ed.sinytra.connectorextras.continuitycompat;

import dev.su5ed.sinytra.connectorextras.continuitycompat.mixin.ModelWrappingHandlerAccessor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ContinuityCompatSetup {

    public static void onClientSetup(FMLClientSetupEvent event) {
        ModelWrappingHandlerAccessor.setBLOCK_STATE_MODEL_IDS(ModelWrappingHandlerAccessor.callCreateBlockStateModelIdMap());
    }

    private ContinuityCompatSetup() {}
}
