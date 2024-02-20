package com.kazuha.antiplayer;


import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import static com.kazuha.antiplayer.main.*;

public class listener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPing(ProxyPingEvent e) {
        if (config.getBoolean("isantimode")) {
            e.setResponse(ping);
        }
    }


    @EventHandler
    public void onJoin(PostLoginEvent e) {
        if (e.getPlayer().hasPermission("antiplayer.bypass")) {
            return;
        }
        if (!main.isMantenance) {
            return;
        }
        e.getPlayer().disconnect(new TextComponent(finaled));
    }


}
