package fr.juke.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.juke.commands.util.SuggestionsProvider;
import fr.juke.config.Config;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.io.IOException;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

public class FormatCommand {
    static Config config = Config.INSTANCE;

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("format")
                .executes(FormatCommand::formatCommand));

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("toggleformatting")
                .requires(source -> source.hasPermissionLevel(1))
                .executes(FormatCommand::toggleformatting));

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("setheader")
                .then(CommandManager.argument("text", greedyString())
                        .suggests(SuggestionsProvider.headerFooterSuggestions())
                        .requires(source -> source.hasPermissionLevel(1))
                        .executes(FormatCommand::setHeader)
                ));

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("setfooter")
                .then(CommandManager.argument("text", greedyString())
                        .suggests(SuggestionsProvider.headerFooterSuggestions())
                        .requires(source -> source.hasPermissionLevel(1))
                        .executes(FormatCommand::setFooter)
                ));

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("displayitem")
                .executes(FormatCommand::displayItem)
        );

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("toggletablist")
                .requires(source -> source.hasPermissionLevel(1))
                .executes(FormatCommand::toggleTablist)
        );

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("setmotd")
                .then(CommandManager.argument("text", greedyString())
                        .requires(source -> source.hasPermissionLevel(1))
                        .executes(FormatCommand::setMotd)
                ));
    }

    private static int setMotd(CommandContext<ServerCommandSource> ctx) {
        config.motd = getString(ctx, "text");
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.getSource().sendFeedback((Text) Text.of("set motd").getWithStyle(
                Style.EMPTY.withColor(Formatting.GREEN)
        ), true);
        return 1;
    }

    private static int toggleTablist(CommandContext<ServerCommandSource> ctx) {
        clearTablist(ctx);
        ctx.getSource().sendFeedback((Text) Text.of("Toggled tablist updates").getWithStyle(
                Style.EMPTY.withColor(Formatting.GREEN)
        ), true);
        config.enableTablistFormatting = !config.enableTablistFormatting;
        return 1;
    }

    private static void clearTablist(CommandContext<ServerCommandSource> ctx) {
        config.header = "";
        config.footer = "";
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.getSource().getServer().getPlayerManager().sendToAll(new PlayerListHeaderS2CPacket(
                Text.of(""),
                Text.of("")
        ));
    }

    private static int displayItem(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ItemStack itemStack = ctx.getSource().getPlayer().getStackInHand(Hand.MAIN_HAND);
        if (itemStack == ItemStack.EMPTY) {
            ctx.getSource().sendFeedback((Text) Text.of("You're currently not holding anything!").getWithStyle(
                    Style.EMPTY.withColor(Formatting.RED)
            ), true);
        } else {
            MutableText nametxt = ctx.getSource().getDisplayName().copy();
            Text hoverText = nametxt.append(" wants to show you their ").append(itemStack.toHoverableText());

            ctx.getSource().getServer().getPlayerManager().getPlayerList().forEach(player -> player.sendMessage(hoverText, false));
        }
        return 1;
    }

    private static int setHeader(CommandContext<ServerCommandSource> ctx) {
        config.header = getString(ctx, "text");
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.getSource().sendFeedback((Text) Text.of("Header set.").getWithStyle(
                Style.EMPTY.withColor(Formatting.GREEN)
        ), true);
        return 1;
    }

    private static int setFooter(CommandContext<ServerCommandSource> ctx) {
        config.footer = getString(ctx, "text");
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.getSource().sendFeedback((Text) Text.of("Footer set.").getWithStyle(
                Style.EMPTY.withColor(Formatting.GREEN)
        ), true);
        return 1;
    }

    private static int toggleformatting(CommandContext<ServerCommandSource> ctx) {
        config.enableColor = !config.enableColor;
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.getSource().sendFeedback((Text) Text.of("Toggled text formatting").getWithStyle(
                Style.EMPTY.withColor(Formatting.GREEN)
        ), true);
        return 1;
    }

    private static int formatCommand(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(
                Text.of("""
                        Use the \"&\" character following one of the formatting codes for your message to be formatted.\n Use the \"\\\" character to escape format codes.\n
                        "0 is &0black&f\n
                        "1 is &1dark blue&f\n
                        "2 is &2dark green&f\n
                        "3 is &3dark aqua&f\n
                        "4 is &4dark red&f\n
                        "5 is &5dark purple&f\n
                        "6 is &6gold&f\n
                        "7 is &7gray&f\n
                        "8 is &8dark gray&f\n
                        "9 is &9blue&f\n
                        "a is &agreen&f\n
                        "b is &baqua&f\n
                        "c is &cred&f\n
                        "d is &dlight purple&f\n
                        "e is &eyellow&f\n
                        "f is &fwhite&f\n
                        "----------------------------------------------------\n
                        "k is &kobfuscated&f\n
                        "l is &lbold&f\n
                        "m is &mstrikethrough&f\n
                        "n is &nunderline&f\n
                        "o is &oitalic&f\n
                        "----------------------------------------------------\n
                        "n is #N next line\n
                        "n is #UPTIME show uptime\n
                        "n is #TPS show ticks per second\n
                        "n is #MSPT next line\n
                        "n is #PLAYERCOUNT show how many players are online\n
                        """)
                , false);
        return 1;
    }

}
