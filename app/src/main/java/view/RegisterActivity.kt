package view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafioestagiobloomidea.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    // Firebase.
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializando o objeto FireBaseAuth
        auth = FirebaseAuth.getInstance()


        // Função que ao ser disparada, irá levar o usuário a tela de logar na conta.
        actionLoginLayout()

        // Função que irá registrar novos usuários, verificando se os campos foram preenchidos corretamente.
        actionRegisterNewUser()

    }

    // Função que irá registrar novos usuários, verificando se os campos foram preenchidos corretamente, utilizando
    // a autenticação da biblioteca do FireBase.
    fun actionRegisterNewUser() {
        val novoEmail = findViewById<EditText>(R.id.edtNovoEmailUsuario)
        val novaPalavraPasse = findViewById<EditText>(R.id.edtNovaPalavraPasse)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {

            // Recuperando os valores dos campos Email e Palavra passe e as convertendo.
            val email = novoEmail.text.toString()
            val palavraPasse = novaPalavraPasse.text.toString()

            // If condicionais para verificar os preenchimentos dos campos e-mail e palavra passe.
            if (email.isEmpty()) {
                novoEmail.error = "Necessário preencher o campo e-mail."
                novoEmail.requestFocus()
                return@setOnClickListener // Retornando o método setOnClickLister, evita da aplicação crachar caso os campos estejam vázio.
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                novoEmail.error = "Tipo de e-mail inválido"
                novoEmail.requestFocus()
                return@setOnClickListener // Retornando o método setOnClickLister, evita da aplicação crachar caso os campos estejam vázio.
            }

            if (palavraPasse.isEmpty() || palavraPasse.length < 6) {
                novaPalavraPasse.error = "Minímo 6 caractéres na palavra passe."
                novaPalavraPasse.requestFocus()
                return@setOnClickListener // Retornando o método setOnClickLister, evita da aplicação crachar caso os campos estejam vázio.
            }

            // Chamada da funcao da implementação do FireBase para registrar novos usuários  de acordo com o email e palavra passe
            registrarNovoUsuario(email, palavraPasse)
        }

    }

    // Implementação da função seguindo documentação do FireBase.
    // https://firebase.google.com/docs/auth/android/password-auth#kotlin+ktx_1
    private fun registrarNovoUsuario(email: String, palavraPasse: String) {
        auth.createUserWithEmailAndPassword(email, palavraPasse)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Criado com sucesso.", "Usuário criado com sucesso.")
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        finish()
                    }
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this,
                        "Falha, verificar se todos os campos estão preenchidos.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    // Função que ao ser disparada, irá levar o usuário a tela de logar na conta.
    fun actionLoginLayout() {
        val txtAccessLoginScreen = findViewById<TextView>(R.id.textViewJaTensConta)
        txtAccessLoginScreen.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}