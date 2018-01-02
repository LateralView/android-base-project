package co.lateralview.myapp.infraestructure.networking;

import co.lateralview.myapp.BuildConfig;

public class RestConstants
{
    public static final String BASE_URL = BuildConfig.BASE_URL;

    //TODO Set Auth Header
    public static final String HEADER_AUTH = "auth-header";

    public enum Subcode
    {
        INVALID_TOKEN(200002);

        private int mSubcode;

        Subcode(int subcode)
        {
            mSubcode = subcode;
        }

        public static Subcode fromInt(int code)
        {
            for (Subcode subcode : Subcode.values())
            {
                if (subcode.getSubcode() == code)
                {
                    return subcode;
                }
            }
            return null;
        }

        public int getSubcode()
        {
            return mSubcode;
        }
    }
}
