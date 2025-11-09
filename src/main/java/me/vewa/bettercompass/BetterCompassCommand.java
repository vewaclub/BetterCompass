package me.vewa.bettercompass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BetterCompassCommand implements CommandExecutor, TabCompleter {

    private final BetterCompass plugin;

    public BetterCompassCommand(BetterCompass plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " <reload|on|off>");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "reload":
                if (!sender.hasPermission("bettercompass.reload")) {
                    sender.sendMessage(ChatColor.RED + "You dont have permissions to execute this command.");
                    return true;
                }

                plugin.reloadConfig();
                plugin.startActionbarTask();
                sender.sendMessage(ChatColor.GREEN + "BetterCompass config reloaded.");
                return true;

            case "on":
            case "off":
                if (!sender.hasPermission("bettercompass.toggle")) {
                    sender.sendMessage(ChatColor.RED + "You dont have permissions to execute this command.");
                    return true;
                }

                // /on  -> hide coords (reducedDebugInfo = true)
                // /off -> show coords (reducedDebugInfo = false)
                boolean hide = sub.equals("on");
                boolean reducedDebug = hide;

                plugin.setReducedDebugInfoAllWorlds(reducedDebug);

                String state = hide
                        ? "Coordinates in F3 are hidden in all worlds"
                        : "Coordinates in F3 are shown in all worlds";

                sender.sendMessage(ChatColor.GREEN + state + ChatColor.GRAY +
                        " (gamerule reducedDebugInfo = " + reducedDebug + ")");

                return true;

            default:
                sender.sendMessage(ChatColor.YELLOW + "Unknown subcommand. Usage: /" + label + " <reload|on|off>");
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            if ("reload".startsWith(prefix)) result.add("reload");
            if ("on".startsWith(prefix)) result.add("on");
            if ("off".startsWith(prefix)) result.add("off");
        }
        return result;
    }
}
