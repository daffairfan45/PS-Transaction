package com.example.transactionsapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class TransactionRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addTransaction(transaction: Transaction): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_DESCRIPTION, transaction.description)
            put(DatabaseHelper.COLUMN_AMOUNT, transaction.amount)
            put(DatabaseHelper.COLUMN_DATE, transaction.date)
        }
        return db.insert(DatabaseHelper.TABLE_NAME, null, values)
    }

    fun getAllTransactions(): List<Transaction> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            null, null, null, null, null, null
        )
        val transactions = mutableListOf<Transaction>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                val description = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION))
                val amount = getDouble(getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT))
                val date = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE))
                transactions.add(Transaction(id, description, amount, date))
            }
        }
        cursor.close()
        return transactions
    }

    fun deleteTransaction(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DatabaseHelper.TABLE_NAME, "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()))
    }
}
