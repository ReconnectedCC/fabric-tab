package me.simon;

import me.simon.commands.*;

import me.simon.commands.util.TextFormatter;
import me.simon.commands.util.TablistVariables;
import me.simon.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;

import java.io.IOException;

public class Main implements ModInitializer {
    //TODO fix the spaghetti called TextFormatter
    public static final String MOD_ID = "colorchat";
    public static final String MOD_NAME = "colorchat";

    @Override
    public void onInitialize() {
        try {
            Config.INSTANCE.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CommandRegistrationCallback.EVENT.register(FormatCommand::register);
        ServerTickCallback.EVENT.register(TablistVariables::onTick);
    }
}