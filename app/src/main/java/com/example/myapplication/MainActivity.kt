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
    private var redProgress = 0
    private var greenProgress = 0
    private var blueProgress = 0

    private lateinit var myViewModel: ColorViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyPreferenceRepository.initialize(this)
        setupUI()

        myViewModel = ColorViewModel()
        myViewModel.loadValueInputs(this)
        myViewModel.loadProgressInputs(this)

        // Restore the state if there's a saved instance state
        if (savedInstanceState != null) {
            redValue = savedInstanceState.getFloat("redValue", 0f)
            greenValue = savedInstanceState.getFloat("greenValue", 0f)
            blueValue = savedInstanceState.getFloat("blueValue", 0f)

            // Update UI with restored values
            updateColor()
            redSeekBar.progress = (redValue * 100).toInt()
            greenSeekBar.progress = (greenValue * 100).toInt()
            blueSeekBar.progress = (blueValue * 100).toInt()
            redInput.setText(redValue.toString())
            greenInput.setText(greenValue.toString())
            blueInput.setText(blueValue.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save your data to the bundle here
        outState.putFloat("redValue", redValue)
        outState.putFloat("greenValue", greenValue)
        outState.putFloat("blueValue", blueValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore your data from the bundle here
        redValue = savedInstanceState.getFloat("redValue", 0f)
        greenValue = savedInstanceState.getFloat("greenValue", 0f)
        blueValue = savedInstanceState.getFloat("blueValue", 0f)

        // Update UI with restored values
        updateColor()
        redSeekBar.progress = (redValue * 100).toInt()
        greenSeekBar.progress = (greenValue * 100).toInt()
        blueSeekBar.progress = (blueValue * 100).toInt()
        redInput.setText(redValue.toString())
        greenInput.setText(greenValue.toString())
        blueInput.setText(blueValue.toString())
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
                redSeekBar.progress = (if (redInput.text.toString().isEmpty()) 0 else redInput.text.toString().toFloat() * 100).toInt()
                greenSeekBar.progress = (if (greenInput.text.toString().isEmpty()) 0 else greenInput.text.toString().toFloat() * 100).toInt()
                blueSeekBar.progress = (if (blueInput.text.toString().isEmpty()) 0 else blueInput.text.toString().toFloat() * 100).toInt()
                myViewModel.saveProgressValue(redSeekBar.progress, 1)
                myViewModel.saveProgressValue(greenSeekBar.progress, 2)
                myViewModel.saveProgressValue(blueSeekBar.progress, 3)
                updateColorFromEditText()


            }
        }

        redInput.addTextChangedListener(textWatcher)
        greenInput.addTextChangedListener(textWatcher)
        blueInput.addTextChangedListener(textWatcher)

        val resetButton = findViewById<Button>(R.id.button)
        fun resetValues() {
            if (redSwitch.isChecked) {
                myViewModel.saveSwitchState(false, 1)
                redSwitch.isChecked = false

                redInput.isFocusable = true
                redInput.isFocusableInTouchMode = true
            }

            if (greenSwitch.isChecked) {
                myViewModel.saveSwitchState(false, 2)
                greenSwitch.isChecked = false

                greenInput.isFocusable = true
                greenInput.isFocusableInTouchMode = true
            }

            if (blueSwitch.isChecked) {
                myViewModel.saveSwitchState(false, 3)
                blueSwitch.isChecked = false

                blueInput.isFocusable = true
                blueInput.isFocusableInTouchMode = true
            }

            redValue = 0f
            greenValue = 0f
            blueValue = 0f

            myViewModel.saveValue(redValue, 1)
            myViewModel.saveValue(blueValue, 2)
            myViewModel.saveValue(greenValue, 3)

            redSeekBar.progress = 0
            greenSeekBar.progress = 0
            blueSeekBar.progress = 0

            myViewModel.saveProgressValue(redSeekBar.progress, 1)
            myViewModel.saveProgressValue(greenSeekBar.progress, 2)
            myViewModel.saveProgressValue(blueSeekBar.progress, 3)

            redProgress = 0
            greenProgress = 0
            blueProgress = 0

            redInput.setText("0")
            greenInput.setText("0")
            blueInput.setText("0")

            myViewModel.saveEditTextValue(redInput.text.toString(), 1)
            myViewModel.saveEditTextValue(greenInput.text.toString(), 2)
            myViewModel.saveEditTextValue(blueInput.text.toString(), 3)

            updateColor()
        }

        resetButton.setOnClickListener {
            resetValues()
            resetValues()
        }

       // val saveButton = findViewById<Button>(R.id.button2)
       // saveButton.setOnClickListener{
         //   saveData()
       // }

        redSwitch = findViewById(R.id.redswitch)
        redSwitch.setOnCheckedChangeListener { _, isChecked ->
            myViewModel.saveSwitchState(redSwitch.isChecked,1)
            if (isChecked) {
                redProgress = redSeekBar.progress
                redSeekBar.progress = 0
                redInput.isFocusable = false
                redInput.isFocusableInTouchMode = false

            } else {
                redSeekBar.progress = redProgress
                redInput.isFocusable = true
                redInput.isFocusableInTouchMode = true

            }
            redSeekBar.isEnabled = !isChecked
            redValue = if (isChecked) 0f else redSeekBar.progress / 100f
            myViewModel.saveProgressValue(redSeekBar.progress,1)

            updateColor()
            findViewById<EditText>(R.id.rededit).setText(if (isChecked) "0" else redValue.toString())
            myViewModel.saveEditTextValue(redInput.text.toString(), 1)

        }

        greenSwitch = findViewById(R.id.greenswitch)
        greenSwitch.setOnCheckedChangeListener { _, isChecked ->
            myViewModel.saveSwitchState(greenSwitch.isChecked,2)

            if (isChecked) {
                greenProgress = greenSeekBar.progress
                greenSeekBar.progress = 0
                greenInput.isFocusable = false
                greenInput.isFocusableInTouchMode = false
            } else {
                greenSeekBar.progress = greenProgress
                greenInput.isFocusable = true
                greenInput.isFocusableInTouchMode = true
            }
            greenSeekBar.isEnabled = !isChecked
            greenValue = if (isChecked) 0f else greenSeekBar.progress / 100f
            updateColor()

            myViewModel.saveProgressValue(greenSeekBar.progress,2)

            findViewById<EditText>(R.id.greenedit).setText(if (isChecked) "0" else greenValue.toString())
            myViewModel.saveEditTextValue(greenInput.text.toString(), 2)

        }


        blueSwitch = findViewById(R.id.blueswitch)
        blueSwitch.setOnCheckedChangeListener { _, isChecked ->
            myViewModel.saveSwitchState(blueSwitch.isChecked,3)

            if (isChecked) {
                blueProgress = blueSeekBar.progress
                blueSeekBar.progress = 0
                blueInput.isFocusable = false
                blueInput.isFocusableInTouchMode = false
            }else{
                blueSeekBar.progress = blueProgress
                blueInput.isFocusable = true
                blueInput.isFocusableInTouchMode = true
            }
            blueSeekBar.isEnabled = !isChecked
            blueValue = if (isChecked) 0f else blueSeekBar.progress / 100f
            updateColor()
            myViewModel.saveProgressValue(blueSeekBar.progress,3)
            findViewById<EditText>(R.id.blueedit).setText(if (isChecked) "0" else blueValue.toString())
            myViewModel.saveEditTextValue(blueInput.text.toString(), 3)

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
                    myViewModel.saveValue(redValue,1)
                    myViewModel.saveValue(blueValue,2)
                    myViewModel.saveValue(greenValue,3)
                    myViewModel.saveProgressValue(redSeekBar.progress, 1)
                    myViewModel.saveProgressValue(greenSeekBar.progress, 2)
                    myViewModel.saveProgressValue(blueSeekBar.progress, 3)



                    when (index) {
                        0 -> findViewById<EditText>(R.id.rededit).setText(redValue.toString())
                        1 -> findViewById<EditText>(R.id.greenedit).setText(greenValue.toString())
                        2 -> findViewById<EditText>(R.id.blueedit).setText(blueValue.toString())
                    }
                    myViewModel.saveEditTextValue(redInput.text.toString(), 1)
                    myViewModel.saveEditTextValue(greenInput.text.toString(), 2)
                    myViewModel.saveEditTextValue(blueInput.text.toString(), 3)

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
        myViewModel.saveValue(redValue,1)
        myViewModel.saveValue(blueValue,2)
        myViewModel.saveValue(greenValue,3)
        myViewModel.saveProgressValue(redSeekBar.progress, 1)
        myViewModel.saveProgressValue(greenSeekBar.progress, 2)
        myViewModel.saveProgressValue(blueSeekBar.progress, 3)



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

