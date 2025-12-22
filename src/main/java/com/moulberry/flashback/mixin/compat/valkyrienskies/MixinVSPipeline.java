package com.moulberry.flashback.mixin.compat.valkyrienskies;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.moulberry.flashback.Flashback;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.impl.config.VSCoreConfig;
import org.valkyrienskies.core.impl.shadow.FC;
import org.valkyrienskies.core.impl.shadow.FJ;
import org.valkyrienskies.core.impl.shadow.FM;
import org.valkyrienskies.core.internal.world.VsiServerShipWorld;

@IfModLoaded("valkyrienskies")
@Pseudo
@Mixin(value = FM.class, remap = false)
public class MixinVSPipeline {

    @WrapMethod(method = "getArePhysicsRunning", remap = false)
    public boolean getArePhysicsRunning(Operation<Boolean> original) {
        // System.out.println("getArePhysicsRunning called");
        // Do not run the physics engine
        return !Flashback.isInReplay() && original.call();
    }

    @Inject(method = "postTickGame", at = @At(value = "INVOKE", target = "Lkotlin/jvm/internal/Intrinsics;checkNotNull(Ljava/lang/Object;)V"), remap = false, cancellable = true)
    public void postTickGame(CallbackInfo ci) {
        // thanks bricky
        // Do not accumulate game frames
        if (Flashback.isInReplay()) {
            ci.cancel();
        }
    }

}
