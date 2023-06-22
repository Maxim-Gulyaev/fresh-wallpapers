package android.maxim.freshwallpapers

import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = WallpapersRepository()
        repository.categoriesList
    }
}