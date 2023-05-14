package com.cmat.lasertech.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class LTKeyBindingBuilder {

    @Nullable
    private String description;
    private IKeyConflictContext keyConflictContext = KeyConflictContext.UNIVERSAL;
    private KeyModifier keyModifier = KeyModifier.NONE;
    @Nullable
    private InputConstants.Key key;
    private String category = "LaserTech";
    @Nullable
    private BiConsumer<KeyMapping, Boolean> onKeyDown;
    @Nullable
    private Consumer<KeyMapping> onKeyUp;
    @Nullable
    private BooleanSupplier toggleable;
    private boolean repeating;

    public LTKeyBindingBuilder description(String description) {
        this.description = Objects.requireNonNull(description, "Description cannot be null.");
        return this;
    }

    public LTKeyBindingBuilder conflictInGame() {
        return conflictContext(KeyConflictContext.IN_GAME);
    }

    public LTKeyBindingBuilder conflictInGui() {
        return conflictContext(KeyConflictContext.GUI);
    }

    public LTKeyBindingBuilder conflictContext(IKeyConflictContext keyConflictContext) {
        this.keyConflictContext = Objects.requireNonNull(keyConflictContext, "Key conflict context cannot be null.");
        return this;
    }

    public LTKeyBindingBuilder modifier(KeyModifier keyModifier) {
        this.keyModifier = Objects.requireNonNull(keyModifier, "Key modifier cannot be null.");
        return this;
    }

    public LTKeyBindingBuilder keyCode(int keyCode) {
        return keyCode(InputConstants.Type.KEYSYM, keyCode);
    }

    public LTKeyBindingBuilder keyCode(InputConstants.Type keyType, int keyCode) {
        Objects.requireNonNull(keyType, "Key type cannot be null.");
        return keyCode(keyType.getOrCreate(keyCode));
    }

    public LTKeyBindingBuilder keyCode(InputConstants.Key key) {
        this.key = Objects.requireNonNull(key, "Key cannot be null.");
        return this;
    }

    public LTKeyBindingBuilder category(String category) {
        this.category = Objects.requireNonNull(category, "Category cannot be null.");
        return this;
    }

    public LTKeyBindingBuilder onKeyDown(BiConsumer<KeyMapping, Boolean> onKeyDown) {
        this.onKeyDown = Objects.requireNonNull(onKeyDown, "On key down cannot be null when manually specified.");
        return this;
    }

    public LTKeyBindingBuilder onKeyUp(Consumer<KeyMapping> onKeyUp) {
        this.onKeyUp = Objects.requireNonNull(onKeyUp, "On key up cannot be null when manually specified.");
        return this;
    }

    public LTKeyBindingBuilder toggleable() {
        return toggleable(() -> true);
    }

    public LTKeyBindingBuilder toggleable(BooleanSupplier toggleable) {
        this.toggleable = Objects.requireNonNull(toggleable, "Toggleable supplier cannot be null when manually specified.");
        return this;
    }

    public LTKeyBindingBuilder repeating() {
        this.repeating = true;
        return this;
    }

    public KeyMapping build() {
        return new LTKeyBinding(
                Objects.requireNonNull(description, "Description has not been set."),
                keyConflictContext,
                keyModifier,
                Objects.requireNonNull(key, "Key has not been set"),
                category,
                onKeyDown,
                onKeyUp,
                toggleable,
                repeating
        );
    }
}
