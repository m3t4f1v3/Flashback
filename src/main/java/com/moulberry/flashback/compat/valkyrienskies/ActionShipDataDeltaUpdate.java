package com.moulberry.flashback.compat.valkyrienskies;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.action.Action;
import com.moulberry.flashback.mixin.compat.valkyrienskies.BullshitInvoker;
import com.moulberry.flashback.playback.ReplayServer;

import io.netty.buffer.ByteBufInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.impl.game.ships.ShipDataCommon;
import org.valkyrienskies.core.impl.shadow.Ep;
import org.valkyrienskies.core.impl.shadow.aj;
import org.valkyrienskies.core.impl.util.serialization.VSJacksonUtil;
import org.valkyrienskies.core.internal.ships.VsiMutableQueryableShipData;
import org.valkyrienskies.core.internal.world.VsiServerShipWorld;
import org.valkyrienskies.mod.common.IShipObjectWorldClientProvider;
import org.valkyrienskies.mod.common.IShipObjectWorldServerProvider;

import java.io.IOException;

public class ActionShipDataDeltaUpdate implements Action {
    private static final ResourceLocation NAME = Flashback.createResourceLocation("action/valkyrien_skies_ship_data_delta_update");
    public static final ActionShipDataDeltaUpdate INSTANCE = new ActionShipDataDeltaUpdate();
    private ActionShipDataDeltaUpdate() {
    }

    @Override
    public ResourceLocation name() {
        return NAME;
    }

    @Override
    public void handle(ReplayServer replayServer, FriendlyByteBuf friendlyByteBuf) {
        var shipWorld = ((VsiServerShipWorld) ((IShipObjectWorldServerProvider) replayServer).getShipObjectWorld());
        while (friendlyByteBuf.isReadable()) {
            try {
                long id = friendlyByteBuf.readLong();
                for (ServerShip ship : shipWorld.getAllShips()) {
                    System.out.println("ship id " + ship.getId());
                    System.out.println("ship position " + ship.getTransform().getPosition());
                }
                for (ServerShip ship : shipWorld.getLoadedShips()) {
                    System.out.println("loaded ship id " + ship.getId());
                    System.out.println("loaded ship position " + ship.getTransform().getPosition());
                }
                ServerShip ship = shipWorld.getAllShips().getById(id);

                if (ship == null) {
                    System.err.println("id cooked: " + id);
                    int var3 = friendlyByteBuf.readInt();
                    friendlyByteBuf.skipBytes(var3);
                    continue;
                    // throw new RuntimeException("got null for ship id " + id);
                }

                
                for (var shiprizz : (((IShipObjectWorldClientProvider) Minecraft.getInstance()).getShipObjectWorld())
                        .getAllShips()) {
                    System.out
                            .println("existing ship id " + shiprizz.getId() + " at position "
                                    + shiprizz.getTransform().getPosition());
                }

                var oldNode = (ObjectNode) VSJacksonUtil.INSTANCE.getDeltaMapper().valueToTree(ship);
                int var3 = friendlyByteBuf.readInt();
                JsonNode var4 = VSJacksonUtil.INSTANCE.getDeltaMapper().readTree(new ByteBufInputStream(friendlyByteBuf, var3));
                var newNode = (ObjectNode) aj.a(var4, oldNode);

                // (((IShipObjectWorldClientProvider) Minecraft.getInstance()).getShipObjectWorld())
                //         .getAllShips().add(new Ep(ship, (JsonNode) newNode));

                // ((VsiMutableQueryableShipData<ClientShip>)(((IShipObjectWorldClientProvider) Minecraft.getInstance()).getShipObjectWorld())
                //         .getAllShips()).add(BullshitInvoker.invokeConstructor((ShipDataCommon)ship, VSJacksonUtil.INSTANCE.getDeltaMapper().valueToTree(ship)));
                // var newNode = (ObjectNode) ShipObject.getJsonDiffDeltaAlgorithm().apply(oldNode, friendlyByteBuf);

                oldNode.set("transform", newNode.get("transform"));
                oldNode.set("prevTickTransform", newNode.get("prevTickTransform"));

                VSJacksonUtil.INSTANCE.getDeltaMapper()
                        .readerFor(ShipDataCommon.class)
                        .withValueToUpdate(ship)
                        .readValue(oldNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        friendlyByteBuf.skipBytes(friendlyByteBuf.readableBytes());
    }

}
