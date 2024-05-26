package com.example.aman;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
public class LoadingDialog {
    private Context context;
    private String message;
    private AlertDialog dialog;
    private boolean cancel;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    LoadingDialog(Context context, String message, boolean cancel){
        this.context = context;
        this.message = message;
        this.cancel = cancel;
    }
    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_progress_bar,null);
        TextView msgTV = layout.findViewById(R.id.msg);
        msgTV.setText(message);
        builder.setView(layout);
        builder.setCancelable(cancel);
        dialog = builder.create();
        dialog.show();


    }
    void dismissDialog(){
if (dialog!= null &&dialog.isShowing()){
    dialog.dismiss();
}
    }
}
