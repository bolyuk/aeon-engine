package bl0.aeon.gl.base;

import bl0.aeon.gl.GLState;
import bl0.bjs.common.base.BJSBaseClass;
import bl0.bjs.common.base.IContext;

public class GLBaseClass extends BJSBaseClass {

    protected final GLState glState;

    public GLBaseClass(GLState state, IContext ctx) {
        super(ctx);
        this.glState = state;
    }
}
