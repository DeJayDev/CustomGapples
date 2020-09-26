package dev.dejay.colt.customgapples;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class CustomGapples extends JavaPlugin implements Listener {

    private final List<String> effects = getConfig().getStringList("effects");

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
    }

    @EventHandler
    public void itemConsumeEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if ((event.getItem().getType() == Material.GOLDEN_APPLE)
            && (event.getItem().getDurability() == 1)
            && (player.hasPermission("customgapple.use"))) {
            ItemStack heldItem = player.getInventory().getItemInHand();
            if (event.getItem().getAmount() == 1) {
                player.getInventory().remove(event.getItem());
            } else {
                heldItem.setAmount(heldItem.getAmount() - 1);
            }
            for (String potion : effects) {
                String[] inf = potion.split(":");
                if ((inf.length == 3) && (PotionEffectType.getByName(inf[0].toUpperCase()) != null)) {
                    PotionEffectType pot = PotionEffectType.getByName(inf[0].toUpperCase());
                    int dur = Math.min(Integer.parseInt(inf[1]), Integer.MAX_VALUE);
                    int amp = Math.min(Integer.parseInt(inf[2]), 255);
                    PotionEffect effect = new PotionEffect(pot, dur, amp);
                    if (player.hasPotionEffect(pot)) {
                        player.removePotionEffect(pot);
                    }
                    player.addPotionEffect(effect);
                }
            }
            event.setCancelled(true);
        }
    }
}