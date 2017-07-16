package co.lateralview.myapp.infraestructure.manager.interfaces;

import java.lang.reflect.Type;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public interface ParserManager
{
    String toJson(Object object);

    <T> T fromJson(String json, Class<T> type);

    <T> T fromJson(String json, Type type);
}
