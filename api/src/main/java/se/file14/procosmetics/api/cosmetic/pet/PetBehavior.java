package se.file14.procosmetics.api.cosmetic.pet;

import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

public interface PetBehavior extends CosmeticBehavior<PetType> {

    void onSetupEntity(CosmeticContext<PetType> context, Entity entity);

    void onUpdate(CosmeticContext<PetType> context, Entity entity);

}