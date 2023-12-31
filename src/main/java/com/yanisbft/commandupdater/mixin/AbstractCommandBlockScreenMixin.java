package com.yanisbft.commandupdater.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.yanisbft.commandupdater.CommandUpdater;
import com.yanisbft.commandupdater.gui.IconButtonWidget;
import com.yanisbft.commandupdater.nbt.NbtUpdater;
import com.yanisbft.commandupdater.nbt.NbtUpdaters;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mixin(AbstractCommandBlockScreen.class)
public abstract class AbstractCommandBlockScreenMixin extends Screen {
    @Shadow private TextFieldWidget consoleCommandTextField;

    @Shadow abstract CommandBlockExecutor getCommandExecutor();

    private IconButtonWidget relativeCoordinatesButton;
    private IconButtonWidget absoluteCoordinatesButton;
    private IconButtonWidget updateNbtButton;

    protected AbstractCommandBlockScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    private void onInit(CallbackInfo ci) {
        int x = consoleCommandTextField.getX() + consoleCommandTextField.getWidth() + 4;
        int y = consoleCommandTextField.getY();

        relativeCoordinatesButton = addDrawableChild(new IconButtonWidget(x, y, Text.translatable("command_block.button.relative_coordinates"),
                CommandUpdater.id("relative_coordinates"), 15, 15,
                button -> changeToRelative()));

        absoluteCoordinatesButton = addDrawableChild(new IconButtonWidget(x + 24, y, Text.translatable("command_block.button.absolute_coordinates"),
                CommandUpdater.id("absolute_coordinates"), 15, 15,
                button -> changeToAbsolute()));

        updateNbtButton = addDrawableChild(new IconButtonWidget(x, y + 24, Text.translatable("command_block.button.update_nbt"),
                CommandUpdater.id("update_nbt"), 15, 15,
                button -> updateNbt()));
    }

    @Inject(method = "onCommandChanged", at = @At(value = "TAIL"))
    private void onCommandChanged(String text, CallbackInfo ci) {
        String command = consoleCommandTextField.getText();
        Map<CommandNode<CommandSource>, CommandSyntaxException> exceptions = this.client.getNetworkHandler().getCommandDispatcher().parse(command, this.client.getNetworkHandler().getCommandSource()).getExceptions();

        relativeCoordinatesButton.active = exceptions.isEmpty();
        absoluteCoordinatesButton.active = exceptions.isEmpty();
        updateNbtButton.active = exceptions.isEmpty();
    }

    private void changeToRelative() {
        boolean prefix = consoleCommandTextField.getText().startsWith("/");
        String command = prefix ? consoleCommandTextField.getText().substring(1) : consoleCommandTextField.getText();
        BlockPos commandBlockPos = BlockPos.ofFloored(getCommandExecutor().getPos());

        ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
        CommandDispatcher<CommandSource> dispatcher = networkHandler.getCommandDispatcher();
        CommandContextBuilder<?> commandContext = dispatcher.parse(command, networkHandler.getCommandSource()).getContext();

        boolean minecraftPrefix = command.startsWith("minecraft:");

        ParsedCommandNode<?> originalNode = commandContext.getNodes().get(0);
        String originalNodeName = originalNode.getNode().getName();
        if (originalNode.getNode().getRedirect() != null) {
            String realNodeName = originalNode.getNode().getRedirect().getName();
            command = new StringBuilder(command).replace(originalNode.getRange().getStart(), originalNode.getRange().getEnd(), realNodeName).toString();
            command = minecraftPrefix ? "minecraft:" + command : command;
        }

        commandContext = dispatcher.parse(command, networkHandler.getCommandSource()).getContext();
        List<? extends ParsedArgument<?, ?>> args = commandContext.getArguments().values().stream().toList();

        for (int i = 0; i < args.size(); i++) {
            ParsedArgument<?, ?> parsedArg = dispatcher.parse(command, networkHandler.getCommandSource()).getContext().getArguments().values().stream().toList().get(i);
            if (parsedArg.getResult() instanceof DefaultPosArgument posArgument) {
                DefaultPosArgumentAccessor accessor = (DefaultPosArgumentAccessor) posArgument;

                String x = toRelative(commandBlockPos.getX(), accessor.getX());
                String y = toRelative(commandBlockPos.getY(), accessor.getY());
                String z = toRelative(commandBlockPos.getZ(), accessor.getZ());

                String newCoords = x + " " + y + " " + z;
                command = new StringBuilder(command).replace(parsedArg.getRange().getStart(), parsedArg.getRange().getEnd(), newCoords).toString();
                consoleCommandTextField.setText(command);
            }
        }
    }

    private String toRelative(int commandBlockCoord, CoordinateArgument arg) {
        double relative;

        if (arg.isRelative()) {
            relative = arg.toAbsoluteCoordinate(0);
        } else {
            relative = arg.toAbsoluteCoordinate(0) - commandBlockCoord;
        }

        relative = Math.round(relative * 100.0) / 100.0;

        if (relative != 0) {
            return "~" + BigDecimal.valueOf(relative).stripTrailingZeros().toPlainString();
        } else {
            return "~";
        }
    }

