package com.kazuha.antiplayer;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class main extends Plugin{
    public static Boolean isMantenance;
    public static ServerPing ping;
    public static Plugin ins;
    public static Configuration config;
    public static String finaled;
    @Override
    public void onEnable(){
        long start = System.currentTimeMillis();
        getLogger().info("§a§l[ANTIPLAYER]§f插件版本："+getDescription().getVersion() + "作者: Frk");
        getLogger().info("§a§l[ANTIPLAYER]§f加载插件中..");
        ins = this;
        saveConfigFile();
        StringBuilder builder = new StringBuilder();
        for(String s : main.config.getStringList("kick-message")){
            builder.append(s).append("\n");
        }
        finaled = builder.toString().replace("{REASON}",main.config.getString("reason")).replace("{EXP_DATE}",main.config.getString("enddate"));
        isMantenance = config.getBoolean("isantimode");
        ping = manager.regPing();
        ProxyServer.getInstance().getPluginManager().registerListener(this, new listener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new command("ap"));

        getLogger().info("§a§l[ANTIPLAYER]§f加载完毕.("+(System.currentTimeMillis() - start)+"ms)");
        getLogger().info("§a§l[ANTIPLAYER]§f好的插件 爱来自Frk");
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
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
