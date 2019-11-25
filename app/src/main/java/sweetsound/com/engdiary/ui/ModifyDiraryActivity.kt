package sweetsound.com.engdiary.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_modify_diary.*
import kotlinx.android.synthetic.main.modification_dialog_layout.view.*


class ModifyDiraryActivity(): AppCompatActivity() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, ModifyDiraryActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(sweetsound.com.engdiary.R.layout.activity_modify_diary)

        // 선택 해도 키보드 올라오지 않도록 설정.
        english_diary_edittext.inputType = InputType.TYPE_NULL
        english_diary_edittext.setTextIsSelectable(true)

        // 수정 할 때 띄울 Dialog 설정
        val editDialog = AlertDialog.Builder(this)
        val dialogLayout = LayoutInflater.from(this).inflate(sweetsound.com.engdiary.R.layout.modification_dialog_layout, null)
        editDialog.setNegativeButton(sweetsound.com.engdiary.R.string.cancel, null)
        editDialog.setOnDismissListener {
            dialogLayout.modification_editext.setText("")
        }

        // 이건 메뉴를 선택 했을 때 할 수 있는 동작
        english_diary_edittext.customSelectionActionModeCallback = object: android.view.ActionMode.Callback {
            override fun onActionItemClicked(mode: android.view.ActionMode, menuItem: MenuItem): Boolean {
                // 내가 원하는 메뉴의 동작
                // 앞 / 뒤 블럭 선택 추가 기능 필요
                when (menuItem.itemId) {
                    sweetsound.com.engdiary.R.id.edit -> {
                        val startIndex = english_diary_edittext.selectionStart
                        val endIndex = english_diary_edittext.selectionEnd

                        dialogLayout.target_textview.text = english_diary_edittext.text.toString().substring(startIndex, endIndex)

                        editDialog.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                            var modifiedText = "(" + dialogLayout.modification_editext.text + ")"
                            val modifiedTextLength = modifiedText.length

                            if (modifiedTextLength > 0) {
                                val targetText = english_diary_edittext.text

                                // 선택 한 부분 취소선 긋고 회색 처리
                                targetText.setSpan(StrikethroughSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                targetText.setSpan(ForegroundColorSpan(Color.parseColor("#9F9F9F")), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                                // 수정한 글자 추가
                                // 선택한 글자의 앞, 뒤에 linefeed가 있는지 체크 해서 문장을 수정한건지 부분을 수정한 건지 체크.
                                // 문정을 수정한 경우 아래에 붙이고 부분을 수정한 경우 선택한 부분 뒤에 붙인다.
                                var isStartIndex = false
                                var isSentence = false

                                var preText = " "

                                // 문장의 시작인지 체크
                                if (startIndex > 0) {
                                    if (targetText[startIndex -1] == '\n') {
                                        isStartIndex = true;
                                    }
                                } else {
                                    isStartIndex = true;
                                }

                                // 선택 한 것의 시작이 문장의 시작이라면 선택 한 것의 끝이 문장의 끝인지 체크
                                if (isStartIndex == true) {
                                    if (endIndex >= targetText.length) {
                                        isSentence = true;
                                    } else {
                                        val compareChar = targetText[endIndex]

                                        // 문장의 끝이라면
                                        if (compareChar == '\n' || compareChar == '?' || compareChar == '!' || compareChar == '.') {
                                            isSentence = true;
                                        }
                                    }
                                }

                                // 선택한 것이 문장이면 preText를 line feed로 변경 한다.
                                if (isSentence == true) {
                                    preText = "\n"
                                }

                                // 문장인지 부분 선택인지에 따라 앞에 공백 or Line feed를 붙여 준다.
                                modifiedText = preText + modifiedText

                                english_diary_edittext.setSelection(endIndex)
                                english_diary_edittext.text.insert(endIndex, modifiedText)

                                // 수정한 글자 색 변경
                                targetText.setSpan(ForegroundColorSpan(Color.parseColor("#FF3232")), endIndex, endIndex + modifiedTextLength +1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                                english_diary_edittext.setText(targetText, TextView.BufferType.SPANNABLE)
                            }
                        })

                        dialogLayout.modification_editext.setOnFocusChangeListener(object : OnFocusChangeListener {
                            override fun onFocusChange(v: View, hasFocus: Boolean) {
                                dialogLayout.modification_editext.post(Runnable {
                                    val inputMethodManager = this@ModifyDiraryActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    inputMethodManager.showSoftInput(dialogLayout.modification_editext, InputMethodManager.SHOW_IMPLICIT)
                                })
                            }
                        })
                        dialogLayout.modification_editext.requestFocus()

                        if (dialogLayout.parent != null) {
                            (dialogLayout.parent as ViewGroup).removeView(dialogLayout)
                        }

                        editDialog.setView(dialogLayout)

                        val dialog = editDialog.create()
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.window?.setGravity(Gravity.BOTTOM)
                        dialog.show()

                        mode.finish()
                        return true
                    }
                }
                return false
            }

            override fun onCreateActionMode(mode: android.view.ActionMode, menu: Menu): Boolean {
                // 내가 원하는 메뉴 생성
                menu.clear()
                mode.menuInflater.inflate(sweetsound.com.engdiary.R.menu.selected_text_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: android.view.ActionMode?) { }
        }
    }
}