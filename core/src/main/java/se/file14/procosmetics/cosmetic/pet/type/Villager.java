package se.file14.procosmetics.cosmetic.pet.type;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager.Profession;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class Villager implements PetBehavior {

    private static final List<Profession> PROFESSIONS = List.of(
            Profession.FARMER,
            Profession.LIBRARIAN,
            Profession.BUTCHER
    );

    @Override
    public void onEquip(CosmeticContext<PetType> context) {
    }

    @Override
    public void onSetupEntity(CosmeticContext<PetType> context, Entity entity) {
        if (entity instanceof org.bukkit.entity.Villager villager) {
            villager.setProfession(PROFESSIONS.get(MathUtil.THREAD_LOCAL_RANDOM.nextInt(PROFESSIONS.size())));
            villager.setAgeLock(true);
        }
    }

    @Override
    public void onUpdate(CosmeticContext<PetType> context, Entity entity) {
    }

    @Override
    public void onUnequip(CosmeticContext<PetType> context) {
    }
}