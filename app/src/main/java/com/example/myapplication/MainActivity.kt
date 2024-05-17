package com.example.myapplication

import ColorViewModel
import MyPreferenceRepository
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var redSeekBar: SeekBar
    lateinit var greenSeekBar: SeekBar
    lateinit var blueSeekBar: SeekBar
    lateinit var redInput: EditText
    lateinit var greenInput: EditText
    lateinit var blueInput: EditText
    lateinit var redSwitch: Switch
    lateinit var greenSwitch: Switch
    lateinit var blueSwitch:Switch


    var redValue = 0f
    var greenValue = 0f
    var blueValue = 0f

    private lateinit var myViewModel: ColorViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyPreferenceRepository.initialize(this)

        myViewModel = ColorViewModel()
        myViewModel.loadValueInputs(this)
        myViewModel.loadProgressInputs(this)
        setupUI()

        // Update EditTexts with corresponding progress values???
      //  findViewById<EditText>(R.id.rededit).setText((redSeekBar.progress / 100f).toString())
      //  findViewById<EditText>(R.id.greenedit).setText((greenSeekBar.progress / 100f).toString())
      //  findViewById<EditText>(R.id.blueedit).setText((blueSeekBar.progress / 100f).toString())


    }



    private fun setupUI() {
        updateColor()
        redSeekBar = findViewById(R.id.redseek)
        greenSeekBar = findViewById(R.id.greenseek)
        blueSeekBar = findViewById(R.id.blueseek)
        redSeekBar.setOnSeekBarChangeListener(createSeekBarChangeListener(0))
        greenSeekBar.setOnSeekBarChangeListener(createSeekBarChangeListener(1))
        blueSeekBar.setOnSeekBarChangeListener(createSeekBarChangeListener(2))





        redInput = findViewById(R.id.rededit)
        greenInput = findViewById(R.id.greenedit)
        blueInput = findViewById(R.id.blueedit)

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                updateColorFromEditText()
            }
        }

        redInput.addTextChangedListener(textWatcher)
        greenInput.addTextChangedListener(textWatcher)
        blueInput.addTextChangedListener(textWatcher)

        val resetButton = findViewById<Button>(R.id.button)
        resetButton.setOnClickListener {
            redValue = 0f
            greenValue = 0f
            blueValue = 0f
            redSeekBar.progress = 0
            greenSeekBar.progress = 0
            blueSeekBar.progress = 0
            updateColor()

            findViewById<EditText>(R.id.rededit).setText("0")
            findViewById<EditText>(R.id.greenedit).setText("0")
            findViewById<EditText>(R.id.blueedit).setText("0")
        }

        val saveButton = findViewById<Button>(R.id.button2)
        saveButton.setOnClickListener{
            saveData()
        }

        redSwitch = findViewById(R.id.redswitch)
        redSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redSeekBar.progress = 0
                redInput.isFocusable = false
                redInput.isFocusableInTouchMode = false
            }else{
                redInput.isFocusable = true
                redInput.isFocusableInTouchMode = true
            }
            redSeekBar.isEnabled = !isChecked
            redValue = if (isChecked) 0f else redSeekBar.progress / 100f
            updateColor()
            findViewById<EditText>(R.id.rededit).setText(if (isChecked) "0" else redValue.toString())
        }

        greenSwitch = findViewById(R.id.greenswitch)
        greenSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                greenSeekBar.progress = 0
                greenInput.isFocusable = false
                greenInput.isFocusableInTouchMode = false
            }else{
                greenInput.isFocusable = true
                greenInput.isFocusableInTouchMode = true
            }
            greenSeekBar.isEnabled = !isChecked
            greenValue = if (isChecked) 0f else greenSeekBar.progress / 100f
            updateColor()
            findViewById<EditText>(R.id.greenedit).setText(if (isChecked) "0" else greenValue.toString())
        }

        blueSwitch = findViewById(R.id.blueswitch)
        blueSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                blueSeekBar.progress = 0
                blueInput.isFocusable = false
                blueInput.isFocusableInTouchMode = false
            }else{

                blueInput.isFocusable = true
                blueInput.isFocusableInTouchMode = true
            }
            blueSeekBar.isEnabled = !isChecked
            blueValue = if (isChecked) 0f else blueSeekBar.progress / 100f
            updateColor()
            findViewById<EditText>(R.id.blueedit).setText(if (isChecked) "0" else blueValue.toString())
        }

    }

    private fun createSeekBarChangeListener(index: Int): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val colors = arrayOf(redValue, greenValue, blueValue)
                    colors[index] = progress / 100f
                    redValue = colors[0]
                    greenValue = colors[1]
                    blueValue = colors[2]
                    updateColor()

                    when (index) {
                        0 -> findViewById<EditText>(R.id.rededit).setText(redValue.toString())
                        1 -> findViewById<EditText>(R.id.greenedit).setText(greenValue.toString())
                        2 -> findViewById<EditText>(R.id.blueedit).setText(blueValue.toString())
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }




    private fun updateColor() {
        val color = android.graphics.Color.rgb(
            (redValue * 255).toInt(),
            (greenValue * 255).toInt(),
            (blueValue * 255).toInt()
        )
        findViewById<View>(R.id.view).setBackgroundColor(color)

    }

    private fun updateColorFromEditText() {

        redValue = if (redInput.text.toString().isEmpty()) 0f else redInput.text.toString().toFloat()
        greenValue = if (greenInput.text.toString().isEmpty()) 0f else greenInput.text.toString().toFloat()
        blueValue = if (blueInput.text.toString().isEmpty()) 0f else blueInput.text.toString().toFloat()

        findViewById<SeekBar>(R.id.redseek).progress = (redValue * 100).toInt()
        findViewById<SeekBar>(R.id.greenseek).progress = (greenValue * 100).toInt()
        findViewById<SeekBar>(R.id.blueseek).progress = (blueValue * 100).toInt()

        updateColor()
    }
    private fun saveData(){
        myViewModel.saveValue(redValue,1)
        myViewModel.saveValue(blueValue,2)
        myViewModel.saveValue(greenValue,3)
        myViewModel.saveEditTextValue(redInput.text.toString(), 1)
        myViewModel.saveEditTextValue(greenInput.text.toString(), 2)
        myViewModel.saveEditTextValue(blueInput.text.toString(), 3)
        myViewModel.saveSwitchState(redSwitch.isChecked,1)
        myViewModel.saveSwitchState(greenSwitch.isChecked,2)
        myViewModel.saveSwitchState(blueSwitch.isChecked,3)
        myViewModel.saveProgressValue(redSeekBar.progress, 1)
        myViewModel.saveProgressValue(greenSeekBar.progress, 2)
        myViewModel.saveProgressValue(blueSeekBar.progress, 3)




    }

}

