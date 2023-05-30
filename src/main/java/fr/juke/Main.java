package fr.juke;

import fr.juke.commands.FormatCommand;
import fr.juke.config.Config;
import fr.juke.commands.*;

import fr.juke.commands.util.TablistVariables;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.io.IOException;

public class Main implements ModInitializer {
    //TODO fix the spaghetti called TextFormatter
    public static final String MOD_ID = "fabric-tab";
    public static final String MOD_NAME = "fabric-tab";

    @Override
    public void onInitialize() {
        try {
            Config.INSTANCE.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CommandRegistrationCallback.EVENT.register(FormatCommand::register);
        ServerTickEvents.END_SERVER_TICK.register(TablistVariables::onTick);
    }
}