package kr.ds.clipboard;


import android.content.Context;
import android.text.ClipboardManager;

/**
 * Access the system clipboard using the now deprecated {@link ClipboardManager}
 */
@SuppressWarnings("deprecation")
public class ClipboardManagerApi1 extends kr.ds.clipboard.ClipboardManager {
	
    public ClipboardManagerApi1(Context context) {
        super(context);
    }

    @Override
    public void setText(String label, String text) {
        ClipboardManager clipboardManager =
                (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(text);
        if(mOnChangeListner != null){
        	mOnChangeListner.change(text);
        }
    }
}


