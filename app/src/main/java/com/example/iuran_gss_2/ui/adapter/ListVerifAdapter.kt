package com.example.iuran_gss_2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.PembayaranItemBinding
import com.example.iuran_gss_2.model.remote.DataTransactionAdmin

class ListVerifAdapter(private var transaction: List<DataTransactionAdmin>) :
    RecyclerView.Adapter<ListVerifAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: PembayaranItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var nama: String
        private lateinit var noPembayaran: String
        private lateinit var nominal: String
        private lateinit var status: String
        private var statusColor: Int = 0
        fun bind(data: DataTransactionAdmin) {
            binding.apply {
                nama = ("${tvNama.text} ${data.list.username}")
                noPembayaran = ("${tvNoPembayaran.text} ${data.list.tNumber}")
                nominal = ("${tvNominal.text} ${data.list.harga}")
                status = (data.list.status)
                tvNama.text = nama
                tvNoPembayaran.text = noPembayaran
                tvNominal.text = nominal
                tvStatus.text = status
                when (status) {
                    "Pending" -> {
                        statusColor = itemView.context.getColor(R.color.pendingColor)
                        tvStatus.visibility = View.VISIBLE
                    }
                    "Ditolak" -> {
                        statusColor = itemView.context.getColor(R.color.rejectColor)
                    }
                    else -> {
                        statusColor = itemView.context.getColor(R.color.acceptColor)
                    }
                }
                tvStatus.setTextColor(statusColor)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            PembayaranItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = transaction[position]
        holder.bind(data)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(transaction[holder.adapterPosition]) }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataTransactionAdmin)
    }

    override fun getItemCount(): Int = transaction.size
}