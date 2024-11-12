package network.xyo.app.xyo.sample.application

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_CONTACTS
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import network.xyo.client.XyoPanel
import network.xyo.client.address.XyoAccount
import network.xyo.client.witness.location.info.LocationActivity
import network.xyo.client.witness.location.info.XyoLocationWitness

const val REQUEST_CODE_LOCATION_AND_CONTACTS_PERMISSION = 12345

class LocationWitnessActivity : LocationActivity() {
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(REQUEST_CODE_LOCATION_AND_CONTACTS_PERMISSION)
    private fun methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION, READ_CONTACTS)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_location_and_contacts_rationale_message),
                REQUEST_CODE_LOCATION_AND_CONTACTS_PERMISSION,
                ACCESS_FINE_LOCATION, READ_CONTACTS
            )
        }
    }

    @ExperimentalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        methodRequiresTwoPermission()

        setContentView(R.layout.location_witness_activity)

        val button: Button = findViewById(R.id.witness_button)

        val context = this

        button.setOnClickListener {
            val panel = XyoPanel(this, arrayListOf(Pair(nodeUrl, XyoAccount())), listOf(
                XyoLocationWitness()
            ))

            CoroutineScope(Dispatchers.Main).launch {
                panel.let {
                    it.reportAsyncQuery().apiResults?.forEach { action ->
                        if (action.response !== null) {
                            Log.i("xyoSampleApp", "received api result in panel")
                            Toast.makeText(context, "Button Clicked!", Toast.LENGTH_SHORT).show()
                        }
                        if (action.errors !== null) {
                            action.errors!!.forEach {
                                Log.e("xyoSampleApp", it.message ?: it.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}