package com.justin.popupbarchartsample

import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.justin.popupbarchart.PopupBarChart
import com.justin.popupbarchart.GraphValue
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val widget = findViewById<PopupBarChart>(R.id.customBarchart).apply {
            barSize = 10
            barCount = 12
            isMonthGraph = false
            setBottom(false)
            setGraphValues(
                arrayListOf(
                    GraphValue(
                        day = "DAY 14444",
                        id = 1,
                        progress = 0,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "",
                        id = 2,
                        progress = 70,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "",
                        id = 3,
                        progress = 100,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "",
                        id = 4,
                        progress = 0,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 1",
                        id = 5,
                        progress = 50,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 1",
                        id = 6,
                        progress = 50,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 1",
                        id = 7,
                        progress = 25,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                )
            )
        }

        val widget2 = findViewById<PopupBarChart>(R.id.customBarchart2).apply {
            barSize = 10
            barCount = 12
            setBottom(true)
            isMonthGraph = false
            setGraphValues(
                arrayListOf(
                    GraphValue(
                        day = "DAY 1134141",
                        id = 1,
                        progress = 10,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 2",
                        id = 2,
                        progress = 30,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 3",
                        id = 3,
                        progress = 0,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 4",
                        id = 4,
                        progress = 0,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 5",
                        id = 5,
                        progress = 0,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 6",
                        id = 6,
                        progress = 0,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ),
                    GraphValue(
                        day = "DAY 7",
                        id = 7,
                        progress = 25,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ), GraphValue(
                        day = "DAY 8",
                        id = 7,
                        progress = 25,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ), GraphValue(
                        day = "DAY 9",
                        id = 7,
                        progress = 25,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ), GraphValue(
                        day = "DAY 10",
                        id = 7,
                        progress = 70,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ), GraphValue(
                        day = "DAY 11",
                        id = 7,
                        progress = 60,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    ), GraphValue(
                        day = "DAY 12",
                        id = 7,
                        progress = 100,
                        isToday = false,
                        isDayVisible = true,
                        showToolTip = false
                    )
                )
            )
        }


        findViewById<CheckBox>(R.id.cb_round_corner).setOnCheckedChangeListener { compoundButton, b ->
            widget.roundCorner = b
        }

        findViewById<AppCompatButton>(R.id.colorPicker).setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Choose secondary color")
                .setPreferenceName("secondaryColor")
                .setPositiveButton("confirm",
                    ColorEnvelopeListener { envelope, fromUser ->
                        Log.d(
                            "TAG_JUSTIN",
                            "--> ${envelope.color} || ${envelope.argb} || ${envelope.hexCode}"
                        )
                        widget.secondaryColor = envelope.color
                    })
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }



        findViewById<AppCompatButton>(R.id.colorPickerStartColor).setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Choose Start color")
                .setPreferenceName("startColor")
                .setPositiveButton("confirm",
                    ColorEnvelopeListener { envelope, fromUser ->
                        Log.d(
                            "TAG_JUSTIN",
                            "--> ${envelope.color} || ${envelope.argb} || ${envelope.hexCode}"
                        )
                        widget.startColor = envelope.color
                    })
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }


        findViewById<AppCompatButton>(R.id.colorPickerEndColor).setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Choose end color")
                .setPreferenceName("endColor")
                .setPositiveButton("confirm",
                    ColorEnvelopeListener { envelope, fromUser ->
                        Log.d(
                            "TAG_JUSTIN",
                            "--> ${envelope.color} || ${envelope.argb} || ${envelope.hexCode}"
                        )
                        widget.endColor = envelope.color
                    })
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }

        findViewById<SeekBar>(R.id.bar_size).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                widget.barSize = p0?.progress ?: 16
            }

        })

        findViewById<AppCompatButton>(R.id.btn_rubik_italic).setOnClickListener {
            widget.barTextFontFamily = R.font.rubik_italic
        }

        findViewById<AppCompatButton>(R.id.btn_rubik_medium).setOnClickListener {
            widget.barTextFontFamily = R.font.rubik_medium
        }

        findViewById<AppCompatButton>(R.id.btn_hurricane_regular).setOnClickListener {
            widget.barTextFontFamily = R.font.hurricane_regular
        }

        findViewById<AppCompatButton>(R.id.colorPickerDayText).setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Choose Day Text color")
                .setPreferenceName("dayTextColor")
                .setPositiveButton("confirm",
                    ColorEnvelopeListener { envelope, fromUser ->
                        Log.d(
                            "TAG_JUSTIN",
                            "--> ${envelope.color} || ${envelope.argb} || ${envelope.hexCode}"
                        )
                        widget.barTextColor = envelope.color
                    })
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }

        findViewById<SeekBar>(R.id.day_text_size).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                widget.barTextSize = p0?.progress ?: 16
            }

        })


        findViewById<AppCompatButton>(R.id.colorPickerTooltipBg).setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Choose Tooltip bg color")
                .setPreferenceName("dayTooltipBgColor")
                .setPositiveButton("confirm",
                    ColorEnvelopeListener { envelope, fromUser ->
                        Log.d(
                            "TAG_JUSTIN",
                            "--> ${envelope.color} || ${envelope.argb} || ${envelope.hexCode}"
                        )
                        widget.tooltipBg = envelope.color
                    })
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }


        findViewById<AppCompatButton>(R.id.colorPickerTooltipTitleTextColor).setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Choose Tooltip title color")
                .setPreferenceName("dayTooltipTitleColor")
                .setPositiveButton("confirm",
                    ColorEnvelopeListener { envelope, fromUser ->
                        Log.d(
                            "TAG_JUSTIN",
                            "--> ${envelope.color} || ${envelope.argb} || ${envelope.hexCode}"
                        )
                        widget.tooltipTitleTextColor = envelope.color
                    })
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }

        findViewById<AppCompatButton>(R.id.colorPickerTooltipSubTitleTextColor).setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Choose Tooltip sub title color")
                .setPreferenceName("dayTooltipSubTitleColor")
                .setPositiveButton("confirm",
                    ColorEnvelopeListener { envelope, fromUser ->
                        Log.d(
                            "TAG_JUSTIN",
                            "--> ${envelope.color} || ${envelope.argb} || ${envelope.hexCode}"
                        )
                        widget.tooltipSubTitleTextColor = envelope.color
                    })
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }


        findViewById<AppCompatButton>(R.id.btn_rubik_italic_tooltip_title).setOnClickListener {
            widget.tooltipTitleTextFontFamily = R.font.rubik_italic
        }

        findViewById<AppCompatButton>(R.id.btn_rubik_medium_tooltip_title).setOnClickListener {
            widget.tooltipTitleTextFontFamily = R.font.rubik_medium
        }

        findViewById<AppCompatButton>(R.id.btn_hurricane_regular_tooltip_title).setOnClickListener {
            widget.tooltipTitleTextFontFamily = R.font.hurricane_regular
        }


        findViewById<AppCompatButton>(R.id.btn_rubik_italic_tooltip_sub_family).setOnClickListener {
            widget.tooltipSubTitleTextFontFamily = R.font.rubik_italic
        }

        findViewById<AppCompatButton>(R.id.btn_rubik_medium_tooltip_sub_family).setOnClickListener {
            widget.tooltipSubTitleTextFontFamily = R.font.rubik_medium
        }

        findViewById<AppCompatButton>(R.id.btn_hurricane_regular_tooltip_sub_family).setOnClickListener {
            widget.tooltipSubTitleTextFontFamily = R.font.hurricane_regular
        }

        findViewById<SeekBar>(R.id.tooltip_title_size).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                widget.tooltipTitleTextSize = p0?.progress ?: 16
            }

        })

        findViewById<SeekBar>(R.id.tooltip_sub_title_size).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                widget.tooltipSubTitleTextSize = p0?.progress ?: 16
            }

        })

    }
}