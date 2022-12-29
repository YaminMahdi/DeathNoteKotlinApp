package com.yklab.deathnote

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.recyclerview.widget.RecyclerView
import com.yklab.deathnote.databinding.DialogLayoutViewBinding
import com.yklab.deathnote.databinding.NoteViewBinding
import p32929.androideasysql_library.EasyDB


data class NoteInfo(val id: Int,val note: String, val time: String)

class NoteRecyclerViewAdapter(private val noteList: List<NoteInfo>) :
    RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: NoteViewBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {


        fun bindView(noteList: List<NoteInfo>, position: Int) {
            binding.timeShowId.text = noteList[position].time

            binding.NoteId.setDelay(50)
            binding.NoteId.setWithMusic(true)
            binding.NoteId.removeAnimation()
            if (position == noteList.size - 1)
                binding.NoteId.animateText(noteList[position].note)
            else
                binding.NoteId.text = noteList[position].note
            //if(position==0)
                //binding.cardViewBg.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFC01F"))

            binding.root.setOnClickListener {
                binding.NoteId.removeAnimation()
                val dialog= Dialog(it.context)
                val dialogBinding = DialogLayoutViewBinding.inflate(LayoutInflater.from(it.context))
                val easyDB = EasyDB.init(it.context, "TEST")
                    .addColumn("time", "text")
                    .addColumn("note", "text")
                    .doneTableColumn()
                dialog.window?.setBackgroundDrawable(ColorDrawable(0))
                dialog.setContentView(dialogBinding.root)
                //dialog.setCancelable(false)

                dialogBinding.editField.setText(binding.NoteId.text.toString())
                dialogBinding.btnSaveNote.setOnClickListener {
                    easyDB.updateData(2, dialogBinding.editField.text.toString())
                        .rowID(noteList[position].id)
                    dialog.dismiss()
                    //NoteRecyclerViewAdapter(noteList).updateData(position)
                    recreate(context as Activity)
                }
                dialogBinding.btnDeleteNote.setOnClickListener {
                    easyDB.deleteRow(noteList[position].id)
                    dialog.dismiss()
                    recreate(context as Activity)
                }
                dialog.show()
            }
        }



        init {
            // Define click listener for the ViewHolder's View.
            binding.root.setOnLongClickListener {
                binding.NoteId.removeAnimation()
                Toast.makeText(it.context, "Note copied", Toast.LENGTH_SHORT).show()
                val clipboard = it.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val tmp=binding.NoteId.text.toString()
                val clip = ClipData.newPlainText("msgData", tmp)
                clipboard.setPrimaryClip(clip)
                return@setOnLongClickListener true
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        NoteViewBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        ),
        viewGroup.context
    )

    override fun getItemCount() = noteList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bindView(noteList,position)
    }
}