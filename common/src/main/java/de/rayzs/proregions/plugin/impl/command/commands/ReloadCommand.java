package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.command.Command;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ReloadCommand extends Command {

    public ReloadCommand(final ProRegionsAPI api) {
        super(api,
                "reload",
                "reload",
                ""
        );
    }

    @Override
    public boolean onExecute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        final String loadingMessage = api.getMessageProvider().get(
                "reload.loading",
                "&eReloading..."
        );

        final String completedMessage = api.getMessageProvider().get(
                "reload.completed",
                "&aReload completed!"
        );

        api.getMessageProvider().sendMessage(
                sender, loadingMessage
        );

        api.reload();

        api.getMessageProvider().sendMessage(
                sender, completedMessage
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull String[] args) {
        return List.of();
    }
}
