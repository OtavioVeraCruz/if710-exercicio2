package br.ufpe.cin.if710.rss.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.ufpe.cin.if710.rss.ItemRSS
import org.jetbrains.anko.db.*

class SQLiteRSSHelper(ctx:Context) : ManagedSQLiteOpenHelper(ctx, "if710", null, 1) {
    companion object {
        //Nome da tabela do Banco a ser usada
        val DATABASE_TABLE = "items"
        //Versão atual do banco
        private val DB_VERSION = 1
        //colunas
        val ITEM_ROWID = "_id"
        val ITEM_TITLE = "title"
        val ITEM_DATE = "date"
        val ITEM_DESC = "description"
        val ITEM_LINK = "link"
        val columns = arrayOf(ITEM_ROWID, ITEM_TITLE, ITEM_DATE,ITEM_DESC,ITEM_LINK)

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
                ITEM_LINK to TEXT+ NOT_NULL+ UNIQUE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //nao estamos usando
    }

    fun insertItem(item:ItemRSS){
        val db = this.writableDatabase

        db.insert(DATABASE_TABLE, ITEM_TITLE to item.title, ITEM_DATE to item.pubDate,
                ITEM_DESC to item.description, ITEM_LINK to item.link)
    }
}
// Access property for Context
val Context.database: SQLiteRSSHelper
    get() = SQLiteRSSHelper.getInstance(getApplicationContext())


