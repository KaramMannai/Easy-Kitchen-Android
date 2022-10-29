package devsec.app.easykitchen.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import devsec.app.easykitchen.R

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val logo = findViewById<ImageView>(R.id.LogoIV)
        logo.animate().apply {
            alpha(1f).duration = 3000
        }.start()

        handler = Handler()

        val activity: Activity = GuideActivity()

        handler.postDelayed({
            val intent = Intent(this, activity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}