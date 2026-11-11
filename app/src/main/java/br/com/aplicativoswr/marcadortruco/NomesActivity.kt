package br.com.aplicativoswr.marcadortruco

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NomesActivity : AppCompatActivity() {

    private lateinit var etJogador1: EditText
    private lateinit var etJogador2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nomes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etJogador1 = findViewById(R.id.etJogador1)
        etJogador2 = findViewById(R.id.etJogador2)

        if (savedInstanceState == null) {
            etJogador1.setText(intent.getStringExtra(MainActivity.NOME1))
            etJogador2.setText(intent.getStringExtra(MainActivity.NOME2))
        }

    }

    fun btConfirmarOnClick(view: View) {
        val nome1 = etJogador1.text.toString().trim()
            .ifEmpty { getString(R.string.jogador1_padrao) }
        val nome2 = etJogador2.text.toString().trim()
            .ifEmpty { getString(R.string.jogador2_padrao) }

        val intent = Intent()
        intent.putExtra(MainActivity.NOME1, nome1)
        intent.putExtra(MainActivity.NOME2, nome2)
        setResult(RESULT_OK, intent)
        finish()
    }
}
