package co.lateralview.myapp.infraestructure.networking.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by Julian Falcionelli on 1/8/2017.
 */

public class AnnotationExclusionStrategy implements ExclusionStrategy
{

    @Override
    public boolean shouldSkipField(FieldAttributes f)
    {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz)
    {
        return false;
    }
}