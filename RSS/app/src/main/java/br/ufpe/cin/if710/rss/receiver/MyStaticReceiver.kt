package br.ufpe.cin.if710.rss.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import br.ufpe.cin.if710.rss.MainActivity
import br.ufpe.cin.if710.rss.R

class MyStaticReceiver: BroadcastReceiver() {
    companion object {
        val channel_id="0"
    }
    override fun onReceive(context: Context?, intent: Intent?) {

        val intent_main=Intent(context,MainActivity::class.java)
        val pendingIntent= PendingIntent.getActivity(context,0,intent_main,0)
        val builder=NotificationCompat.Builder(context!!)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Nova notícia inserida!")
        builder.setContentText("Não fique desatualizado!")
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)

        Log.d("UpdateFeed","Funcionou!")
        NotificationManagerCompat.from(context).notify(0,builder.build())

    }

}