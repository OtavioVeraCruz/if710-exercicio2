package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity()  {

    var RSS_FEED=""
    private var mAdapter=FeedAdapter(emptyList(),this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conteudoRSS.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false) as RecyclerView.LayoutManager?
        conteudoRSS.adapter = mAdapter
        RSS_FEED = getString(R.string.RSS)
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
            val prefs= PreferenceManager.getDefaultSharedPreferences(this)
            val feed= prefs.getString(USERNAME,RSS_FEED)
            async(feed)
        }
        catch (message:IOException){
            message.printStackTrace()
        }

    }
    companion object {
        val USERNAME = "uname"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.prefs -> {
              startActivity(Intent(this@MainActivity,PrefsActivity::class.java))
              return true
            }
        }
        return super.onOptionsItemSelected(item)
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
