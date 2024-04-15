import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorPickerViewModel : ViewModel() {
    private val _redValue = MutableLiveData<Float>()
    val redValue: LiveData<Float> = _redValue

    private val _greenValue = MutableLiveData<Float>()
    val greenValue: LiveData<Float> = _greenValue

    private val _blueValue = MutableLiveData<Float>()
    val blueValue: LiveData<Float> = _blueValue

    init {
        _redValue.value = 0f
        _greenValue.value = 0f
        _blueValue.value = 0f
    }

    fun updateRedValue(value: Float) {
        _redValue.value = value
    }

    fun updateGreenValue(value: Float) {
        _greenValue.value = value
    }

    fun updateBlueValue(value: Float) {
        _blueValue.value = value
    }

    fun resetColor() {
        _redValue.value = 0f
        _greenValue.value = 0f
        _blueValue.value = 0f
    }
}
