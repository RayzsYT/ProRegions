package de.rayzs.proregions.api.region;

public class RegionEnums {

    private RegionEnums() {}


    public enum FlagState {
        ALLOW, DENY;
    }

    public enum FlagTargetType {
        LIQUID, BLOCK, ITEM,
        ENTITY, PROJECTILE,
        CHAT, MOVE,
        NONE
    }

    public enum Flags {

        //ENTER                    (FlagTargetType.MOVE, FlagState.ALLOW, false),
        //LEAVE                    (FlagTargetType.MOVE, FlagState.ALLOW, false),

        PLACE                    (FlagTargetType.BLOCK, FlagState.DENY, true),
        BREAK                    (FlagTargetType.BLOCK, FlagState.DENY, true),
        PISTON                   (FlagTargetType.BLOCK, FlagState.DENY, true),
        EXPLODE_BLOCKS           (FlagTargetType.BLOCK, FlagState.DENY, true),
        FIRE_SPREAD              (FlagTargetType.BLOCK, FlagState.DENY, false),
        INTERACT_BLOCK           (FlagTargetType.BLOCK, FlagState.DENY, true),
        TRAMPLE_CROPS            (FlagTargetType.BLOCK, FlagState.DENY, false),

        FLOW                     (FlagTargetType.LIQUID, FlagState.DENY, true),

        PROJECTILE               (FlagTargetType.PROJECTILE, FlagState.ALLOW, true),

        BUCKET_FILL              (FlagTargetType.BLOCK, FlagState.DENY, false),
        BUCKET_EMPTY             (FlagTargetType.BLOCK, FlagState.DENY, false),

        MILK_ENTITY              (FlagTargetType.ENTITY, FlagState.DENY, false),
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
        ANIMAL_SPAWN             (FlagTargetType.ENTITY, FlagState.DENY, true),;

        private final FlagTargetType targetType;
        private final FlagState defaultState;
        private final boolean specifiable;

        Flags (final FlagTargetType targetType, final FlagState defaultState, final boolean specifiable) {
            this.targetType = targetType;
            this.specifiable = specifiable;
            this.defaultState = defaultState;
        }

        public boolean isSpecifiable() {
            return this.specifiable;
        }

        public FlagState getDefaultState() {
            return defaultState;
        }

        public FlagTargetType getTargetType() {
            return this.targetType;
        }
    }

}
