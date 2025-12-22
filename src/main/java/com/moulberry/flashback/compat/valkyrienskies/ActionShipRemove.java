package com.moulberry.flashback.compat.valkyrienskies;

import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.action.Action;
import com.moulberry.flashback.playback.ReplayServer;

import io.netty.buffer.ByteBufInputStream;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;

import org.valkyrienskies.core.impl.networking.impl.PacketShipRemove;
import org.valkyrienskies.core.impl.util.serialization.VSJacksonUtil;
import org.valkyrienskies.core.internal.world.VsiServerShipWorld;
import org.valkyrienskies.mod.common.IShipObjectWorldServerProvider;

public class ActionShipRemove implements Action {
    private static final ResourceLocation NAME = Flashback.createResourceLocation("action/valkyrien_skies_ship_remove");
    public static final ActionShipRemove INSTANCE = new ActionShipRemove();

    private ActionShipRemove() {
    }

    @Override
    public ResourceLocation name() {
        return NAME;
    }

    @Override
    public void handle(ReplayServer replayServer, FriendlyByteBuf friendlyByteBuf) {
        var kClass = (KClass<PacketShipRemove>) Reflection.createKotlinClass(PacketShipRemove.class);
        try {
            var packet = VSJacksonUtil.INSTANCE.getPacketMapper().readValue(
                    (InputStream) (new ByteBufInputStream(friendlyByteBuf)), JvmClassMappingKt.getJavaClass(kClass));
            friendlyByteBuf.skipBytes(friendlyByteBuf.readableBytes());

            var shipWorld = ((VsiServerShipWorld) ((IShipObjectWorldServerProvider) replayServer).getShipObjectWorld());
            for (var toRemove : packet.getToRemove()) {
                var ship = shipWorld.getAllShips().getById(toRemove);
                if (ship != null) {
                    shipWorld.deleteShip(ship);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
