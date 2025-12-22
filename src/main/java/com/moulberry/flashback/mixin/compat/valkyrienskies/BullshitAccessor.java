package com.moulberry.flashback.mixin.compat.valkyrienskies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.valkyrienskies.core.impl.shadow.FD;
import org.valkyrienskies.core.impl.shadow.FI;
import org.valkyrienskies.core.impl.shadow.FJ;
import org.valkyrienskies.core.impl.shadow.FM;

@Mixin(FM.class)
public interface BullshitAccessor {
    @Accessor
    boolean getC();

    @Accessor
    FI getD();

    @Accessor
    FD getE();

    @Accessor
    FJ getB();
}
