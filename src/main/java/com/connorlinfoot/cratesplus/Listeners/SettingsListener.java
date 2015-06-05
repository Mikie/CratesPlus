package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SettingsListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Crate Winnings")) {
            String crateName = ChatColor.stripColor(event.getInventory().getTitle().replaceAll("Edit ", "").replaceAll(" Crate Winnings", ""));
            List<String> items = new ArrayList<String>();
            for (ItemStack itemStack : event.getInventory().getContents()) {
                String itemString = CrateHandler.itemstackToString(itemStack);
                if (itemString != null) items.add(itemString);
            }
            Crate crate = CratesPlus.crates.get(crateName);
            if (crate == null) {
                return; // TODO Error handling here
            }
            CratesPlus.getPlugin().getConfig().set("Crates." + crate.getName(false) + ".Items", items);
            CratesPlus.getPlugin().saveConfig();
            CratesPlus.reloadPlugin();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        player.sendMessage("listener");

        if (event.getInventory().getTitle() == null)
            return;


        if (event.getInventory().getTitle().contains("CratesPlus Settings")) {

            if (itemStack == null || itemStack.getType() == Material.AIR || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Edit Crates")) {
                event.setCancelled(true);
                player.closeInventory();
                CratesPlus.settingsHandler.openCrates(player);
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.RED + "")) {
                event.setCancelled(true);
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "Coming Soon");
            }

        } else if (event.getInventory().getTitle().contains("Crates")) {

            if (itemStack == null || itemStack.getType() == Material.AIR || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().getType() == Material.CHEST) {
                player.closeInventory();
                CratesPlus.settingsHandler.openCrate(player, ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
            }

        } else if (event.getInventory().getTitle().contains(" Crate Winnings")) {
            return;
        } else if (event.getInventory().getTitle().contains("Edit ")) {
            if (itemStack == null || itemStack.getType() == Material.AIR || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Edit Crate Winnings")) {
                event.setCancelled(true);
                player.closeInventory();
                String name = ChatColor.stripColor(event.getInventory().getTitle().replaceAll("Edit ", "").replaceAll(" Crate", ""));
                CratesPlus.settingsHandler.openCrateWinnings(player, name);
            }

        }

    }

}
