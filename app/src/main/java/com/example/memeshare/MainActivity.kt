package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var currentImageUrl: String? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMeme()
    }

    private fun loadMeme() {
        // Instantiate the RequestQueue.
        binding.progressBar.visibility = View.VISIBLE
        binding.nextButton.isEnabled = false
        binding.shareButton.isEnabled = false
//        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a json object response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentImageUrl = response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        binding.nextButton.isEnabled = true
                        binding.shareButton.isEnabled = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        binding.nextButton.isEnabled = true
                        binding.shareButton.isEnabled = true
                        return false
                    }
                }).into(binding.memeImageView)
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this cool meme I got from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}