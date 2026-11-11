package br.com.aplicativoswr.marcadortruco

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvJogador1: TextView
    private lateinit var tvPontos1: TextView
    private lateinit var tvJogador2: TextView
    private lateinit var tvPontos2: TextView

    private lateinit var banco: DatabaseHandler

    private var pontos1 = 0
    private var pontos2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvJogador1 = findViewById(R.id.tvJogador1)
        tvPontos1 = findViewById(R.id.tvPontos1)
        tvJogador2 = findViewById(R.id.tvJogador2)
        tvPontos2 = findViewById(R.id.tvPontos2)

        banco = DatabaseHandler(this)

        if (savedInstanceState != null) {
            pontos1 = savedInstanceState.getInt(PONTOS1)
            pontos2 = savedInstanceState.getInt(PONTOS2)

            if (pontos1 >= PONTOS_VITORIA || pontos2 >= PONTOS_VITORIA) {
                pontos1 = 0
                pontos2 = 0
            }
        }

        atualizarTela()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PONTOS1, pontos1)
        outState.putInt(PONTOS2, pontos2)
    }

    override fun onDestroy() {
        banco.close()
        super.onDestroy()
    }

    fun btPontuarOnClick(view: View) {
        when (view.id) {
            R.id.btJog1Mais1 -> pontos1 += 1
            R.id.btJog1Mais3 -> pontos1 += 3
            R.id.btJog1Mais6 -> pontos1 += 6
            R.id.btJog1Mais9 -> pontos1 += 9
            R.id.btJog1Mais12 -> pontos1 += 12
            R.id.btJog2Mais1 -> pontos2 += 1
            R.id.btJog2Mais3 -> pontos2 += 3
            R.id.btJog2Mais6 -> pontos2 += 6
            R.id.btJog2Mais9 -> pontos2 += 9
            R.id.btJog2Mais12 -> pontos2 += 12
        }

        val vencedor = when {
            pontos1 >= PONTOS_VITORIA -> DatabaseHandler.JOGADOR1
            pontos2 >= PONTOS_VITORIA -> DatabaseHandler.JOGADOR2
            else -> null
        }

        atualizarTela()

        if (vencedor != null) {
            banco.incrementarVitoria(vencedor)

            AlertDialog.Builder(this)
                .setTitle(R.string.titulo_vitoria)
                .setMessage(getString(R.string.msg_vitoria, banco.buscar(vencedor).nome))
                .setCancelable(false)
                .setPositiveButton(R.string.ok) { _, _ ->
                    pontos1 = 0
                    pontos2 = 0
                    atualizarTela()
                }
                .show()
        }
    }

    fun btHistoricoOnClick(view: View) {
        val intent = Intent(this, HistoricoActivity::class.java)
        startActivity(intent)
    }

    fun btZerarHistoricoOnClick(view: View) {
        pontos1 = 0
        pontos2 = 0
        banco.zerar()
        atualizarTela()

        Toast.makeText(this, getString(R.string.msg_historico_zerado), Toast.LENGTH_SHORT).show()
    }

    fun btInfNomesOnClick(view: View) {
        val intent = Intent(this, NomesActivity::class.java)
        intent.putExtra(NOME1, banco.buscar(DatabaseHandler.JOGADOR1).nome)
        intent.putExtra(NOME2, banco.buscar(DatabaseHandler.JOGADOR2).nome)
        getResult.launch(intent)
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getStringExtra(NOME1)?.let {
                    banco.atualizarNome(DatabaseHandler.JOGADOR1, it)
                }
                resultado.data?.getStringExtra(NOME2)?.let {
                    banco.atualizarNome(DatabaseHandler.JOGADOR2, it)
                }
                atualizarTela()
                Toast.makeText(this, getString(R.string.msg_nomes_alterados), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun atualizarTela() {
        tvJogador1.text = getString(R.string.nome_jogador, banco.buscar(DatabaseHandler.JOGADOR1).nome)
        tvJogador2.text = getString(R.string.nome_jogador, banco.buscar(DatabaseHandler.JOGADOR2).nome)
        tvPontos1.text = pontos1.toString()
        tvPontos2.text = pontos2.toString()
    }

    companion object {
        const val PONTOS_VITORIA = 12

        const val NOME1 = "nome1"
        const val NOME2 = "nome2"
        private const val PONTOS1 = "pontos1"
        private const val PONTOS2 = "pontos2"
    }

}
