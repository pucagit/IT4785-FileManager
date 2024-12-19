package com.example.filemanager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filemanager.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Get the root or the selected directory
        val path = intent.getStringExtra("path")
        val root = if (path != null) File(path) else Environment.getExternalStorageDirectory()
        val files = root.listFiles()?.toList() ?: emptyList()

        // Set up RecyclerView with FileAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = FileAdapter(files) { file ->
            if (file.isDirectory) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("path", file.absolutePath)
                startActivity(intent)
            } else {
                if (file.extension == "txt") {
                    val intent = Intent(this, FileContentActivity::class.java)
                    intent.putExtra("path", file.absolutePath)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Không hỗ trợ file này", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}