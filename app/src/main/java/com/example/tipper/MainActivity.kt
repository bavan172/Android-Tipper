package com.example.tipper

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat


private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 5

class MainActivity : AppCompatActivity() {
    private lateinit var baseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        baseAmount = findViewById(R.id.baseAmount)
        seekBarTip = findViewById(R.id.seekBar)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tipAmountR)
        tvTotal = findViewById(R.id.totalAmountR)
        tvTipDescription = findViewById(R.id.description)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        baseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChanged $p0")
                computeTipAndTotal()
            }

        })

    }

    private fun updateTipDescription(progress: Int) {
        val tipDescription = when(progress){
            in 0..9 -> "Not Bad"
            in 10..15 -> "Acceptable"
            in 15..21 -> "Good"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            progress.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this,R.color.worst_color),
            ContextCompat.getColor(this,R.color.best_color)
        )as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(baseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotal.text = ""
            return
        }
        val amount = baseAmount.text.toString().toDouble()
        val percent = seekBarTip.progress

        val calc = amount * percent / 100
        val final =  amount + calc

        tvTipAmount.text = "%.2f".format(calc)
        tvTotal.text = "%.2f".format(final)
    }
}