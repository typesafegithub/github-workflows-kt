// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.reactivecircus

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Android Emulator Runner
 *
 * Installs, configures and starts an Android Emulator directly on macOS virtual machines.
 *
 * [Action on GitHub](https://github.com/ReactiveCircus/android-emulator-runner)
 */
public class AndroidEmulatorRunnerV2(
    /**
     * API level of the platform and system image - e.g. 23 for Android Marshmallow, 29 for Android
     * 10
     */
    public val apiLevel: String,
    /**
     * target of the system image - default, google_apis, google_apis_playstore, aosp_atd,
     * google_atd, android-wear, android-wear-cn, android-tv or google-tv
     */
    public val target: String? = null,
    /**
     * CPU architecture of the system image - x86, x86_64 or arm64-v8a
     */
    public val arch: String? = null,
    /**
     * hardware profile used for creating the AVD - e.g. `Nexus 6`
     */
    public val profile: String? = null,
    /**
     * the number of cores to use for the emulator
     */
    public val cores: String? = null,
    /**
     * size of RAM to use for this AVD, in KB or MB, denoted with K or M. - e.g. `2048M`
     */
    public val ramSize: String? = null,
    /**
     * size of heap to use for this AVD in MB. - e.g. `512M`
     */
    public val heapSize: String? = null,
    /**
     * path to the SD card image for this AVD or the size of a new SD card image to create for this
     * AVD, in KB or MB, denoted with K or M. - e.g. `path/to/sdcard`, or `1000M`
     */
    public val sdcardPathOrSize: String? = null,
    /**
     * disk size to use for this AVD. Either in bytes or KB, MB or GB, when denoted with K, M or G
     */
    public val diskSize: String? = null,
    /**
     * custom AVD name used for creating the Android Virtual Device
     */
    public val avdName: String? = null,
    /**
     * whether to force create the AVD by overwriting an existing AVD with the same name as
     * `avd-name` - `true` or `false`
     */
    public val forceAvdCreation: String? = null,
    /**
     * command-line options used when launching the emulator - e.g.
     * `-no-window -no-snapshot -camera-back emulated`
     */
    public val emulatorOptions: String? = null,
    /**
     * whether to disable animations - true or false
     */
    public val disableAnimations: String? = null,
    /**
     * whether to disable spellchecker - `true` or `false`
     */
    public val disableSpellchecker: String? = null,
    /**
     * whether to disable hardware acceleration on Linux machines - `true` or `false` or `auto`
     */
    public val disableLinuxHwAccel: String? = null,
    /**
     * whether to enable hardware keyboard - `true` or `false`.
     */
    public val enableHwKeyboard: String? = null,
    /**
     * build number of a specific version of the emulator binary to use - e.g. `6061023` for
     * emulator v29.3.0.0
     */
    public val emulatorBuild: String? = null,
    /**
     * A custom working directory - e.g. `./android` if your root Gradle project is under the
     * `./android` sub-directory within your repository
     */
    public val workingDirectory: String? = null,
    /**
     * version of NDK to install - e.g. 21.0.6113669
     */
    public val ndk: String? = null,
    /**
     * version of CMake to install - e.g. 3.10.2.4988404
     */
    public val cmake: String? = null,
    /**
     * Channel to download the SDK components from - `stable`, `beta`, `dev`, `canary`
     */
    public val channel: String? = null,
    /**
     * custom script to run - e.g. `./gradlew connectedCheck`
     */
    public val script: String,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("ReactiveCircus", "android-emulator-runner", _customVersion ?: "v2.25.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "api-level" to apiLevel,
            target?.let { "target" to it },
            arch?.let { "arch" to it },
            profile?.let { "profile" to it },
            cores?.let { "cores" to it },
            ramSize?.let { "ram-size" to it },
            heapSize?.let { "heap-size" to it },
            sdcardPathOrSize?.let { "sdcard-path-or-size" to it },
            diskSize?.let { "disk-size" to it },
            avdName?.let { "avd-name" to it },
            forceAvdCreation?.let { "force-avd-creation" to it },
            emulatorOptions?.let { "emulator-options" to it },
            disableAnimations?.let { "disable-animations" to it },
            disableSpellchecker?.let { "disable-spellchecker" to it },
            disableLinuxHwAccel?.let { "disable-linux-hw-accel" to it },
            enableHwKeyboard?.let { "enable-hw-keyboard" to it },
            emulatorBuild?.let { "emulator-build" to it },
            workingDirectory?.let { "working-directory" to it },
            ndk?.let { "ndk" to it },
            cmake?.let { "cmake" to it },
            channel?.let { "channel" to it },
            "script" to script,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
