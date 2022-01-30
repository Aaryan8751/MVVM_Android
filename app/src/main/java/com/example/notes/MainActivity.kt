package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv_view = findViewById<RecyclerView>(R.id.rv_view)

        val submitButton = findViewById<Button>(R.id.btn_submit)

        submitButton.setOnClickListener {
            val noteText = findViewById<TextView>(R.id.input)
            if(noteText.text.toString().isNotEmpty()){
                viewModel.insertNote(Note(noteText.text.toString()))

                Toast.makeText(this@MainActivity, "${noteText.text} is Inserted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Please Enter Note", Toast.LENGTH_SHORT).show()
            }
        }

        rv_view.layoutManager = LinearLayoutManager(this)
        val adapter : NotesRVAdapter = NotesRVAdapter(this, object : INotesRVAdapter {
            override fun onItemClicked(note: Note) {
                viewModel.deleteNote(note)
                Toast.makeText(this@MainActivity, "${note.text} is Deleted", Toast.LENGTH_SHORT).show()
            }
        })

        rv_view.adapter = adapter

        viewModel = ViewModelProvider(this@MainActivity,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this@MainActivity, Observer {list ->
            list?.let{
                adapter.updateList(it)
            }
        })



    }
}