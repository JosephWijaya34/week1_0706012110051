package com.example.week1_0706012110051

import Database.GlobalVar
import Model.Hewan
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.week1_0706012110051.databinding.ActivityTambahHewanBinding

class TambahHewanActivity : AppCompatActivity() {
    private lateinit var tempHew:Hewan
    var tempUri:String =""
    private var position = -1
    private val GetResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val uri = it.data?.data
            viewBind.importFotoImageView.setImageURI(uri)
            if(uri!=null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    baseContext.getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                tempUri = uri.toString()
            }
        }
    }

    private lateinit var viewBind: ActivityTambahHewanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = ActivityTambahHewanBinding.inflate(layoutInflater)
        setContentView(viewBind.root)

        GetIntent()
        Listener()
    }

    private fun GetIntent(){
        position = intent.getIntExtra("position",-1)
        if(position!=-1){
            viewBind.homeToolbarDetail.setTitle("EDIT HEWAN")
            viewBind.simpanButton.setText("SIMPAN")
            val tempHew = GlobalVar.listDataHewan[position]
            Display(tempHew)
        }
    }

    private fun Display(tempHew:Hewan?){
        viewBind.importFotoImageView.setImageURI(Uri.parse(tempHew?.imageUri))
        viewBind.namaHewanTextInputLayout.editText?.setText(tempHew?.nama)
        viewBind.jenisHewanTextInputLayout.editText?.setText(tempHew?.jenis)
        viewBind.usiaHewanTextInputLayout.editText?.setText(tempHew?.usia)
    }

    private fun Listener(){
        viewBind.simpanButton.setOnClickListener{
            var nama = viewBind.namaHewanTextInputLayout.editText?.text.toString().trim()
            var jenis = viewBind.jenisHewanTextInputLayout.editText?.text.toString().trim()
            var usia = viewBind.usiaHewanTextInputLayout.editText?.text.toString().trim()

            tempHew = Hewan(nama,jenis,usia)
            checker()
        }

        viewBind.backButtonImageView.setOnClickListener{
            val myIntent = Intent(this,MainActivity::class.java)
            startActivity(myIntent)
        }

        viewBind.importFotoImageView.setOnClickListener{
            val myIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            myIntent.type="image/*"
            GetResult.launch(myIntent)
        }
    }
    private fun checker(){
        var isCompleted:Boolean = true
        if(tempHew.nama!!.isEmpty()){
            viewBind.namaHewanTextInputLayout.error = "Nama harus diisi"
            isCompleted = false
        }else{
            viewBind.namaHewanTextInputLayout.error = ""
        }

        if(tempHew.jenis!!.isEmpty()){
            viewBind.jenisHewanTextInputLayout.error = "jenis harus diisi"
            isCompleted=false
        }else{
            viewBind.jenisHewanTextInputLayout.error = ""
        }

        if(tempHew.usia!!.isEmpty()){
            viewBind.usiaHewanTextInputLayout.error = "usia harus diisi 1-10"
            isCompleted=false
        }else if(tempHew.usia!!.contains(".*[A-Z].*".toRegex())){
            viewBind.usiaHewanTextInputLayout.error = "usia tidak boleh ada huruf"
            isCompleted=false
        }else if(tempHew.usia!!.contains(".*[a-z].*".toRegex())){
            viewBind.usiaHewanTextInputLayout.error = "usia tidak boleh ada huruf"
            isCompleted=false
        }else if(tempHew.usia!!.contains(".*[0-9].*".toRegex())){
            if(tempHew.usia!!.toInt() < 0) {
                viewBind.usiaHewanTextInputLayout.error = "usia harus lebih besar dari 0"
                isCompleted = false
            }
            else {
                viewBind.usiaHewanTextInputLayout.error = ""
            }
        }
        else{
            viewBind.usiaHewanTextInputLayout.error = "usia tidak boleh ada simbol"
            isCompleted=false
        }

        if(isCompleted){
            if(position==-1){
                tempHew.imageUri= tempUri
                GlobalVar.listDataHewan.add(tempHew)
                Toast.makeText(this, "Hewan Successfully Added", Toast.LENGTH_SHORT).show()
                val myIntent = Intent(this,MainActivity::class.java)
                startActivity(myIntent)
            }
            else{
                if(tempUri==GlobalVar.listDataHewan[position].imageUri) {
                    tempHew.imageUri = GlobalVar.listDataHewan[position].imageUri
                }
                else if(tempUri==""){
                    tempHew.imageUri = GlobalVar.listDataHewan[position].imageUri
                }
                else{
                    tempHew.imageUri = tempUri
                }
                GlobalVar.listDataHewan[position]=tempHew
                Toast.makeText(this, "Movie Successfully Edited", Toast.LENGTH_SHORT).show()
                val myIntent = Intent(this,MainActivity::class.java)
                startActivity(myIntent)
            }
            finish()
        }
    }
}