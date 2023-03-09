// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package it.krzeminski.githubactions.actions.reactivecircus

import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class AndroidEmulatorRunnerV2 private constructor(
    /**
     * API level of the platform and system image - e.g. 23 for Android Marshmallow, 29 for Android
     * 10
     */
    public val apiLevel: Int,
    /**
     * target of the system image - default, google_apis, google_apis_playstore, aosp_atd,
     * google_atd, android-wear, android-wear-cn, android-tv or google-tv
     */
    public val target: AndroidEmulatorRunnerV2.Target? = null,
    /**
     * CPU architecture of the system image - x86, x86_64 or arm64-v8a
     */
    public val arch: AndroidEmulatorRunnerV2.Arch? = null,
    /**
     * hardware profile used for creating the AVD - e.g. `Nexus 6`
     */
    public val profile: String? = null,
    /**
     * the number of cores to use for the emulator
     */
    public val cores: Int? = null,
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
    public val forceAvdCreation: Boolean? = null,
    /**
     * command-line options used when launching the emulator - e.g.
     * `-no-window -no-snapshot -camera-back emulated`
     */
    public val emulatorOptions: String? = null,
    /**
     * whether to disable animations - true or false
     */
    public val disableAnimations: Boolean? = null,
    /**
     * whether to disable the Android spell checker framework, a common source of flakiness in text
     * fields - `true` or `false`
     */
    public val disableSpellchecker: Boolean? = null,
    /**
     * whether to disable hardware acceleration on Linux machines - `true` or `false` or `auto`
     */
    public val disableLinuxHwAccel: String? = null,
    /**
     * whether to enable hardware keyboard - `true` or `false`.
     */
    public val enableHwKeyboard: Boolean? = null,
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
    public val channel: AndroidEmulatorRunnerV2.Channel? = null,
    /**
     * custom script to run - e.g. `./gradlew connectedCheck`
     */
    public val script: String,
    /**
     * custom script to run after creating the AVD and before launching the emulator - e.g.
     * `./adjust-emulator-configs.sh`
     */
    public val preEmulatorLaunchScript: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<Action.Outputs>("ReactiveCircus", "android-emulator-runner", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        apiLevel: Int,
        target: AndroidEmulatorRunnerV2.Target? = null,
        arch: AndroidEmulatorRunnerV2.Arch? = null,
        profile: String? = null,
        cores: Int? = null,
        ramSize: String? = null,
        heapSize: String? = null,
        sdcardPathOrSize: String? = null,
        diskSize: String? = null,
        avdName: String? = null,
        forceAvdCreation: Boolean? = null,
        emulatorOptions: String? = null,
        disableAnimations: Boolean? = null,
        disableSpellchecker: Boolean? = null,
        disableLinuxHwAccel: String? = null,
        enableHwKeyboard: Boolean? = null,
        emulatorBuild: String? = null,
        workingDirectory: String? = null,
        ndk: String? = null,
        cmake: String? = null,
        channel: AndroidEmulatorRunnerV2.Channel? = null,
        script: String,
        preEmulatorLaunchScript: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(apiLevel=apiLevel, target=target, arch=arch, profile=profile, cores=cores,
            ramSize=ramSize, heapSize=heapSize, sdcardPathOrSize=sdcardPathOrSize,
            diskSize=diskSize, avdName=avdName, forceAvdCreation=forceAvdCreation,
            emulatorOptions=emulatorOptions, disableAnimations=disableAnimations,
            disableSpellchecker=disableSpellchecker, disableLinuxHwAccel=disableLinuxHwAccel,
            enableHwKeyboard=enableHwKeyboard, emulatorBuild=emulatorBuild,
            workingDirectory=workingDirectory, ndk=ndk, cmake=cmake, channel=channel, script=script,
            preEmulatorLaunchScript=preEmulatorLaunchScript, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "api-level" to apiLevel.toString(),
            target?.let { "target" to it.stringValue },
            arch?.let { "arch" to it.stringValue },
            profile?.let { "profile" to it },
            cores?.let { "cores" to it.toString() },
            ramSize?.let { "ram-size" to it },
            heapSize?.let { "heap-size" to it },
            sdcardPathOrSize?.let { "sdcard-path-or-size" to it },
            diskSize?.let { "disk-size" to it },
            avdName?.let { "avd-name" to it },
            forceAvdCreation?.let { "force-avd-creation" to it.toString() },
            emulatorOptions?.let { "emulator-options" to it },
            disableAnimations?.let { "disable-animations" to it.toString() },
            disableSpellchecker?.let { "disable-spellchecker" to it.toString() },
            disableLinuxHwAccel?.let { "disable-linux-hw-accel" to it },
            enableHwKeyboard?.let { "enable-hw-keyboard" to it.toString() },
            emulatorBuild?.let { "emulator-build" to it },
            workingDirectory?.let { "working-directory" to it },
            ndk?.let { "ndk" to it },
            cmake?.let { "cmake" to it },
            channel?.let { "channel" to it.stringValue },
            "script" to script,
            preEmulatorLaunchScript?.let { "pre-emulator-launch-script" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Target(
        public val stringValue: String,
    ) {
        public object Default : AndroidEmulatorRunnerV2.Target("default")

        public object GoogleApis : AndroidEmulatorRunnerV2.Target("google_apis")

        public object GoogleApisPlaystore : AndroidEmulatorRunnerV2.Target("google_apis_playstore")

        public object AospAtd : AndroidEmulatorRunnerV2.Target("aosp_atd")

        public object GoogleAtd : AndroidEmulatorRunnerV2.Target("google_atd")

        public object AndroidWear : AndroidEmulatorRunnerV2.Target("android-wear")

        public object AndroidWearCn : AndroidEmulatorRunnerV2.Target("android-wear-cn")

        public object AndroidTv : AndroidEmulatorRunnerV2.Target("android-tv")

        public object GoogleTv : AndroidEmulatorRunnerV2.Target("google-tv")

        public class Custom(
            customStringValue: String,
        ) : AndroidEmulatorRunnerV2.Target(customStringValue)
    }

    public sealed class Arch(
        public val stringValue: String,
    ) {
        public object X86 : AndroidEmulatorRunnerV2.Arch("x86")

        public object X8664 : AndroidEmulatorRunnerV2.Arch("x86_64")

        public object Arm64V8a : AndroidEmulatorRunnerV2.Arch("arm64-v8a")

        public class Custom(
            customStringValue: String,
        ) : AndroidEmulatorRunnerV2.Arch(customStringValue)
    }

    public sealed class Channel(
        public val stringValue: String,
    ) {
        public object Stable : AndroidEmulatorRunnerV2.Channel("stable")

        public object Beta : AndroidEmulatorRunnerV2.Channel("beta")

        public object Dev : AndroidEmulatorRunnerV2.Channel("dev")

        public object Canary : AndroidEmulatorRunnerV2.Channel("canary")

        public class Custom(
            customStringValue: String,
        ) : AndroidEmulatorRunnerV2.Channel(customStringValue)
    }
}
