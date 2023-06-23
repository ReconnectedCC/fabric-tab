package fr.juke;

import fr.juke.config.Config;
import fr.juke.util.TablistVariables;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class FabricTab implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(FabricTab.class);

    @Override
    public void onInitialize() {
        try {
            Config.INSTANCE.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Config.INSTANCE.enabled) {
            ServerTickEvents.END_SERVER_TICK.register(TablistVariables::onTick);
        }
        LOGGER.info("Loaded fabric-tab");
    }
}
