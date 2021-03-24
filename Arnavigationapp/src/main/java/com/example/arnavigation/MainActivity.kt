package com.example.arnavigation

//import com.example.arnavigation.MainActivity.VisionMode.Camera
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.method.LinkMovementMethod
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.setPadding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.Utils.dpToPx
import com.mapbox.vision.VisionManager
import com.mapbox.vision.mobile.core.interfaces.VisionEventsListener
import com.mapbox.vision.mobile.core.models.Country
import com.mapbox.vision.mobile.core.models.classification.FrameSignClassifications
import com.mapbox.vision.mobile.core.models.position.VehicleState
import com.mapbox.vision.mobile.core.models.road.RoadDescription
import com.mapbox.vision.mobile.core.utils.SystemInfoUtils
import com.mapbox.vision.performance.ModelPerformance
import com.mapbox.vision.performance.ModelPerformanceMode
import com.mapbox.vision.performance.ModelPerformanceRate
import com.mapbox.vision.utils.VisionLogger
import com.mapbox.vision.view.VisionView
import com.mapbox.vision.view.VisualizationMode
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity(), PermissionsListener{

    enum class VisionMode {
        Camera
    }
    companion object {
        private val BASE_SESSION_PATH = "${Environment.getExternalStorageDirectory().absolutePath}/MapboxVisionTelemetry"
        private const val START_AR_MAP_ACTIVITY_FOR_NAVIGATION_RESULT_CODE = 100
        private const val START_AR_MAP_ACTIVITY_FOR_RECORDING_RESULT_CODE = 110
    }
    private var country = Country.Unknown
    private var visionMode = VisionMode.Camera
    private var sessionPath = ""
    private val permissionsManager: PermissionsManager? = null
    private var isPermissionsGranted = true
    private var visionManagerWasInit = false
    private var modelPerformance = ModelPerformance.On(
        ModelPerformanceMode.FIXED,
        ModelPerformanceRate.LOW
    )

    private val visionEventsListener = object : VisionEventsListener {

        override fun onCountryUpdated(country: Country) {
            runOnUiThread {
                this@MainActivity.country = country
                requireBaseVisionFragment()?.updateCountry(country)
            }
        }

        override fun onFrameSignClassificationsUpdated(frameSignClassifications: FrameSignClassifications) {
            runOnUiThread {
                //requireSignDetectionFragment()?.drawSigns(frameSignClassifications)
            }
        }

        override fun onRoadDescriptionUpdated(roadDescription: RoadDescription) {
            runOnUiThread {
               // requireLaneDetectionFragment()?.drawLanesDetection(roadDescription)
            }
        }

        override fun onVehicleStateUpdated(vehicleState: VehicleState) {
            runOnUiThread {
                requireBaseVisionFragment()?.updateLastSpeed(vehicleState.speed)
            }
        }

        override fun onCameraUpdated(camera: com.mapbox.vision.mobile.core.models.Camera) {
            runOnUiThread {
                requireBaseVisionFragment()?.updateCalibrationProgress(camera.calibrationProgress)
               // fps_performance_view.setCalibrationProgress(camera.calibrationProgress)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onUpdateCompleted() {
            runOnUiThread {
                if (visionManagerWasInit) {
                    val frameStatistics = when (visionMode) {
                        VisionMode.Camera -> VisionManager.getFrameStatistics()
                    }
                    //fps_performance_view.showInfo(frameStatistics)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)

        if (!SystemInfoUtils.isVisionSupported()) {
            AlertDialog.Builder(this)
                .setTitle(R.string.vision_not_supported_title)
                .setView(
                    TextView(this).apply {
                        setPadding(dpToPx(20f).toInt())
                        movementMethod = LinkMovementMethod.getInstance()
                        isClickable = true
                        text = HtmlCompat.fromHtml(
                            getString(R.string.vision_not_supported_message),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    }
                )
                .setCancelable(false)
                .show()

            VisionLogger.e(
                "BoardNotSupported",
                "System Info: [${SystemInfoUtils.obtainSystemInfo()}]"
            )
        }

        setContentView(R.layout.activity_main)
        onPermissionsGranted()
        /*if (!PermissionsUtils.requestPermissions(this)) {
            onPermissionsGranted()
        }*/
    }

    private fun createSessionFolderIfNotExist() {
        val folder = File(BASE_SESSION_PATH)
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw IllegalStateException("Can't create image folder = $folder")
            }
        }
    }

    private fun onPermissionsGranted() {
        isPermissionsGranted = true

        createSessionFolderIfNotExist()

        initArNavigationButton()
        //initReplayModeButton()
        startVision()
    }

    private fun initArNavigationButton() {
        ar_navigation_button.setOnClickListener {
            when (visionMode) {
                VisionMode.Camera -> startArMapActivityForNavigation()
            }
        }
    }

    private fun startVision() {
        if (isPermissionsGranted && !visionManagerWasInit) {
            visionManagerWasInit = when (visionMode) {
                VisionMode.Camera -> initVisionManagerCamera(vision_view)

            }
        }
    }

    private fun stopVision() {
        if (isPermissionsGranted && visionManagerWasInit) {
            visionManagerWasInit = false
            when (visionMode) {
                VisionMode.Camera -> destroyVisionManagerCamera()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        startVision()
        vision_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        stopVision()
        vision_view.onPause()
    }

    /*private fun onBackClick() {
        dashboard_container.show()
        back.hide()
        playback_seek_bar_view.hide()
    }*/

    private fun initVisionManagerCamera(visionView: VisionView): Boolean {
        VisionManager.create()
        visionView.setVisionManager(VisionManager)
        VisionManager.visionEventsListener = visionEventsListener
        VisionManager.start()
        VisionManager.setModelPerformance(modelPerformance)
        return true
    }

    private fun destroyVisionManagerCamera() {
        VisionManager.stop()
        VisionManager.destroy()
    }

    private fun startArMapActivityForNavigation() {
        startArMapActivity(START_AR_MAP_ACTIVITY_FOR_NAVIGATION_RESULT_CODE)
    }

    private fun startArMapActivity(resultCode: Int) {
        val intent = Intent(this@MainActivity, Armap::class.java)
        startActivityForResult(intent, resultCode)
    }


   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermissionsUtils.arePermissionsGranted(this, requestCode)) {
            onPermissionsGranted()
        } else {
            val notGranted = PermissionsUtils.getNotGrantedPermissions(this).joinToString(", ")

            AlertDialog.Builder(this)
                .setTitle("R.string.permissions_missing_title")
                .setMessage(
                    "getString(R.string.permissions_missing_message, notGranted)"
                )
                .setCancelable(false)
                .setPositiveButton(
                    " R.string.request_permissions"
                ) { _, _ -> PermissionsUtils.requestPermissions(this) }
                .show()

            VisionLogger.e(
                MainActivity::class.java.simpleName,
                "Permissions are not granted : $notGranted"
            )
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            START_AR_MAP_ACTIVITY_FOR_NAVIGATION_RESULT_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val jsonRoute = data.getStringExtra(Armap.ARG_RESULT_JSON_ROUTE)
                    if (!jsonRoute.isNullOrEmpty()) {
                        ArNavigation.start(this, jsonRoute)
                    }
                }
            }
            /*START_AR_MAP_ACTIVITY_FOR_RECORDING_RESULT_CODE -> {
                val jsonRoute = data?.getStringExtra(ArMapActivity.ARG_RESULT_JSON_ROUTE)
                onCameraSelected()
                vision_view.visualizationMode = VisualizationMode.Clear

                // set lowest model performance to allow fair 30 fps all the time
                VisionManager.setModelPerformance(ModelPerformance.Off)

                // Using state loss here to keep code simple, lost of RecorderFragment is not critical for UX
                showRecorderFragment(jsonRoute, stateLoss = true)
            }*/
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun requireBaseVisionFragment() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? BaseVisionFragment
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("Not yet implemented")
        Toast.makeText(this, " R.string.user_location_permission_explanation", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        TODO("Not yet implemented")
        onPermissionsGranted()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
/*

val ImageView = findViewById<ImageView>(R.id.back)
val MapView = findViewById<MapView>(R.id.mapView)
val Button = findViewById<Button>(R.id.start_ar)*/
