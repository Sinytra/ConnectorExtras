package dev.su5ed.sinytra.connectorextras.pehkuibridge.mixin;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.api.ScaleEventCallback;
import virtuoel.pehkui.api.ScaleType;

@Mixin(ScaleType.class)
public class ScaleTypeMixin {
    @Unique
    private Event<ScaleEventCallback> connectorextras_scaleTypeChangedEvent;
    @Unique
    private Event<ScaleEventCallback> connectorextras_preTickChangedEvent = connector_createScaleEvent();
    @Unique
    private Event<ScaleEventCallback> connectorextras_postTickChangedEvent = connector_createScaleEvent();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(ScaleType.Builder builder, CallbackInfo ci) {
        this.connectorextras_scaleTypeChangedEvent = connector_createScaleEvent();
        this.connectorextras_preTickChangedEvent = connector_createScaleEvent();
        this.connectorextras_postTickChangedEvent = connector_createScaleEvent();
        ((ScaleType) (Object) this).getScaleChangedEvent().add(data -> connectorextras_scaleTypeChangedEvent.invoker().onEvent(data));
        ((ScaleType) (Object) this).getPreTickEvent().add(data -> connectorextras_preTickChangedEvent.invoker().onEvent(data));
        ((ScaleType) (Object) this).getPostTickEvent().add(data -> connectorextras_postTickChangedEvent.invoker().onEvent(data));
    }

    @Unique
    public Event<ScaleEventCallback> getScaleChangedEvent() {
        return this.connectorextras_scaleTypeChangedEvent;
    }

    @Unique
    public Event<ScaleEventCallback> getPreTickEvent() {
        return this.connectorextras_preTickChangedEvent;
    }

    @Unique
    public Event<ScaleEventCallback> getPostTickEvent() {
        return this.connectorextras_postTickChangedEvent;
    }

    @Unique
    private static Event<ScaleEventCallback> connector_createScaleEvent() {
        return EventFactory.createArrayBacked(
            ScaleEventCallback.class,
            data -> {
            },
            callbacks -> data ->
            {
                for (ScaleEventCallback callback : callbacks) {
                    callback.onEvent(data);
                }
            }
        );
    }
}
