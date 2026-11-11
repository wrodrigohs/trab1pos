package br.com.aplicativoswr.marcadortruco

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    data class Jogador(val id: Int, val nome: String, val vitorias: Int)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_JOGADOR (" +
                    "$COL_ID INTEGER PRIMARY KEY, " +
                    "$COL_NOME TEXT NOT NULL, " +
                    "$COL_VITORIAS INTEGER NOT NULL DEFAULT 0)"
        )

        // os dois jogadores já nascem cadastrados com os nomes padrão
        db.insert(TABLE_JOGADOR, null, valoresPadrao(JOGADOR1))
        db.insert(TABLE_JOGADOR, null, valoresPadrao(JOGADOR2))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JOGADOR")
        onCreate(db)
    }

    fun buscar(id: Int): Jogador {
        readableDatabase.query(
            TABLE_JOGADOR, null, "$COL_ID = ?", arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                return Jogador(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_VITORIAS))
                )
            }
        }
        return Jogador(id, nomePadrao(id), 0)
    }

    fun atualizarNome(id: Int, nome: String) {
        val valores = ContentValues()
        valores.put(COL_NOME, nome)
        writableDatabase.update(TABLE_JOGADOR, valores, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun incrementarVitoria(id: Int) {
        writableDatabase.execSQL(
            "UPDATE $TABLE_JOGADOR SET $COL_VITORIAS = $COL_VITORIAS + 1 WHERE $COL_ID = ?",
            arrayOf<Any>(id)
        )
    }

    fun zerar() {
        writableDatabase.update(TABLE_JOGADOR, valoresPadrao(JOGADOR1), "$COL_ID = ?", arrayOf(JOGADOR1.toString()))
        writableDatabase.update(TABLE_JOGADOR, valoresPadrao(JOGADOR2), "$COL_ID = ?", arrayOf(JOGADOR2.toString()))
    }

    private fun valoresPadrao(id: Int): ContentValues {
        val valores = ContentValues()
        valores.put(COL_ID, id)
        valores.put(COL_NOME, nomePadrao(id))
        valores.put(COL_VITORIAS, 0)
        return valores
    }

    private fun nomePadrao(id: Int): String =
        if (id == JOGADOR1) context.getString(R.string.jogador1_padrao)
        else context.getString(R.string.jogador2_padrao)

    companion object {
        const val JOGADOR1 = 1
        const val JOGADOR2 = 2

        private const val DATABASE_NAME = "marcadortruco.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_JOGADOR = "jogador"
        private const val COL_ID = "_id"
        private const val COL_NOME = "nome"
        private const val COL_VITORIAS = "vitorias"
    }
}
