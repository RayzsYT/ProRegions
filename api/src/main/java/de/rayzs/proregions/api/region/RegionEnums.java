package de.rayzs.proregions.api.region;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class RegionEnums {

    private RegionEnums() {}


    public enum FlagState {
        ALLOW, DENY;
    }

    public enum FlagTargetType {

        LIQUID                (Arrays.asList("lava", "water")),
        PLACEABLE_BUCKET      (Arrays.asList("lava", "water", "powder_snow")),
        BLOCK                 (fetchList(Material.values(), Material::isBlock)),
        ITEM                  (fetchList(Material.values(), Material::isItem)),
        ENTITY                (fetchList(EntityType.values(), type -> type.getEntityClass() != null)),
        PROJECTILE            (fetchList(EntityType.values(), type -> {
            final Class<? extends Entity> clazz = type.getEntityClass();
            return clazz != null && Projectile.class.isAssignableFrom(clazz);
        })),

        CHAT, MOVE, NONE;

        private final List<String> elements;
        FlagTargetType(final List<String> elements) {
            this.elements = elements;
        }

        FlagTargetType() {
            this.elements = List.of();
        }

        private static <E extends Enum<E>> List<String> fetchList(final E[] values, final Predicate<E> filter) {
            return Arrays.stream(values).filter(filter).map(e -> e.name().toLowerCase()).toList();
        }

        /**
         * Gets the list of elements associated with this target type.
         *
         * @return an unmodifiable list of elements for this target type.
         */
        public List<String> getElements() {
            return elements;
        }

        /**
         * Checks if the input element exists in the list of elements for this target type.
         * If it does, it returns the input in lowercase, otherwise null.
         *
         * @param element the element to validate.
         * @return the validated element in lowercase if it exists, otherwise null.
         */
        public String validateIfExist(String element) {
            if (this.elements.isEmpty()) {
                return null;
            }

            element = element.toLowerCase();
            if (this.elements.contains(element)) {
                return element;
            }

            return null;
        }
    }

    public enum Flags {

        ENTER                    (FlagTargetType.MOVE, FlagState.ALLOW, false),
        LEAVE                    (FlagTargetType.MOVE, FlagState.ALLOW, false),

        PLACE                    (FlagTargetType.BLOCK, FlagState.DENY, true),
        BREAK                    (FlagTargetType.BLOCK, FlagState.DENY, true),
        PISTON                   (FlagTargetType.BLOCK, FlagState.DENY, true),
        EXPLODE_BLOCKS           (FlagTargetType.BLOCK, FlagState.DENY, true),
        FIRE_SPREAD              (FlagTargetType.BLOCK, FlagState.DENY, false),
        INTERACT_BLOCK           (FlagTargetType.BLOCK, FlagState.DENY, true),
        TRAMPLE_CROPS            (FlagTargetType.BLOCK, FlagState.DENY, false),

        FLOW                     (FlagTargetType.LIQUID, FlagState.DENY, true),

        PROJECTILE               (FlagTargetType.PROJECTILE, FlagState.ALLOW, true),

        BUCKET_FILL              (FlagTargetType.PLACEABLE_BUCKET, FlagState.DENY, true),
        BUCKET_EMPTY             (FlagTargetType.PLACEABLE_BUCKET, FlagState.DENY, true),

        MILK_ENTITY              (FlagTargetType.ENTITY, FlagState.DENY, true),
        HUNGER                   (FlagTargetType.ENTITY, FlagState.DENY, false),
        PVP                      (FlagTargetType.ENTITY, FlagState.DENY, false),
        PVE                      (FlagTargetType.ENTITY, FlagState.DENY, true),
        INTERACT_ENTITY          (FlagTargetType.ENTITY, FlagState.ALLOW, true),
        INTERACT_ITEM            (FlagTargetType.ITEM, FlagState.ALLOW, true),
        FISHING                  (FlagTargetType.ENTITY, FlagState.DENY, true),

        DROP                     (FlagTargetType.ITEM, FlagState.DENY, true),
        PICKUP                   (FlagTargetType.ITEM, FlagState.DENY, true),

        FALLING_BLOCK_DAMAGE     (FlagTargetType.ENTITY, FlagState.DENY, false),
        FALL_DAMAGE              (FlagTargetType.ENTITY, FlagState.DENY, false),
        BURN_DAMAGE              (FlagTargetType.ENTITY, FlagState.DENY, false),
        DROWNING_DAMAGE          (FlagTargetType.ENTITY, FlagState.DENY, false),

        MONSTER_SPAWN            (FlagTargetType.ENTITY, FlagState.DENY, true),
        ANIMAL_SPAWN             (FlagTargetType.ENTITY, FlagState.DENY, true);

        private final FlagTargetType targetType;
        private final FlagState defaultState;
        private final boolean specifiable;

        Flags (final FlagTargetType targetType, final FlagState defaultState, final boolean specifiable) {
            this.targetType = targetType;
            this.specifiable = specifiable;
            this.defaultState = defaultState;
        }

        /**
         * If flag is specifiable or not.
         * Refers to whether the state of the flag can be applied to only a certain type.
         * The type is determined by the FlagTargetType from getDefaultState().
         *
         * @return true if the flag is specifiable, false otherwise.
         */
        public boolean isSpecifiable() {
            return this.specifiable;
        }

        /**
         * The default state of the flag.
         *
         * @return the default state of the flag, either ALLOW or DENY
         */
        public FlagState getDefaultState() {
            return this.defaultState;
        }

        /**
         * The target type of the flag. Determines what type
         * this flag targets and affects
         *
         * @return the target type of the flag, such as BLOCK, ENTITY, ITEM, etc.
         */
        public FlagTargetType getTargetType() {
            return this.targetType;
        }
    }

}
