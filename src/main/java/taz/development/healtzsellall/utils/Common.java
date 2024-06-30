package taz.development.healtzsellall.utils;

import org.bukkit.ChatColor;

public class Common {

    private Common() {}

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}