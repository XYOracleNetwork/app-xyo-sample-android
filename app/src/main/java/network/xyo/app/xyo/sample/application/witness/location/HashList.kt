package network.xyo.app.xyo.sample.application.witness.location
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun WitnessLocationButton(witnessLocation: () -> Unit) {
    Button(onClick = { witnessLocation() }) {
        Text("WitnessLocation")
    }
}

@Composable
fun HashList(hashesViewModel: HashesViewModel) {
    val state = rememberLazyListState()
    // Collect the list from the ViewModel
    val hashes by hashesViewModel.items.collectAsState()
    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize().padding(24.dp, 44.dp)
    ) {
        items(hashes) { hash ->
            HashItem(hash)
        }
    }
}

@Composable
fun HashItem(hash: String) {
    val context = LocalContext.current // Access the current context

    Box(
        Modifier.border(1.dp, Color.Gray)
    ) {
        Text(text = hash, modifier = Modifier
            .padding(10.dp).clickable {
                // Open the link in the browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://beta.explore.xyo.network/$hash"))
                context.startActivity(intent)
            })
    }

}