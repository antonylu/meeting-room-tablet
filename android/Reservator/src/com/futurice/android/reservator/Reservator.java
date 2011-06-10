package com.futurice.android.reservator;

import com.futurice.android.reservator.model.DataProxy;
import com.futurice.android.reservator.model.ReservatorException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Reservator extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SharedPreferences preferences = getSharedPreferences(
				this.getString(R.string.PREFERENCES_NAME), 0);

		if (preferences.contains("username")
				&& preferences.contains("password")
				&& login(preferences.getString("username", null), preferences.getString("password", null))) {
			// do nothing, activity is changed in login
		} else {
			setContentView(R.layout.login);
			((Button) findViewById(R.id.loginButton)).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		TextView password = (TextView) findViewById(R.id.password);
		TextView username = (TextView) findViewById(R.id.username);
		login(username.getText().toString(), password.getText().toString());
		v.setEnabled(true);
	}

	private boolean login(String username, String password) {

		DataProxy dataProxy = ((ReservatorApplication) getApplication())
				.getDataProxy();
		try {
			dataProxy.setCredentials(username, password);
			dataProxy.getRooms(); // checks the credentials with room query
			SharedPreferences preferences = getSharedPreferences(
					this.getString(R.string.PREFERENCES_NAME), 0);
			Editor editor = preferences.edit();
			editor.putString("username", username);
			editor.putString("password", password);
			editor.commit();

			Intent i = new Intent(this, HomeActivity.class);
			startActivityForResult(i, 0);
			return true;
		} catch (ReservatorException ex) {
			Toast err = Toast.makeText(this, ex.getMessage(),
					Toast.LENGTH_LONG);
			err.show();
			return false;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}

}
