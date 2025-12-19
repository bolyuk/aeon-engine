package bl0.aeon.render.common.resource;

import bl0.aeon.render.common.base.IName;

public interface IResource extends IName {
    IResource makeCopy(String name);
}

