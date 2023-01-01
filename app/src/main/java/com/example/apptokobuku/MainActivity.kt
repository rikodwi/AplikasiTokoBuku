package com.example.apptokobuku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.apptokobuku.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var listBuku = ArrayList<Buku>()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            var intent = Intent (this, BukuActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        var dbAdapter = DBAdapter(this)
        var cursor = dbAdapter.allQuery()

        listBuku.clear()
        if(cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val judul = cursor.getString(cursor.getColumnIndex("Judul"))
                val pengarang = cursor.getString(cursor.getColumnIndex("Pengarang"))
                val harga = cursor.getString(cursor.getColumnIndex("Harga"))
                val jumlah = cursor.getString(cursor.getColumnIndex("Jumlah"))

                listBuku.add(Buku(id, judul, pengarang, harga, jumlah))
            } while (cursor.moveToNext())
        }
        var bukuAdapter = BukuAdapter(this, listBuku)
        lvBuku.adapter = bukuAdapter
    }

    inner class BukuAdapter: BaseAdapter {

        private var bukuList = ArrayList<Buku>()
        private var context: Context? = null

        constructor(context: Context, bukuList: ArrayList<Buku>) : super(){
            this.bukuList = bukuList
            this.context = context
        }

        override fun getCount(): Int {
            return bukuList.size
        }

        override fun getItem(p0: Int): Any {
            return  bukuList[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if(convertView == null){
                view = layoutInflater.inflate(R.layout.buku, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mBuku = bukuList[position]

            vh.tvJudul.text = mBuku.judul
            vh.tvPengarang.text = mBuku.pengarang
            vh.tvHarga.text = "Rp." + mBuku.harga
            vh.tvJumlah.text = mBuku.jumlah

            lvBuku.onItemClickListener = AdapterView.OnItemClickListener {adapterView, view, position, id ->
                updateBuku(mBuku)
            }
            return view
        }

    }

    private fun updateBuku(buku: Buku) {
        var intent = Intent (this, BukuActivity::class.java)
        intent.putExtra("MainActId", buku.id)
        intent.putExtra("MainActJudul", buku.judul)
        intent.putExtra("MainActPengarang", buku.pengarang)
        intent.putExtra("MainActHarga", buku.harga)
        intent.putExtra("MainActJumlah", buku.jumlah)
        startActivity(intent)
    }

    private class ViewHolder(view: View?){
        val tvJudul: TextView
        val tvPengarang: TextView
        val tvHarga: TextView
        val tvJumlah: TextView

        init {
            this.tvJudul = view?.findViewById(R.id.tvJudul) as TextView
            this.tvPengarang = view?.findViewById(R.id.tvPengarang) as TextView
            this.tvHarga = view?.findViewById(R.id.tvHarga) as TextView
            this.tvJumlah = view?.findViewById(R.id.tvJumlah) as TextView
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}