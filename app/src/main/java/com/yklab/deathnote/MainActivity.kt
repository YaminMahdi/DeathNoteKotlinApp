package com.yklab.deathnote

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.room.InvalidationTracker
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.yklab.deathnote.databinding.ActivityMainBinding
import com.yklab.deathnote.model.NoteInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val notes= mutableListOf<NoteInfo>()
    val auth = FirebaseAuth.getInstance();
    val email = auth.currentUser!!.email!!
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: MainViewModel by viewModels()

//        val easyDB = EasyDB.init(this, "TEST") // TEST is the name of the DATABASE
//            //.setTableName("DEMO TABLE") // You can ignore this line if you want
//            .addColumn("time", "text") // Contrains like "text", "unique", "not null" are not case sensitive
//            .addColumn("note", "text")
//            .doneTableColumn()
//
//        var res: Cursor = easyDB.allData
//        while (res.moveToNext()) {
//            notes.add(NoteInfo(res.getInt(0),res.getString(2),res.getString(1)))
//            pos++
//        }
        var adapter: NoteRecyclerViewAdapter? = null
        var noteListSize: Int? = null
        viewModel.readAllNote.observe(this
         ) { notes ->
            adapter = NoteRecyclerViewAdapter(notes,viewModel,db.collection(email))
            binding.noteView.adapter = adapter
            noteListSize=notes.size
            binding.noteView.scrollToPosition(noteListSize!! - 1)

            val fireNotes = mutableListOf<NoteInfo>()
            db.collection(email)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        val note= document.toObject<NoteInfo>()
                        fireNotes.add(note)
                    }
                    if(fireNotes!=notes){
                        viewModel.addAllNote(fireNotes)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error getting documents: ", exception)
                }
         }

        binding.btnAddNote.setOnClickListener{
            val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM, hh:mm a")
            val now: LocalDateTime = LocalDateTime.now()

//            val done = easyDB
//                .addData("time", dtf.format(now))
//                .addData("note", binding.noteInput.text.toString())
//                .doneDataAdding()
//
//            res=easyDB.allData
//            res.moveToLast()
//            notes.add(NoteInfo(res.getInt(0),res.getString(2),res.getString(1)))
            val note = NoteInfo(0,binding.noteInput.text.toString(),dtf.format(now))
            viewModel.addNote(note){
                db.collection(email).document(it.toString()).set(note.copy(id=it.toInt())).addOnCompleteListener{task ->
                    if(task.isComplete){
                        Toast.makeText(this, "Note Added to Firebase", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            noteListSize = noteListSize!! + 1
            adapter?.notifyItemInserted(noteListSize!!)
            binding.noteInput.setText("")
            binding.noteView.scrollToPosition(noteListSize!! - 1)

        }



//        startActivity(Intent(this,AddNoteActivity::class.java))
    }

}