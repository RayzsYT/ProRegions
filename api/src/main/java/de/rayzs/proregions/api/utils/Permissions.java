package de.rayzs.proregions.api.utils;

import org.bukkit.command.CommandSender;

public enum Permissions {

    COMMAND                             ("proregions.command.%s"),

    BYPASS_PERMISSION                   ("proregions.bypass"),

    BYPASS_PERMISSION_REGION            ("proregions.bypass.%"),        // region
    BYPASS_PERMISSION_REGION_FLAG       ("proregions.bypass.%.%"),      // region flag
    BYPASS_PERMISSION_REGION_FLAG_SPEC  ("proregions.bypass.%.%.%");    // region, flag, specification



    private final String permission;
    private final boolean requiresArguments;

    Permissions(final String permission) {
        this.permission = permission;
        this.requiresArguments = permission.contains("%");
    }

    /**
     * Returns the permission of this enum.
     *
     * @return The permission.
     */
    public String getPermission() {
        if (this.requiresArguments) {
            // Expects arguments.
            throw new IllegalStateException("This permission requires arguments: " + this.permission);
        }

        return this.permission;
    }

    /**
     * Returns the permission of this enum with the given arguments.
     *
     * @param arguments the arguments to replace the placeholders in the permission.
     * @return The permission with the given arguments.
     */
    public String getPermission(final String... arguments) {
        return format(arguments);
    }

    /**
     * Checks if the sender has the permission of this enum.
     *
     * @param sender the sender to check the permission for.
     * @return true if the sender has the permission, false otherwise.
     */
    public boolean hasPermission(final CommandSender sender) {
        if (this.requiresArguments) {
            // Expects arguments.
            throw new IllegalStateException("This permission requires arguments: " + this.permission);
        }

        return sender.hasPermission(this.permission);
    }

    /**
     * Checks if the sender has the permission of this enum with the given arguments.
     *
     * @param sender the sender to check the permission for.
     * @param arguments the arguments to replace the placeholders in the permission.
     * @return true if the sender has the permission, false otherwise.
     */
    public boolean hasPermission(final CommandSender sender, final String... arguments) {
        return sender.hasPermission(format(arguments));
    }

    /**
     * Formats the permission by replacing the placeholders with the given arguments.
     *
     * @param arguments the arguments to replace the placeholders in the permission.
     * @return The formatted permission.
     */
    private String format(final String... arguments) {
        if (!this.requiresArguments) {
            // Does not expect arguments. So it skips the formatting and just returns the permission as it is.
            return this.permission;
        }

        final String[] cpy = this.permission.split("\\.");

        int argumentIndex = 0;
        for (int i = 0; i < cpy.length; i++) {
            if (argumentIndex == arguments.length) {
                // Not enough arguments provided.
                throw new IllegalArgumentException("Arguments are missing for the permission: " + this.permission);
            }

            // Replaces the placeholder.
            if (cpy[i].equalsIgnoreCase("%")) {
                cpy[i] = arguments[argumentIndex];
            }

            argumentIndex++;
        }

        return String.join(".", cpy);
    }
}