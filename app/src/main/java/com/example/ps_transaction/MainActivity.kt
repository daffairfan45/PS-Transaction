package com.example.transactionsapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ps_transaction.R

class MainActivity : AppCompatActivity() {
    private lateinit var etDescription: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDate: EditText
    private lateinit var btnAddTransaction: Button
    private lateinit var lvTransactions: ListView
    private lateinit var tvTitle: TextView

    private lateinit var transactionRepository: TransactionRepository
    private lateinit var transactionsAdapter: ArrayAdapter<String>
    private val transactionsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etDescription = findViewById(R.id.etDescription)
        etAmount = findViewById(R.id.etAmount)
        etDate = findViewById(R.id.etDate)
        btnAddTransaction = findViewById(R.id.btnAddTransaction)
        lvTransactions = findViewById(R.id.lvTransactions)
        tvTitle = findViewById(R.id.tvTitle)

        transactionRepository = TransactionRepository(this)
        transactionsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transactionsList)
        lvTransactions.adapter = transactionsAdapter

        btnAddTransaction.setOnClickListener {
            val description = etDescription.text.toString()
            val amount = etAmount.text.toString().toDoubleOrNull()
            val date = etDate.text.toString()

            if (description.isNotEmpty() && amount != null && date.isNotEmpty()) {
                val transaction = Transaction(description = description, amount = amount, date = date)
                transactionRepository.addTransaction(transaction)
                updateTransactionList()
                clearInputFields()
            }
        }

        updateTransactionList()
    }

    private fun updateTransactionList() {
        transactionsList.clear()
        val transactions = transactionRepository.getAllTransactions()
        transactions.forEach {
            transactionsList.add("${it.description} - ${it.amount} - ${it.date}")
        }
        transactionsAdapter.notifyDataSetChanged()
    }

    private fun clearInputFields() {
        etDescription.text.clear()
        etAmount.text.clear()
        etDate.text.clear()
    }
}
