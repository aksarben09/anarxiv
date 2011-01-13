/**
 * 
 */
package org.anarxiv;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * @author lihe
 *
 */
public class UiUtils 
{
	/**
	 * show an alert box.
	 */
	public static void showErrorMessage(Context context, String errMsg)
	{
		new AlertDialog.Builder(context)
					   .setTitle(R.string.error_dialog_title)
					   .setMessage(errMsg)
					   .setPositiveButton(R.string.confirm_btn_caption, null)
					   .show();
	}
	
	/**
	 * show a toast.
	 */
	public static void showToast(Context context, String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
