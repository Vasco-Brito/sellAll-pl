package taz.development.healtzsellall;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.objects.ChestType;
import com.bgsoftware.wildchests.api.objects.chests.StorageChest;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import taz.development.healtzsellall.commands.giveSellAll;
import taz.development.healtzsellall.items.obj.CustomItem;
import taz.development.healtzsellall.items.obj.CustomItemHandler;
import taz.development.healtzsellall.utils.ConfigUtil;
import taz.development.healtzsellall.utils.Prices;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Healtz_SellAll extends JavaPlugin {

    private static Economy econ = null;
    private ConfigUtil configUtil;
    @Getter
    private static Healtz_SellAll instance;
    public static NamespacedKey specialItemKey;
    public static NamespacedKey specialItemKeyId;
    public static Map<String, CustomItem> customItemMap;
    public static Map<Material, Prices> prices = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Plugin ativado");
        instance = this;

        specialItemKey = new NamespacedKey(this, "tazWanted-itemKey");
        specialItemKeyId = new NamespacedKey(this, "tazWanted-itemKey-id");
        customItemMap = new HashMap<>();
        readConfigFile();

        if (!setupEconomy()) {
            getLogger().severe("Vault não encontrado! Desativando plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //registerItem(new FeatherOfAllSellings());
        registerListeners(new CustomItemHandler());
        getCommand("giveShop").setExecutor(new giveSellAll());
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin desabilitado!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void readConfigFile() {
        saveDefaultConfig();
        configUtil = new ConfigUtil(this);

        String errorMessage = configUtil.getErrorMessage();
        getLogger().info("Mensagem de erro " + errorMessage);
        Integer maxItems = configUtil.getLimite();

        prices = configUtil.getPrecoDeVenda();

        Map<String, ConfigUtil.ItemConfig> itens = configUtil.getItens();
        itens.forEach((k, v) -> {

            CustomItem itemRegister = new CustomItem() {
                @Override
                public String getName() {
                    return v.getNome();
                }

                @Override
                public Material getMaterial() {
                    return v.getItem();
                }

                @Override
                public List<String> getLore() {
                    return v.getLore();
                }

                @Override
                public Boolean isStackable() {
                    return true;
                }

                @Override
                public void handleLeftClick(Player player, ItemStack itemStack, PlayerInteractEvent event) {
                }

                @Override
                public void handleRightClick(Player player, ItemStack itemStack, PlayerInteractEvent event) {
                    @Nullable Block block = event.getClickedBlock();
                    if (block == null || block.getType() == Material.AIR) return;

                    if (block.getType() != Material.CHEST) return;

                    try {
                        if (WildChestsAPI.getChest(block.getLocation()).getChestType() == ChestType.STORAGE_UNIT) {
                            StorageChest chest = WildChestsAPI.getStorageChest(block.getLocation());
                            @NotNull Material itemType = chest.getItemStack().getType();
                            if (prices.containsKey(itemType)) {
                                Integer totalItems = Integer.valueOf(chest.getAmount() + "");
                                if (totalItems > maxItems) {
                                    int excessItems = totalItems - maxItems;
                                    chest.setAmount(excessItems);
                                    totalItems = maxItems;
                                } else {
                                    chest.setAmount(0);
                                }
                                if (totalItems == 0) return;
                                double dinheiro = (Double.parseDouble(totalItems + "") * prices.get(itemType).getPrice()) * v.getBonus();
                                econ.depositPlayer(player, dinheiro);
                                player.sendMessage("§fVocê vendeu §a" + totalItems + " itens §fpor §a" + dinheiro + " coins");
                                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                            }
                            return;
                        } else {
                            player.sendMessage("§cErro: §fNão pode vender neste bau.");
                            player.sendMessage("§cErro: §fSe acha que isto é um erro, fale com um staff.");
                        }
                    } catch (Exception e) {
                        Chest chest = (Chest) block.getState();
                        @NotNull Inventory chestInventory = chest.getInventory();
                        Map<ItemStack, Integer> contents = new HashMap<>();

                        for (ItemStack item : chestInventory.getContents()) {
                            if (item != null) {
                                if (item.getAmount() <= 0) return;
                                if (prices.containsKey(item.getType())) {
                                    if (contents.containsKey(item)) {
                                        contents.put(item.clone(), contents.get(item) + item.getAmount());
                                        item.setAmount(0);
                                    } else {
                                        contents.put(item.clone(), item.getAmount());
                                        item.setAmount(0);
                                    }
                                }
                            }
                        }

                        final int[] totalItems = {0};
                        final double[] dinheiro = {0};

                        contents.forEach((key, value) -> {
                            if (prices.containsKey(key.getType())) {
                                totalItems[0] += value;
                                double itemValue = (value * prices.get(key.getType()).getPrice()) * v.getBonus();
                                dinheiro[0] += itemValue;
                                econ.depositPlayer(player, itemValue);
                            }
                        });

                        if (totalItems[0] == 0) return;
                        player.sendMessage("§fVocê vendeu §a" + totalItems[0] + " itens §fpor §a" + dinheiro[0] + " coins");
                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                    }
                }

                @Override
                public String getId() {
                    return k.toString().toLowerCase();
                }
            };

            registerItem(itemRegister);
        });
    }

    private void registerItem(CustomItem... customItems) {
        Arrays.asList(customItems).forEach(ci -> customItemMap.put(ci.getId(), ci));
    }

    private void registerListeners(Listener... listeners) {
        Arrays.asList(listeners).forEach(i -> Bukkit.getPluginManager().registerEvents(i, this));
    }
}
