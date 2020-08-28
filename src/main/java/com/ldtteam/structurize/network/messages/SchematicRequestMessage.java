package com.ldtteam.structurize.network.messages;

import com.ldtteam.structurize.Network;
import com.ldtteam.structurize.api.util.Log;
import com.ldtteam.structurize.util.StructureLoadingUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Request a schematic from the server.
 * Created: Feb 07, 2017
 *
 * @author xavier
 */
public class SchematicRequestMessage implements IMessage
{

    private String filename;

    /**
     * Empty constructor used when registering the message.
     */
    public SchematicRequestMessage()
    {
        super();
    }

    /**
     * Creates a Schematic request message.
     *
     * @param filename of the structure based on schematics folder
     *                 Ex: schematics/stone/Builder1.nbt
     */
    public SchematicRequestMessage(final String filename)
    {
        super();
        this.filename = filename;
    }

    @Override
    public void fromBytes(@NotNull final PacketBuffer buf)
    {
        filename = buf.readString(32767);
    }

    @Override
    public void toBytes(@NotNull final PacketBuffer buf)
    {
        buf.writeString(filename);
    }

    @Nullable
    @Override
    public LogicalSide getExecutionSide()
    {
        return LogicalSide.SERVER;
    }

    @Override
    public void onExecute(final NetworkEvent.Context ctxIn, final boolean isLogicalServer)
    {
        final byte[] structureAsByteArray = StructureLoadingUtils.getByteArray(filename);
        if (structureAsByteArray.length == 0)
        {
            Log.getLogger().error("SchematicRequestMessage: file \"" + filename + "\" not found");
        }
        else
        {
            Log.getLogger().info("Request: player " + ctxIn.getSender().getName().getUnformattedComponentText() + " is requesting schematic " + filename);
            Network.getNetwork().sendToPlayer(new SchematicSaveMessage(structureAsByteArray, UUID.randomUUID(), 1, 1), ctxIn.getSender());
        }
    }
}
