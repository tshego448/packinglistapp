package com.example.packinglistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"

        // Parallel arrays to store packing list data
        val itemNames     = mutableListOf<String>()
        val categories    = mutableListOf<String>()
        val quantities    = mutableListOf<Int>()
        val comments      = mutableListOf<String>()
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG, "MainActivity started")

        // Pre-populate with example data on first launch
        if (itemNames.isEmpty()) {
            populateSampleData()
        }

        // Wire up buttons
        findViewById<Button>(R.id.btnAddItem).setOnClickListener {
            Log.d(TAG, "Add to Packing List button clicked")
            showAddItemDialog()
        }

        findViewById<Button>(R.id.btnViewList).setOnClickListener {
            Log.d(TAG, "Navigate to Screen Two clicked")
            navigateToSecondScreen()
        }

        findViewById<Button>(R.id.btnExit).setOnClickListener {
            Log.d(TAG, "Exit button clicked")
            exitApp()
        }
    }

    // -------------------------------------------------------------------------
    // Sample data
    // -------------------------------------------------------------------------
    private fun populateSampleData() {
        itemNames.addAll(listOf("T-Shirts and Pants", "Toothbrush", "Shoes", "Passport"))
        categories.addAll(listOf("Clothing", "Toiletries", "Clothing", "Documents"))
        quantities.addAll(listOf(5, 1, 2, 1))
        comments.addAll(listOf(
            "Comfortable for travel",
            "Essential for hygiene",
            "Walking and smart casual",
            "Don't forget this!"
        ))
        Log.i(TAG, "Sample data loaded – ${itemNames.size} items")
    }

    // -------------------------------------------------------------------------
    // Add Item Dialog
    // -------------------------------------------------------------------------
    private fun showAddItemDialog() {
        Log.d(TAG, "showAddItemDialog() called")

        // Inflate a custom dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_item, null)

        val etItemName = dialogView.findViewById<EditText>(R.id.etItemName)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val etQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)
        val etComments = dialogView.findViewById<EditText>(R.id.etComments)

        AlertDialog.Builder(this)
            .setTitle("Add Packing Item")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name     = etItemName.text.toString().trim()
                val category = etCategory.text.toString().trim()
                val qtyStr   = etQuantity.text.toString().trim()
                val comment  = etComments.text.toString().trim()

                // ---- Error handling ----------------------------------------
                if (!validateInput(name, category, qtyStr)) return@setPositiveButton

                val qty = qtyStr.toInt()
                addItemToList(name, category, qty, comment)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                Log.d(TAG, "Add item dialog cancelled")
                dialog.dismiss()
            }
            .show()
    }

    // -------------------------------------------------------------------------
    // Input validation
    // -------------------------------------------------------------------------
    private fun validateInput(name: String, category: String, qtyStr: String): Boolean {
        if (name.isEmpty()) {
            Log.w(TAG, "Validation failed: item name is empty")
            showError("Item name cannot be empty.")
            return false
        }
        if (category.isEmpty()) {
            Log.w(TAG, "Validation failed: category is empty")
            showError("Category cannot be empty.")
            return false
        }
        if (qtyStr.isEmpty()) {
            Log.w(TAG, "Validation failed: quantity is empty")
            showError("Quantity cannot be empty.")
            return false
        }
        val qty = qtyStr.toIntOrNull()
        if (qty == null || qty <= 0) {
            Log.w(TAG, "Validation failed: invalid quantity '$qtyStr'")
            showError("Quantity must be a positive whole number.")
            return false
        }
        Log.d(TAG, "Validation passed for item: $name")
        return true
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // -------------------------------------------------------------------------
    // Add item to parallel arrays
    // -------------------------------------------------------------------------
    private fun addItemToList(name: String, category: String, qty: Int, comment: String) {
        itemNames.add(name)
        categories.add(category)
        quantities.add(qty)
        comments.add(comment)

        Log.i(TAG, "Item added: name=$name, category=$category, qty=$qty")
        Toast.makeText(this, "\"$name\" added to packing list!", Toast.LENGTH_SHORT).show()
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------
    private fun navigateToSecondScreen() {
        Log.i(TAG, "Navigating to SecondActivity")
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }

    private fun exitApp() {
        Log.i(TAG, "Exiting application")
        finishAffinity()
    }
}
