package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : Activity() {

    val RSS_FEED= "http://leopoldomt.com/if1001/g1brasil.xml"
    private var mAdapter=FeedAdapter(emptyList(),this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conteudoRSS.layoutManager =LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        conteudoRSS.adapter = mAdapter
    }

    @Throws(IOException::class)
    private fun getRssFeed(feed:String):String {
        var inputStream: InputStream? = null
        var rssFeed = ""
        try{
            val url= URL(feed)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            inputStream = conn.getInputStream()
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var count:Int = inputStream.read(buffer)
            while (count != -1) {
                out.write(buffer, 0, count)
                count = inputStream.read(buffer)
            }
            val response = out.toByteArray()
            rssFeed = String(response, charset("UTF-8"))
        }finally {
            if(inputStream != null) {
                inputStream.close()
            }
        }
        return rssFeed
    }

    override fun onStart() {
        super.onStart()
        try {
            async(RSS_FEED)
        }
        catch (message:IOException){
            message.printStackTrace()
        }

    }

    fun async(url:String){
        doAsync {
            val xml =  getRssFeed(url)
            val list = ParserRSS.parse(xml)
            //permite que conteudos da thread principal posssam ser alteradas para uma thread alternativa
            uiThread {
                //modifica a lista do adapter
                mAdapter.items = list
                mAdapter.notifyDataSetChanged()
            }
        }
    }
}
