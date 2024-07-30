package fr.juke.mixins;

import fr.juke.config.Config;
import fr.juke.util.TextFormatter;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    public int customizationUpdateTimer;

    @Shadow
    public abstract void sendToAll(Packet<?> packet);

    @Inject(at = @At("HEAD"), method = "updatePlayerLatency")
    public void updatePlayerLatency(CallbackInfo ci) {
        if (
                Config.INSTANCE.enabled
                        && ++this.customizationUpdateTimer >
                        (Config.INSTANCE.refreshTickInterval == 0
                                ? 20 // fallback if unset in config file
                                : Config.INSTANCE.refreshTickInterval)
        ) {
            this.sendToAll(new PlayerListHeaderS2CPacket(
                    Text.literal(TextFormatter.tablistChars(
                            Config.INSTANCE.header != null
                                    ? Config.INSTANCE.header
                                    : "" // fallback if unset in config file
                    )),
                    Text.literal(TextFormatter.tablistChars(
                            Config.INSTANCE.footer != null
                                    ? Config.INSTANCE.footer
                                    : "" // fallback if unset in config file
                    ))));
            this.customizationUpdateTimer = 0;
        }
    }

    @Inject(at = @At("TAIL"), method = "onPlayerConnect")
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        if (
                Config.INSTANCE.enabled
                        && Config.INSTANCE.motd != null // check if set in config file
                        && !Config.INSTANCE.motd.isEmpty())
            player.sendMessage(
                    Text.literal(Config.INSTANCE.motd), false);
    }
}
