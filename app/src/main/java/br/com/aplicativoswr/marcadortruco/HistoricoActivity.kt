package br.com.aplicativoswr.marcadortruco

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HistoricoActivity : AppCompatActivity() {

    private lateinit var tvNomeJogador1: TextView
    private lateinit var tvVitorias1: TextView
    private lateinit var tvNomeJogador2: TextView
    private lateinit var tvVitorias2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historico)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvNomeJogador1 = findViewById(R.id.tvNomeJogador1)
        tvVitorias1 = findViewById(R.id.tvVitorias1)
        tvNomeJogador2 = findViewById(R.id.tvNomeJogador2)
        tvVitorias2 = findViewById(R.id.tvVitorias2)

        DatabaseHandler(this).use { banco ->
            val jogador1 = banco.buscar(DatabaseHandler.JOGADOR1)
            val jogador2 = banco.buscar(DatabaseHandler.JOGADOR2)

            tvNomeJogador1.text = getString(R.string.nome_jogador, jogador1.nome)
            tvNomeJogador2.text = getString(R.string.nome_jogador, jogador2.nome)
            tvVitorias1.text = jogador1.vitorias.toString()
            tvVitorias2.text = jogador2.vitorias.toString()
        }

    }
}
