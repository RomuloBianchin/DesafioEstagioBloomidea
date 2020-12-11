package view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.desafioestagiobloomidea.R
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    // Firebase.
    private lateinit var auth: FirebaseAuth
    lateinit var emailUsuario: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializando o objeto FireBase
        auth = FirebaseAuth.getInstance()

        // Funcao que irá realizar o login caso as informações como email e palavra passe já constem no FireBase.
        actLoginUser()

        // Função que ao ser disparada, irá levar o usuário a tela de cadastro de novos usuários.
        actionRegisterLayout()
    }

    // Funcao que irá realizar o verificação dos campos preenchidos e realizar o login no Firebase,
    // caso as informações o usuário já tenha um cadastro, utilizando uma função de autenticação.
    private fun actLoginUser() {
        emailUsuario = findViewById<EditText>(R.id.edtEmailUsuario)
        val palavraPasseUsuario = findViewById<EditText>(R.id.edtPalavraPasse)


        findViewById<Button>(R.id.btnEntrar).setOnClickListener {

            // Recuperando os valores dos campos email e palavra passe e convertendo.
            val recEmail = emailUsuario.text.toString()
            val recPalavraPasse = palavraPasseUsuario.text.toString()

            // Verificando os preenchimento dos campos email e palavra passe.
            if (recEmail.isEmpty()) {
                emailUsuario.error = "Necessário preencher o campo e-mail."
                emailUsuario.requestFocus()
                return@setOnClickListener // Retornando o método setOnClickLister, evita da aplicação crachar caso os campos estejam vázio.
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(recEmail).matches()) {
                emailUsuario.error = "Tipo de e-mail inválido"
                emailUsuario.requestFocus()
                return@setOnClickListener // Retornando o método setOnClickLister, evita da aplicação crachar caso os campos estejam vázio.
            }

            if (recPalavraPasse.isEmpty() || recPalavraPasse.length < 6) {
                palavraPasseUsuario.error = "Minímo 6 caractéres na palavra passe."
                palavraPasseUsuario.requestFocus()
                return@setOnClickListener // Retornando o método setOnClickLister, evita da aplicação crachar caso os campos estejam vázio.
            }



            // Chamada da função de autenticação do FireBase.
            conectarUsuario(recEmail, recPalavraPasse)
        }
    }

    // Implementação da função seguindo documentação do FireBase.
    // https://firebase.google.com/docs/auth/android/password-auth#kotlin+ktx_1
    private fun conectarUsuario(email: String, palavraPasse: String) {
        auth.signInWithEmailAndPassword(email, palavraPasse)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Criado com sucesso.", "Usuário criado com sucesso.");

                    val pegarNomeUsuario = email
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        intent.putExtra("nomeUsuario",email)
                        Log.d("TesteRetorno","verificar valor da variável $pegarNomeUsuario")
                        Log.d("teste","Verificar o que está sendo guardando em $email")
                        finish()
                    }
                    startActivity(intent)
                } else {
                    Log.w("Erro_logar", "Erro no momento do login", task.exception)
                    Toast.makeText(
                        this,
                        "Erro ao logar, verifique os preenchimentos.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun actionRegisterLayout() {
        val txtAccessRegisterScreen = findViewById<TextView>(R.id.textViewRegistrar)
        txtAccessRegisterScreen.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

}

