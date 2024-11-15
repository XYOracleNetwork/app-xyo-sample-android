package network.xyo.app.xyo.sample.application.witness.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.xyo.app.xyo.sample.application.Constants
import network.xyo.app.xyo.sample.application.R
import network.xyo.app.xyo.sample.application.nodeUrl
import network.xyo.client.datastore.AccountPrefsRepository
import network.xyo.client.payload.XyoPayload
import network.xyo.client.witness.location.info.WitnessLocationHandler
import network.xyo.client.witness.types.WitnessResult

const val REQUEST_CODE_LOCATION = 1000

class LocationWitnessActivity : LocationActivity() {
    val hashes = HashesViewModel()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)) {
            // Already have permission, do the thing
            Log.i("xyoSampleApp", "Permissions already granted")
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_location_and_contacts_rationale_message),
                REQUEST_CODE_LOCATION,
                ACCESS_FINE_LOCATION
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        methodRequiresTwoPermission()

        setContent {
            WitnessLocationButton(witnessLocation = { handleLocationWitness(this, Constants.witnessDispatcher) })
            HashList(hashes)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun handleLocationWitness(context: Context, dispatcher: CoroutineDispatcher) {
        CoroutineScope(dispatcher).launch {
            val account = AccountPrefsRepository.getInstance(context).getAccount()
            when (val result = WitnessLocationHandler().witness(context, arrayListOf(Pair(nodeUrl, account)))) {
                is WitnessResult.Success<List<XyoPayload?>> -> {
                    result.data.forEach() { it ->
                        if (it !== null) {
                            val hash = it.hash()
                            hashes.addHash(hash)
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(
                                    context,
                                    "Witnessed payload - $hash",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                is WitnessResult.Error -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Failed to witness payload - ${result.exception.first().message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}