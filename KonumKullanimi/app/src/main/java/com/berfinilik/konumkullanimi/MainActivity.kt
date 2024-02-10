package com.berfinilik.konumkullanimi

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.berfinilik.konumkullanimi.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var izinKontrol=0//global degisken oluşturduk(onayladı mı bilgisini alan değişken)

    private lateinit var flpc:FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>//konumla ilgili bilgi almamızı ssaglar son konumu buna aktarırız

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        flpc=LocationServices.getFusedLocationProviderClient(this)

        binding.buttonKonumAl.setOnClickListener {
            izinKontrol=ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)

            if (izinKontrol==PackageManager.PERMISSION_GRANTED) {//İZİN ONAYLANMIŞSA
                locationTask=flpc.lastLocation//locationtaska son konumu aktardık
                konumBilgisiAl()

            }
            else{
                //HANGİ AKTİVİTEDESİN,HANGİ İZNİ SORGULAYACAKSIN,REQUESTCODE
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100)
            }

        }

        }

    fun konumBilgisiAl() {
        locationTask.addOnSuccessListener {
            if(it!=null) {//if in içinde konum var mı
                binding.textViewEnlem.text="Enlem:${it.latitude}"
                binding.textViewBoylam.text="Boylam:${it.longitude}"


            }else{
                binding.textViewEnlem.text="Enlem:Bulunamadı"
                binding.textViewBoylam.text="Boylam:Bulunamadı"

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100){

            izinKontrol=ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext,"İzin Onaylandı",Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(applicationContext,"İzin Onaylanmadı",Toast.LENGTH_SHORT).show()
        }
    }
}
