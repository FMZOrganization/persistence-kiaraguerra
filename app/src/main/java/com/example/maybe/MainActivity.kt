//Kiara Guerra
//CPSC 411A
package com.example.maybe
import ColorPickerViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ColorPickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ColorPickerViewModel::class.java)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        updateColor()

        // Red
        val redSeekBar = findViewById<SeekBar>(R.id.seekRed)
        redSeekBar.setOnSeekBarChangeListener(getSeekBarChangeListener(0))

        // Green
        val greenSeekBar = findViewById<SeekBar>(R.id.seekGreen)
        greenSeekBar.setOnSeekBarChangeListener(getSeekBarChangeListener(1))

        // Blue
        val blueSeekBar = findViewById<SeekBar>(R.id.seekBlue)
        blueSeekBar.setOnSeekBarChangeListener(getSeekBarChangeListener(2))

        // Reset
        val resetButton = findViewById<Button>(R.id.button)
        resetButton.setOnClickListener {
            viewModel.resetColor()
        }

        // Red
        val redSwitch = findViewById<Switch>(R.id.switchRed)
        redSwitch.setOnCheckedChangeListener { _, isChecked ->
            findViewById<SeekBar>(R.id.seekRed).isEnabled = isChecked
            viewModel.updateRedValue(if (isChecked) redSeekBar.progress / 100f else 0f)
        }

        // Green
        val greenSwitch = findViewById<Switch>(R.id.switchGreen)
        greenSwitch.setOnCheckedChangeListener { _, isChecked ->
            findViewById<SeekBar>(R.id.seekGreen).isEnabled = isChecked
            viewModel.updateGreenValue(if (isChecked) greenSeekBar.progress / 100f else 0f)
        }

        // Blue
        val blueSwitch = findViewById<Switch>(R.id.switchBlue)
        blueSwitch.setOnCheckedChangeListener { _, isChecked ->
            findViewById<SeekBar>(R.id.seekBlue).isEnabled = isChecked
            viewModel.updateBlueValue(if (isChecked) blueSeekBar.progress / 100f else 0f)
        }
    }

    private fun observeViewModel() {
        viewModel.redValue.observe(this, { red ->
            findViewById<SeekBar>(R.id.seekRed).progress = (red * 100).toInt()
        })

        viewModel.greenValue.observe(this, { green ->
            findViewById<SeekBar>(R.id.seekGreen).progress = (green * 100).toInt()
        })

        viewModel.blueValue.observe(this, { blue ->
            findViewById<SeekBar>(R.id.seekBlue).progress = (blue * 100).toInt()
        })
    }

    private fun getSeekBarChangeListener(index: Int): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (index) {
                    0 -> viewModel.updateRedValue(progress / 100f)
                    1 -> viewModel.updateGreenValue(progress / 100f)
                    2 -> viewModel.updateBlueValue(progress / 100f)
                }
                updateColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }

    private fun updateColor() {
        val color = android.graphics.Color.rgb(
            (viewModel.redValue.value!! * 255).toInt(),
            (viewModel.greenValue.value!! * 255).toInt(),
            (viewModel.blueValue.value!! * 255).toInt()
        )
        findViewById<TextView>(R.id.la_0).text = "Red: ${viewModel.redValue.value}"
        findViewById<TextView>(R.id.la_1).text = "Green: ${viewModel.greenValue.value}"
        findViewById<TextView>(R.id.la_2).text = "Blue: ${viewModel.blueValue.value}"
        findViewById<View>(R.id.view_color).setBackgroundColor(color)
    }
}
