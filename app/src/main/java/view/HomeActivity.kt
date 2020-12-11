package view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafioestagiobloomidea.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_home.*
import model.ItensLista
import adapter.MyAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {

    // Criando constante Estaticas para a camera e para a galeria.
    private val CAMERA_PERMISSION_CODE = 1
    private val REQUEST_CODE_IMAGE = 2
    private val RESULT_OPEN_GALERY = 3
    private val REQUEST_PERM_WRITE_STORAGE = 4

    // Objetos do FireBase Storage.
    private var filePath: String = "android:storage" // implementar o caminho
    internal var storage: FirebaseStorage? = null

    internal var storageReference: StorageReference? = null

    private var imagesPath: File? = null

    private var itensFotos = ArrayList<ItensLista>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Iniciando o Firebase.
        storage = FirebaseStorage.getInstance()


        abriCamera()
        iniciarRecyclerView()
        abrirGaleriaFotos()
        receberNomeUsuario()

    }

    /* O tentar recuperar o email digitado pelo usuário na tela Login, o valor recebido é nulo, conforme
    Log.d de erro capturado no Log.D comentado, mesmo pesquiando muito não consegui chegar a uma solução.
    D/TESTANDONOME: com.google.android.material.textview.MaterialTextView{d329315 V.ED..... ......
    ID 0,0-0,0 #7f080183 app:id/txtNomeUtilizador} null
      */

    private fun receberNomeUsuario(){
        val nomeUsuario = intent.getStringExtra("nomeUsuario")
        val txtNomeUtilizador = findViewById<TextView>(R.id.txtNomeUtilizador)
        txtNomeUtilizador.setText(nomeUsuario)
        //Log.d("TESTANDONOME","$txtNomeUtilizador $nomeUsuario")

    }

    // Funcao para abrir a galeria de Fotos.
    private fun abrirGaleriaFotos() {
        val btnAbrirGaleria = findViewById<ImageView>(R.id.btnAbrirGaleria)
        btnAbrirGaleria.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, RESULT_OPEN_GALERY)
        }
    }

    // Funcao de implementação para iniciar a RecyclerView.
    private fun iniciarRecyclerView() {

        my_recycler_view.layoutManager = LinearLayoutManager(this)
        val listAdapter = MyAdapter(itensFotos)
        my_recycler_view.adapter = listAdapter

    }

    // Funcao para abrir a câmera.
    private fun abriCamera() {
        // Verificar se o dispositivo possuí camera e seguir com o procesos de abrir a mesma.
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            val btnCamera = findViewById<ImageView>(R.id.btnTirarFoto)
            btnCamera.setOnClickListener {
                // Realizando verificação da permissão.
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_CODE_IMAGE)
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_CODE
                    )
                }
            }
        } else {
            Toast.makeText(this, "Dispositivo sem câmera ou a não funcionar", Toast.LENGTH_LONG).show()
        }
    }

    // Sub escrevendo o método onRequestPermissionResult para determinar o que irá ocorrer
    // após o usuário dar ou não a permissão de acesso a camera do dispositivo.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION_CODE && grantResults.isEmpty()
            && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_CODE_IMAGE)

            } else {
                Toast.makeText(this, R.string.permissao_negada, Toast.LENGTH_LONG).show()
            }
        }

    // Funcao implementada seguindo tutorial Firebase - Download a file
    private fun carregarImgFireBase(){

    }

    // Sub escrevendo o método onActivityResult para conseguir utilizar a foto capturada pela camera.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && resultCode == RESULT_OK && null !=data) {
            if(requestCode == REQUEST_CODE_IMAGE) {

                TODO(
                    /* Fiz um teste com a implemetancao do código abaixo apenas com um objeto ImageView dentro do Layout HomeActivity ao invés
                      de utilizar um RecylcerView, e utilizando apenas o ImageView, consigo pegar a foto e salvar na imagem, já utilizando o RecyclerView,
                      a aplicaçáo se encerra com o seguinte erro: Caused by: java.lang.NullPointerException:
                      Attempt to invoke virtual method 'void android.widget.ImageView.setImageBitmap(android.graphics.Bitmap)'
                      on a null object reference -- E não estou conseguindo resolver.
                      */  )

                val img = findViewById<ImageView>(R.id.fotos)
                val pictures : Bitmap = data.extras?.get("data") as Bitmap
                img.setImageBitmap(pictures)
            }
        }
    }

}


// Código tentando implementar o Storage do FireBase seguindo  a página do tutorial do próprio FireBase.
// Erro:
//java.lang.RuntimeException: Failure delivering result ResultInfo{who=null, request=2, result=-1, data=Intent
// { act=inline-data flg=0x1 (has extras) }} to activity {com.example.desafioestagiobloomidea/view.HomeActivity}:
// java.lang.NullPointerException: imgView must not be null


/*
val imgView = findViewById<ImageView>(R.id.fotos)
// Configurar a imagem para que a mesma fique salva em memória Cache.
imgView.isDrawingCacheEnabled = true

// Recuperar o Bitmap da imagem carregada.
val bitmap: Bitmap = imgView.getDrawingCache()

// Comprimindo o bitmap para um formato pnj ou jpeg e deixando com + ou - 75% de qualidade e um OutPutStream.
val baos = ByteArrayOutputStream()
bitmap.compress(Bitmap.CompressFormat.JPEG,75,baos)

// Convertendo o ByteArrayOutputStream para um Array de bytes, pois sem essa conversão não é possível enviar
// pelo firebase, pois o mesmo requer uma Matriz ou Array de bytes.
val dadosImagem: ByteArray = baos.toByteArray()

// Definindo os nós do FireBase.
val storageRef = storage?.reference
val imagens: StorageReference? = storageRef?.child("imagens")
val imagemRef: StorageReference = imagens!!.child(filePath)


val uploadTask: UploadTask = imagemRef.putBytes(dadosImagem)
uploadTask.addOnFailureListener(this, OnFailureListener {
    imagemRef.downloadUrl.addOnCompleteListener( OnCompleteListener {
        val url: UploadTask.TaskSnapshot = uploadTask.result
    })
    Toast.makeText(this,"Erro ao carregar foto no Firebase",Toast.LENGTH_LONG).show()
}).addOnSuccessListener(this, OnSuccessListener {
    Toast.makeText(this,"Imagem salva no FireBase com sucesso.",Toast.LENGTH_LONG).show()
})*/
