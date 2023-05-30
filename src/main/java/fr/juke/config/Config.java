package fr.juke.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Config {
    public static Config INSTANCE = new Config();
    public int configVersion = 1;
    public String header = "§7§l§oCOOL#N      SERVER       #N#N§6very cool§f#N";
    public String footer = "#N§b#TPS tps - #MSPT mspt - #PLAYERCOUNT players - #UPTIME uptime#N";
    public String motd = "§7§oWelcome to the server.§f";

    public void save() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File config = new File(FabricLoader.getInstance().getConfigDirectory(), "fabric-tab.json");
        try (FileWriter file = new FileWriter(config)) {
            file.write(gson.toJson(this));
        }
    }

    public void load() throws IOException {
        File config1 = new File(FabricLoader.getInstance().getConfigDirectory(), "fabric-tab.json");
        if (!config1.exists()) {
            this.save();
            return;
        }
        if (config1.isDirectory()) {
            if (config1.delete()) {
                this.save();
                return;
            }
        }
        try (FileReader reader = new FileReader(config1)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Config config = gson.fromJson(reader, Config.class);
            this.header = config.header;
            this.footer = config.footer;
            this.motd = config.motd;
            if (config.configVersion != this.configVersion) {
                this.save();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
