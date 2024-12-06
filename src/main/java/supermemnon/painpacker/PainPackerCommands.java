package supermemnon.painpacker;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ObjectiveArgument;
import net.minecraft.command.arguments.ScoreHolderArgument;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.text.StringTextComponent;

import java.util.Map;

public class PainPackerCommands {
    public static final String baseCommand = "painpacker";
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> commandStructure = Commands.literal(baseCommand).requires(source -> source.hasPermission(2));
        commandStructure = appendScoreboardCommand(commandStructure);

        dispatcher.register(commandStructure);
    }

    private static LiteralArgumentBuilder<CommandSource> appendScoreboardCommand(LiteralArgumentBuilder<CommandSource> command) {
        return command.then(Commands.literal("scoreboard")
                .then(Commands.literal("transferall")
                        .then(Commands.argument("from", ScoreHolderArgument.scoreHolder())
                                .then(Commands.argument("to", ScoreHolderArgument.scoreHolder())
                                        .executes(context -> runScoreboardTransferAll(context.getSource(), ScoreHolderArgument.getName(context, "from"), ScoreHolderArgument.getName(context, "to")))
                                )
                        )
                )
                .then(Commands.literal("transfer")
                        .then(Commands.argument("objective", ObjectiveArgument.objective())
                                .then(Commands.argument("from", ScoreHolderArgument.scoreHolder())
                                        .then(Commands.argument("to", ScoreHolderArgument.scoreHolder())
                                                .executes(context -> runScoreboardTransferObjective(context.getSource(), ScoreHolderArgument.getName(context, "from"), ScoreHolderArgument.getName(context, "to"), ObjectiveArgument.getObjective(context, "objective")))
                                        )
                                )
                        )
                )
        );
    }

//    private static LiteralArgumentBuilder<CommandSource> appendRemoveCommand(LiteralArgumentBuilder<CommandSource> command) {
//        return command.then(Commands.literal("remove")
//                .then(Commands.literal("requireditem")
//                        .then(Commands.argument("index", IntegerArgumentType.integer())
//                                .executes(
//                                        context -> runRemoveRequiredItem(context.getSource(), IntegerArgumentType.getInteger(context, "index"))
//                                )
//                        )
//                )
//                .then(Commands.literal("pokelootcommand")
//                        .then(Commands.argument("blockpos", BlockPosArgument.blockPos())
//                                .then(Commands.argument("index", IntegerArgumentType.integer())
//                                        .executes(
//                                                context -> runRemovePokeLootCommand(context.getSource(), BlockPosArgument.getOrLoadBlockPos(context, "blockpos"), IntegerArgumentType.getInteger(context, "index"))
//                                        )
//                                )
//                        )
//                )
//                .then(Commands.literal("dialogue")
//                        .then(Commands.argument("index", IntegerArgumentType.integer())
//                                .executes(context -> runRemoveCustomDialogue(context.getSource(), IntegerArgumentType.getInteger(context, "index"))
//                                )
//                        )
//                )
//                .then(Commands.literal("npcstare")
//                        .then(Commands.argument("entity", EntityArgument.entity())
//                                .executes(context -> runRemoveNpcStare(context.getSource(), EntityArgument.getEntity(context, "entity"))
//                                )
//                        )
//                )
//        );
//    }

    private static int runScoreboardTransferAll(CommandSource source, String from, String to) throws CommandSyntaxException {
            Map<ScoreObjective, Score> fromScores = source.getServer().getScoreboard().getPlayerScores(from);
            for (ScoreObjective objective : fromScores.keySet()) {
                source.getServer().getScoreboard().getOrCreatePlayerScore(to, objective).setScore(fromScores.get(objective).getScore());
            }
            source.sendSuccess(new StringTextComponent(String.format("Transferred all objective scores from %s to %s", from, to)), false);
            return 0;
    }

    private static int runScoreboardTransferObjective(CommandSource source, String from, String to, ScoreObjective objective) throws CommandSyntaxException {
        int value = source.getServer().getScoreboard().getOrCreatePlayerScore(from, objective).getScore();
        source.getServer().getScoreboard().getOrCreatePlayerScore(to, objective).setScore(value);
        source.sendSuccess(new StringTextComponent(String.format("Transferred objective %s score from %s to %s", objective.getName(), from, to)), false);
        return 0;
    }
}
