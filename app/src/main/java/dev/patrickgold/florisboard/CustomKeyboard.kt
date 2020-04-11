package dev.patrickgold.florisboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.LinearLayout

class CustomKeyboard : LinearLayout {

    private val showLanguageButton: Boolean

    var caps: Boolean = false
    var layoutName: String?
    var inputMethodService: InputMethodService? = null
    val popupManager = KeyPopupManager(this)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.customKeyboardStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttrs: Int) : super(context, attrs, defStyleAttrs) {
        context.obtainStyledAttributes(
            attrs, R.styleable.CustomKeyboard, defStyleAttrs,
            R.style.CustomKeyboardStyle).apply {
            try {
                layoutName = getNonResourceString(R.styleable.CustomKeyboard_layoutName)
                showLanguageButton = getBoolean(R.styleable.CustomKeyboard_showLanguageButton, false)
            } finally {}
        }.recycle()
    }

    fun onKeyClicked(code: Int) {
        val ic = inputMethodService?.currentInputConnection ?: return
        when (code) {
            KeyCodes.DELETE -> ic.deleteSurroundingText(1, 0)
            KeyCodes.ENTER -> ic.sendKeyEvent(
                KeyEvent(
                    KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_ENTER
                )
            )
            KeyCodes.LANGUAGE_SWITCH -> {}
            KeyCodes.SHIFT -> {
                caps = !caps
            }
            KeyCodes.VIEW_SYMOBLS -> {}
            else -> {
                var text = code.toChar()
                if (caps) {
                    text = Character.toUpperCase(text)
                }
                ic.commitText(text.toString(), 1)
            }
        }
    }
}