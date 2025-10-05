package se.file14.procosmetics.cosmetic.pet.type;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;

public class ColorfulLamb implements PetBehavior {

    private final static DyeColor[] DYE_COLORS = DyeColor.values();
    private int color;

    @Override
    public void onEquip(CosmeticContext<PetType> context) {
    }

    @Override
    public void onSetupEntity(CosmeticContext<PetType> context, Entity entity) {
    }

    @Override
    public void onUpdate(CosmeticContext<PetType> context, Entity entity) {
        if (entity instanceof Sheep sheep) {
            if (color >= DYE_COLORS.length) {
                color = 0;
            }
            sheep.setColor(DYE_COLORS[color]);
            color++;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<PetType> context) {
    }
}