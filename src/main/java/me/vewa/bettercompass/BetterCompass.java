package me.vewa.bettercompass;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.bstats.bukkit.Metrics;

public final class BetterCompass extends JavaPlugin {

    private int taskId = -1;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        // bStats
        new Metrics(this, 27925);

        if (getConfig().getBoolean("auto-hide-f3", true)) {
            setReducedDebugInfoAllWorlds(true);
        }

        startActionbarTask();

        BetterCompassCommand cmd = new BetterCompassCommand(this);
        if (getCommand("bettercompass") != null) {
            getCommand("bettercompass").setExecutor(cmd);
            getCommand("bettercompass").setTabCompleter(cmd);
        }

        getLogger().info("BetterCompass enabled");
    }

    @Override
    public void onDisable() {
        stopActionbarTask();
    }

    public void startActionbarTask() {
        stopActionbarTask();

        if (!getConfig().getBoolean("actionbar.enabled", true)) return;

        int interval = getConfig().getInt("actionbar.interval-ticks", 5);
        if (interval <= 0) interval = 5;

        taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {

                // ⬇️ НОВАЯ ЛОГИКА: проверяем компас и в основной, и в левой руке
                ItemStack main = p.getInventory().getItemInMainHand();
                ItemStack off  = p.getInventory().getItemInOffHand();

                boolean hasCompass =
                        (main != null && main.getType() == Material.COMPASS) ||
                                (off  != null && off.getType()  == Material.COMPASS);

                if (!hasCompass) {
                    continue;
                }
                // ⬆️ конец изменения

                Location loc = p.getLocation();
                String msg = getConfig().getString(
                        "actionbar.format",
                        "&eX: &f{x}  &aY: &f{y}  &bZ: &f{z}"
                );

                msg = msg.replace("{x}", String.valueOf(loc.getBlockX()))
                        .replace("{y}", String.valueOf(loc.getBlockY()))
                        .replace("{z}", String.valueOf(loc.getBlockZ()));

                msg = ChatColor.translateAlternateColorCodes('&', msg);

                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
            }
        }, 0L, interval);
    }

    public void stopActionbarTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    public void setReducedDebugInfoAllWorlds(boolean value) {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, value);
        }
    }

    public void setReducedDebugInfoForPlayerWorld(Player player, boolean value) {
        player.getWorld().setGameRule(GameRule.REDUCED_DEBUG_INFO, value);
    }
}
