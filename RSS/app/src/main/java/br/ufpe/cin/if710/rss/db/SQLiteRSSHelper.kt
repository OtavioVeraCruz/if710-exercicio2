package br.ufpe.cin.if710.rss.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import br.ufpe.cin.if710.rss.ItemRSS
import org.jetbrains.anko.db.*

class SQLiteRSSHelper(ctx:Context) : ManagedSQLiteOpenHelper(ctx, "if710", null, 1) {

    companion object {
        //Nome da tabela do Banco a ser usada
        val DATABASE_TABLE = "items"
        //Versão atual do banco
        private val DB_VERSION = 1
        //colunas
        val ITEM_ROWID= "_id"
        val ITEM_TITLE = "title"
        val ITEM_DATE = "date"
        val ITEM_DESC = "description"
        val ITEM_LINK = "link"
        val ITEM_READ ="unread"
        val columns = arrayOf(ITEM_ROWID, ITEM_TITLE, ITEM_DATE,ITEM_DESC,ITEM_LINK, ITEM_READ)

        private var instance: SQLiteRSSHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): SQLiteRSSHelper {
            if (instance == null) {
                instance = SQLiteRSSHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(DATABASE_TABLE, true,
                ITEM_ROWID to INTEGER + PRIMARY_KEY+ AUTOINCREMENT,
                ITEM_TITLE to TEXT,
                ITEM_DATE to TEXT,
                ITEM_DESC to TEXT,
                ITEM_LINK to TEXT+ NOT_NULL+ UNIQUE,
                ITEM_READ to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //nao estamos usando
    }

    fun insertItem(item:ItemRSS){
        val db = this.writableDatabase
        db.insert(DATABASE_TABLE, ITEM_TITLE to item.title, ITEM_DATE to item.pubDate,
                    ITEM_DESC to item.description, ITEM_LINK to item.link, ITEM_READ to item.read)

    }

    fun getItem(link:String): ItemRSS? {
        val selection= ITEM_LINK+" = ?"
        val selection_args= arrayOf(link)

        val c:Cursor=this.readableDatabase.query(DATABASE_TABLE, columns,selection,selection_args,
                null,null,null)

        var aux:ItemRSS?=null

        while (c.moveToNext()){
            var title=c.getString(c.getColumnIndexOrThrow(ITEM_TITLE))
            var date=c.getString(c.getColumnIndexOrThrow(ITEM_DATE))
            var desc=c.getString(c.getColumnIndexOrThrow(ITEM_DESC))
            var link=c.getString(c.getColumnIndexOrThrow(ITEM_LINK))
            var read=c.getString(c.getColumnIndexOrThrow(ITEM_READ))

            aux= ItemRSS(title,link,date,desc,read)
        }

        return aux

    }

    fun getItems():List<ItemRSS>{
        val query="select * from "+ DATABASE_TABLE+" where "+ ITEM_READ+" = "+"0"

        val c= this.readableDatabase.rawQuery(query,null)
        var lista= ArrayList<ItemRSS>()
        while (c.moveToNext()){

            var title=c.getString(c.getColumnIndexOrThrow(ITEM_TITLE))
            var date=c.getString(c.getColumnIndexOrThrow(ITEM_DATE))
            var desc=c.getString(c.getColumnIndexOrThrow(ITEM_DESC))
            var link=c.getString(c.getColumnIndexOrThrow(ITEM_LINK))
            var read=c.getString(c.getColumnIndexOrThrow(ITEM_READ))

            if (read.equals("0")) {
                lista.add(ItemRSS(title, link, date, desc, read))
            }
        }
        return lista

    }

    fun markAsRead(link:String):Boolean{
        var aux=false
        val item = getItem(link)
        val content:ContentValues= ContentValues()
        if (item != null) {
            content.put(ITEM_TITLE,item.title)
            content.put(ITEM_LINK,item.link)
            content.put(ITEM_DATE,item.pubDate)
            content.put(ITEM_DESC,item.description)
            content.put(ITEM_READ,"1")
            val clause = ITEM_LINK+ " = ?"
            val up:Int=this.writableDatabase.update(DATABASE_TABLE,content,clause, arrayOf(link))
            aux = (up==1)
            Log.d("Item",item.title+" marcado como lido!")
        }
        return aux
    }

    fun markAsUnRead(link:String):Boolean{
        var aux=false
        val item = getItem(link)
        val content:ContentValues= ContentValues()
        if (item != null) {
            content.put(ITEM_TITLE,item.title)
            content.put(ITEM_LINK,item.link)
            content.put(ITEM_DATE,item.pubDate)
            content.put(ITEM_DESC,item.description)
            content.put(ITEM_READ,"0")
            val clause = ITEM_LINK+ " = ?"
            val up:Int=this.writableDatabase.update(DATABASE_TABLE,content,clause, arrayOf(link))
            aux = (up==1)
        }
        return aux

    }
}
// Access property from Context
val Context.database: SQLiteRSSHelper
    get() = SQLiteRSSHelper.getInstance(getApplicationContext())


