package ar.com.sofrecom.app.gastos;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ar.com.sofrecom.av.util.GenLog;

public class DropboxAuthenticate extends Activity {
	private static final String TAG = "DropboxAuthenticate";
	final static private String APP_KEY = "r61t9vlxqdi5oen";
	final static private String APP_SECRET = "57vq5g6l3mn22cf";
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
	private static final String ACCESS_KEY_NAME = "dropboxkey";
	private static final String ACCESS_SECRET_NAME = "dropboxsecret";
	public static final String START_ACTIVITY_IF_LOGGED_IN = "startActivityIfLoggedIn";
    private boolean loggedIn;
    private Button submit;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidAuthSession session = buildSession();
        Main.dbApi = new DropboxAPI<AndroidAuthSession>(session);
        setContentView(R.layout.dropbox_authenticate);
        submit = (Button) findViewById(R.id.auth_button);
		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// This logs you out if you're logged in, or vice versa
				if (loggedIn) {
					logOut();
				} else {
					// Start the remote authentication
					Main.dbApi.getSession().startAuthentication(DropboxAuthenticate.this);
				}
			}
		});        
        setLoggedIn(Main.dbApi.getSession().isLinked());
        if (loggedIn) {
        	startActivityIfAny();
        }
	}
	
	@Override
	protected void onResume() {
	    super.onResume();

	    if (loggedIn && (getActivityName() != null)) {
	    	finish();
	    }
	    
	    if (Main.dbApi.getSession().authenticationSuccessful()) {
	        try {
	            // MANDATORY call to complete auth.
	            // Sets the access token on the session
	            Main.dbApi.getSession().finishAuthentication();

	            AccessTokenPair tokens = Main.dbApi.getSession().getAccessTokenPair();

	            // Provide your own storeKeys to persist the access token pair
	            // A typical way to store tokens is using SharedPreferences
	            storeKeys(tokens.key, tokens.secret);
                setLoggedIn(true);
                startActivityIfAny();
	        } catch (IllegalStateException e) {
	    		GenLog.errorMsg(TAG, this, e);
	        }
	    }

	    // ...
	}
	
	@SuppressWarnings("unchecked")
	private void startActivityIfAny() {
		String activityName = getActivityName();
		if (activityName != null) {
	    	Class<Activity> activity;
	    	try {
				activity = (Class<Activity>) Class.forName(activityName);
	            Intent auth = new Intent(this, activity);
	            startActivity(auth);	        	
			} catch (ClassNotFoundException e) {
				GenLog.err(TAG, e);
			}
		}
	}
	
	private String getActivityName() {
		String activityName = null;
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
	        activityName = bundle.getString(START_ACTIVITY_IF_LOGGED_IN);
		}
		return activityName;
	}

    private void logOut() {
        // Remove credentials from the session
        Main.dbApi.getSession().unlink();
        // Clear our stored keys
        clearKeys();
        setLoggedIn(false);
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    private void setLoggedIn(boolean loggedIn) {
    	this.loggedIn = loggedIn;
    	if (loggedIn) {
    		submit.setText(R.string.dropboxUnlink);
    	} else {
    		submit.setText(R.string.dropboxLink);
    	}
    }

	private void storeKeys(String key, String secret) {
		SharedPreferences prefs = getSharedPreferences(Main.PREF_PACKAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(ACCESS_KEY_NAME, key);
		editor.putString(ACCESS_SECRET_NAME, secret);
		editor.commit();
	}
	
    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(Main.PREF_PACKAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(ACCESS_KEY_NAME);
        editor.remove(ACCESS_SECRET_NAME);
        editor.commit();
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     *
     * @return Array of [access_key, access_secret], or null if none stored
     */
    private String[] getKeys() {
    	SharedPreferences prefs = getSharedPreferences(Main.PREF_PACKAGE, Context.MODE_PRIVATE);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
        	String[] ret = new String[2];
        	ret[0] = key;
        	ret[1] = secret;
        	return ret;
        } else {
        	return null;
        }
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session;
        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }
        return session;
    }

}
