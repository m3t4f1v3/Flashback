package com.moulberry.flashback.mixin.compat.valkyrienskies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.valkyrienskies.core.impl.game.ships.ShipDataCommon;
import org.valkyrienskies.core.impl.shadow.Ep;

import com.fasterxml.jackson.databind.JsonNode;

@Mixin(Ep.class)
public interface BullshitInvoker {
    @Invoker("<init>")
    static Ep invokeConstructor(ShipDataCommon var1, JsonNode var2) {
        throw new AssertionError();
    }
}
