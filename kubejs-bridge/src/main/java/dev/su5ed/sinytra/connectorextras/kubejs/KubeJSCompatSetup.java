package dev.su5ed.sinytra.connectorextras.kubejs;

import dev.architectury.platform.Mod;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import org.sinytra.connector.loader.ConnectorEarlyLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class KubeJSCompatSetup {
    public static List<String> loadedMods;
    public static List<KubeJSPlugin> fabricPlugins;

    public static void initFabricPlugins() {
        List<Mod> mods = ConnectorEarlyLoader.getConnectorMods().stream()
            .<Mod>map(LoadingModImpl::new)
            .toList();
        loadedMods = mods.stream().map(Mod::getModId).toList();

        KubeJSPlugins.load(mods, FMLEnvironment.dist.isClient());
        fabricPlugins = List.copyOf(KubeJSPlugins.getAll());

        KubeJSPlugins.forEachPlugin(KubeJSPlugin::init);
        KubeJSPlugins.forEachPlugin(KubeJSPlugin::registerEvents);
        KubeJSPlugins.forEachPlugin(KubeJSPlugin::initStartup);
    }

    public static void forForgePluginsOnly(Consumer<KubeJSPlugin> callback) {
        KubeJSPlugins.forEachPlugin(plugin -> {
            if (!fabricPlugins.contains(plugin)) {
                callback.accept(plugin);
            }
        });
    }

    private static class LoadingModImpl implements Mod {
        private final IModInfo info;

        public LoadingModImpl(IModInfo modInfo) {
            this.info = modInfo;
        }

        @Override
        @NotNull
        public String getModId() {
            return info.getModId();
        }

        @Override
        @NotNull
        public String getVersion() {
            return info.getVersion().toString();
        }

        @Override
        @NotNull
        public String getName() {
            return info.getDisplayName();
        }

        @Override
        @NotNull
        public String getDescription() {
            return info.getDescription();
        }

        @Override
        public Optional<String> getLogoFile(int i) {
            return this.info.getLogoFile();
        }

        @Override
        public List<Path> getFilePaths() {
            return List.of(getFilePath());
        }

        @Override
        public Path getFilePath() {
            return this.info.getOwningFile().getFile().getSecureJar().getRootPath();
        }

        @Override
        public Optional<Path> findResource(String... path) {
            return Optional.of(this.info.getOwningFile().getFile().findResource(path)).filter(Files::exists);
        }

        @Override
        public Collection<String> getAuthors() {
            Optional<String> optional = this.info.getConfig().getConfigElement("authors")
                .map(String::valueOf);
            return optional.isPresent() ? Collections.singleton(optional.get()) : Collections.emptyList();
        }

        @Override
        public @Nullable Collection<String> getLicense() {
            return Collections.singleton(this.info.getOwningFile().getLicense());
        }

        @Override
        public Optional<String> getHomepage() {
            return this.info.getConfig().getConfigElement("displayURL")
                .map(String::valueOf);
        }

        @Override
        public Optional<String> getSources() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getIssueTracker() {
            IModFileInfo owningFile = this.info.getOwningFile();
            if (owningFile instanceof ModFileInfo info) {
                return Optional.ofNullable(info.getIssueURL())
                    .map(URL::toString);
            }
            return Optional.empty();
        }

        @Override
        public void registerConfigurationScreen(ConfigurationScreenProvider configurationScreenProvider) {
            throw new UnsupportedOperationException();
        }
    }
}
