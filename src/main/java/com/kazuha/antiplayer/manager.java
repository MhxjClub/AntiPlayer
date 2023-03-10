package com.kazuha.antiplayer;

import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.kazuha.antiplayer.main.config;
import static com.kazuha.antiplayer.main.ins;

public class manager {
    public static void save() {
        File file = new File(ins.getDataFolder(), "config.yml");
        try{
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            ConfigurationProvider s = ConfigurationProvider.getProvider(YamlConfiguration.class);
            s.save(config,writer);
        }catch (Exception e){
            throw new RuntimeException(" FUCK YOU ");

        }
    }
    public static ServerPing regPing(){
        ServerPing ping = new ServerPing();
        ping.setFavicon(Favicon.create(Objects.requireNonNull(base64ToBufferedImage(config.getString("motd.logo")))));
        ping.setVersion(new ServerPing.Protocol(config.getString("motd.v"),114514));
        ping.setDescriptionComponent(new TextComponent(config.getString("motd.l1") + "\n" + config.getString("motd.l2")));
        return ping;
    }
    private static BufferedImage base64ToBufferedImage(String base64) {
        BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
