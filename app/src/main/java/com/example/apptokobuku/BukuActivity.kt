package com.example.apptokobuku

import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_buku.*

class BukuActivity : AppCompatActivity() {
    var id=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buku)

        try{
            var bundle: Bundle = intent.extras!!
            id = bundle.getInt("MainActId", 0)
            if(id != 0){
                txJudul.setText(bundle.getString("MainActJudul"))
                txPengarang.setText(bundle.getString("MainActPengarang"))
                txHarga.setText(bundle.getString("MainActHarga"))
                txJumlah.setText(bundle.getString("MainActJumlah"))
            }
        } catch (ex: Exception){
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)

        val itemDelete: MenuItem = menu.findItem(R.id.action_delete)

        if(id == 0){
            itemDelete.isVisible = false
        } else {
            itemDelete.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_save -> {
                var dbAdapter = DBAdapter(this)

                var values = ContentValues()
                values.put("Judul", txJudul.text.toString())
                values.put("Pengarang", txPengarang.text.toString())
                values.put("Harga", txHarga.text.toString())
                values.put("Jumlah", txJumlah.text.toString())

                if(id == 0){
                    val mID = dbAdapter.insert(values)

                    if(mID > 0){
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    var selectionArgs = arrayOf(id.toString())
                    var mID = dbAdapter.update(values, "Id=?", selectionArgs)
                    if(mID > 0){
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            R.id.action_delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Data")
                builder.setMessage("This Data Will Be Deleted")

                builder.setPositiveButton("Delete") {dialog: DialogInterface?, which: Int ->
                    var dbAdapter = DBAdapter(this)
                    val selectionArgs = arrayOf(id.toString())
                    dbAdapter.delete("Id=?", selectionArgs)
                    Toast.makeText(this, "DELETED", Toast.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("Cancel") {dialog: DialogInterface?, which: Int -> }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)
    }
}