package co.lateralview.myapp.infraestructure.manager.interfaces;

public interface SharedPreferencesManager
{
	void save(String key, boolean value);

	void save(String key, String value);

	void save(String key, int value);

	boolean getBoolean(String key);

	boolean getBoolean(String key, boolean defaultValue);

	String getString(String key);

	String getString(String key, String defaultValue);

	int getInt(String key);

	int getInt(String key, int defaultValue);

	<T> void save(String key, T model);

	<T> T get(String key, Class<T> type);

	void clear();

	void remove(String key);
}
