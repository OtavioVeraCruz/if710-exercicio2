package br.ufpe.cin.if710.rss.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import br.ufpe.cin.if710.rss.ItemRSS
import br.ufpe.cin.if710.rss.MainActivity
import br.ufpe.cin.if710.rss.ParserRSS
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class CarregaFeedClass: IntentService("CarregaFeed") {

    val db=SQLiteRSSHelper.getInstance(this)
    companion object {
        val FEED_LOADED="br.ufpe.cin.if710.rss.action.FEED_LOADED"
    }

    //val INSERTED_DATA="br.ufpe.cin.if710.rss.INSERTED_DATA"


    override fun onHandleIntent(intent: Intent?) {
        val link= intent!!.getStringExtra("link")
        try {
            val xml= MainActivity.getRssFeed(link)
            val list = ParserRSS.parse(xml)

            for (item:ItemRSS in list){
                val aux=db.getItem(item.link)
                if (aux==null){
                    //sendBroadcast(Intent(INSERTED_DATA))
                    db.insertItem(item)
                    Log.d("DB", "Encontrado pela primeira vez: " + item.title)
                }
            }

            sendBroadcast(Intent(FEED_LOADED))
        }
        catch (io:IOException){
            io.printStackTrace()
        }catch (e:XmlPullParserException){
            e.printStackTrace()
        }
        finally {
            db.close()
        }

    }

}