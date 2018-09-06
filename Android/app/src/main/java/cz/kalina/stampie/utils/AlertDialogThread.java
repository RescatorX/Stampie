package cz.kalina.stampie.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;

public class AlertDialogThread extends Thread {

    public Looper ThreadLooper;
    public Handler mHandler;
    public Boolean Visible;

    public AlertDialog ThreadDialog;
    public Context DialogContext;
    public String DialogTitle;
    public String DialogMessage;

    public AlertDialogThread(Context mContext, String mTitle, String mMessage)
    {
        DialogContext = mContext;
        DialogTitle = mTitle;
        DialogMessage = mMessage;
        Visible = false;
    }

    public void show() {

        this.start();

        Visible = true;

        while (Visible)
            synchronized(this) {
                try { wait(10); }
                catch (InterruptedException e) {}
            }
    }

    public void run()
    {
        Looper.prepare();
        ThreadLooper = Looper.myLooper();

        mHandler = new Handler();

        ThreadDialog = new AlertDialog.Builder(DialogContext).create();
        ThreadDialog.setTitle(DialogTitle);
        ThreadDialog.setMessage(DialogMessage);
        ThreadDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Visible = false;
            }

        });
        // reakce na BACK button
        ThreadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                Visible = false;
            }
        });
        ThreadDialog.show();

        Looper.loop();
    }
}
