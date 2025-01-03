package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.models.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityInteractionListener implements Listener {
    private final XMyIsland plugin;

    public EntityInteractionListener(XMyIsland plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        
        Player player = (Player) event.getDamager();
        Island island = plugin.getIslandManager().getIslandAt(event.getEntity().getLocation());
        
        if (island != null) {
            Permission requiredPermission = event.getEntity() instanceof Player ? 
                Permission.PVP : Permission.ATTACK_MOBS;
                
            if (!hasPermission(player, island, requiredPermission)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Island island = plugin.getIslandManager().getIslandAt(event.getRightClicked().getLocation());
        
        if (island != null) {
            if (!hasPermission(player, island, Permission.INTERACT_ENTITIES)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean hasPermission(Player player, Island island, Permission permission) {
        if (island.getOwner().equals(player.getUniqueId())) {
            return true;
        }

        return island.getTrustedPlayers().containsKey(player.getUniqueId()) &&
               island.getTrustedPlayers().get(player.getUniqueId()).hasPermission(permission);
    }
}