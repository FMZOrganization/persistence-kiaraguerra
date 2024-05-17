import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ColorViewModel : ViewModel() {

    private val prefs = MyPreferenceRepository.get()


    fun saveProgressValue(i: Int, index: Int){
        viewModelScope.launch { prefs.saveProgressValue(i, index) }
    }

    fun saveValue(f:Float, index: Int){
        viewModelScope.launch { prefs.saveValue(f, index) }
    }
    fun saveSwitchState(state: Boolean, index: Int) {
        viewModelScope.launch {
            prefs.saveSwitchState(state, index)
        }
    }
    fun saveEditTextValue(value: String, index: Int) {
        viewModelScope.launch {
            prefs.saveEditTextValue(value, index)
        }
    }

    fun loadValueInputs(act: MainActivity){
        viewModelScope.launch {
            prefs.redValue.collectLatest {
                act.redValue = it
            }
        }
        viewModelScope.launch {
            prefs.blueValue.collectLatest {
                act.blueValue = it
            }
        }
        viewModelScope.launch {
            prefs.greenValue.collectLatest {
                act.greenValue = it
            }
        }

    }

    fun loadProgressInputs(act: MainActivity){
        viewModelScope.launch {
            prefs.redProgress.collectLatest {
                act.redSeekBar.progress = it
            }
        }
        viewModelScope.launch {
            prefs.blueProgress.collectLatest {
                act.blueSeekBar.progress = it
            }
        }
        viewModelScope.launch {
            prefs.greenProgress.collectLatest {
                act.greenSeekBar.progress = it
            }
        }
        viewModelScope.launch {
            prefs.editTextValue1.collectLatest {
                act.redInput.setText(it)
            }
        }
        viewModelScope.launch {
            prefs.editTextValue2.collectLatest {
                act.greenInput.setText(it)
            }
        }
        viewModelScope.launch {
            prefs.editTextValue3.collectLatest {
                act.blueInput.setText(it)
            }
        }
        viewModelScope.launch {
            prefs.switchState1.collectLatest {
                act.redSwitch.isChecked = it
            }
        }
        viewModelScope.launch {
            prefs.switchState2.collectLatest {
                act.greenSwitch.isChecked = it
            }
        }
        viewModelScope.launch {
            prefs.switchState3.collectLatest {
                act.blueSwitch.isChecked = it
            }
        }
    }
}
