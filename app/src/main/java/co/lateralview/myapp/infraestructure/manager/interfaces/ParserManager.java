package co.lateralview.myapp.infraestructure.manager.interfaces;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public interface ParserManager
{
	String toJson(Object object);

	<T> T fromJson(String json, Class<T> type);
}
