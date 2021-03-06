package kr.ds.config;

import android.content.Context;

public class Config {
	public Context mContext;
	
	public static String URL = "http://parsingds.cafe24.com/app/";
	public static String URL_XML = "karaokesong/";
	public static String URL_LIST = "list.php";
	public static String URL_SEARCH = "search.php";
	public static String URL_BOOKMARK = "bookmark.php";
	public static String URL_CHANNEL = "channel.php";
	public static String URL_RECOM = "recom.php";


	public static String URL_GCM = "gcm/gcm.php";
	public static String URL_SEND_CHECK = "gcm/gcm_send_check.php";
	public static String URL_SEND_UPDATE = "gcm/gcm_send_update.php";


	//gcm
	public static final String ANDROID_ID = "android_id";
	public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
	public static final String REGISTRATION_COMPLETE = "registrationComplete";
	public static final String TOKEN = "token";
	//gcmend


	public final static String PREFERENCE_LOCATION =  "PREFERENCE_LOCATION";//위치동의
	public final static String PREFERENCE_PREV_LAT =  "PREFERENCE_PREV_LAT";
	public final static String PREFERENCE_PREV_LON =  "PREFERENCE_PREV_LON";

	public final static int MARKET = 1;
	public final static int TSTORE = 2;
	public final static int TYPE = MARKET;

	public static String APP_DOWN_URL_MARKET = "kr.ds.karaokesong";
	public static String APP_DOWN_TITLE = "무료 노래방";

	public static final String GCM_INTENT_FILLTER = "kr.ds.GCM_INTENT_FILLTER";
}
