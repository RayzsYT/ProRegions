package de.rayzs.proregions.api.region;

public class RegionEnums {

    private RegionEnums() {}


    public enum FlagState {
        ALLOW, DENY;
    }

    public enum FlagTargetType {
        BLOCK, ENTITY, CHAT, NONE
    }

    public enum Flags {
        PLACE                    (FlagTargetType.BLOCK),
        BREAK                    (FlagTargetType.BLOCK),
        PISTON                   (FlagTargetType.BLOCK),
        FLOW                     (FlagTargetType.BLOCK),
        EXPLODE_BLOCKS           (FlagTargetType.BLOCK),
        FIRE_SPREAD              (FlagTargetType.BLOCK),
        INTERACT_BLOCK           (FlagTargetType.BLOCK),
        TRAMPLE_CROPS            (FlagTargetType.BLOCK),

        PVP                      (FlagTargetType.ENTITY),
        PVE                      (FlagTargetType.ENTITY),
        INTERACT_ENTITY          (FlagTargetType.ENTITY),

        FALLING_BLOCK_DAMAGE     (FlagTargetType.ENTITY),
        FALL_DAMAGE              (FlagTargetType.ENTITY),
        BURN_DAMAGE              (FlagTargetType.ENTITY),
        DROWNING_DAMAGE          (FlagTargetType.ENTITY),

        MONSTER_SPAWN            (FlagTargetType.ENTITY),
        ANIMAL_SPAWN             (FlagTargetType.ENTITY),;

        private final FlagTargetType targetType;

        Flags(final FlagTargetType targetType) {
            this.targetType = targetType;
        }

        public FlagTargetType getTargetType() {
            return targetType;
        }
    }

}
