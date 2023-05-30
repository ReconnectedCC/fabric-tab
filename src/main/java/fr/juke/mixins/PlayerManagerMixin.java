package fr.juke.mixins;

import fr.juke.commands.util.TextFormatter;
import fr.juke.config.Config;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow
    public abstract void sendToAll(Packet<?> packet);

    @Inject(at = @At("HEAD"), method = "updatePlayerLatency")
    public void updatePlayerLatency(CallbackInfo ci) {
        @SuppressWarnings("ConstantConditions")
        PlayerListMixin packet = (PlayerListMixin) new PlayerListHeaderS2CPacket(
                Text.literal(TextFormatter.tablistChars(Config.INSTANCE.header)),
                Text.literal(TextFormatter.tablistChars(Config.INSTANCE.footer))
        );
        this.sendToAll(packet);
    }

    @Inject(at = @At("TAIL"), method = "onPlayerConnect")
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        if (!Config.INSTANCE.motd.isEmpty()) player.sendMessage(
                Text.literal(Config.INSTANCE.motd), false);
    }
}
