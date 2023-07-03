package android.maxim.freshwallpapers

import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import android.maxim.freshwallpapers.ui.categories.CategoriesViewModel
import android.maxim.freshwallpapers.utils.Constants.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.activity.viewModels

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: WallpapersRepository
    private val mainActivityViewModel: CategoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository.getImageList("sports")

    }
}