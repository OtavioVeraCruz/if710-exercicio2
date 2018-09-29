package br.ufpe.cin.if710.rss.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.ufpe.cin.if710.rss.MainActivity

class MyDynamicReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null) {
            MainActivity.exibirFeed(context)
        }
    }

}