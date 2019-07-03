package com.mercandalli.android.apps.feature_file_aes_sample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.mercandalli.android.apps.feature_file_aes_sample.ActivityUtils.bind
import com.mercandalli.android.sdk.feature_aes.AesModule
import com.mercandalli.android.sdk.feature_aes.AesMode
import decodeBase64ToByteArray
import decodeBase64ToString
import encodeBase64ToString
import java.io.File
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private val encodeAssetFile: View by bind(R.id.activity_main_encode_asset_file)
    private val decodeFile: View by bind(R.id.activity_main_decode_file)
    private val test: View by bind(R.id.activity_main_test)
    private val inputClear: EditText by bind(R.id.activity_main_input_clear)
    private val inputUnclear: EditText by bind(R.id.activity_main_input_unclear)
    private val key: EditText by bind(R.id.activity_main_key)
    private val initializationVector: EditText by bind(R.id.activity_main_initialization_vector)
    private val outputUnclear: TextView by bind(R.id.activity_main_output_unclear)
    private val outputClear: TextView by bind(R.id.activity_main_output_clear)
    private val clipboard by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val assetFile by lazy {
        assets.open("ffmpeg_default_test_file.mp3")
    }
    private val encodedFile by lazy {
        createEncodedFile()
    }
    private val decodedFile by lazy {
        createDecodedFile()
    }
    private val aesManager by lazy {
        AesModule().createAesManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        encodeAssetFile.setOnClickListener {
            encodeAssetFile()
        }
        decodeFile.setOnClickListener {
            decodeFile()
        }
        test.setOnClickListener {
            test()
        }
        outputUnclear.setOnClickListener {
            clipboard.setPrimaryClip(ClipData.newPlainText("data", outputUnclear.text.toString()))
        }
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                computeOutput()
            }
        }
        inputClear.addTextChangedListener(textWatcher)
        inputUnclear.addTextChangedListener(textWatcher)
        key.addTextChangedListener(textWatcher)
        initializationVector.addTextChangedListener(textWatcher)
        computeOutput()
    }

    private fun computeOutput() {
        outputUnclear.text = computeUnclearOutputByteArray()?.encodeBase64ToString() ?: ""
        outputClear.text = computeClearOutputByteArray()?.decodeBase64ToString() ?: ""
    }

    private fun computeUnclearOutputByteArray(): ByteArray? {
        val inputClearString = inputClear.text?.toString() ?: ""
        val keyString = key.text?.toString() ?: ""
        val initializationVectorString = initializationVector.text?.toString() ?: ""
        if (inputClearString == "" || keyString == "") {
            return null
        }
        return aesManager.encode(
            AesMode.ECB,
            inputClearString.toByteArray().copyOf(),
            keyString.toByteArray().copyOf(),
            if (initializationVectorString == "") null else initializationVectorString.toByteArray().copyOf()
        )
    }

    private fun computeClearOutputByteArray(): ByteArray? {
        val inputUnclearString = inputUnclear.text?.toString() ?: ""
        val keyString = key.text?.toString() ?: ""
        val initializationVectorString = initializationVector.text?.toString() ?: ""
        if (inputUnclearString == "" || keyString == "" || !inputUnclearString.contains(" ")) {
            return null
        }
        return aesManager.decode(
            AesMode.ECB,
            inputUnclearString.decodeBase64ToByteArray(),
            keyString.toByteArray().copyOf(),
            if (initializationVectorString == "") null else initializationVectorString.toByteArray().copyOf()
        )
    }

    private fun encodeAssetFile() {
        if (encodedFile.exists()) {
            encodedFile.delete()
        }
        val fileBytes = assetFile.readBytes().copyOf()
        aesManager.encode(
            AesMode.ECB,
            fileBytes,
            keyByteArray
        )
        encodedFile.writeBytes(fileBytes)
    }

    private fun decodeFile() {
        if (!encodedFile.exists()) {
            toast("Encoded file does not exists")
            return
        }
        if (decodedFile.exists()) {
            decodedFile.delete()
        }
        val bytes = encodedFile.readBytes().copyOf()
        aesManager.decode(
            AesMode.ECB,
            bytes,
            keyByteArray
        )
        decodedFile.writeBytes(bytes)
    }

    private fun createEncodedFile(): File {
        return File(filesDir, "ffmpeg_default_test_file.aes")
    }

    private fun createDecodedFile(): File {
        return File(filesDir, "ffmpeg_default_test_file_decoded.mp3")
    }

    private fun test() {
        val messageByteArray = createByteArray(
            0x6b,
            0xc1,
            0xbe,
            0xe2,
            0x2e,
            0x40,
            0x9f,
            0x96,
            0xe9,
            0x3d,
            0x7e,
            0x11,
            0x73,
            0x93,
            0x17,
            0x2a
        )
        val expectedOutputByteArray = createByteArray(
            0x3a,
            0xd7,
            0x7b,
            0xb4,
            0x0d,
            0x7a,
            0x36,
            0x60,
            0xa8,
            0x9e,
            0xca,
            0xf3,
            0x24,
            0x66,
            0xef,
            0x97
        )
        val outputByteArray = messageByteArray.copyOf()
        aesManager.encode(
            AesMode.ECB,
            outputByteArray,
            keyByteArray
        )
        if (expectedOutputByteArray.size != outputByteArray.size) {
            toast("Oops, ${messageByteArray.size} != ${outputByteArray.size}")
            val min = min(expectedOutputByteArray.size, outputByteArray.size)
            for (i in 0 until min) {
                Log.d("jm/debug", "" + expectedOutputByteArray[i] + " : " + outputByteArray[i])
            }
            return
        }
        for (i in 0 until expectedOutputByteArray.size) {
            Log.d("jm/debug", "" + expectedOutputByteArray[i] + " : " + outputByteArray[i])
            if (expectedOutputByteArray[i] != outputByteArray[i]) {
                toast("Oops, divergence on i: $i")
                return
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {

        private val keyByteArray = createByteArray(
            0x2b,
            0x7e,
            0x15,
            0x16,
            0x28,
            0xae,
            0xd2,
            0xa6,
            0xab,
            0xf7,
            0x15,
            0x88,
            0x09,
            0xcf,
            0x4f,
            0x3c
        )

        private fun createByteArray(vararg ints: Int) =
            ByteArray(ints.size) { pos -> ints[pos].toByte() }
    }
}
