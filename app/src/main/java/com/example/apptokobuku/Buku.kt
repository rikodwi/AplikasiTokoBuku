package com.example.apptokobuku

class Buku {
    var id: Int? = null
    var judul: String? = null
    var pengarang: String? = null
    var harga: String? = null
    var jumlah: String? = null

    constructor(id: Int, judul: String, pengarang: String, harga: String, jumlah: String ){
        this.id = id
        this.judul = judul
        this.pengarang = pengarang
        this. harga = harga
        this.jumlah = jumlah
    }
}