package com.yklab.deathnote

import android.app.Dialog
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.yklab.deathnote.databinding.ActivityMainBinding
import com.yklab.deathnote.databinding.DialogLayoutViewBinding
import p32929.androideasysql_library.EasyDB
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    var pos=0
    lateinit var binding: ActivityMainBinding
    private val notes= mutableListOf<NoteInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val easyDB = EasyDB.init(this, "TEST") // TEST is the name of the DATABASE
            //.setTableName("DEMO TABLE") // You can ignore this line if you want
            .addColumn("time", "text") // Contrains like "text", "unique", "not null" are not case sensitive
            .addColumn("note", "text")
            .doneTableColumn()

        var res: Cursor = easyDB.allData
        while (res.moveToNext()) {
            notes.add(NoteInfo(res.getInt(0),res.getString(2),res.getString(1)))
            pos++
        }

        val adapter = NoteRecyclerViewAdapter(notes)
        binding.noteView.adapter = adapter
        binding.noteView.scrollToPosition(pos-1)

        binding.btnAddNote.setOnClickListener{
            val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM, hh:mm a")
            val now: LocalDateTime = LocalDateTime.now()

            val done = easyDB
                .addData("time", dtf.format(now))
                .addData("note", binding.noteInput.text.toString())
                .doneDataAdding()

            res=easyDB.allData
            res.moveToLast()
            notes.add(NoteInfo(res.getInt(0),res.getString(2),res.getString(1)))
            binding.noteInput.setText("")
            adapter.notifyItemInserted(++pos)
            binding.noteView.scrollToPosition(pos-1)

        }



//        startActivity(Intent(this,AddNoteActivity::class.java))
    }

}