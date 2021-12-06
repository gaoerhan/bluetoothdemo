package tech.tosee.bluetoothdemo.utils

import android.bluetooth.BluetoothClass

/**
 * @author: geh
 * @date: 2021/8/24
 */
object BTUtil {
    const val PROFILE_HEADSET = 0
    const val PROFILE_A2DP = 1
    const val PROFILE_OPP = 2
    const val PROFILE_HID = 3
    const val PROFILE_PANU = 4
    const val PROFILE_NAP = 5
    const val PROFILE_A2DP_SINK = 6
    @JvmStatic
    fun getDeviceType(bluetoothClass: BluetoothClass?): String {
        return if (bluetoothClass == null) {
            "ic_settings_bluetooth"
        } else when (bluetoothClass.majorDeviceClass) {
            BluetoothClass.Device.Major.MISC -> "ic_bt_misc"
            BluetoothClass.Device.Major.COMPUTER -> when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.COMPUTER_UNCATEGORIZED -> "ic_bt_COMPUTER_UNCATEGORIZED"
                BluetoothClass.Device.COMPUTER_DESKTOP -> "ic_bt_COMPUTER_DESKTOP"
                BluetoothClass.Device.COMPUTER_SERVER -> "ic_bt_COMPUTER_SERVER"
                BluetoothClass.Device.COMPUTER_LAPTOP -> "ic_bt_COMPUTER_LAPTOP"
                BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA -> "ic_bt_COMPUTER_HANDHELD_PC_PDA"
                BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA -> "ic_bt_COMPUTER_PALM_SIZE_PC_PDA"
                BluetoothClass.Device.COMPUTER_WEARABLE -> "ic_bt_COMPUTER_WEARABLE"
                else -> "ic_bt_computer"
            }
            BluetoothClass.Device.Major.PHONE -> when(bluetoothClass.deviceClass){
                BluetoothClass.Device.PHONE_UNCATEGORIZED -> "ic_bt_PHONE_UNCATEGORIZED"
                BluetoothClass.Device.PHONE_CELLULAR -> "ic_bt_PHONE_CELLULAR"
                BluetoothClass.Device.PHONE_CORDLESS -> "ic_bt_PHONE_CORDLESS"
                BluetoothClass.Device.PHONE_SMART -> "ic_bt_PHONE_SMART"
                BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY -> "ic_bt_PHONE_MODEM_OR_GATEWAY"
                BluetoothClass.Device.PHONE_ISDN -> "ic_bt_PHONE_ISDN"
                else -> "ic_bt_cellphone"
            }
            BluetoothClass.Device.Major.NETWORKING -> "ic_bt_networking"
            BluetoothClass.Device.Major.AUDIO_VIDEO -> when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED -> "ic_bt_AUDIO_VIDEO_UNCATEGORIZED"
                BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET -> "ic_bt_AUDIO_VIDEO_WEARABLE_HEADSET"
                BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE -> "ic_bt_AUDIO_VIDEO_HANDSFREE"
                BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE -> "ic_bt_AUDIO_VIDEO_MICROPHONE"
                BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER -> "ic_bt_AUDIO_VIDEO_LOUDSPEAKER"
                BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES -> "ic_bt_AUDIO_VIDEO_HEADPHONES"
                BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO -> "ic_bt_AUDIO_VIDEO_PORTABLE_AUDIO"
                BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> "ic_bt_AUDIO_VIDEO_CAR_AUDIO"
                BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX -> "ic_bt_AUDIO_VIDEO_SET_TOP_BOX"
                BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO -> "ic_bt_AUDIO_VIDEO_HIFI_AUDIO"
                BluetoothClass.Device.AUDIO_VIDEO_VCR -> "ic_bt_AUDIO_VIDEO_VCR"
                BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA -> "ic_bt_AUDIO_VIDEO_VIDEO_CAMERA"
                BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER -> "ic_bt_AUDIO_VIDEO_CAMCORDER"
                BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR -> "ic_bt_AUDIO_VIDEO_VIDEO_MONITOR"
                BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER -> "ic_bt_AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER"
                BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING -> "ic_bt_AUDIO_VIDEO_VIDEO_CONFERENCING"
                BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY -> "ic_bt_AUDIO_VIDEO_VIDEO_GAMING_TOY"
                else -> "ic_bt_video"
            }
            BluetoothClass.Device.Major.PERIPHERAL -> "ic_bt_peripheral"
            BluetoothClass.Device.Major.IMAGING -> "ic_bt_imaging"
            BluetoothClass.Device.Major.WEARABLE -> when(bluetoothClass.deviceClass){
                BluetoothClass.Device.WEARABLE_UNCATEGORIZED -> "ic_bt_WEARABLE_UNCATEGORIZED"
                BluetoothClass.Device.WEARABLE_WRIST_WATCH -> "ic_bt_WEARABLE_WRIST_WATCH"
                BluetoothClass.Device.WEARABLE_PAGER -> "ic_bt_WEARABLE_PAGER"
                BluetoothClass.Device.WEARABLE_JACKET -> "ic_bt_WEARABLE_JACKET"
                BluetoothClass.Device.WEARABLE_HELMET -> "ic_bt_WEARABLE_HELMET"
                BluetoothClass.Device.WEARABLE_GLASSES -> "ic_bt_WEARABLE_GLASSES"
                else -> "ic_bt_wearable"
            }
            BluetoothClass.Device.Major.TOY -> when(bluetoothClass.deviceClass){
                    BluetoothClass.Device.TOY_UNCATEGORIZED -> "ic_bt_TOY_UNCATEGORIZED"
                    BluetoothClass.Device.TOY_ROBOT -> "ic_bt_TOY_ROBOT"
                    BluetoothClass.Device.TOY_VEHICLE -> "ic_bt_TOY_VEHICLE"
                    BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE -> "ic_bt_TOY_DOLL_ACTION_FIGURE"
                    BluetoothClass.Device.TOY_CONTROLLER -> "ic_bt_TOY_CONTROLLER"
                    BluetoothClass.Device.TOY_GAME -> "ic_bt_TOY_GAME"
                    else -> "ic_bt_toy"
                }
            BluetoothClass.Device.Major.HEALTH -> when(bluetoothClass.deviceClass){
                BluetoothClass.Device.HEALTH_UNCATEGORIZED -> "ic_bt_HEALTH_UNCATEGORIZED"
                BluetoothClass.Device.HEALTH_BLOOD_PRESSURE -> "ic_bt_HEALTH_BLOOD_PRESSURE"
                BluetoothClass.Device.HEALTH_THERMOMETER -> "ic_bt_HEALTH_THERMOMETER"
                BluetoothClass.Device.HEALTH_WEIGHING -> "ic_bt_HEALTH_WEIGHING"
                BluetoothClass.Device.HEALTH_GLUCOSE -> "ic_bt_HEALTH_GLUCOSE"
                BluetoothClass.Device.HEALTH_PULSE_OXIMETER -> "ic_bt_HEALTH_PULSE_OXIMETER"
                BluetoothClass.Device.HEALTH_PULSE_RATE -> "ic_bt_HEALTH_PULSE_RATE"
                BluetoothClass.Device.HEALTH_DATA_DISPLAY -> "ic_bt_HEALTH_DATA_DISPLAY"
                else -> "ic_bt_health"
            }
            else -> //                Log.e("BTUtil","设备deviceclass类：" + bluetoothClass.getDeviceClass());
//                if (doesClassMatch(bluetoothClass, PROFILE_HEADSET))
//                    return "ic_bt_headset_hfp";
//                else if (doesClassMatch(bluetoothClass, PROFILE_A2DP)) {
//                    return "ic_bt_headphones_a2dp";
//                } else {
//                    return "ic_settings_bluetooth";
//                }
                "ic_bt_UNCATEGORIZED"
        }
    }

    private fun doesClassMatch(bluetoothClass: BluetoothClass, profile: Int): Boolean {
        return if (profile == PROFILE_A2DP) {
            if (bluetoothClass.hasService(BluetoothClass.Service.RENDER)) {
                return true
            }
            when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO, BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES, BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER, BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> true
                else -> false
            }
        } else if (profile == PROFILE_A2DP_SINK) {
            if (bluetoothClass.hasService(BluetoothClass.Service.CAPTURE)) {
                return true
            }
            when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO, BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX, BluetoothClass.Device.AUDIO_VIDEO_VCR -> true
                else -> false
            }
        } else if (profile == PROFILE_HEADSET) {
            // The render service class is required by the spec for HFP, so is a
            // pretty good signal
            if (bluetoothClass.hasService(BluetoothClass.Service.RENDER)) {
                return true
            }
            when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE, BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET, BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> true
                else -> false
            }
        } else if (profile == PROFILE_OPP) {
            if (bluetoothClass.hasService(BluetoothClass.Service.OBJECT_TRANSFER)) {
                return true
            }
            when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.COMPUTER_UNCATEGORIZED, BluetoothClass.Device.COMPUTER_DESKTOP, BluetoothClass.Device.COMPUTER_SERVER, BluetoothClass.Device.COMPUTER_LAPTOP, BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA, BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA, BluetoothClass.Device.COMPUTER_WEARABLE, BluetoothClass.Device.PHONE_UNCATEGORIZED, BluetoothClass.Device.PHONE_CELLULAR, BluetoothClass.Device.PHONE_CORDLESS, BluetoothClass.Device.PHONE_SMART, BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY, BluetoothClass.Device.PHONE_ISDN -> true
                else -> false
            }
        } else if (profile == PROFILE_HID) {
            bluetoothClass.deviceClass and BluetoothClass.Device.Major.PERIPHERAL == BluetoothClass.Device.Major.PERIPHERAL
        } else if (profile == PROFILE_PANU || profile == PROFILE_NAP) {
            // No good way to distinguish between the two, based on class bits.
            if (bluetoothClass.hasService(BluetoothClass.Service.NETWORKING)) {
                true
            } else bluetoothClass.deviceClass and BluetoothClass.Device.Major.NETWORKING == BluetoothClass.Device.Major.NETWORKING
        } else {
            false
        }
    }
}