/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.loaders;

import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.core.graphic.shaders.Shader;
import org.bl0.aeon.engine.interfaces.parsing.ILoader;

public class FragmentShaderLoader
extends ILoader<Shader> {
    public FragmentShaderLoader(GameContext context) {
        super(context);
    }

    @Override
    public Shader load(String rawData) {
        return new Shader(Shader.ShaderType.FRAGMENT, rawData);
    }

    @Override
    public String parse(Shader data) {
        return data.source;
    }

    @Override
    public String extension() {
        return ".frag";
    }
}

