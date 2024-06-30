package taz.development.healtzsellall.items.obj;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import taz.development.healtzsellall.Healtz_SellAll;

public class CustomItemHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (isCustomItem(heldItem)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                CustomItem customItem = Healtz_SellAll.customItemMap.get(getItemId(heldItem));
                customItem.handleRightClick(player, heldItem, event);
            }

            if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                CustomItem customItem = Healtz_SellAll.customItemMap.get(getItemId(heldItem));
                customItem.handleLeftClick(player, heldItem, event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() != 39 || event.getSlotType() != InventoryType.SlotType.ARMOR) {
            return;
        }

        ItemStack cursor = event.getCursor();
        ItemStack item = event.getCurrentItem();
        Player p = (Player) event.getWhoClicked();

        if (cursor == null || item == null) {
            return;
        }
        if (!isCustomItem(cursor)) {
            return;
        }

        if (item.getType() == Material.AIR && cursor.getType() != Material.AIR) {
            p.setItemOnCursor(null);
            Bukkit.getScheduler().runTask(Healtz_SellAll.getInstance(), () -> p.getInventory().setHelmet(cursor));
        } else {
            Bukkit.getScheduler().runTask(Healtz_SellAll.getInstance(), () -> p.getInventory().setHelmet(cursor));
            p.setItemOnCursor(item);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack im = player.getItemInHand();
    }

    private boolean isCustomItem(ItemStack itemStack) {
        return (itemStack.hasItemMeta() &&
                itemStack.getItemMeta().getPersistentDataContainer().has(Healtz_SellAll.specialItemKey, PersistentDataType.STRING));
    }

    private String getItemId(ItemStack itemstack) {
        return itemstack.getItemMeta().getPersistentDataContainer().get(Healtz_SellAll.specialItemKey, PersistentDataType.STRING);
    }

}