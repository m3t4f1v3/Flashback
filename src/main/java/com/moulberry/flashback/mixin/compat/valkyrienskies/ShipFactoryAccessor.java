package com.moulberry.flashback.mixin.compat.valkyrienskies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.valkyrienskies.core.impl.game.ships.ShipData;
import org.valkyrienskies.core.impl.shadow.Cy;
@Mixin(ShipData.Factory.class)
public interface ShipFactoryAccessor {
    @Accessor
    Cy getAttachmentHolderFactory();
}