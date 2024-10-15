package com.example.calculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caculatorapp.R
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var workingsTV: TextView
    private lateinit var resultsTV: TextView

    private var workings: String = ""
    private var result: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        workingsTV = findViewById(R.id.workingsTV)
        resultsTV = findViewById(R.id.resultsTV)

        // Initialize window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun enableEdgeToEdge() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    // Handle number button click
    fun numberAction(view: View) {
        if (view is Button) {
            // If result was already shown, clear workings to start a new calculation
            if (resultsTV.text.isNotEmpty()) {
                workings = ""
                resultsTV.text = ""
            }
            workings += view.text
            workingsTV.text = workings
        }
    }

    // Handle operator button click
    fun operatorAction(view: View) {
        if (view is Button) {
            if (resultsTV.text.isNotEmpty()) {
                workings = resultsTV.text.toString()
                resultsTV.text = ""
            }
            workings += " ${view.text} "
            workingsTV.text = workings
        }
    }

    // Handle equals button click
    fun equalsAction(view: View) {
        try {
            // Sanitize the workings string by removing any trailing operators
            val sanitizedWorkings = workings.trim()

            // Check for valid ending (expression should not end with an operator)
            if (sanitizedWorkings.endsWith(" ") || sanitizedWorkings.endsWith("+") || sanitizedWorkings.endsWith("-") ||
                sanitizedWorkings.endsWith("x") || sanitizedWorkings.endsWith("÷")) {
                resultsTV.text = "Error"
                return
            }

            // Build and evaluate the expression (replace 'x' and '÷' with '*', '/')
            val expression = ExpressionBuilder(sanitizedWorkings.replace("x", "*").replace("÷", "/")).build()
            val result = expression.evaluate()

            // Display the result, removing any trailing '.0' for whole numbers
            resultsTV.text = if (result % 1 == 0.0) result.toInt().toString() else result.toString()

            // Clear workings after result is shown
            workings = resultsTV.text.toString()
            workingsTV.text = workings
        } catch (e: Exception) {
            // Display error if evaluation fails
            resultsTV.text = "Error"
        }
    }

    // Handle all clear button click
    fun allClearAction(view: View) {
        workings = ""
        result = ""
        workingsTV.text = ""
        resultsTV.text = ""
    }

    // Handle backspace button click
    fun backSpaceAction(view: View) {
        if (workings.isNotEmpty()) {
            workings = workings.dropLast(1)
            workingsTV.text = workings
        }
    }


}
