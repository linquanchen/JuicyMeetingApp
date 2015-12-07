package edu.cmu.juicymeeting.chat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import edu.cmu.juicymeeting.juicymeeting.R;

public class LayerPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //Don't show a notification on boot
        if(intent.getAction() == Intent.ACTION_BOOT_COMPLETED)
            return;

        // Get notification content
        Bundle extras = intent.getExtras();
        String message = "";
        Uri conversationId = null;
        if (extras.containsKey("layer-push-message")) {
            message = extras.getString("layer-push-message");
        }
        if (extras.containsKey("layer-conversation-id")) {
            conversationId = extras.getParcelable("layer-conversation-id");
        }

        // Build the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE);

        // Set the action to take when a user taps the notification
        Intent resultIntent = new Intent(context, GroupChatActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.putExtra("layer-conversation-id", conversationId);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Show the notification
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, mBuilder.build());
    }
}