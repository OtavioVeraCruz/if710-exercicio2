package br.ufpe.cin.if710.rss.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.ufpe.cin.if710.rss.MainActivity
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper

class MyReceiver: BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
       val db= SQLiteRSSHelper.getInstance(context!!)

    }

}