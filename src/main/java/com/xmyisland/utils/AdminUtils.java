package com.xmyisland.utils;

import org.bukkit.entity.Player;

public class AdminUtils {
    public static boolean canBypassProtection(Player player) {
        return player.isOp();
    }
}