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

    var db:SQLiteRSSHelper?=null
    companion object {
        val FEED_LOADED="br.ufpe.cin.if710.rss.FEED_LOADED"
        val INSERTED_DATA="br.ufpe.cin.if710.rss.INSERTED_DATA"
    }




    override fun onHandleIntent(intent: Intent?) {
        db=SQLiteRSSHelper.getInstance(this.applicationContext)
        Log.d("CarregaFeed", "Ok")
        val link= intent!!.getStringExtra("link")
        try {
            val xml= MainActivity.getRssFeed(link)
            val list = ParserRSS.parse(xml)

            for (item:ItemRSS in list){
                val aux= db!!.getItem(item.link)
                if (aux==null){

                    sendBroadcast(Intent(INSERTED_DATA))
                    Log.d("DB", "Encontrado pela primeira vez: " + item.title)
                    db!!.insertItem(item)
                }
                else{
                    Log.d("DB", "Já foi inserido: " + item.title)
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
            db!!.close()
        }

    }

}