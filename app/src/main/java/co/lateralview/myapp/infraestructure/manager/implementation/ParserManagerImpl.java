package co.lateralview.myapp.infraestructure.manager.implementation;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import co.lateralview.myapp.infraestructure.manager.interfaces.ParserManager;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public class ParserManagerImpl implements ParserManager
{
	public String toJson(Object object)
	{
		return new Gson().toJson(object);
	}

	public <T> T fromJson(String json, Class<T> type)
	{
		return new Gson().fromJson(json, type);
	}

	@Override
	public <T> T fromJson(String json, Type type)
	{
		return new Gson().fromJson(json, type);
	}
}
