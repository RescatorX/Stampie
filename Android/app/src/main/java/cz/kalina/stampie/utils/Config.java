package cz.kalina.stampie.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Config extends ObscuredSharedPreferences {

    public static Boolean inicialized = false;
    public static Config instance = null;

    public static final String MAP_POSX = "mapposx";
    public static final String MAP_POSY = "mapposy";
    public static final String MAP_ZOOM = "mapzoom";

    public Config(Context ctx, SharedPreferences prefs) {

        super(ctx, prefs);

        if (instance == null) instance = this;

        inicialized = true;
    }

    public static Boolean getExists(String key) throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        return instance.contains(key);
    }

    public static void removeKey(String key) throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        instance.edit().remove(key).commit();
    }

    public static void removeAll() throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        instance.edit().clear().commit();
    }

    public static Double getMapPosX(Double def) throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        Double value = (double)instance.getFloat(MAP_POSX, def.floatValue());
        if ((value == null) && (def != null)) throw new Exception("Config key not found");

        return value;
    }

    public static void putMapPosX(Double val) throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        Boolean success = instance.edit().putFloat(MAP_POSX, val.floatValue()).commit();
        if (!success) throw new Exception("Value cannot be saved");
    }

    public static Double getMapPosY(Double def) throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        Double value = (double)instance.getFloat(MAP_POSY, def.floatValue());
        if ((value == null) && (def != null)) throw new Exception("Config key not found");

        return value;
    }

    public static void putMapPosY(Double val) throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        Boolean success = instance.edit().putFloat(MAP_POSY, val.floatValue()).commit();
        if (!success) throw new Exception("Value cannot be saved");
    }

    public static Float getMapZoom(Float def) throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        Float value = instance.getFloat(MAP_ZOOM, def);
        if ((value == null) && (def != null)) throw new Exception("Config key not found");

        return value;
    }

    public static void putMapZoom(Float val) throws Exception {

        if (!inicialized) throw new Exception("Configuration not inicialized");

        Boolean success = instance.edit().putFloat(MAP_ZOOM, val).commit();
        if (!success) throw new Exception("Value cannot be saved");
    }
}
