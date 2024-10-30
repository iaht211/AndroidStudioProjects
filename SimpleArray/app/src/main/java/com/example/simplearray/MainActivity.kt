package com.example.simplearray

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var editTextNumber: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonShow: Button
    private lateinit var listView: ListView
    private lateinit var textViewError: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Thiết lập window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Khởi tạo các view
        editTextNumber = findViewById(R.id.editTextNumber)
        radioGroup = findViewById(R.id.radioGroup)
        buttonShow = findViewById(R.id.buttonShow)
        listView = findViewById(R.id.listView)
        textViewError = findViewById(R.id.textViewError)

        buttonShow.setOnClickListener {
            processInput()
        }
    }

    private fun processInput() {
        // Reset error message
        textViewError.text = ""

        // Lấy giá trị từ EditText
        val input = editTextNumber.text.toString()

        // Kiểm tra input hợp lệ
        if (input.isEmpty()) {
            textViewError.text = "Vui lòng nhập số!"
            return
        }

        val n = input.toIntOrNull()
        if (n == null || n < 0) {
            textViewError.text = "Vui lòng nhập số nguyên dương!"
            return
        }

        // Tạo danh sách số theo lựa chọn
        val numbers = when (radioGroup.checkedRadioButtonId) {
            R.id.radioEven -> getEvenNumbers(n)
            R.id.radioOdd -> getOddNumbers(n)
            R.id.radioPerfectSquare -> getPerfectSquareNumbers(n)
            else -> {
                textViewError.text = "Vui lòng chọn loại số!"
                return
            }
        }

        // Hiển thị danh sách
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numbers)
        listView.adapter = adapter
    }

    private fun getEvenNumbers(n: Int): List<Int> {
        return (0..n).filter { it % 2 == 0 }
    }

    private fun getOddNumbers(n: Int): List<Int> {
        return (1..n).filter { it % 2 != 0 }
    }

    private fun getPerfectSquareNumbers(n: Int): List<Int> {
        return (0..n).filter {
            val sqrt = kotlin.math.sqrt(it.toDouble()).toInt()
            sqrt * sqrt == it
        }
    }
}