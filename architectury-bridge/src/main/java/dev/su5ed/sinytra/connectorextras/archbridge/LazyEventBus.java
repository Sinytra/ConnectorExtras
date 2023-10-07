package dev.su5ed.sinytra.connectorextras.archbridge;

import net.minecraftforge.eventbus.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LazyEventBus implements IEventBus {
    private final List<Object> toRegister = new ArrayList<>();
    private IEventBus bus;

    public void setBus(IEventBus bus) {
        if (this.bus != null) {
            throw new IllegalStateException();
        }
        this.bus = bus;
        this.toRegister.forEach(bus::register);
        this.toRegister.clear();
    }

    @Override
    public void register(Object target) {
        if (this.bus != null) {
            this.bus.register(target);
        } else {
            this.toRegister.add(target);
        }
    }

    @Override
    public <T extends Event> void addListener(Consumer<T> consumer) {
        if (this.bus != null) {
            this.bus.addListener(consumer);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, Consumer<T> consumer) {
        if (this.bus != null) {
            this.bus.addListener(priority, consumer);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, Consumer<T> consumer) {
        if (this.bus != null) {
            this.bus.addListener(priority, receiveCancelled, consumer);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer) {
        if (this.bus != null) {
            this.bus.addListener(priority, receiveCancelled, eventType, consumer);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, Consumer<T> consumer) {
        if (this.bus != null) {
            this.bus.addGenericListener(genericClassFilter, consumer);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, Consumer<T> consumer) {
        if (this.bus != null) {
            this.bus.addGenericListener(genericClassFilter, priority, consumer);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Consumer<T> consumer) {
        if (this.bus != null) {
            this.bus.addGenericListener(genericClassFilter, priority, receiveCancelled, consumer);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer) {
        if (this.bus != null) {
            this.bus.addGenericListener(genericClassFilter, priority, receiveCancelled, eventType, consumer);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void unregister(Object object) {
        if (this.bus != null) {
            this.bus.unregister(object);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean post(Event event) {
        if (this.bus != null) {
            return this.bus.post(event);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean post(Event event, IEventBusInvokeDispatcher wrapper) {
        if (this.bus != null) {
            return this.bus.post(event, wrapper);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void shutdown() {
        if (this.bus != null) {
            this.bus.shutdown();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void start() {
        if (this.bus != null) {
            this.bus.start();
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
