package de.rayzs.proregions.api.region;

public class RegionEnums {

    private RegionEnums() {}


    public enum FlagState {
        ALLOW, DENY;
    }

    public enum FlagTargetType {
        LIQUID, BLOCK, ENTITY, PROJECTILE, CHAT, ITEM, NONE
    }

    public enum Flags {
        PLACE                    (FlagTargetType.BLOCK, true),
        BREAK                    (FlagTargetType.BLOCK, true),
        PISTON                   (FlagTargetType.BLOCK, true),
        EXPLODE_BLOCKS           (FlagTargetType.BLOCK, true),
        FIRE_SPREAD              (FlagTargetType.BLOCK, false),
        INTERACT_BLOCK           (FlagTargetType.BLOCK, true),
        TRAMPLE_CROPS            (FlagTargetType.BLOCK, false),

        FLOW                     (FlagTargetType.LIQUID, true),

        PROJECTILE               (FlagTargetType.PROJECTILE, true),

        BUCKET_FILL              (FlagTargetType.BLOCK, false),
        BUCKET_EMPTY             (FlagTargetType.BLOCK, false),

        MILK_ENTITY              (FlagTargetType.ENTITY, false),
        HUNGER                   (FlagTargetType.ENTITY, false),
        PVP                      (FlagTargetType.ENTITY, false),
        PVE                      (FlagTargetType.ENTITY, true),
        INTERACT_ENTITY          (FlagTargetType.ENTITY, true),

        DROP                     (FlagTargetType.ITEM, true),
        PICKUP                   (FlagTargetType.ITEM, true),

        FALLING_BLOCK_DAMAGE     (FlagTargetType.ENTITY, false),
        FALL_DAMAGE              (FlagTargetType.ENTITY, false),
        BURN_DAMAGE              (FlagTargetType.ENTITY, false),
        DROWNING_DAMAGE          (FlagTargetType.ENTITY, false),

        MONSTER_SPAWN            (FlagTargetType.ENTITY, true),
        ANIMAL_SPAWN             (FlagTargetType.ENTITY, true),;

        private final FlagTargetType targetType;
        private final boolean excludable;

        Flags (final FlagTargetType targetType, final boolean excludable) {
            this.targetType = targetType;
            this.excludable = excludable;
        }

        public boolean isExcludable() {
            return excludable;
        }

        public FlagTargetType getTargetType() {
            return targetType;
        }
    }

}
