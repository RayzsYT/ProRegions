package de.rayzs.proregions.api.region.context;

import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;

@FunctionalInterface
public interface ContextEval<A, B, C, D> {
    RegionEnums.FlagState evaluate(
            final Region region,
            final RegionEnums.Flags flag,
            final A a,
            final B b,
            final C c,
            final D d
    );
}