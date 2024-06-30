package taz.development.healtzsellall.utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUtil {

    private final JavaPlugin plugin;
    private final FileConfiguration config;

    public ConfigUtil(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public String getErrorMessage() {
        return config.getString("messages.error");
    }

    public Integer getLimite() {
        return config.getInt("Limite");
    }

    public Map<Material, Prices> getPrecoDeVenda() {
        List<String> venderItensList = config.getStringList("Preco_De_Venda");
        Map<Material, Prices> venderItensMap = new HashMap<>();
        for (String item : venderItensList) {
            String[] parts = item.split("#|:");
            if (parts.length == 3) {
                String id = parts[0];
                String numero = parts[1];
                double preco = Double.parseDouble(parts[2]);
                Prices prices = new Prices(Integer.parseInt(numero), preco);
                venderItensMap.put(Material.getMaterial(id), prices);
            }
        }
        return venderItensMap;
    }

    public Map<String, ItemConfig> getItens() {
        Map<String, ItemConfig> itensMap = new HashMap<>();
        for (String key : config.getConfigurationSection("itens").getKeys(false)) {
            String itemMaterial = config.getString("itens." + key + ".item");
            int durabilidade = config.getInt("itens." + key + ".Durabilidade");
            String nome = config.getString("itens." + key + ".Nome");
            List<String> lore = config.getStringList("itens." + key + ".Lore");
            double bonus = config.getDouble("itens." + key + ".Bonus");
            itensMap.put(key, new ItemConfig(Material.valueOf(itemMaterial), durabilidade, nome, lore, bonus));
        }
        return itensMap;
    }

    public static class ItemConfig {
        private final Material item;
        private final int durabilidade;
        private final String nome;
        private final List<String> lore;
        private final double bonus;

        public ItemConfig(Material item, int durabilidade, String nome, List<String> lore, double bonus) {
            this.item = item;
            this.durabilidade = durabilidade;
            this.nome = nome;
            this.lore = lore;
            this.bonus = bonus;
        }

        public Material getItem() {
            return item;
        }

        public int getDurabilidade() {
            return durabilidade;
        }

        public String getNome() {
            return nome;
        }

        public List<String> getLore() {
            return lore;
        }

        public double getBonus() {
            return bonus;
        }
    }
}
