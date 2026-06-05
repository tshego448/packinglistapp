package com.example.packinglistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SecondActivity"
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        Log.i(TAG, "SecondActivity started – ${MainActivity.itemNames.size} items available")

        // Wire up buttons
        findViewById<Button>(R.id.btnShowAll).setOnClickListener {
            Log.d(TAG, "Show full packing list clicked")
            displayFullList()
        }

        findViewById<Button>(R.id.btnShowHighQty).setOnClickListener {
            Log.d(TAG, "Show items with qty >= 2 clicked")
            displayHighQuantityItems()
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            Log.d(TAG, "Back button clicked – returning to MainActivity")
            goBack()
        }
    }

    // -------------------------------------------------------------------------
    // Display full packing list
    // -------------------------------------------------------------------------
    private fun displayFullList() {
        val tvOutput = findViewById<TextView>(R.id.tvOutput)

        if (MainActivity.itemNames.isEmpty()) {
            Log.w(TAG, "No items to display")
            tvOutput.text = "Your packing list is empty.\nGo back and add some items!"
            return
        }

        val sb = StringBuilder()
        sb.appendLine("=== Full Packing List ===\n")

        // Loop through all parallel arrays
        for (i in MainActivity.itemNames.indices) {
            sb.appendLine("Item ${i + 1}")
            sb.appendLine("  Name     : ${MainActivity.itemNames[i]}")
            sb.appendLine("  Category : ${MainActivity.categories[i]}")
            sb.appendLine("  Quantity : ${MainActivity.quantities[i]}")
            sb.appendLine("  Comments : ${MainActivity.comments[i]}")
            sb.appendLine()
        }

        sb.appendLine("Total items: ${MainActivity.itemNames.size}")

        tvOutput.text = sb.toString()
        Log.i(TAG, "Full packing list displayed – ${MainActivity.itemNames.size} items")
    }

    // -------------------------------------------------------------------------
    // Display only items with quantity >= 2
    // -------------------------------------------------------------------------
    private fun displayHighQuantityItems() {
        val tvOutput = findViewById<TextView>(R.id.tvOutput)

        if (MainActivity.itemNames.isEmpty()) {
            Log.w(TAG, "No items to filter")
            tvOutput.text = "Your packing list is empty.\nGo back and add some items!"
            return
        }

        val sb = StringBuilder()
        sb.appendLine("=== Items with Qty ≥ 2 ===\n")

        var found = 0

        // Loop through parallel arrays and filter by quantity
        for (i in MainActivity.itemNames.indices) {
            if (MainActivity.quantities[i] >= 2) {
                sb.appendLine("• ${MainActivity.itemNames[i]} (${MainActivity.categories[i]}) — Qty: ${MainActivity.quantities[i]}")
                found++
                Log.d(TAG, "Filtered item: ${MainActivity.itemNames[i]} qty=${MainActivity.quantities[i]}")
            }
        }

        if (found == 0) {
            sb.appendLine("No items with a quantity of 2 or more.")
            Log.w(TAG, "No items matched the quantity >= 2 filter")
            Toast.makeText(this, "No items found with quantity 2 or more.", Toast.LENGTH_SHORT).show()
        } else {
            sb.appendLine("\n$found item(s) found.")
            Log.i(TAG, "$found item(s) with qty >= 2 displayed")
        }

        tvOutput.text = sb.toString()
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------
    private fun goBack() {
        Log.i(TAG, "Navigating back to MainActivity")
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
