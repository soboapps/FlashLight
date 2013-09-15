package com.soboapps.flashlight;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.Toast;

public class About extends Activity {

	private ContextWrapper context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		//try {
		//	String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		//} catch (NameNotFoundException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		//PackageManager manager = this.getPackageManager();
		//PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
		//Toast.makeText(this,
		//     "PackageName = " + info.packageName + "\nVersionCode = "
		//       + info.versionCode + "\nVersionName = "
		//       + info.versionName + "\nPermissions = " + info.permissions, Toast.LENGTH_SHORT).show();
	}
}
