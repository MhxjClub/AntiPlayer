package com.kazuha.antiplayer;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class main extends Plugin {
    public static Boolean isMantenance;
    public static ServerPing ping;
    public static Plugin ins;
    public static Configuration config;
    public static String finaled;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        getLogger().info("§a§l[ANTIPLAYER]§f插件版本：" + getDescription().getVersion());
        getLogger().info("§a§l[ANTIPLAYER]§f加载插件中..");
        ins = this;
        saveConfigFile();
        StringBuilder builder = new StringBuilder();
        for (String s : main.config.getStringList("kick-message")) {
            builder.append(s).append("\n");
        }
        finaled = builder.toString().replace("{REASON}", main.config.getString("reason")).replace("{EXP_DATE}", main.config.getString("enddate"));
        isMantenance = config.getBoolean("isantimode");
        try {
            ping = manager.regPing();
        } catch (Exception e) {
            getLogger().warning("§a§l[ANTIPLAYER]§4加载错误!"+e.getMessage());
            onDisable();
            return;
        }
        ProxyServer.getInstance().getPluginManager().registerListener(this, new listener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new command("ap"));

        getLogger().info("§a§l[ANTIPLAYER]§f加载完毕.(" + (System.currentTimeMillis() - start) + "ms)");
        getLogger().info("§a§l[ANTIPLAYER]§f好的插件 爱来自Frk");
    }
    @Override
    public void onDisable(){
        ProxyServer.getInstance().getPluginManager().unregisterListeners(this);
        ProxyServer.getInstance().getPluginManager().unregisterCommands(this);
        getLogger().info("§a§l[ANTIPLAYER]§fBye bye");
    }
    public void saveConfigFile() {
        File dir = getDataFolder();
        if (!dir.exists()) dir.mkdir();
        File file = new File(dir, "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            File file1 = new File(dir,"logo.jpg");
            try (InputStream in = getResourceAsStream("logo.jpg")) {
                Files.copy(in, file1.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
