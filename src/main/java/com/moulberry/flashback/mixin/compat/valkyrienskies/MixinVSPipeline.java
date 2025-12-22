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
import org.valkyrienskies.core.impl.config.VSCoreConfig;
import org.valkyrienskies.core.impl.shadow.FC;
import org.valkyrienskies.core.impl.shadow.FJ;
import org.valkyrienskies.core.impl.shadow.FM;
import org.valkyrienskies.core.internal.world.VsiServerShipWorld;

@IfModLoaded("valkyrienskies")
@Pseudo
@Mixin(value = FM.class, remap = false)
public abstract class MixinVSPipeline {

    @Shadow(remap = false)
    public abstract VsiServerShipWorld getShipWorld();

    @WrapMethod(method = "getArePhysicsRunning", remap = false)
    public boolean getArePhysicsRunning(Operation<Boolean> original) {
        // Do not run the physics engine
        return !Flashback.isInReplay() && original.call();
    }

    @Overwrite(remap = false)
    public void postTickGame() {
        System.out.println("rizz");
        if (((BullshitAccessor) (Object) this).getC()) {
            Lock var1;
            (var1 = (Lock) ((BullshitAccessor) (Object) this).getD().b()).lock();

            try {
                int var2 = VSCoreConfig.SERVER.getPt().getPhysicsTicksPerGameTick();

                while (((BullshitAccessor) (Object) this).getD().a() < var2) {
                    ((BullshitAccessor) (Object) this).getD().c().await();
                }

                Unit var10000 = Unit.INSTANCE;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                var1.unlock();
            }
        }

        FC var7 = ((BullshitAccessor) (Object) this).getE().b();
        FJ var6 = ((BullshitAccessor) (Object) this).getB();
        Intrinsics.checkNotNull(var6);
        FJ var3 = var6;

        if (!Flashback.isInReplay()) {
            var3.a(var7);
        }
    }

}
