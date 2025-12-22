package com.moulberry.flashback.mixin.compat.valkyrienskies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.valkyrienskies.core.impl.game.ships.ShipData;
import org.valkyrienskies.core.impl.shadow.Er;

@Mixin(Er.class)
public interface ShipObjectServerWorldAccessor {
    @Accessor
    ShipData.Factory getB();
}
