package network.xyo.app.xyo.sample.application.witness.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.xyo.app.xyo.sample.application.Constants
import network.xyo.app.xyo.sample.application.R
import network.xyo.app.xyo.sample.application.nodeUrl
import network.xyo.app.xyo.sample.application.witness.WitnessResult
import network.xyo.client.address.XyoAccount
import network.xyo.client.payload.XyoPayload
import network.xyo.client.witness.location.info.LocationActivity

const val REQUEST_CODE_LOCATION = 1000

class LocationWitnessActivity : LocationActivity() {
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)) {
            // Already have permission, do the thing
            // ...
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

        setContentView(R.layout.location_witness_activity)

        val button: Button = findViewById(R.id.witness_button)

        val context = this

        var isRunning = false
        button.setOnClickListener {
            if (isRunning) return@setOnClickListener
            isRunning = true
            handleLocationWitness(context, Constants.witnessDispatcher)
            isRunning = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun handleLocationWitness(context: Context, dispatcher: CoroutineDispatcher) {
        CoroutineScope(dispatcher).launch {
            when (val result = WitnessLocationHandler().witness(context, arrayListOf(Pair(nodeUrl, XyoAccount())))) {
                is WitnessResult.Success<List<XyoPayload?>> -> {
                    Looper.prepare()
                    result.data.forEach() { it ->
                        Toast.makeText(
                            context,
                            "Location saved to archivist! - ${it?.schema}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is WitnessResult.Error -> {
                    Looper.prepare()
                    Toast.makeText(context, "Location was NOT Saved to archivist! - ${result.exception.first().message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}