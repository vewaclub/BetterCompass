package me.vewa.bettercompass;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterCompassPlugin extends JavaPlugin {

    private int taskId = -1;

    @Override
    public void onEnable() {
        // Загружаем/создаём конфиг
        saveDefaultConfig();
        reloadConfig();

        // Применяем авто-скрытие F3, если включено
        if (getConfig().getBoolean("auto-hide-f3", true)) {
            setReducedDebugInfoAllWorlds(true); // true = урезанная инфа, координаты скрыты
        }

        // Запускаем периодический таск для actionbar
        startActionbarTask();

        // Регистрируем команду
        BetterCompassCommand commandExecutor = new BetterCompassCommand(this);
        if (getCommand("bettercompass") != null) {
            getCommand("bettercompass").setExecutor(commandExecutor);
            getCommand("bettercompass").setTabCompleter(commandExecutor);
        }

        getLogger().info("BetterCompass enabled");
    }

    @Override
    public void onDisable() {
        // Останавливаем таск
        stopActionbarTask();

        // По желанию можно вернуть стандартное поведение:
        // setReducedDebugInfoAllWorlds(false);

        getLogger().info("BetterCompass disabled");
    }

    public void startActionbarTask() {
        stopActionbarTask();

        boolean enabled = getConfig().getBoolean("actionbar.enabled", true);
        if (!enabled) {
            return;
        }

        int interval = getConfig().getInt("actionbar.interval-ticks", 5);
        if (interval <= 0) interval = 5;

        taskId = getServer().getScheduler().scheduleSyncRepeatingTask(
                this,
                () -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ItemStack mainHand = player.getInventory().getItemInMainHand();

                        if (mainHand != null && mainHand.getType() == Material.COMPASS) {
                            Location loc = player.getLocation();
                            int x = loc.getBlockX();
                            int y = loc.getBlockY();
                            int z = loc.getBlockZ();

                            String rawFormat = getConfig().getString(
                                    "actionbar.format",
                                    "&eX: &f{x}  &aY: &f{y}  &bZ: &f{z}"
                            );

                            String msg = rawFormat
                                    .replace("{x}", String.valueOf(x))
                                    .replace("{y}", String.valueOf(y))
                                    .replace("{z}", String.valueOf(z));

                            msg = ChatColor.translateAlternateColorCodes('&', msg);

                            player.spigot().sendMessage(
                                    ChatMessageType.ACTION_BAR,
                                    new TextComponent(msg)
                            );
                        }
                    }
                },
                0L,
                interval
        );
    }

    public void stopActionbarTask() {
        if (taskId != -1) {
            getServer().getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    /**
     * Устанавливает gamerule reducedDebugInfo для всех миров.
     * true  -> урезанная инфа, координаты скрыты.
     * false -> полная инфа, координаты видны.
     */
    public void setReducedDebugInfoAllWorlds(boolean value) {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, value);
        }
    }

    /**
     * Устанавливает reducedDebugInfo только в мире указанного игрока.
     * ВАЖНО: это влияет на всех игроков в этом мире.
     */
    public void setReducedDebugInfoForPlayerWorld(Player player, boolean value) {
        World world = player.getWorld();
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, value);
    }
}
