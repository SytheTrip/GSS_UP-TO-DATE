package com.example.iuran_gss_2.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.ItemHistoryBinding
import com.example.iuran_gss_2.model.remote.ListHistoryItem

class HistoryAdapter(private var transaction: List<ListHistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val btnRequest: Button = binding.btnRequest
        private var onItemClickCallback: OnItemClickCallback? = null
        private lateinit var nama: String
        private lateinit var noPembayaran: String
        private lateinit var jumlah: String
        private lateinit var status: String
        private var keteranganColor: Int = 0
        fun bind(data: ListHistoryItem) {
            binding.apply {
                nama = tvNama.text.toString()
                noPembayaran = tvNoPembayaran.text.toString()
                jumlah = tvJumlah.text.toString()
                status = tvStatus.text.toString()

                data.apply {
                    nama += dataTransaction.namaLengkap
                    noPembayaran += dataTransaction.tNumber
                    jumlah += dataTransaction.harga
                    status = dataTransaction.status
                }


                tvNama.text = nama
                tvNoPembayaran.text = noPembayaran
                tvJumlah.text = jumlah
                tvStatus.text = status
                when (status) {
                    "Diterima" -> {
                        keteranganColor = itemView.context.getColor(R.color.acceptColor)
                        btnRequest.visibility = View.VISIBLE
                        btnRequest.setOnClickListener {
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                onItemClickCallback?.onItemClicked(data)
                            }
                        }
                    }

                    "Ditolak" -> {
                        keteranganColor = itemView.context.getColor(R.color.rejectColor)
                        btnRequest.visibility = View.VISIBLE
                        btnRequest.setOnClickListener {
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                onItemClickCallback?.onItemClicked(data)
                            }
                        }
                    }
                    "Pending" -> {
                        keteranganColor = itemView.context.getColor(R.color.pendingColor)
                        btnRequest.visibility = View.GONE
                    }
                    else -> {
                        keteranganColor = 0
                        tvKeterangan.visibility = View.GONE
                        btnRequest.visibility = View.GONE
                    }
                }
                tvKeterangan.setTextColor(keteranganColor)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = transaction[position]
        holder.bind(data)
        holder.btnRequest.setOnClickListener {
            onItemClickCallback.onItemClicked(transaction[position])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListHistoryItem)
    }

    override fun getItemCount(): Int = transaction.size
}