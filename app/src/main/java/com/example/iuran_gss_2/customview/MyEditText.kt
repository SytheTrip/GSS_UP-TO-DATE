package com.example.iuran_gss_2.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.iuran_gss_2.R

class MyEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var clearButtonImage: Drawable
    private lateinit var personIcon: Drawable
    private lateinit var emailIcon: Drawable
    private lateinit var passwordIcon: Drawable
    private lateinit var visibilityIconOn: Drawable
    private lateinit var visibilityIconOff: Drawable
    private lateinit var phoneIcon: Drawable
    private var isPasswordVisible: Boolean = false
    private var isEmail: Boolean = false
    private var isPassword: Boolean = false
    private var isName: Boolean = false
    private var isPhone: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()

    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyEditText)
        isPassword = typedArray.getBoolean(R.styleable.MyEditText_isPassword, false)
        isName = typedArray.getBoolean(R.styleable.MyEditText_isName, false)

        typedArray.recycle()
    }

    private fun init() {
        clearButtonImage =
            ContextCompat.getDrawable(context, R.drawable.outline_close_24) as Drawable
        personIcon = ContextCompat.getDrawable(context, R.drawable.baseline_person_24) as Drawable
        emailIcon = ContextCompat.getDrawable(context, R.drawable.ic_email_focused) as Drawable
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_lock_focused) as Drawable
        phoneIcon =
            ContextCompat.getDrawable(context, R.drawable.baseline_phone_android_24) as Drawable
        visibilityIconOn =
            ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24) as Drawable
        visibilityIconOff =
            ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24) as Drawable
        setOnTouchListener(this)
        maxLines = 1
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
                if (isPassword) {
                    if (isPasswordVisible) {
                        showClearButton()
                    } else {
                        hideClearButton()
                    }
                }
                if (s.toString().isNotEmpty()) {
                    showClearButton()
                } else {
                    hideClearButton()
                }
                if (isName) {
                    val regex = Regex("[^A-Za-z\\d ]")
                    error = if (regex.containsMatchIn(s)) {
                        context.getString(R.string.errorName)
                    } else {
                        null
                    }
                }
                if (isEmail) {
                    error = if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        null
                    } else {
                        context.getString(R.string.errorEmail)
                    }
                } else if (isPassword) {
                    error = if (s.length >= 8) {
                        null
                    } else {
                        context.getString(R.string.errorPass)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun showClearButton() {
        showIcon()
    }

    private fun hideClearButton() {
        showIcon()
    }

    private fun showIcon() {
        if (isEmail) {
            setButtonDrawables(startOfTheText = emailIcon, endOfTheText = clearButtonImage)
        }
        if (isPassword) {
            if (isPasswordVisible) {
                setButtonDrawables(startOfTheText = passwordIcon, endOfTheText = visibilityIconOn)
            } else {
                setButtonDrawables(startOfTheText = passwordIcon, endOfTheText = visibilityIconOff)
            }
        }
        if (isName) {
            setButtonDrawables(startOfTheText = personIcon, endOfTheText = clearButtonImage)
        }
        if (isPhone) {
            setButtonDrawables(startOfTheText = phoneIcon, endOfTheText = clearButtonImage)
        }
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val visibilityButton = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                event.x < (visibilityIconOff.intrinsicWidth + paddingStart).toFloat()
            } else {
                event.x > (width - paddingEnd - visibilityIconOff.intrinsicWidth).toFloat()
            }
            if (visibilityButton) {
                if (isPasswordVisible) {
                    when (event.action) {
                        MotionEvent.ACTION_UP -> {
                            inputType =
                                if (inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                } else {
                                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                                }
                            text?.let { setSelection(it.length) }
                            isPasswordVisible = false
                            hideClearButton()
                            return true
                        }
                        else -> return false
                    }
                } else {
                    when (event.action) {
                        MotionEvent.ACTION_UP -> {
                            inputType =
                                if (inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                } else {
                                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                                }
                            text?.let { setSelection(it.length) }
                            isPasswordVisible = true
                            hideClearButton()
                            return true
                        }
                        else -> return false
                    }
                }
            }
        }
        return false
    }


}
