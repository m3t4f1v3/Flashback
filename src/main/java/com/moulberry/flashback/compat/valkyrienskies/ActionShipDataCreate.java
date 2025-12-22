package com.moulberry.flashback.compat.valkyrienskies;

import com.fasterxml.jackson.databind.JsonNode;
import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.action.Action;
import com.moulberry.flashback.mixin.compat.valkyrienskies.BullshitInvoker;
import com.moulberry.flashback.mixin.compat.valkyrienskies.ShipFactoryAccessor;
import com.moulberry.flashback.mixin.compat.valkyrienskies.ShipObjectServerWorldAccessor;
import com.moulberry.flashback.playback.ReplayServer;

import io.netty.buffer.ByteBufInputStream;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;

import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.impl.game.ships.PhysPoseImpl;
import org.valkyrienskies.core.impl.game.ships.ShipData;
import org.valkyrienskies.core.impl.game.ships.ShipInertiaDataImpl;
import org.valkyrienskies.core.impl.game.ships.serialization.shipserver.dto.ShipDataCommon;
import org.valkyrienskies.core.impl.networking.impl.PacketShipDataCreate;
import org.valkyrienskies.core.impl.shadow.Ei;
import org.valkyrienskies.core.impl.shadow.Ep;
import org.valkyrienskies.core.impl.shadow.Er;
import org.valkyrienskies.core.impl.util.serialization.VSJacksonUtil;
import org.valkyrienskies.core.internal.ships.VsiMutableQueryableShipData;
import org.valkyrienskies.mod.common.IShipObjectWorldClientProvider;
import org.valkyrienskies.mod.common.IShipObjectWorldServerProvider;

public class ActionShipDataCreate implements Action {
    private static final ResourceLocation NAME = Flashback
            .createResourceLocation("action/valkyrien_skies_ship_data_create");
    public static final ActionShipDataCreate INSTANCE = new ActionShipDataCreate();

    private ActionShipDataCreate() {
    }

    @Override
    public ResourceLocation name() {
        return NAME;
    }

    @Override
    public void handle(ReplayServer replayServer, FriendlyByteBuf friendlyByteBuf) {
        var kClass = (KClass<PacketShipDataCreate>) Reflection.createKotlinClass(PacketShipDataCreate.class);
        try {
            // for (int i = 0; i < friendlyByteBuf.readableBytes(); i++) {
            // System.out.print((char) friendlyByteBuf.getByte(friendlyByteBuf.readerIndex()
            // + i));
            // }

            var packet = VSJacksonUtil.INSTANCE.getPacketMapper().readValue(
                    (InputStream) (new ByteBufInputStream(friendlyByteBuf)), JvmClassMappingKt.getJavaClass(kClass));
            friendlyByteBuf.skipBytes(friendlyByteBuf.readableBytes());

            // VsiServerShipWorld shipWorld
            var shipWorld = ((Er) ((IShipObjectWorldServerProvider) replayServer).getShipObjectWorld());
            for (var toCreate : packet.getToCreate()) {
                // ShipData newShip = ((ShipObjectServerWorldAccessor) (Object) (Er)
                // shipWorld).getB().createEmpty(toCreate.getSlug(), toCreate.getId(),
                // toCreate.getChunkClaim(), toCreate.getChunkClaimDimension(),
                // toCreate.getTransform().getPositionInWorld(),
                // toCreate.getTransform().getPositionInShip(),
                // toCreate.getTransform().getScaling().x(), false);
                // newShip.setKinematics(toCreate.getKinematics());
                // newShip.setWorldAABB(toCreate.getWorldAABB());
                // newShip.setShipAABB(toCreate.getShipAABB());

                System.out.println("creating ship with id " + toCreate.getId());
                ShipData newShip = new ShipData(
                        toCreate.getId(),
                        toCreate.getSlug(),
                        toCreate.getChunkClaim(),
                        toCreate.getChunkClaimDimension(),
                        // we need SOME mass for VS to load the ship
                        new ShipInertiaDataImpl(new Vector3d(), 1, new Matrix3d()),
                        toCreate.getKinematics(),
                        toCreate.getWorldAABB(),
                        toCreate.getShipAABB(),
                        toCreate.getActiveChunksSet(),
                        false,
                        new PhysPoseImpl(toCreate.getKinematics().getPosition(),
                                toCreate.getKinematics().getRotation()),
                        ((ShipFactoryAccessor) (Object) ((ShipObjectServerWorldAccessor) (Object) shipWorld).getB())
                                .getAttachmentHolderFactory().a(),
                        0);
                shipWorld.a().add(newShip);
                // ((VsiMutableQueryableShipData<ClientShip>)(((IShipObjectWorldClientProvider) Minecraft.getInstance()).getShipObjectWorld())
                //         .getAllShips()).add(BullshitInvoker.invokeConstructor(newShip, VSJacksonUtil.INSTANCE.getDeltaMapper().valueToTree(newShip)));
            }
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
