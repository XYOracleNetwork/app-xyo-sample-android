package network.xyo.app.xyo.sample.application.witness.location

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HashesViewModel : ViewModel() {
    // Use StateFlow to hold the list
    private val _hashes = MutableStateFlow(listOf<String>())
    val items: StateFlow<List<String>> = _hashes

    // Method to add an item
    fun addHash(hash: String) {
        _hashes.value += hash
    }
}