    private void changeToAbsolute() {
        boolean prefix = consoleCommandTextField.getText().startsWith("/");
        String command = prefix ? consoleCommandTextField.getText().substring(1) : consoleCommandTextField.getText();
        BlockPos commandBlockPos = BlockPos.ofFloored(getCommandExecutor().getPos());

        ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
        CommandDispatcher<CommandSource> dispatcher = networkHandler.getCommandDispatcher();
        CommandContextBuilder<?> commandContext = dispatcher.parse(command, networkHandler.getCommandSource()).getContext();

        boolean minecraftPrefix = command.startsWith("minecraft:");

        ParsedCommandNode<?> originalNode = commandContext.getNodes().get(0);
        String originalNodeName = originalNode.getNode().getName();
        if (originalNode.getNode().getRedirect() != null) {
            String realNodeName = originalNode.getNode().getRedirect().getName();
            command = new StringBuilder(command).replace(originalNode.getRange().getStart(), originalNode.getRange().getEnd(), realNodeName).toString();
            command = minecraftPrefix ? "minecraft:" + command : command;
        }

        commandContext = dispatcher.parse(command, networkHandler.getCommandSource()).getContext();
        List<? extends ParsedArgument<?, ?>> args = commandContext.getArguments().values().stream().toList();

        for (int i = 0; i < args.size(); i++) {
            ParsedArgument<?, ?> parsedArg = dispatcher.parse(command, networkHandler.getCommandSource()).getContext().getArguments().values().stream().toList().get(i);
            if (parsedArg.getResult() instanceof DefaultPosArgument posArgument) {
                DefaultPosArgumentAccessor accessor = (DefaultPosArgumentAccessor) posArgument;

                String x = toAbsolute(commandBlockPos.getX(), accessor.getX());
                String y = toAbsolute(commandBlockPos.getY(), accessor.getY());
                String z = toAbsolute(commandBlockPos.getZ(), accessor.getZ());

                String newCoords = x + " " + y + " " + z;
                command = new StringBuilder(command).replace(parsedArg.getRange().getStart(), parsedArg.getRange().getEnd(), newCoords).toString();
                consoleCommandTextField.setText(command);
            }
        }
    }

    private String toAbsolute(int commandBlockCoord, CoordinateArgument arg) {
        if (arg.isRelative()) {
            return BigDecimal.valueOf(arg.toAbsoluteCoordinate(0) + commandBlockCoord).stripTrailingZeros().toPlainString();
        }

        return BigDecimal.valueOf(arg.toAbsoluteCoordinate(0)).stripTrailingZeros().toPlainString();
    }

    private void updateNbt() {
        boolean prefix = consoleCommandTextField.getText().startsWith("/");
        String command = prefix ? consoleCommandTextField.getText().substring(1) : consoleCommandTextField.getText();

        ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
        CommandContextBuilder<?> commandContext = networkHandler.getCommandDispatcher().parse(command, networkHandler.getCommandSource()).getContext();

        boolean minecraftPrefix = command.startsWith("minecraft:");

        ParsedCommandNode<?> originalNode = commandContext.getNodes().get(0);
        String originalNodeName = originalNode.getNode().getName();
        if (originalNode.getNode().getRedirect() != null) {
            String realNodeName = originalNode.getNode().getRedirect().getName();
            command = new StringBuilder(command).replace(originalNode.getRange().getStart(), originalNode.getRange().getEnd(), realNodeName).toString();
            command = minecraftPrefix ? "minecraft:" + command : command;
        }

        commandContext = networkHandler.getCommandDispatcher().parse(command, networkHandler.getCommandSource()).getContext();

        for (ParsedArgument<?, ?> parsedArg : commandContext.getArguments().values()) {

            // entity nbt
            if (parsedArg.getResult() instanceof NbtCompound entityNbt) {
                for (NbtUpdater nbtUpdater : NbtUpdaters.ALL) {
                    nbtUpdater.update(entityNbt);
                }

                String newArg = entityNbt.toString();
                command = new StringBuilder(command).replace(parsedArg.getRange().getStart(), parsedArg.getRange().getEnd(), newArg).toString();
                command = prefix ? "/" + command : command;
                consoleCommandTextField.setText(command);
            }

            // item nbt
            if (parsedArg.getResult() instanceof ItemStackArgument itemStackArg) {
                NbtCompound itemNbt = ((ItemStackArgumentAccessor) itemStackArg).getNbt();

                for (NbtUpdater nbtUpdater : NbtUpdaters.ALL) {
                    nbtUpdater.update(itemNbt);
                }

                Identifier itemId = Registries.ITEM.getId(itemStackArg.getItem());
                String newArg = itemId + itemNbt.toString();
                command = new StringBuilder(command).replace(parsedArg.getRange().getStart(), parsedArg.getRange().getEnd(), newArg).toString();
                command = prefix ? "/" + command : command;
                consoleCommandTextField.setText(command);
            }

            // block nbt
            if (parsedArg.getResult() instanceof BlockStateArgument blockStateArg) {
                NbtCompound blockNbt = ((BlockStateArgumentAccessor) blockStateArg).getData();

                for (NbtUpdater nbtUpdater : NbtUpdaters.ALL) {
                    nbtUpdater.update(blockNbt);
                }

                String newArg = BlockArgumentParser.stringifyBlockState(blockStateArg.getBlockState()) + blockNbt.toString();
                command = new StringBuilder(command).replace(parsedArg.getRange().getStart(), parsedArg.getRange().getEnd(), newArg).toString();
                command = prefix ? "/" + command : command;
                consoleCommandTextField.setText(command);
            }
        }
    }
}
