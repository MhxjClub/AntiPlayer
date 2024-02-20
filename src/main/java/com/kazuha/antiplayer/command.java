package com.kazuha.antiplayer;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static com.kazuha.antiplayer.main.*;

public class command extends Command {
    private final HashMap<CommandSender, Long> checkmap = new HashMap<>();

    public command(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission("antiplayer.op")) {
            commandSender.sendMessage(new TextComponent("§c你没有权限"));
            return;
        }
        if (strings.length < 1) {
            commandSender.sendMessage(new TextComponent("§c没参数的吗？"));
            commandSender.sendMessage(new TextComponent("§c可用命令："));
            commandSender.sendMessage(new TextComponent("§c/ap toggle <on/off> 切换模式"));
            commandSender.sendMessage(new TextComponent("§c/ap end <时间> 设置结束时间"));
            commandSender.sendMessage(new TextComponent("§c/ap reason <理由> 设置维护理由"));
            commandSender.sendMessage(new TextComponent("§c/ap reload 重载"));
            return;
        }
        switch (strings[0]) {
            case "toggle": {
                if (strings.length < 2) {
                    if (isMantenance) {
                        config.set("isantimode", false);
                        commandSender.sendMessage(new TextComponent("§a维护模式已关闭！"));
                    } else {
                        if (checkmap.containsKey(commandSender)) {
                            if (checkmap.get(commandSender) < System.currentTimeMillis()) {
                                checkmap.put(commandSender, System.currentTimeMillis() + 120000L);
                                commandSender.sendMessage(new TextComponent("§c你真的要开启维护模式吗? 如果是,请再次输入该命令以确认。"));
                                return;
                            }
                        } else {
                            checkmap.put(commandSender, System.currentTimeMillis() + 120000L);
                            commandSender.sendMessage(new TextComponent("§c你真的要开启维护模式吗? 如果是,请再次输入该命令以确认。"));
                            return;
                        }
                        config.set("isantimode", true);
                        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                            if (p.hasPermission("antiplayer.bypass")) {
                                continue;
                            }
                            p.disconnect(new TextComponent(finaled));
                        }
                        commandSender.sendMessage(new TextComponent("§c维护模式已启动!"));
                    }
                } else {
                    if (strings[1].equalsIgnoreCase("on") || strings[1].equalsIgnoreCase("true")) {
                        if (isMantenance) {
                            commandSender.sendMessage(new TextComponent("§c维护模式已经开启了。"));
                        } else {
                            if (checkmap.containsKey(commandSender)) {
                                if (checkmap.get(commandSender) < System.currentTimeMillis()) {
                                    checkmap.put(commandSender, System.currentTimeMillis() + 120000L);
                                    commandSender.sendMessage(new TextComponent("§c你真的要开启维护模式吗? 如果是,请再次输入该命令以确认。"));
                                    return;
                                }
                            } else {
                                checkmap.put(commandSender, System.currentTimeMillis() + 120000L);
                                commandSender.sendMessage(new TextComponent("§c你真的要开启维护模式吗? 如果是,请再次输入该命令以确认。"));
                                return;
                            }
                            config.set("isantimode", true);
                            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                                if (p.hasPermission("antiplayer.bypass")) {
                                    continue;
                                }
                                p.disconnect(new TextComponent(finaled));
                            }
                            commandSender.sendMessage(new TextComponent("§c维护模式已启动!"));
                        }
                    } else if (strings[1].equalsIgnoreCase("off") || strings[1].equalsIgnoreCase("false")) {
                        if (isMantenance) {
                            config.set("isantimode", false);
                            commandSender.sendMessage(new TextComponent("§a维护模式已关闭！"));
                        } else {
                            commandSender.sendMessage(new TextComponent("§a维护模式已经关闭了。"));
                        }
                    }
                }
                manager.save();
                break;
            }
            case "end": {
                if (strings.length < 2) {
                    commandSender.sendMessage(new TextComponent("§c??"));
                    return;
                }
                config.set("enddate", strings[1]);
                commandSender.sendMessage(new TextComponent("§a成功"));
                manager.save();
                break;
            }
            case "reload": {
                long start = System.currentTimeMillis();
                try {
                    config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(ins.getDataFolder(), "config.yml"));
                    ping = manager.regPing();
                    StringBuilder builder = new StringBuilder();
                    for (String s : main.config.getStringList("kick-message")) {
                        builder.append(s).append("\n");
                    }
                    finaled = builder.toString().replace("{REASON}", main.config.getString("reason")).replace("{EXP_DATE}", main.config.getString("enddate"));
                } catch (IOException e) {
                    commandSender.sendMessage(new TextComponent("§c失败"));
                    e.printStackTrace();
                    break;
                }
                commandSender.sendMessage(new TextComponent("§a成功.(" + (System.currentTimeMillis() - start) + "ms)"));
                break;
            }
            case "reason": {
                if (strings.length < 2) {
                    commandSender.sendMessage(new TextComponent("§c不懂。"));
                    return;
                }
                config.set("reason", strings[1]);
                commandSender.sendMessage(new TextComponent("§a成功"));
                manager.save();
                break;
            }
            case "help": {
                commandSender.sendMessage(new TextComponent("§c可用命令："));
                commandSender.sendMessage(new TextComponent("§c/ap toggle <on/off> 切换模式"));
                commandSender.sendMessage(new TextComponent("§c/ap end <时间> 设置结束时间"));
                commandSender.sendMessage(new TextComponent("§c/ap reason <理由> 设置维护理由"));
                commandSender.sendMessage(new TextComponent("§c/ap reload 重载"));
                break;
            }
            default: {
                commandSender.sendMessage(new TextComponent("§c我没理解，是我的问题吗？"));
                commandSender.sendMessage(new TextComponent("§c可用命令："));
                commandSender.sendMessage(new TextComponent("§c/ap toggle <on/off> 切换模式"));
                commandSender.sendMessage(new TextComponent("§c/ap end <时间> 设置结束时间"));
                commandSender.sendMessage(new TextComponent("§c/ap reason <理由> 设置维护理由"));
                commandSender.sendMessage(new TextComponent("§c/ap reload 重载"));
                break;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String s : main.config.getStringList("kick-message")) {
            builder.append(s).append("\n");
        }
        finaled = builder.toString().replace("{REASON}", main.config.getString("reason"));
        finaled = finaled.replace("{EXP_DATE}", main.config.getString("enddate"));
        isMantenance = config.getBoolean("isantimode");
    }
}
