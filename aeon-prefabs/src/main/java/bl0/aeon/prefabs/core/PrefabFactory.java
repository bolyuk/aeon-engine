package bl0.aeon.prefabs.core;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.exception.DeveloperException;
import bl0.aeon.base.exception.WrongOperationException;
import bl0.aeon.prefabs.base.PrefabData;
import bl0.aeon.prefabs.data.ResourceData;
import bl0.aeon.render.common.base.IName;
import bl0.aeon.render.common.base.IResource;

import java.util.ArrayList;

public class PrefabFactory {

    public static Prefab createPrefab(IName object) {
        if(object == null)
            throw new NullPointerException("object is null");

        if(object instanceof Prefab)
            throw new WrongOperationException("trying to create a prefab with an object of type Prefab");
        Class<?> clazz = object.getClass();

        String name = object.getName()+"_prefab";
        String className = clazz.getName();


        ArrayList<PrefabData> data = new ArrayList<>();

        for(var field : clazz.getFields()){
            try {
                field.setAccessible(true);
                var fData = field.get(object);
                if(fData instanceof IResource ir){
                    data.add(new ResourceData(field.getName(), ir.getName()));
                }
            } catch (Exception e){
                throw new DeveloperException(e.getMessage());
            }
        }

        return new Prefab(name, className,data.toArray(new PrefabData[0]));
    }

    public static IName initialize(Prefab prefab, IEngineContext eCtx) {

    }
}
