package fr.juke;

import fr.juke.commands.util.TablistVariables;
import fr.juke.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.io.IOException;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        try {
            Config.INSTANCE.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerTickEvents.END_SERVER_TICK.register(TablistVariables::onTick);
    }
}