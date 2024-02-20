package com.kazuha.antiplayer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.kazuha.antiplayer.main.config;
import static com.kazuha.antiplayer.main.ins;

public class manager {
    public static void save() {
        File file = new File(ins.getDataFolder(), "config.yml");
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8));
            ConfigurationProvider s = ConfigurationProvider.getProvider(YamlConfiguration.class);
            s.save(config, writer);
        } catch (Exception e) {
            throw new RuntimeException(" e ");

        }
    }

    public static ServerPing regPing() {
        ServerPing ping = new ServerPing();
        try {
            ping.setFavicon(Favicon.create(ImageIO.read(new File(main.ins.getDataFolder() + config.getString("motd.logo")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ping.setVersion(new ServerPing.Protocol(config.getString("motd.version"), 114514));
        ping.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("motd.line1") + "\n" + config.getString("motd.line2"))));
        return ping;
    }
//    private static BufferedImage base64ToBufferedImage(String base64) {
//        try {
//            byte[] bytes1 = Base64.getDecoder().decode(base64);
//            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
//            return ImageIO.read(bais);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
