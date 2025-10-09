/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.engine.loaders;

import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.core.graphic.shaders.Shader;
import org.bl0.aeon.engine.interfaces.parsing.ILoader;

public class VertexShaderLoader
extends ILoader<Shader> {
    public VertexShaderLoader(GameContext context) {
        super(context);
    }

    @Override
    public Shader load(String rawData) {
        return new Shader(Shader.ShaderType.VERTEX, rawData);
    }

    @Override
    public String parse(Shader data) {
        return data.source;
    }

    @Override
    public String extension() {
        return ".vert";
    }
}

