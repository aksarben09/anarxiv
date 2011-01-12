/**
 * 
 */
package org.anarxiv;

import android.app.AlertDialog;
import android.content.Context;

/**
 * @author lihe
 *
 */
public class UIUtils 
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
}
